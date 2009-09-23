package org.realtors.rets.server.admin.swingui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        mTopLeftInsets = new Insets(0, 0, 0, 5);
        mTopRightInsets = new Insets(0, 0, 0, 0);
        mLeftInsets = new Insets(5, 0, 0, 5);
        mRightInsets = new Insets(5, 0, 0, 0);
        mLabels = new ArrayList();
        mComponents = new ArrayList();
    }

    public void addRow(String text, JComponent component)
    {
        addRow(text, component, GridBagConstraints.HORIZONTAL);
    }

    public void addRow(String text, JComponent component, int fill)
    {
        addRow(new JLabel(text), component, fill);
    }

    public void addRow(JComponent label, JComponent component)
    {
        addRow(label, component, GridBagConstraints.HORIZONTAL);
    }

    public void addRow(JComponent label, JComponent component, int fill)
    {
        Insets leftInsets;
        Insets rightInsets;
        if (mLabels.size() == 0)
        {
            leftInsets = mTopLeftInsets;
            rightInsets = mTopRightInsets;
        }
        else
        {
            leftInsets = mLeftInsets;
            rightInsets = mRightInsets;
        }

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

        mLabels.add(label);
        mComponents.add(component);
    }

    public int getNumberOfRows()
    {
        return mLabels.size();
    }

    public void setRowVisible(int row, boolean visible)
    {
        JComponent label = (JComponent) mLabels.get(row);
        JComponent component = (JComponent) mComponents.get(row);
        label.setVisible(visible);
        component.setVisible(visible);
    }


    private GridBagLayout mGridBag;
    private GridBagConstraints mConstraints;
    private Insets mTopLeftInsets;
    private Insets mLeftInsets;
    private Insets mTopRightInsets;
    private Insets mRightInsets;
    private List mLabels;
    private List mComponents;
}
