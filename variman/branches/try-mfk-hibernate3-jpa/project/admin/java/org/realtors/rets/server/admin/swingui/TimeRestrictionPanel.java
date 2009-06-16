package org.realtors.rets.server.admin.swingui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.realtors.rets.server.CalendarUtils;
import org.realtors.rets.server.config.TimeRestriction;

public class TimeRestrictionPanel extends JPanel
{
    TimeRestrictionPanel()
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        String[] policyStrings = { ALLOW, DENY };
        String[] am_pm = { AM, PM };
        mMinuteFormat = NumberFormat.getNumberInstance(Locale.US);
        mMinuteFormat.setMinimumIntegerDigits(2);
        mReformatter = new WholeNumberReformatter();

        mPolicy = new JComboBox(policyStrings);
        add(mPolicy);
        addStrut();
        mStartHour = createHourField(12);
        add(mStartHour);
        addStrut();
        add(new JLabel(":"));
        addStrut();
        mStartMinutes = createMinuteField(0);
        add(mStartMinutes);
        addStrut();
        mStartAmPm = new JComboBox(am_pm);
        mStartAmPm.setSelectedItem(AM);
        add(mStartAmPm);
        addStrut();
        add(new JLabel(" to "));
        addStrut();
        mEndHour = createHourField(11);
        add(mEndHour);
        addStrut();
        add(new JLabel(":"));
        addStrut();
        mEndMinute = createMinuteField(59);
        add(mEndMinute);
        addStrut();
        mEndAmPm = new JComboBox(am_pm);
        mEndAmPm.setSelectedItem(PM);
        add(mEndAmPm);
    }

    TimeRestrictionPanel(TimeRestriction timeRestriction)
    {
        this();
        setTimeRestriction(timeRestriction);
    }

    private void addStrut()
    {
        add(Box.createHorizontalStrut(5));
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
            setEnabled(false);
        }
        else
        {
            setEnabled(true);
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
        if (mPolicy.getSelectedItem().equals(ALLOW))
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
        CalendarUtils.setTime(calendar, mEndHour.getValue(),
                              mEndMinute.getValue(),
                              toAmPm(mEndAmPm));
        return calendar;
    }

    private Calendar getStartCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        CalendarUtils.setTime(calendar, mStartHour.getValue(),
                              mStartMinutes.getValue(),
                              toAmPm(mStartAmPm));
        return calendar;
    }

    public boolean isValidContent()
    {
        TimeRestriction timeRestriction = getTimeRestriction();
        if (timeRestriction == null)
        {
            return true;
        }

        Calendar start = timeRestriction.getStartAsCalendar();
        Calendar end = timeRestriction.getEndAsCalendar();
        if  (!start.before(end))
        {
            JOptionPane.showMessageDialog(
                SwingUtils.getAdminFrame(),
                "The time restriction start time must come before the end " +
                "time.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void setToHour(WholeNumberField field, Calendar calendar)
    {
        field.setValue(CalendarUtils.getHour(calendar));
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

    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        mPolicy.setEnabled(enabled);
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
