package org.realtors.rets.server.admin.swingui;

import javax.swing.*;

import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.DatabaseType;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 11, 2004
 * Time: 3:55:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabasePropertiesDialog extends JDialog
{
    public DatabasePropertiesDialog(DatabaseConfig config)
    {
        super(AdminFrame.getInstance());
        setModal(true);
        setTitle("Database Properties");

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        TextValuePanel tvp = new TextValuePanel();

        createDatabaseTypes(config);
        tvp.addRow("Database Type:", mDatabaseTypes);
        mHostName = new JTextField(config.getHostName(), TEXT_WIDTH);
        tvp.addRow("Host Name:", mHostName);
        mDatabaseName = new JTextField(config.getDatabaseName(), TEXT_WIDTH);
        tvp.addRow("Database Name:", mDatabaseName);
        mUsername = new JTextField(config.getUsername(), TEXT_WIDTH);
        tvp.addRow("Username:", mUsername);
        mPassword = new JPasswordField(config.getPassword(), TEXT_WIDTH);
        tvp.addRow("Password:", mPassword);
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(tvp);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(new JButton("Ok"));
        buttonBox.add(new JButton("Cancel"));
        buttonBox.add(new JButton("Test Connection..."));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(buttonBox);

        getContentPane().add(content);
        pack();
        setResizable(false);
    }

    private void createDatabaseTypes(DatabaseConfig config)
    {
        mDatabaseTypes = new JComboBox();
        mDatabaseTypeObjects = new DatabaseType[]{
            DatabaseType.POSTGRESQL,
            DatabaseType.SQLSERVER_JSQL
        };
        for (int i = 0; i < mDatabaseTypeObjects.length; i++)
        {
            DatabaseType databaseType = mDatabaseTypeObjects[i];
            mDatabaseTypes.addItem(databaseType.getLongName());
        }
        mDatabaseTypes.setSelectedItem(config.getDatabaseType().getLongName());
    }

    private static final int TEXT_WIDTH = 25;
    private JComboBox mDatabaseTypes;
    private DatabaseType[] mDatabaseTypeObjects;
    private JTextField mHostName;
    private JTextField mDatabaseName;
    private JTextField mUsername;
    private JPasswordField mPassword;
}
