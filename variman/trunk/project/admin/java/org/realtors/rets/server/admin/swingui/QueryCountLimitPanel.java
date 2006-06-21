package org.realtors.rets.server.admin.swingui;

import javax.swing.*;

public class QueryCountLimitPanel extends JPanel
{
    public QueryCountLimitPanel()
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        String[] limitPeriodStrings = { PER_HOUR, PER_DAY, PER_MINUTE };

        mLimitField = createLimitField(1);
        restrictMaximumSize(mLimitField);
        add(mLimitField);
        add(Box.createHorizontalStrut(5));
        mLimitPeriod = new JComboBox(limitPeriodStrings);
        restrictMaximumSize(mLimitPeriod);
        add(mLimitPeriod);
        add(Box.createHorizontalGlue());

        setEnabled(false);
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

    private void restrictMaximumSize(JComponent component)
    {
        component.setMaximumSize(component.getPreferredSize());
    }

    private static final String PER_HOUR = "per hour";
    private static final String PER_DAY = "per day";
    private static final String PER_MINUTE = "per minute";

    private WholeNumberField mLimitField;
    private JComboBox mLimitPeriod;
}
