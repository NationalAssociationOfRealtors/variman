package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.admin.Admin;

public class LogPanel extends AdminTab
{
    public LogPanel()
    {
        super(new BorderLayout());
        readLoggingProperties();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(new HeaderPanel("Logging Parameters"));
        TextValuePanel logConfig = new TextValuePanel();

        mLogLevelItems = new ArrayList();
        mLogLevelItems.add(new ComboItem("Normal", LOG_LEVEL_NORMAL));
        mLogLevelItems.add(new ComboItem("Debug", LOG_LEVEL_DEBUG));
        mLogLevel = new JComboBox();
        addAllItems(mLogLevel, mLogLevelItems);
        logConfig.addRow("Log Level:", mLogLevel, GridBagConstraints.NONE);

        mSqlLevelItems = new ArrayList();
        mSqlLevelItems.add(new ComboItem("Enabled", SQL_LEVEL_ENABLED));
        mSqlLevelItems.add(new ComboItem("Disabled", SQL_LEVEL_DISABLED));
        mSqlLevel = new JComboBox();
        addAllItems(mSqlLevel, mSqlLevelItems);
        logConfig.addRow("SQL Logging:", mSqlLevel, GridBagConstraints.NONE);

        logConfig.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        content.add(logConfig);

        content.add(new HeaderPanel("Current Log File"));
        add(content, BorderLayout.NORTH);

        JPanel logPanel = new JPanel(new BorderLayout());
        mTextArea = new JTextArea();
        mTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mTextArea);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        logPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        add(logPanel, BorderLayout.CENTER);
        refreshLevels();

        mLogLevel.addActionListener(new OnLogLevelChanged());
        mSqlLevel.addActionListener(new OnSqlLevelChanged());
    }

    private void addAllItems(JComboBox comboBox, List items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            ComboItem comboItem = (ComboItem) items.get(i);
            comboBox.addItem(comboItem);
        }
    }

    private void readLoggingProperties()
    {
        String logPropertiesFileName = Admin.getLogConfigFile();
        mLogPropertiesFile = new File(logPropertiesFileName);
        mLogProperties = new Properties();
        mLogProperties.setProperty(LOG_LEVEL, LOG_LEVEL_NORMAL);
        mLogProperties.setProperty(SQL_LOG_LEVEL, SQL_LEVEL_ENABLED);
        try
        {
            if (mLogPropertiesFile.exists() && mLogPropertiesFile.canRead())
            {
                    mLogProperties.load(new FileInputStream(mLogPropertiesFile));
            }
        }
        catch (IOException e)
        {
            // No logging file found, so just ignore and use defaults
        }
    }

    private void writeLoggingProperties()
    {
        try
        {
            FileOutputStream outputStream =
                new FileOutputStream(mLogPropertiesFile);
            mLogProperties.store(outputStream, null);
        }
        catch (IOException e)
        {
            LOG.error("Caught", e);
        }
    }

    private void refreshLevels()
    {
        String logLevel = mLogProperties.getProperty(LOG_LEVEL);
        ComboItem item = findByValue(mLogLevelItems, logLevel);
        mLogLevel.setSelectedItem(item);

        String sqlLevel = mLogProperties.getProperty(SQL_LOG_LEVEL);
        item = findByValue(mSqlLevelItems, sqlLevel);
        mSqlLevel.setSelectedItem(item);
    }

    private ComboItem findByValue(List comboItems, String value)
    {
        ComboItem foundItem = null;
        for (int i = 0; i < comboItems.size(); i++)
        {
            ComboItem item = (ComboItem) comboItems.get(i);
            if (item.value.equals(value))
            {
                foundItem = item;
                break;
            }
        }
        return foundItem;
    }

    public void refreshTab()
    {
        refreshLevels();
        SwingWorker worker = new SwingWorker()
        {
            public Object construct()
            {
                return readLog();
            }

            public void finished()
            {
                String log = (String) get();
                mTextArea.setText(log);
                mTextArea.setCaretPosition(log.length());
            }
        };
        worker.start();
    }

    private String readLog()
    {
        try
        {
            String homeDirectory = Admin.getHomeDirectory();
            String logFile = homeDirectory + File.separator + "logs" +
                             File.separator + "variman_webapp.txt";
            String log = IOUtils.readString(logFile);
            return log;
        }
        catch (IOException e)
        {
            LOG.error("Caught", e);
            return "";
        }
    }

    public void addToEditMenu(JMenu editMenu)
    {
        Action[] actions = mTextArea.getActions();
        Map actionMap = new HashMap();
        for (int i = 0; i < actions.length; i++)
        {
            Action action = actions[i];
            actionMap.put(action.getValue(Action.NAME), action);
        }

        int keyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        Action action = (Action) actionMap.get(DefaultEditorKit.copyAction);
        action.putValue(Action.NAME, "Copy");
        action.putValue(Action.ACCELERATOR_KEY,
                        KeyStroke.getKeyStroke(KeyEvent.VK_C,
                                               keyMask));
        editMenu.add(action);

        action = (Action) actionMap.get(DefaultEditorKit.pasteAction);
        action.putValue(Action.NAME, "Paste");
        action.putValue(Action.ACCELERATOR_KEY,
                        KeyStroke.getKeyStroke(KeyEvent.VK_V,
                                               keyMask));
        editMenu.add(action);

        editMenu.addSeparator();
        action = (Action) actionMap.get(DefaultEditorKit.selectAllAction);
        action.putValue(Action.NAME, "Select All");
        action.putValue(Action.ACCELERATOR_KEY,
                        KeyStroke.getKeyStroke(KeyEvent.VK_A,
                                               keyMask));
        editMenu.add(action);
    }

    private class OnLogLevelChanged extends AbstractAction
    {
        public void actionPerformed(ActionEvent event)
        {
            ComboItem item = (ComboItem) mLogLevel.getSelectedItem();
            mLogProperties.setProperty(LOG_LEVEL, item.value);
            writeLoggingProperties();
        }
    }

    private class OnSqlLevelChanged extends AbstractAction
    {
        public void actionPerformed(ActionEvent event)
        {
            ComboItem item = (ComboItem) mSqlLevel.getSelectedItem();
            mLogProperties.setProperty(SQL_LOG_LEVEL, item.value);
            writeLoggingProperties();
        }
    }

    private static class ComboItem
    {
        public ComboItem(String name, String value)
        {
            this.name = name;
            this.value = value;
        }

        public String toString()
        {
            return name;
        }

        public boolean equals(Object obj)
        {
            if (!(obj instanceof LogPanel.ComboItem))
            {
                return false;
            }
            LogPanel.ComboItem rhs = (LogPanel.ComboItem) obj;
            return new EqualsBuilder()
                .append(name, rhs.name)
                .append(value, rhs.value)
                .isEquals();
        }

        public int hashCode()
        {
            return new HashCodeBuilder()
                .append(name)
                .append(value)
                .toHashCode();
        }

        public String name;
        public String value;
    }

    private static final Logger LOG =
        Logger.getLogger(LogPanel.class);
    private static final String LOG_LEVEL = "variman.log.level";
    private static final String LOG_LEVEL_NORMAL = "info";
    private static final String LOG_LEVEL_DEBUG = "debug";
    private static final String SQL_LOG_LEVEL = "variman.log.sql_level";
    private static final String SQL_LEVEL_ENABLED = "all";
    private static final String SQL_LEVEL_DISABLED = "none";

    private List mLogLevelItems;
    private List mSqlLevelItems;
    private JTextArea mTextArea;
    private File mLogPropertiesFile;
    private Properties mLogProperties;
    private JComboBox mLogLevel;
    private JComboBox mSqlLevel;
}
