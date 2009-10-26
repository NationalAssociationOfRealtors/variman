/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.realtors.rets.client.RetsVersion;

public class RetsDateTime extends java.util.GregorianCalendar
{
    public RetsDateTime()
    {
        super();
    }
    
    public RetsDateTime(String dateTime) throws ParseException
    {
        super.setTime(parse(dateTime, mRetsVersion, false));
    }

    public RetsDateTime(String dateTime, RetsVersion retsVersion) 
            throws ParseException
    {
        mRetsVersion = retsVersion;
        super.setTime(parse(dateTime, mRetsVersion, false));
    }


    public Date getDate()
    {
        return super.getTime();
    }
    
    /**
     * Returns java.util.Date
     * @return The class java.util.Date
     */
    public Class<Date> getType() 
    {
        return Date.class;
    }
    
    /**
     * Parse a date in the known RETS formats.
     * 
     * @param value    A string containing the date to parse.
     * @param strict A boolean that if <code>true</code> indicates that strict parsing should be used.
     * @return A Java.util.Date.
     * @throws ParseException
     */
    public Date parse(String value, boolean strict) throws ParseException 
    {
        super.setTime(parse(value, mRetsVersion, strict));
        return super.getTime();
    }
    
    /**
     * Parse a date in the known RETS formats.
     * 
     * @param value    A string containing the date to parse.
     * @param retsVersion The RETS protocol version rules for date parsing.
     * @param strict A boolean that if <code>true</code> indicates that strict parsing should be used.
     * @return A Java.util.Date.
     * @throws ParseException
     */
    public static Date parse(String value, RetsVersion retsVersion, boolean strict) throws ParseException 
    {
        Date d; 
        SimpleDateFormat df = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss z");
        
        if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5) &&
            !retsVersion.equals(RetsVersion.RETS_1_7))
        {
            return parse1_7_2(value, strict);
        }
        
        try 
        {
            d = df.parse(value);
        } 
        catch (ParseException e) 
        {
            try
            {
                // Try no timezone. Pre 1.7.2, this was defined as GMT.
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                df.applyPattern("E, d MMM yyyy HH:mm:ss");
                d = df.parse(value);
            }
            catch (ParseException f) 
            {
                try
                {
                    // Try SQL format.
                    df.applyPattern("yyyy-MM-dd HH:mm:ss");
                    d = df.parse(value);
                    return d;
                } 
                catch (ParseException g)
                {
                    try
                    {
                        // Try RETS format.
                        df.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
                        d = df.parse(value);
                        return d;
                    } 
                    catch (ParseException h)
                    {
                        try
                        {
                            // Lastly, try RETS format with fractional time.
                            df.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            d = df.parse(value);
                        }
                        catch (ParseException i)
                        {
                            if( strict ) 
                                throw i;
                            return null;
                        }
                    }
                }
            }
        }
        return d;
    }
    
    /**
     * Parse a date in RETS 1.7.2 and later format.
     * 
     * @param value    A string containing the date to parse.
     * @param retsVersion The RETS protocol version rules for date parsing.
     * @param strict A boolean that if <code>true</code> indicates that strict parsing should be used.
     * @return A Java.util.Date.
     * @throws ParseException
     */
    public static Date parse1_7_2(String value, boolean strict) throws ParseException 
    {
        Date d; 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        TimeZone tz = df.getTimeZone();
        
        String candidate = value;
        // See if the last character is a Z. If so, translate it to +0000.
        if (value.endsWith("Z"))
        {
            candidate = value.substring(0,value.length() - 1) + "+0000";
        }
        // See if the timezone offset is appended. If so and it contains a colon, remove it.
        int len = candidate.length();
        if (candidate.charAt(len - 6) == '+' || candidate.charAt(len - 6) == '-')
        {
            if (candidate.charAt(len - 3) == ':')
                candidate = candidate.substring(0, len - 3) + candidate.substring(len - 2);
        }
        try
        {
            d = df.parse(candidate);
        } 
        catch (ParseException e)
        {
            try
            {
                // Try default format with timesecfrac.
                df.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SZ");
                d = df.parse(candidate);
            }
            catch (ParseException e1)
            {
                // Try with no TimeZone.
                try
                {
                    df.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
                    d = df.parse(candidate);
                }
                catch (ParseException e2)
                {
                    try
                    {
                        // Lastly, try no timezone and no timesecfrac.
                        df.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
                        d = df.parse(candidate);
                    }
                    catch (ParseException e3)
                    {
                        if( strict ) 
                            throw e3;
                        return null;
                    }
                }
            }
        }

        return d;
    }
    
    /**
     * Parse a date in the known RETS formats.
     * 
     * @param value    A string containing the date to parse.
     * @param retsVersion The RETS protocol version rules for date parsing.
     * @param strict A boolean that if <code>true</code> indicates that strict parsing should be used.
     * @return A Java.util.Date.
     * @throws ParseException
     */
    public static Date parseSql(String value) throws ParseException 
    {
        Date d;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try
        {
            d = df.parse(value);
        }
        catch (ParseException e)
        {
            try
            {
                // Some fields don't have a timestamp. Try just the date.
                df.applyPattern("yyyy-MM-dd");
                d = df.parse(value);
            }
            catch (ParseException f)
            {
                throw f;
            }
        }
        return d;
    }
    
    /**
     * Render a date in the RETS 1.7.2 and later format.
     * @param value A Java.util.Date containing the date to render.
     * @return A string containing the rendered date in the proper format.
     */
    public static String render(Date value) {
        return render(value, RetsVersion.RETS_1_7_2);
    }
    
    /**
     * Render a date in the format dictated by the RETS protocol version.
     * @param value A Java.util.Date containing the date to render.
     * @return A string containing the rendered date in the proper format.
     */
    public static String render(Date value, RetsVersion retsVersion)
    {
        if (value == null)
            return "";
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
            
        String retsDate;
            
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
            retsVersion.equals(RetsVersion.RETS_1_7))
        {
            // Done this way to allow for all future versions that use the 1.7.2 format.
            // df.applyPattern("E, d MMM yyyy HH:mm:ss z");            
            df.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
            retsDate = df.format(value);
        }
        else
        {
            retsDate = df.format(value);
        }
        return retsDate;
    }

    /**
     * Set the Date.
     * @param date The date.
     */
    public void setDate(Date date)
    {
        super.setTime(date);
    }
    
    /**
     * Set the RETS protocol version for date rendering.
     * @param retsVersion A RetsVersion.
     */
    public void setRetsVersion(RetsVersion retsVersion)
    {
        mRetsVersion = retsVersion;
    }
    /**
     * Render a date in the format dictated by the RETS protocol version.
     * @param value A Java.util.Date containing the date to render.
     * @return A string containing the rendered date in the proper format.
     */
    public String toString()
    {
        return render(super.getTime(), mRetsVersion);
    }
    
    private RetsVersion mRetsVersion = RetsVersion.RETS_1_7_2;
}
