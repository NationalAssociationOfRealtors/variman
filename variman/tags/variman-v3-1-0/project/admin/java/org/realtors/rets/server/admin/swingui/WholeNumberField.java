package org.realtors.rets.server.admin.swingui;

import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

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
        this(value, columns, NumberFormat.getNumberInstance(Locale.US));
    }

    public WholeNumberField(int value, int columns, NumberFormat format)
    {
        super(columns);
        mToolkit = Toolkit.getDefaultToolkit();
        mIntegerFormatter = format;
        mIntegerFormatter.setParseIntegerOnly(true);
        mMinValue = Integer.MIN_VALUE;
        mMaxValue = Integer.MAX_VALUE;
        setValue(value);
    }

    public void setMinValue(int minValue)
    {
        mMinValue = minValue;
    }

    public void setMaxValue(int maxValue)
    {
        mMaxValue = maxValue;
    }

    public int getValue()
    {
        int retVal = 0;
        try
        {
            retVal = mIntegerFormatter.parse(getText()).intValue();
        }
        catch (ParseException e)
        {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            mToolkit.beep();
            LOG.error("Unable to parse integer: " + getText());
        }
        return retVal;
    }

    public void setValue(int value)
    {
        setText(mIntegerFormatter.format(value));
    }

    public void reformatValue()
    {
        setValue(getValue());
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
            String currentText = getText(0, getLength());
            String beforeOffset = currentText.substring(0, offs);
            String afterOffset = currentText.substring(offs,
                                                       currentText.length());
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;

            boolean doInsert = true;
            for (int i = 0; i < result.length; i++)
            {
                if (Character.isDigit(source[i]))
                {
                    result[j++] = source[i];
                }
                else
                {
                    doInsert = false;
                }
            }

            String insertedText = new String(result, 0, j);
            String proposedResult = beforeOffset + insertedText + afterOffset;

            try
            {
                int value = mIntegerFormatter.parse(proposedResult).intValue();
                if ((value < mMinValue) || (value > mMaxValue))
                {
                    doInsert = false;
                }
            }
            catch (ParseException e)
            {
                doInsert = false;
            }

            if (doInsert)
            {
                super.insertString(offs, insertedText, a);
            }
            else
            {
                mToolkit.beep();
            }
        }
    }

    private static final Logger LOG =
        Logger.getLogger(WholeNumberField.class);

    private Toolkit mToolkit;
    private NumberFormat mIntegerFormatter;
    private int mMinValue;
    private int mMaxValue;
}
