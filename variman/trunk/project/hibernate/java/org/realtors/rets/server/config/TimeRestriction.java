package org.realtors.rets.server.config;

import java.text.DateFormat;
import java.util.Calendar;

import org.apache.commons.lang.enum.Enum;
import org.apache.commons.lang.builder.EqualsBuilder;

public class TimeRestriction
{
    public static final Policy ALLOW = new Policy("allow");
    public static final Policy DENY = new Policy("deny");

    public TimeRestriction(Policy policy, int startHour, int startMinute,
                           int endHour, int endMinute)
    {
        mPolicy = policy;
        mAbsoluteStart = hourAndMinuteToAbsoluteMinute(startHour, startMinute);
        mAbsoluteEnd = hourAndMinuteToAbsoluteMinute(endHour, endMinute);
        mStartHour = startHour;
        mStartMinute = startMinute;
        mEndHour = endHour;
        mEndMinute = endMinute;
    }

    public Policy getPolicy()
    {
        return mPolicy;
    }

    public boolean isAllowed(int hour, int minute)
    {
        int absoluteMinute = hourAndMinuteToAbsoluteMinute(hour, minute);
        boolean inInterval = ((absoluteMinute >= mAbsoluteStart) &&
                              (absoluteMinute <= mAbsoluteEnd));
        if (mPolicy == ALLOW)
        {
            return inInterval;
        }
        else // if (mPolicy == DENY)
        {
            return !inInterval;
        }
    }

    public Calendar getStartAsCalendar()
    {
        return createCalendar(mStartHour, mStartMinute);
    }

    public Calendar getEndAsCalendar()
    {
        return createCalendar(mEndHour, mEndMinute);
    }

    private Calendar createCalendar(int hourOfDay, int minute)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public String toString()
    {
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        StringBuffer buffer = new StringBuffer();
        buffer.append("Time restriction: ");
        buffer.append(mPolicy.getName()).append(" ");
        buffer.append(formatter.format(getStartAsCalendar().getTime()));
        buffer.append(" to ");
        buffer.append(formatter.format(getEndAsCalendar().getTime()));
        return buffer.toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof TimeRestriction))
        {
            return false;
        }
        TimeRestriction rhs = (TimeRestriction) obj;
        return new EqualsBuilder()
            .append(mPolicy, rhs.mPolicy)
            .append(mStartHour, rhs.mStartHour)
            .append(mStartMinute, rhs.mStartMinute)
            .append(mEndHour, rhs.mEndHour)
            .append(mEndMinute, rhs.mEndMinute)
            .isEquals();
    }

    private int hourAndMinuteToAbsoluteMinute(int hour, int minute)
    {
        return (hour * 60) + minute;
    }



    public static class Policy extends Enum
    {
        private Policy(String policy)
        {
            super(policy);
        }
    }

    private Policy mPolicy;
    private int mAbsoluteStart;
    private int mAbsoluteEnd;
    private int mStartHour;
    private int mStartMinute;
    private int mEndHour;
    private int mEndMinute;
}
