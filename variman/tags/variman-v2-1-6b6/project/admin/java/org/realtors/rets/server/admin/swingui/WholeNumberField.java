package org.realtors.rets.server.admin.swingui;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * Shamelessly borrowed from Sun:
 *
 * http://java.sun.com/docs/books/tutorial/uiswing/components/example-swing/WholeNumberField.java
 */
public class WholeNumberField extends JTextField
{
    public WholeNumberField(int value, int columns)
    {
        super(columns);
        toolkit = Toolkit.getDefaultToolkit();
        integerFormatter = NumberFormat.getNumberInstance(Locale.US);
        integerFormatter.setParseIntegerOnly(true);
        setValue(value);
    }

    public int getValue()
    {
        int retVal = 0;
        try
        {
            retVal = integerFormatter.parse(getText()).intValue();
        }
        catch (ParseException e)
        {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            toolkit.beep();
            LOG.error("Unable to parse integer: " + getText());
        }
        return retVal;
    }

    public void setValue(int value)
    {
        setText(integerFormatter.format(value));
    }

    protected Document createDefaultModel()
    {
        return new WholeNumberDocument();
    }

    protected class WholeNumberDocument extends PlainDocument
    {
        public void insertString(int offs,
                                 String str,
                                 AttributeSet a)
            throws BadLocationException
        {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;

            for (int i = 0; i < result.length; i++)
            {
                if (Character.isDigit(source[i]))
                {
                    result[j++] = source[i];
                }
                else
                {
                    toolkit.beep();
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(WholeNumberField.class);

    private Toolkit toolkit;
    private NumberFormat integerFormatter;
}
