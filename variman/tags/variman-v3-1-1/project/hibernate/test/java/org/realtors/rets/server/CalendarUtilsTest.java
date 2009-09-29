package org.realtors.rets.server;

import java.util.Calendar;

import junit.framework.TestCase;

public class CalendarUtilsTest extends TestCase
{
    private static final int AM = Calendar.AM;
    private static final int PM = Calendar.PM;
    private static final int[][] HOURS = {
        { 0, 12, AM},
        { 1,  1, AM},
        { 2,  2, AM},
        { 3,  3, AM},
        { 4,  4, AM},
        { 5,  5, AM},
        { 6,  6, AM},
        { 7,  7, AM},
        { 8,  8, AM},
        { 9,  9, AM},
        {10, 10, AM},
        {11, 11, AM},
        {12, 12, PM},
        {13,  1, PM},
        {14,  2, PM},
        {15,  3, PM},
        {16,  4, PM},
        {17,  5, PM},
        {18,  6, PM},
        {19,  7, PM},
        {20,  8, PM},
        {21,  9, PM},
        {22, 10, PM},
        {23, 11, PM}
    };

    public void testHourOfDayToHourAndAmPm()
    {
        for (int i = 0; i < 23; i++)
        {
            assertEquals(i, HOURS[i][0]);
            assertEquals(HOURS[i][1], CalendarUtils.hourOfDayToHour(i));
            assertEquals(HOURS[i][2], CalendarUtils.hourOfDayToAmPm(i));
        }
    }

    public void testHourToHourOfDay()
    {
        for (int i = 0; i < 23; i++)
        {
            assertEquals(i, HOURS[i][0]);
            assertEquals(i, CalendarUtils.hourToHourOfDay(HOURS[i][1],
                                                          HOURS[i][2]));
        }
    }
}
