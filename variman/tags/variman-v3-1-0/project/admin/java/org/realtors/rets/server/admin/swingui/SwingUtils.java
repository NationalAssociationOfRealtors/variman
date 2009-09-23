package org.realtors.rets.server.admin.swingui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import javax.swing.InputMap;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.SpringLayout.Constraints;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import javax.swing.text.Utilities;

/**
 * Created by IntelliJ IDEA. User: dave Date: May 13, 2004 Time: 12:06:32 PM To
 * change this template use File | Settings | File Templates.
 */
public class SwingUtils
{
    public static void centerOnFrame(Component component, Frame frame)
    {
        Point frameLocation = frame.getLocationOnScreen();
        Dimension frameSize = frame.getSize();
        int frameCenterX = frameLocation.x + (frameSize.width / 2);
        int frameCenterY = frameLocation.y + (frameSize.height / 2);
        centerRelativeTo(component, frameCenterX, frameCenterY);
    }

    public static void centerOnScreen(Component component)
    {
        Dimension screenSize = component.getToolkit().getScreenSize();
        int screenCenterX = screenSize.width / 2;
        int screenCenterY = screenSize.height / 2;
        centerRelativeTo(component, screenCenterX, screenCenterY);
    }

    public static void centerRelativeTo(Component component, int x, int y)
    {
        Dimension componentSize = component.getSize();
        component.setLocation((x - (componentSize.width / 2)),
                (y - (componentSize.height / 2)));
    }

    public static AdminFrame getAdminFrame()
    {
        return sAdminFrame;
    }

    public static void setAdminFrame(AdminFrame adminFrame)
    {
        sAdminFrame = adminFrame;
    }

    private static SpringLayout.Constraints getConstraintsForComponent(int row,
            int col, Container parent, int cols)
    {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    public static void SpringLayoutGrid(Container parent, int rows, int cols,
            int initialX, int initialY, int xPad, int yPad)
    {
        SpringLayout layout;
        layout = (SpringLayout) parent.getLayout();

        Spring x = Spring.constant(initialX);
        for (int col = 0; col < cols; col++)
        {
            Spring width = Spring.constant(0);
            /*
             * Determine the maximum width for all components in this column.
             */
            for (int row = 0; row < rows; row++)
            {
                width = Spring.max(width, getConstraintsForComponent(row, col,
                        parent, cols).getWidth());
            }
            /*
             * Set the width for each component in this column.
             */
            for (int row = 0; row < rows; row++)
            {
                SpringLayout.Constraints constraints = getConstraintsForComponent(
                        row, col, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        Spring y = Spring.constant(initialY);
        for (int row = 0; row < rows; row++)
        {
            Spring height = Spring.constant(0);
            /*
             * Determine the maximum height for all components in this row.
             */
            for (int col = 0; col < cols; col++)
            {
                height = Spring.max(height, getConstraintsForComponent(row,
                        col, parent, cols).getHeight());
            }
            /*
             * Set the height for each component in this row.
             */
            for (int col = 0; col < cols; col++)
            {
                SpringLayout.Constraints constraints = getConstraintsForComponent(
                        row, col, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }
        /*
         * Finally, set the constraints for the parent container.
         */
        SpringLayout.Constraints parentConstraints = layout
                .getConstraints(parent);
        parentConstraints.setConstraint(SpringLayout.SOUTH, y);
        parentConstraints.setConstraint(SpringLayout.EAST, x);
    }

    public static void addTextComponentActions()
    {
        // note: we can safely register all keystrokes even for password fields:
        // the cut/copy actions won't work (unless the client property
        // "JPasswordField.cutCopyAllowed" has been set on the component)
        // and the deleteTpPrevious/NextAction explicitly check for password
        // fields.
        String[] keys = { "TextField", "FormattedTextField", "PasswordField",
                "TextArea", "TextPane", "EditorPane" };
        registerActions(keys);
    }

    private static void registerActions(String[] propertyPrefixes)
    {
        DeleteToEndOfWordAction deleteToEndOfWordAction = new DeleteToEndOfWordAction();
        DeleteToStartOfWordAction deleteToStartOfWordAction = new DeleteToStartOfWordAction();
        DefaultEditorKit.CopyAction copyAction = new DefaultEditorKit.CopyAction();
        DefaultEditorKit.CutAction cutAction = new DefaultEditorKit.CutAction();
        DefaultEditorKit.PasteAction pasteAction = new DefaultEditorKit.PasteAction();

        for (int i = 0; i < propertyPrefixes.length; i++)
        {
            String propertyPrefix = propertyPrefixes[i];
            UIDefaults defaults = UIManager.getDefaults();
            Object o = defaults.get(propertyPrefix + ".focusInputMap");
            InputMap inputMap = (InputMap) o;

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,
                    InputEvent.SHIFT_MASK, false), pasteAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                    InputEvent.SHIFT_MASK, false), cutAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,
                    InputEvent.CTRL_MASK, false), copyAction);

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,
                    InputEvent.CTRL_MASK, false), deleteToStartOfWordAction);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                    InputEvent.CTRL_MASK, false), deleteToEndOfWordAction);
        }
    }
    
    /*
     * Create an Edit menu with standard edit items cut, paste and select all.
     */
    public static JMenu createEditMenu()
    {
        int keyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        JMenu editMenu = new JMenu("Edit");
    
        JMenuItem menuItem = new JMenuItem("Copy");
        
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, keyMask));
        menuItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                KeyboardFocusManager man = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                Component comp = man.getPermanentFocusOwner();
                if (comp instanceof JTextComponent)
                    ((JTextComponent)comp).copy();
            }
        });
        editMenu.add(menuItem);
        
        menuItem = new JMenuItem("Paste");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, keyMask));
        menuItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                KeyboardFocusManager man = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                Component comp = man.getPermanentFocusOwner();
                if (comp instanceof JTextComponent)
                    ((JTextComponent)comp).paste();
            }
        });
        editMenu.add(menuItem);
        
        editMenu.addSeparator();

        menuItem = new JMenuItem("Select All");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, keyMask));
        menuItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                KeyboardFocusManager man = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                Component comp = man.getPermanentFocusOwner();
                if (comp instanceof JTextComponent)
                    ((JTextComponent)comp).selectAll();
            }
        });

        editMenu.add(menuItem);
        return editMenu;
    }


    private static AdminFrame sAdminFrame;
}

class DeleteToStartOfWordAction extends TextAction
{
    public static final String NAME = "delete-to-previous-word";

    public DeleteToStartOfWordAction()
    {
        super(NAME);
    }

    public void actionPerformed(ActionEvent e)
    {
        JTextComponent textComponent = getTextComponent(e);
        if (textComponent == null || !textComponent.isEditable())
        {
            UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
            return;
        }

        try
        {
            Document document = textComponent.getDocument();
            Caret caret = textComponent.getCaret();
            int caretIndex = caret.getDot();
            int markIndex = caret.getMark();

            int selectionEndIndex = Math.max(caretIndex, markIndex);
            int selectionStartIndex = Math.min(caretIndex, markIndex);
            int startIndex = getStartOfWordOffset(textComponent,
                    selectionStartIndex);

            if (startIndex != selectionEndIndex)
            {
                document.remove(startIndex, selectionEndIndex - startIndex);
            }
            else
                if (caretIndex > 0)
                {
                    UIManager.getLookAndFeel().provideErrorFeedback(
                            textComponent);
                }
        }
        catch (BadLocationException bl)
        {
            UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
        }
    }

    private int getStartOfWordOffset(JTextComponent target, int offset)
            throws BadLocationException
    {
        if (target instanceof JPasswordField)
        {
            return 0;
        }
        Element currentParagraph = Utilities
                .getParagraphElement(target, offset);
        int wordOffset = Utilities.getPreviousWord(target, offset);
        boolean isInPreviousParagraph = wordOffset < currentParagraph
                .getStartOffset();
        if (isInPreviousParagraph)
        {
            // noinspection UnnecessaryLocalVariable
            int endOfPreviousParagraph = Utilities.getParagraphElement(target,
                    wordOffset).getEndOffset() - 1;
            wordOffset = endOfPreviousParagraph;
        }
        return wordOffset;
    }
}

class DeleteToEndOfWordAction extends TextAction
{
    public static final String NAME = "delete-to-next-word";

    public DeleteToEndOfWordAction()
    {
        super(NAME);
    }

    public void actionPerformed(ActionEvent e)
    {
        JTextComponent textComponent = getTextComponent(e);
        if (textComponent == null || !textComponent.isEditable())
        {
            UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
            return;
        }

        try
        {
            Document document = textComponent.getDocument();
            Caret caret = textComponent.getCaret();
            int caretIndex = caret.getDot();
            int markIndex = caret.getMark();

            int selectionEndIndex = Math.max(caretIndex, markIndex);
            int selectionStartIndex = Math.min(caretIndex, markIndex);
            int endIndex = getEndOfWordOffset(textComponent, selectionEndIndex);

            if (endIndex != selectionEndIndex)
            {
                document.remove(selectionStartIndex, endIndex
                        - selectionStartIndex);
            }
            else
                if (caretIndex > 0)
                {
                    UIManager.getLookAndFeel().provideErrorFeedback(
                            textComponent);
                }
        }
        catch (BadLocationException bl)
        {
            UIManager.getLookAndFeel().provideErrorFeedback(textComponent);
        }
    }

    private int getEndOfWordOffset(JTextComponent target, int offset)
            throws BadLocationException
    {
        Element currentPararaph = Utilities.getParagraphElement(target, offset);
        int currentParagraphEndOffset = currentPararaph.getEndOffset();
        if (target instanceof JPasswordField)
        {
            return currentParagraphEndOffset - 1;
        }
        int wordOffset = offset;
        try
        {
            int startOfNextWord = Utilities.getNextWord(target, offset);
            int endOfCurrentWord = Utilities.getWordEnd(target, offset);
            boolean isInWhiteSpace = startOfNextWord == endOfCurrentWord;
            if (isInWhiteSpace)
            {
                wordOffset = Utilities.getWordEnd(target, startOfNextWord);
            }
            else
            {
                wordOffset = endOfCurrentWord;
            }
            if (wordOffset >= currentParagraphEndOffset
                    && offset != currentParagraphEndOffset - 1)
            {
                wordOffset = currentParagraphEndOffset - 1;
            }
        }
        catch (BadLocationException badLocationException)
        {
            int end = target.getDocument().getLength();
            if (wordOffset != end)
            {
                if (offset != currentParagraphEndOffset - 1)
                {
                    wordOffset = currentParagraphEndOffset - 1;
                }
                else
                {
                    wordOffset = end;
                }
            }
            else
            {
                throw badLocationException;
            }
        }
        return wordOffset;
    }
}

class TimedPasswordListener implements DocumentListener, ActionListener 
{
    private Timer timer = new Timer(3000, this);
    private char echoChar;
    private JPasswordField pwf;

    public TimedPasswordListener(JPasswordField jp)
    {
        pwf = jp;
    }

    public void insertUpdate(DocumentEvent e) 
    {
        showText(e, 3);
    }

    public void removeUpdate(DocumentEvent e) 
    {
        //showText(e, 3);
    }

    public void changedUpdate(DocumentEvent e) 
    {
            // Plain text components do not fire these events
    }

    public void showText(DocumentEvent e, int timeOut) 
    {
        if (0 != pwf.getEchoChar())
                echoChar = pwf.getEchoChar();
        pwf.setEchoChar((char)0);

        timer.stop();
        timer.setDelay(timeOut * 1000);
        timer.setInitialDelay(timeOut * 1000);
        timer.setRepeats(false);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) 
    {
            pwf.setEchoChar(echoChar);
    }
}

