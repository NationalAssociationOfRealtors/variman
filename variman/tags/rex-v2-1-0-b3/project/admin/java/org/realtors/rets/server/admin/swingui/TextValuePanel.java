package org.realtors.rets.server.admin.swingui;

import java.awt.*;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: May 6, 2004
 * Time: 3:00:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextValuePanel extends JPanel
{
    public TextValuePanel()
    {
        mGridBag = new GridBagLayout();
        setLayout(mGridBag);
        mConstraints = new GridBagConstraints();
        mConstraints.weighty = 0.0;
        mConstraints.anchor = GridBagConstraints.WEST;
        mNumberAdded = 0;
        mTopLeftInsets = new Insets(0, 0, 0, 5);
        mTopRightInsets = new Insets(0, 0, 0, 0);
        mLeftInsets = new Insets(5, 0, 0, 5);
        mRightInsets = new Insets(5, 0, 0, 0);
    }

    public void addRow(String text, JComponent component)
    {
        addRow(text, component, GridBagConstraints.HORIZONTAL);
    }

    public void addRow(String text, JComponent component, int fill)
    {
        Insets leftInsets;
        Insets rightInsets;
        if (mNumberAdded == 0)
        {
            leftInsets = mTopLeftInsets;
            rightInsets = mTopRightInsets;
        }
        else
        {
            leftInsets = mLeftInsets;
            rightInsets = mRightInsets;
        }

        JLabel label = new JLabel(text);
        mConstraints.gridwidth = GridBagConstraints.RELATIVE;
        mConstraints.fill = GridBagConstraints.NONE;
        mConstraints.weightx = 0.0;
        mConstraints.weighty = 0.0;
        mConstraints.insets = leftInsets;
        add(label, mConstraints);

        mConstraints.gridwidth = GridBagConstraints.REMAINDER;
        mConstraints.fill = fill;
        mConstraints.weightx = 1.0;
        if ((fill == GridBagConstraints.VERTICAL) ||
            (fill == GridBagConstraints.BOTH))
        {
            mConstraints.weighty = 1.0;
        }
        mConstraints.insets = rightInsets;
        add(component, mConstraints);
        mNumberAdded++;
    }

    
    private GridBagLayout mGridBag;
    private GridBagConstraints mConstraints;
    private int mNumberAdded;
    private Insets mTopLeftInsets;
    private Insets mLeftInsets;
    private Insets mTopRightInsets;
    private Insets mRightInsets;
}
