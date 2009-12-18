package org.realtors.rets.server.admin.swingui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.realtors.rets.server.QueryCount;

public class QueryCountLimitPanel extends JPanel
{
    public QueryCountLimitPanel()
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        String[] limitPeriodStrings = { PER_MINUTE, PER_HOUR, PER_DAY };

        mLimitField = createLimitField(1);
        restrictMaximumSize(mLimitField);
        add(mLimitField);
        add(Box.createHorizontalStrut(5));
        mLimitPeriod = new JComboBox(limitPeriodStrings);
        restrictMaximumSize(mLimitPeriod);
        add(mLimitPeriod);
        add(Box.createHorizontalGlue());
    }

    private WholeNumberField createLimitField(int number)
    {
        WholeNumberField field = new WholeNumberField(number, 5);
        field.setMinValue(1);
        return field;
    }

    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        mLimitField.setEnabled(enabled);
        mLimitPeriod.setEnabled(enabled);
    }

    public long getLimit()
    {
        return mLimitField.getValue();
    }

    public void setLimit(long limit)
    {
        mLimitField.setValue((int) limit);
    }

    public QueryCount.LimitPeriod getLimitPeriod()
    {
        Object selectedLimitPeriod = mLimitPeriod.getSelectedItem();
        if (selectedLimitPeriod.equals(PER_MINUTE))
            return QueryCount.PER_MINUTE;
        else if (selectedLimitPeriod.equals(PER_HOUR))
            return QueryCount.PER_HOUR;
        else if (selectedLimitPeriod.equals(PER_DAY))
            return QueryCount.PER_DAY;
        else
        {
            throw new IllegalArgumentException(
                "Unknown selected limit period: " + selectedLimitPeriod);
        }
    }

    public void setLimitPeriod(QueryCount.LimitPeriod limitPeriod)
    {
        if (limitPeriod.equals(QueryCount.PER_DAY))
            mLimitPeriod.setSelectedItem(PER_DAY);
        else if (limitPeriod.equals(QueryCount.PER_HOUR))
            mLimitPeriod.setSelectedItem(PER_HOUR);
        else if (limitPeriod.equals(QueryCount.PER_MINUTE))
            mLimitPeriod.setSelectedItem(PER_MINUTE);
        else
        {
            throw new IllegalArgumentException(
                "Unknown limit period: " + limitPeriod);
        }
    }

    private void restrictMaximumSize(JComponent component)
    {
        component.setMaximumSize(component.getPreferredSize());
    }

    private static final String PER_MINUTE = "per minute";
    private static final String PER_HOUR = "per hour";
    private static final String PER_DAY = "per day";

    private WholeNumberField mLimitField;
    private JComboBox mLimitPeriod;
}
