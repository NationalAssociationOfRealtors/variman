package org.realtors.rets.server.admin.swingui;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
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
        buttonBox.add(new JButton(new OkAction()));
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new CancelAction()));
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(new JButton(new TestConnectionAction()));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(buttonBox);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getContentPane().add(content);
        pack();
        setResizable(false);
        SwingUtils.centerOnFrame(this, AdminFrame.getInstance());
    }

    private void createDatabaseTypes(DatabaseConfig config)
    {
        mDatabaseTypes = new JComboBox();
        mDatabaseTypeObjects = new DatabaseType[]{
            DatabaseType.POSTGRESQL,
            DatabaseType.MYSQL,
            DatabaseType.SQLSERVER_JSQL,
            DatabaseType.SQLSERVER_JTDS,
        };
        for (int i = 0; i < mDatabaseTypeObjects.length; i++)
        {
            DatabaseType databaseType = mDatabaseTypeObjects[i];
            mDatabaseTypes.addItem(databaseType.getLongName());
        }
        mDatabaseTypes.setSelectedItem(config.getDatabaseType().getLongName());
    }

    public int showDialog()
    {
        mOption = JOptionPane.CANCEL_OPTION;
        setVisible(true);
        return mOption;
    }

    private class OkAction extends AbstractAction
    {
        public OkAction()
        {
            super("Ok");
        }

        public void actionPerformed(ActionEvent event)
        {
            setVisible(false);
            mOption = JOptionPane.OK_OPTION;
        }

    }

    private class CancelAction extends AbstractAction
    {
        public CancelAction()
        {
            super("Cancel");
        }

        public void actionPerformed(ActionEvent event)
        {
            setVisible(false);
            mOption = JOptionPane.CANCEL_OPTION;
        }

    }

    private class TestConnectionAction extends AbstractAction
    {
        public TestConnectionAction()
        {
            super("Test Connection...");
        }

        public void actionPerformed(ActionEvent event)
        {
            testConnection();
        }
    }

    private DatabaseType getDatabaseType()
    {
        return mDatabaseTypeObjects[mDatabaseTypes.getSelectedIndex()];
    }

    private void testConnection()
    {
        Connection connection = null;
        try
        {
            DatabaseType type = getDatabaseType();
            String driver = type.getDriverClass();
            String hostName = mHostName.getText();
            String databaseName = mDatabaseName.getText();
            String url = type.getUrl(hostName, databaseName);
            String username = mUsername.getText();
            String password = new String(mPassword.getPassword());

            LOG.debug("driver: " + driver);
            LOG.debug("url: " + url);
            LOG.debug("user: " + username);
            LOG.debug("password = " + password);
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            if (!connection.isClosed())
            {
                JOptionPane.showMessageDialog(
                    this, "Database connection succeeded.", "Test Database",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                // Most likely, an exception will get thrown before we ever get
                // here, but just in case...
            }
        }
        catch (Exception e)
        {
            LOG.error("Caught exception", e);
            JOptionPane.showMessageDialog(
                this, "Database connection failed.\n" + e.getMessage(),
                "Test Database",
                JOptionPane.WARNING_MESSAGE);
        }
        finally
        {
            closeConnection(connection);
        }
    }

    private void closeConnection(Connection connection)
    {
        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            LOG.error("Caught exception", e);
        }
    }

    public void updateConfig(DatabaseConfig config)
    {
        config.setDatabaseType(getDatabaseType());
        config.setHostName(mHostName.getText());
        config.setDatabaseName(mDatabaseName.getText());
        config.setUsername(mUsername.getText());
        config.setPassword(new String(mPassword.getPassword()));
    }

    private static final Logger LOG =
        Logger.getLogger(DatabasePropertiesDialog.class);

    private static final int TEXT_WIDTH = 25;
    private JComboBox mDatabaseTypes;
    private DatabaseType[] mDatabaseTypeObjects;
    private JTextField mHostName;
    private JTextField mDatabaseName;
    private JTextField mUsername;
    private JPasswordField mPassword;
    private int mOption;
}
