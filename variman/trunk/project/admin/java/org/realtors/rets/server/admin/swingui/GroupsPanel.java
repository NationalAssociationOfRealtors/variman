package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.GroupUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.RuleDescription;

public class GroupsPanel extends JPanel
{
    public GroupsPanel()
    {
        super(new BorderLayout());
        mGroupList = new JList();
        mGroupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mGroupList.getSelectionModel().addListSelectionListener(
            new OnSelectionChanged());
        mGroupList.addMouseListener(new ListDoubleClickListener());

        JPanel panel = new JPanel(new BorderLayout());
        TextValuePanel tvp = new TextValuePanel();
        mName = new JLabel();
        tvp.addRow("Name:", mName);
        mDescription = new JLabel();
        tvp.addRow("Description:", mDescription);
        tvp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(tvp, BorderLayout.NORTH);
        panel.add(createRulesPanel(), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              mGroupList, panel);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        mPopup = new JPopupMenu();
        mAddGroupAction = new AddGroupAction();
        mPopup.add(mAddGroupAction);
        mRemoveGroupAction = new RemoveGroupAction(this);
        mPopup.add(mRemoveGroupAction);

        PopupListener popupListener = new PopupListener();
        mGroupList.addMouseListener(popupListener);
        splitPane.addMouseListener(popupListener);

    }

    private JPanel createRulesPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new HeaderPanel("Rules"), BorderLayout.NORTH);

        JPanel box = new JPanel(new BorderLayout());
        mRulesListModel = new ListListModel();
        mRulesListModel.setFormatter(RULE_FORMAETTER);
        mRulesList = new JList(mRulesListModel);
        mRulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        mRulesList.getSelectionModel().addListSelectionListener(
//            new OnGroupSelectionChanged());
        box.add(mRulesList);
        box.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        panel.add(box, BorderLayout.CENTER);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
//        mAddGroupButtonAction = new AddGroupButtonAction(this);
        buttonBox.add(new JButton("New Rule..."));
        buttonBox.add(Box.createHorizontalStrut(5));
//        mRemoveGroupButtonAction = new RemoveGroupButtonAction(this);
        buttonBox.add(new JButton("Remove Rule..."));
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton("Edit Rule..."));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(buttonBox, BorderLayout.SOUTH);
        return panel;
    }

    public Action getAddGroupAction()
    {
        return mAddGroupAction;
    }

    public Action getRemoveGroupAciton()
    {
        return mRemoveGroupAction;
    }

    private void updateComponentsFromSelection()
    {
        int selection = mGroupList.getSelectedIndex();
        if (selection != -1)
        {
            Group group = (Group) mGroupListModel.getElementAt(selection);
            mName.setText(group.getName());
            mDescription.setText(group.getDescription());
            mRemoveGroupAction.setEnabled(true);
            updateRulesList(group);
        }
        else
        {
            mName.setText("");
            mDescription.setText("");
            mRemoveGroupAction.setEnabled(false);
            mRulesListModel.setList(Collections.EMPTY_LIST);
        }
    }

    private void updateRulesList(Group group)
    {
        RetsConfig retsConfig = Admin.getRetsConfig();
        List ruleDescriptions = Collections.EMPTY_LIST;
        List securityConstraints = retsConfig.getSecurityConstraints();
        for (int i = 0; i < securityConstraints.size(); i++)
        {
            GroupRules rules = (GroupRules) securityConstraints.get(i);
            if (rules.getGroupName().equals(group.getName()))
            {
                ruleDescriptions = rules.getRules();
                break;
            }
        }
        mRulesListModel.setList(ruleDescriptions);
        mRulesList.setSelectedIndex(-1);
    }

    public void populateList()
    {
        final int selection = mGroupList.getSelectedIndex();
        SwingWorker worker = new SwingWorker()
        {
            public Object construct()
            {
                List groups = Collections.EMPTY_LIST;
                try
                {
                    groups = GroupUtils.findAll();
                }
                catch (Throwable t)
                {
                    LOG.error("Caught exception", t);
                    // Todo: GroupsPanel.construct show error dialog
                }
                return groups;
            }

            public void finished()
            {
                List groups = (List) get();
                setGroupListModel(new ListListModel(groups));
                int newSelection = selection;
                if (newSelection >= groups.size())
                {
                    newSelection = groups.size() - 1;
                }
                mGroupList.setSelectedIndex(newSelection);
                updateComponentsFromSelection();
            }
        };
        worker.start();
    }

    private void setGroupListModel(ListListModel model)
    {
        mGroupListModel = model;
        mGroupList.setModel(model);
    }

    public Group getSelectedGroup()
    {
        return (Group) mGroupList.getSelectedValue();
    }

    private class OnSelectionChanged implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            updateComponentsFromSelection();
        }
    }

    private class ListDoubleClickListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent event)
        {
            if (event.getClickCount() == 2)
            {
            }
        }
    }

    private class PopupListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent event)
        {
            maybeShowPopup(event);
        }

        public void mouseReleased(MouseEvent event)
        {
            maybeShowPopup(event);
        }

        private void maybeShowPopup(MouseEvent event)
        {
            if (mPopup.isPopupTrigger(event))
            {
                mPopup.show(event.getComponent(), event.getX(), event.getY());
            }
        }
    }

    private static class RuleDescriptionFormatter
        implements ListElementFormatter
    {
        public Object format(Object object)
        {
            RuleDescription description = (RuleDescription) object;
            StringBuffer buffer = new StringBuffer();

            buffer.append(description.getResource()).append(":")
                .append(description.getRetsClass());
            return buffer.toString();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(GroupsPanel.class);
    private static final ListElementFormatter RULE_FORMAETTER =
            new RuleDescriptionFormatter();

    private AddGroupAction mAddGroupAction;
    private JList mGroupList;
    private JLabel mName;
    private JLabel mDescription;
    private JPopupMenu mPopup;
    private ListListModel mGroupListModel;
    private RemoveGroupAction mRemoveGroupAction;
    private JList mRulesList;
    private ListListModel mRulesListModel;
}
