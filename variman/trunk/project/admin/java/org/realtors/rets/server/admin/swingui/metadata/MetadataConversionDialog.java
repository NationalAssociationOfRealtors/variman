package org.realtors.rets.server.admin.swingui.metadata;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.attrib.AttrAlphanum;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;

import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.admin.swingui.AdminFrame;
import org.realtors.rets.server.admin.swingui.SwingUtils;
import org.realtors.rets.server.admin.swingui.SwingWorker;
import org.realtors.rets.server.metadata.MetadataDao;

public class MetadataConversionDialog extends MetadataDialog
{
    public MetadataConversionDialog(JMenu metadataMenu)
    {
        super(SwingUtils.getAdminFrame());
        
        setModal(true);
        setTitle("Metadata Conversion");

        JLabel label = new JLabel("<HTML><FONT style = 'font-weight: normal;'>" +
			                "This will process your existing metadata files and<BR>" +
			                "attempt to convert them into RETS 1.7.2 compatible<BR>" +
			                "format. The resultant file will be <I><B>metadata.xml</B></I><BR>" +
			                "and will be left in your configured metadata directory.<BR>" +
			                "Variman and the admin tool will then use that file as<BR>" +
			                "the primary metadata source from this point on. You<BR>" + 
			                "may then archive and delete your old metadata files.<BR>" +
			                "<B>Note:</B> Validation Expressions must be converted by<BR>" +
			                "hand and are not touched by this tool.");
        int answer = (int) JOptionPane.showConfirmDialog(
                    SwingUtils.getAdminFrame(),
                    label,
                    "Please Confirm",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        if (answer != JOptionPane.OK_OPTION)
        {
            setResponse(JOptionPane.CANCEL_OPTION);
            setVisible(false);
            return;
        }
        
        SwingWorker worker = new SwingWorker()
        {
            public Object construct()
            {
                AdminFrame.getInstance().setStatusText("Converting Metadata");

                try
                {
                    MetadataDao metadataDao = Admin.getMetadataDao();
                    mMetadata = metadataDao.getMetadata();
                    mSystem = mMetadata.getSystem();
                    updateStatus("Converting System");

                    if (mSystem.getTimeZoneOffset() == null || mSystem.getTimeZoneOffset().length() < 1)
                        mSystem.setAttribute(MSystem.TIMEZONEOFFSET, "-00:00");
                    
                    /*
                     * The NAR sample metadata used the tag "Comment" and if anyone used that as a starting
                     * point, the Comment tag needs to be converted to COMMENT.
                     */
                    if (mSystem.getAttributeAsString("Comments") != null)
                        mSystem.setAttribute(MSystem.COMMENTS, mSystem.getAttributeAsString("Comments"));
                    
                    cleanupUnderscores(mSystem);
                    
                    /*
                     * Because of a problem with the 1.7.2 spec regarding the KeyField in RESOURCE (RETSID)
                     * and the SystemName everywhere else, we need to record all keyfields such that we can
                     * translate them when encountered.
                     */
                    for (MResource resource : mSystem.getMResources())
                    {
                        String oldKeyField = resource.getKeyField();
                        cleanupUnderscores(resource);
                        String newKeyField = resource.getKeyField();
                        if (!mKeyMap.containsKey(oldKeyField))
                        	mKeyMap.put(oldKeyField, newKeyField);
                    }

                    for (MForeignKey foreignKey : mSystem.getMForeignKeys())
                    {
                        updateStatus("Converting ForeignKey " + foreignKey.getForeignKeyID());
                        cleanupUnderscores(foreignKey);
                        cleanupKeyFields(foreignKey);
                    }
                    
                    for (MResource resource : mSystem.getMResources())
                    {
                        /*
                         * Enumerate the RETS classes.
                         */
                        for (MClass clazz : resource.getMClasses())
                        {
                            updateStatus("Converting Class " + clazz.getId());
                            if (clazz.getAttributeAsString("DBname") != null)
                            {
                                clazz.setAttribute(MClass.X_DBNAME, clazz.getAttributeAsString("DBName"));
                                clazz.removeAttribute("DBName");
                            }
                            cleanupUnderscores(clazz);
                            cleanupKeyFields(clazz);
                            /*
                             * Iterate over the tables for this class.
                             */
                            for (MTable table : clazz.getMTables())
                            {
                                updateStatus("Converting Table " + table.getId());
                                if (table.getSystemName() != null)
                                    table.setAttribute(MTable.METADATAENTRYID, table.getSystemName());
                                cleanupUnderscores(table);
                                cleanupKeyFields(table);
                            }
                            /*
                             * Iterate over the updates for this class.
                             */
                            for (MUpdate update : clazz.getMUpdates())
                            {
                                updateStatus("Converting Update " + update.getId());
                                if (update.getUpdateName() != null)
                                    update.setAttribute(MUpdate.METADATAENTRYID, update.getUpdateName());
                                /*
                                 * "Date" and "Version" must be converted to "UpdateTypeDate" and "UpdateTypeVersion".
                                 */
                                   if (update.getAttributeAsString("Date") != null)
                                   {
                                	   update.setAttribute(MUpdate.UPDATETYPEDATE, update.getAttributeAsString("Date"));
                                	   update.removeAttribute("Date");
                                   }
                                   if (update.getAttributeAsString("Version") != null)
                                   {
                                	   update.setAttribute(MUpdate.UPDATETYPEVERSION, update.getAttributeAsString("Version"));
                                	   update.removeAttribute("Version");
                                   }
                                cleanupUnderscores(update);
                                cleanupKeyFields(update);
                                /*
                                 * Iterate over the update types for this update.
                                 */
                                for (MUpdateType updateType: update.getMUpdateTypes())
                                {
                                    updateStatus("Converting UpdateType " + updateType.getId());
                                    if (updateType.getSystemName() != null)
                                        updateType.setAttribute(MUpdateType.METADATAENTRYID, updateType.getSystemName());
                                    cleanupUnderscores(updateType);
                                    cleanupKeyFields(updateType);
                                }
                            }
                        }
                        /*
                         * Enumerate the lookups.
                         */
                        for (MLookup lookup : resource.getMLookups())
                        {
                            updateStatus("Converting Lookup " + lookup.getId());
                            if (lookup.getLookupName() != null)
                                lookup.setAttribute(MLookup.METADATAENTRYID, lookup.getLookupName());
                            cleanupUnderscores(lookup);
                            
                            for (MLookupType lookupType : lookup.getMLookupTypes())
                            {
                                updateStatus("Converting LookupType " + lookupType.getId());
                                if (lookupType.getShortValue() != null)
                                    lookupType.setAttribute(MLookupType.METADATAENTRYID, lookupType.getShortValue());
                                cleanupUnderscores(lookupType);
                            }
                        }
                        /*
                         * Enumerate the objects.
                         */
                        for (MObject obj : resource.getMObjects())
                        {
                            updateStatus("Converting Object " + obj.getId());
                            int length = obj.getVisibleName().length();
                            if (length > 0)
                            {
                                if (length > 32)
                                    length = 32;
                                obj.setAttribute(MObject.METADATAENTRYID, obj.getVisibleName().substring(0, length - 1));
                            }
                            obj.removeAttribute("StandardName");
                            cleanupUnderscores(obj);
                           }
                        /*
                         * Enumerate the edit masks.
                         */
                        for (MEditMask editMask : resource.getMEditMasks())
                        {
                            updateStatus("Converting EditMask " + editMask.getId());
                            if (editMask.getEditMaskID() != null)
                                editMask.setAttribute(MEditMask.METADATAENTRYID, editMask.getEditMaskID());
                            cleanupUnderscores(editMask);
                        }
                        /*
                         * Enumerate the search help.
                         */
                        for (MSearchHelp searchHelp : resource.getMSearchHelps())
                        {
                            updateStatus("Converting SearchHelp " + searchHelp.getId());
                            if (searchHelp.getSearchHelpID() != null)
                                searchHelp.setAttribute(MSearchHelp.METADATAENTRYID, searchHelp.getSearchHelpID());
                            cleanupUnderscores(searchHelp);
                        }
                        /*
                         * Enumerate the update help.
                         */
                        for (MUpdateHelp updateHelp : resource.getMUpdateHelps())
                        {
                            updateStatus("Converting UpdateHelp " + updateHelp.getId());
                            if (updateHelp.getUpdateHelpID() != null)
                                updateHelp.setAttribute(MUpdateHelp.METADATAENTRYID, updateHelp.getUpdateHelpID());
                            cleanupUnderscores(updateHelp);
                        }
                        /*
                         * Enumerate the validation lookups.
                         */
                        for (MValidationLookup lookup : resource.getMValidationLookups())
                        {
                            updateStatus("Converting ValidationLookup " + lookup.getId());
                            if (lookup.getValidationLookupName() != null)
                                lookup.setAttribute(MValidationLookup.METADATAENTRYID, lookup.getValidationLookupName());
                            cleanupUnderscores(lookup);
                            /*
                             * Enumerate the validation lookup types.
                             */
                            for (MValidationLookupType lookupType : lookup.getMValidationLookupTypes())
                            {
                                updateStatus("Converting ValidationLookupType " + lookupType.getId());
                                if (lookupType.getValidText() != null)
                                    lookupType.setAttribute(MValidationLookupType.METADATAENTRYID, lookupType.getValidText());
                                cleanupUnderscores(lookupType);
                            }
                        }
                        /*
                         * Enumerate the validation externals.
                         */
                        for (MValidationExternal validation : resource.getMValidationExternals())
                        {
                            updateStatus("Converting ValidationExternal " + validation.getId());
                            if (validation.getValidationExternalName() != null)
                                validation.setAttribute(MValidationExternal.METADATAENTRYID, validation.getValidationExternalName());
                            cleanupUnderscores(validation);
                            /*
                             * Enumerate the validation external types.
                             */
                            for (MValidationExternalType externalType : validation.getMValidationExternalTypes())
                            {
                                updateStatus("Converting ValidationExternalType " + externalType.getId());
                                int length = externalType.getSearchField().length();
                                if (length > 0)
                                {
                                    if (length > 32)
                                        length = 32;
                                    externalType.setAttribute(MValidationExternalType.METADATAENTRYID, 
                                                                externalType.getSearchField().substring(0, length - 1));
                                }
                                cleanupUnderscores(externalType);
                            }
                        }
                        /*
                         * Enumerate the validation expressions.
                         */
                        for (MValidationExpression validationExpression : resource.getMValidationExpressions())
                        {
                            updateStatus("Converting ValidationExpression " + validationExpression.getId());
                            if (validationExpression.getValidationExpressionID() != null)
                                validationExpression.setAttribute(MValidationExpression.METADATAENTRYID, 
                                                                validationExpression.getValidationExpressionID());
                            cleanupUnderscores(validationExpression);
                        }
                    }
                    setResponse(JOptionPane.OK_OPTION);                   
                }
                catch (Throwable t)
                {
                    LOG.error("Caught exception", t);
                    // Todo: MetadataPanel.construct show error dialog
                }
                return mSystem;
            }

            public void finished()
            {
                updateStatus("Metadata converted Successfully");
                AdminFrame.getInstance().setStatusText("Metadata converted Successfully");
            }
        };


        mPanel = new JPanel(new BorderLayout());
        mTextArea = new JTextArea();
        mTextArea.setEditable(false);
        mTextArea.setLineWrap(true);
        mTextArea.setWrapStyleWord(true);
 
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(mTextArea);
        mPanel.add(scrollPane, BorderLayout.CENTER);
        mPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(mPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        getContentPane().add(mPanel);
        pack();
        setResizable(false);
        setSize(580, 360);
        SwingUtils.centerOnFrame(this, AdminFrame.getInstance());
        setResponse(JOptionPane.CANCEL_OPTION);
        worker.start();
        setVisible(true);
    }
    
    /*
     * The RETS 1.7.2 spec has an anomaly between the KeyField declaration in the
     * METADATA-RESOURCE (RETSID) and the StandardName elsewhere. If we removed 
     * the underscore in the RETSID, we need to adjust the names elsewhere.
     */
    private void cleanupKeyFields(MetaObject metaObject)
    {
        for (String attribute : metaObject.getAttributeNames())
        {
            String         value    = metaObject.getAttributeAsString(attribute);
            if (mKeyMap.containsKey(value) && !attribute.equals("DBName"))
            {
                try
                {
                    metaObject.setAttribute(attribute, mKeyMap.get(value), false);
                }
                catch (Exception e) 
                {
                    updateStatus(e.toString());
                } 
            }
        }
    }
    
    private void cleanupUnderscores(MetaObject metaObject)
    {
        for (String attribute : metaObject.getAttributeNames())
        {
            AttrType<?> attrType    = metaObject.getAttributeType(attribute);
            String         value    = metaObject.getAttributeAsString(attribute);
            if (attrType instanceof AttrAlphanum && value != null)
            {
                value = value.replace("_", "");
                try
                {
                    metaObject.setAttribute(attribute, value, false);
                }
                catch (Exception e) 
                {
                    updateStatus(e.toString());
                } 
            }
        }
    }
    
    public Metadata getMetadata()
    {
        return mMetadata;
    }
    
    private void updateStatus(final String msg)
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    mTextArea.append(msg + "\n");
                }
            }
        );
    }
    
    JPanel mPanel;
    JTextArea mTextArea;
    
    Map<String, String> mKeyMap = new HashMap<String, String>();
    Metadata mMetadata;
    MSystem mSystem;

    private static final Logger LOG =
        Logger.getLogger(MetadataConversionDialog.class);
}
