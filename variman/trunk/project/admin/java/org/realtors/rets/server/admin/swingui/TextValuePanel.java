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
        mConstraints.fill = GridBagConstraints.HORIZONTAL;
        mConstraints.weighty = 0.0;
        mNumberAdded = 0;
        mTopLeftInsets = new Insets(0, 0, 0, 5);
        mTopRightInsets = new Insets(0, 0, 0, 0);
        mLeftInsets = new Insets(5, 0, 0, 5);
        mRightInsets = new Insets(5, 0, 0, 0);
    }

    public void addRow(String text, JComponent component)
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
        mConstraints.weightx = 0.0;
        mConstraints.insets = leftInsets;
        add(label, mConstraints);

        mConstraints.gridwidth = GridBagConstraints.REMAINDER;
        mConstraints.fill = GridBagConstraints.HORIZONTAL;
        mConstraints.weightx = 1.0;
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
