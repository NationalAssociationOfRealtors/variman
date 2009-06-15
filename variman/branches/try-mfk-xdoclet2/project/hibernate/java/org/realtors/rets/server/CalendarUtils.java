package org.realtors.rets.server;

import java.util.Calendar;

/**
 * The standard java.util.Calendar class has really weird behavior when
 * trying to deal with the HOUR and AM_PM fields.  It's better to use
 * HOUR_OF_DAY and translate to from 12-hour times ourselves.
 */
public class CalendarUtils
{
    public static int hourOfDayToHour(int hourOfDay)
    {
        if (hourOfDay == 0)
        {
            return 12;
        }
        else if (hourOfDay > 12)
        {
            return hourOfDay - 12;
        }
        else
        {
            return hourOfDay;
        }
    }

    public static int hourOfDayToAmPm(int hourOfDay)
    {
        if (hourOfDay < 12)
        {
            return Calendar.AM;
        }
        else
        {
            return Calendar.PM;
        }
    }

    public static int hourToHourOfDay(int hour, int amPm)
    {
        int hourOfDay = hour;
        if ((hour == 12) && (amPm == Calendar.AM))
        {
            hourOfDay = 0;
        }
        else if ((hour == 12) && (amPm == Calendar.PM))
        {
            hourOfDay = 12;
        }
        else if (amPm == Calendar.PM)
        {
            hourOfDay += 12;
        }
        return hourOfDay;
    }

    public static int getHour(Calendar calendar)
    {
        return hourOfDayToHour(calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static int getAmPm(Calendar calendar)
    {
        return hourOfDayToAmPm(calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static void setTime(Calendar calendar, int hours, int minutes,
                               int amPm)
    {
        calendar.set(Calendar.HOUR_OF_DAY, hourToHourOfDay(hours, amPm));
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static Calendar createCalendar(int hours, int minutes, int amPm)
    {
        Calendar calendar = Calendar.getInstance();
        setTime(calendar, hours, minutes, amPm);
        return calendar;
    }
}
