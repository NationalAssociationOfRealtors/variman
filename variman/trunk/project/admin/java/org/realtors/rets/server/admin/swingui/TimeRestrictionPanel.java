package org.realtors.rets.server.admin.swingui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Calendar;

import javax.swing.*;

import org.realtors.rets.server.config.TimeRestriction;

public class TimeRestrictionPanel extends JPanel
{
    TimeRestrictionPanel(TimeRestriction timeRestriction)
    {
        super();
        String[] policyStrings = { NONE, ALLOW, DENY };
        String[] am_pm = { AM, PM };
        mMinuteFormat = NumberFormat.getNumberInstance(Locale.US);
        mMinuteFormat.setMinimumIntegerDigits(2);
        mReformatter = new WholeNumberReformatter();

        mPolicy = new JComboBox(policyStrings);
        mPolicy.addActionListener(new PolicyChangeListener());
        add(mPolicy);
        mStartHour = createHourField(12);
        add(mStartHour);
        add(new JLabel(":"));
        mStartMinutes = createMinuteField(0);
        add(mStartMinutes);
        mStartAmPm = new JComboBox(am_pm);
        mStartAmPm.setSelectedItem(AM);
        add(mStartAmPm);
        add(new JLabel(" to "));
        mEndHour = createHourField(12);
        add(mEndHour);
        add(new JLabel(":"));
        mEndMinute = createMinuteField(59);
        add(mEndMinute);
        mEndAmPm = new JComboBox(am_pm);
        mEndAmPm.setSelectedItem(PM);
        add(mEndAmPm);
        setTimeRestriction(timeRestriction);
    }

    private WholeNumberField createHourField(int number)
    {
        WholeNumberField field = new WholeNumberField(number, 2);
        field.setMinValue(1);
        field.setMaxValue(12);
        return field;
    }

    private WholeNumberField createMinuteField(int number)
    {
        WholeNumberField field = new WholeNumberField(number, 2, mMinuteFormat);
        field.setMinValue(0);
        field.setMaxValue(59);
        field.addFocusListener(mReformatter);
        return field;
    }

    public void setTimeRestriction(TimeRestriction timeRestriction)
    {
        if (timeRestriction == null)
        {
            mPolicy.setSelectedItem(NONE);
            setTimesEnabled(false);
        }
        else
        {
            setTimesEnabled(true);
            if (timeRestriction.getPolicy() == TimeRestriction.ALLOW)
            {
                mPolicy.setSelectedItem(ALLOW);
            }
            else
            {
                mPolicy.setSelectedItem(DENY);
            }
            Calendar start = timeRestriction.getStartAsCalendar();
            setToHour(mStartHour, start);
            setToMinutes(mStartMinutes, start);
            setToAmPm(mStartAmPm, start);
            Calendar end = timeRestriction.getEndAsCalendar();
            setToHour(mEndHour, end);
            setToMinutes(mEndMinute, end);
            setToAmPm(mEndAmPm, end);
        }
    }

    public TimeRestriction getTimeRestriction()
    {
        TimeRestriction.Policy policy;
        if (mPolicy.getSelectedItem().equals(NONE))
        {
            return null;
        }
        else if (mPolicy.getSelectedItem().equals(ALLOW))
        {
            policy = TimeRestriction.ALLOW;
        }
        else
        {
            policy = TimeRestriction.DENY;
        }

        Calendar calendar = getStartCalendar();
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = calendar.get(Calendar.MINUTE);

        calendar = getEndCalendar();
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = calendar.get(Calendar.MINUTE);

        return new TimeRestriction(policy, startHour, startMinute, endHour,
                                   endMinute);
    }

    private Calendar getEndCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, mEndHour.getValue());
        calendar.set(Calendar.MINUTE, mEndMinute.getValue());
        calendar.set(Calendar.AM_PM, toAmPm(mEndAmPm));
        return calendar;
    }

    private Calendar getStartCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, mStartHour.getValue());
        calendar.set(Calendar.MINUTE, mStartMinutes.getValue());
        calendar.set(Calendar.AM_PM, toAmPm(mStartAmPm));
        return calendar;
    }

    public boolean isValidContent()
    {
        Calendar startCalendar = getStartCalendar();
        Calendar endCalendar = getEndCalendar();
        if (!startCalendar.before(endCalendar))
            return false;
        else
            return true;
    }

    private void setToHour(WholeNumberField field, Calendar calendar)
    {
        field.setValue(calendar.get(Calendar.HOUR));
    }

    private void setToMinutes(WholeNumberField field, Calendar calendar)
    {
        field.setValue(calendar.get(Calendar.MINUTE));
    }

    private int toAmPm(JComboBox comboBox)
    {
        if (comboBox.getSelectedItem() == AM)
        {
            return Calendar.AM;
        }
        else
        {
            return Calendar.PM;
        }
    }

    private void setToAmPm(JComboBox comboBox, Calendar calendar)
    {
        int amPm = calendar.get(Calendar.AM_PM);
        if (amPm == Calendar.AM)
        {
            comboBox.setSelectedItem(AM);
        }
        else
        {
            comboBox.setSelectedItem(PM);
        }
    }

    private void setTimesEnabled(boolean enabled)
    {
        mStartHour.setEnabled(enabled);
        mStartMinutes.setEnabled(enabled);
        mStartAmPm.setEnabled(enabled);
        mEndHour.setEnabled(enabled);
        mEndMinute.setEnabled(enabled);
        mEndAmPm.setEnabled(enabled);
    }

    class WholeNumberReformatter extends FocusAdapter
    {
        public void focusLost(FocusEvent event)
        {
            WholeNumberField field = (WholeNumberField) event.getSource();
            field.reformatValue();
        }
    }

    class PolicyChangeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (mPolicy.getSelectedItem().equals(NONE))
            {
                setTimesEnabled(false);
            }
            else
            {
                setTimesEnabled(true);
            }
        }
    }

    private static final String NONE = "None";
    private static final String ALLOW = "Allow";
    private static final String DENY = "Deny";
    private static final String AM = "AM";
    private static final String PM = "PM";

    private JComboBox mPolicy;
    private WholeNumberField mStartHour;
    private WholeNumberField mStartMinutes;
    private WholeNumberField mEndHour;
    private WholeNumberField mEndMinute;
    private JComboBox mStartAmPm;
    private JComboBox mEndAmPm;
    private NumberFormat mMinuteFormat;
    private WholeNumberReformatter mReformatter;
}
