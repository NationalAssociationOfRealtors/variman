/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Random;

import org.apache.log4j.Logger;
import org.realtors.rets.server.ConnectionHelper;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;

public class CreatePropertiesCommand
{
    public CreatePropertiesCommand(int numProperties)
    {
        mNumProperties = numProperties;
        initData();
    }

    private void initData()
    {
        mRandom = new Random(System.currentTimeMillis());
        mRandom.nextInt();

        mCount = 0;
        mESchools = new int[]{306, 300, 42, 304, 303};
        mAgents = new String[]{"P345", "P123", "M123"};
        mBrokers = new String[]{
            "Laffalot Realty", "Tex Mex Real Estate",
            "Yellow Armadillo Realty", "Retzilla Realty"};
        mStreets = new String[]{
            "Buckingham Dr.", "Main St.",
            "Knoll Creek Dr.", "Randall Rd.", "Exeter Ct.", "Anderson Blvd.",
            "Westminster Circle"};
        mUrls = new String[]{
            "http://www.crt.realtors.org/",
            "http://www.realtors.org", "http://slashdot.org/"};
        mListingTypes = new String[]{"INC", "LND", "RENT", "RES"};
        mLocations = new String[]{
            "AUR", "BATV", "ELBRN", "ELGN", "GENVA",
            "SELGN", "STC", "WYNE", "WCHIC"};
        mStatus = new String[]{
            "A", "C", "X", "L", "O", "P", "S", "T", "U",
            "W"};
        mOwners = new String[]{
            "Keith Garner", "Dave Dribin",
            "Dave Terrell", "Mark Lesswing"};

        mDate = new Date[4];
        Calendar cal = Calendar.getInstance();
        cal.set(2003, 6, 10);
        mDate[mCount++] = new Date(cal.getTime().getTime());
        cal.set(2003, 6, 22);
        mDate[mCount++] = new Date(cal.getTime().getTime());
        cal.set(2002, 11, 5);
        mDate[mCount++] = new Date(cal.getTime().getTime());
        mDate[mCount++] = new Date(new java.util.Date().getTime());
    }

    public void execute()
    {
        try
        {
            dropData();
            createData();
        }
        catch (Exception e)
        {
            LOG.error("Caught exception", e);
        }
    }

    private void dropData()
        throws RetsServerException, SQLException
    {
        ConnectionHelper helper = RetsServer.createHelper();
        Connection connection = null;
        Statement statement = null;
        try
        {
            connection = helper.getConnection();
            statement = connection.createStatement();

            statement.execute("delete from rets_property_res");
            helper.commit();
        }
        finally
        {
            if (statement != null)
                statement.close();
            helper.close(LOG);
        }
    }

    private void createData()
        throws RetsServerException, SQLException
    {

        PreparedStatement ps = null;
        ConnectionHelper helper = RetsServer.createHelper();
        try
        {
            Connection con = helper.getConnectionTransaction();
            ps = con.prepareStatement(
                    "INSERT INTO " +
                    "rets_property_res(id,r_lp,r_broker,r_agent_id,r_ln," +
                    "                  r_zip_code,r_ld,r_sqft,r_e_school," +
                    "                  r_m_school,r_h_school,r_stname,r_url," +
                    "                  r_ltyp, r_stnum, r_vew, r_ar,r_status," +
                    "                  r_owner)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            for (int i = 0; i < mNumProperties; i++)
            {
                // todo: LookupMulti EF needs to be added.
                // todo: LookupMulti IF needs to be added.
                mCount += mRandom.nextInt(10);
                ps.setInt(1, i);
                ps.setInt(2, mRandom.nextInt(1000000));

                ps.setString(3, getNextBroker());
                ps.setString(4, getNextAgent());

                ps.setString(5, "LN" + Long.toHexString(i));

                ps.setInt(6, mRandom.nextInt(99999));
                ps.setDate(7, getNextDate());

                ps.setString(8, Long.toString(mRandom.nextInt(7000)));
                ps.setInt(9, getNextSchool());
                ps.setInt(10, getNextSchool());
                ps.setInt(11, getNextSchool());

                ps.setString(12, getNextStreet());
                ps.setString(13, getNextURL());
                ps.setString(14, getNextListingType());
                ps.setString(15, Long.toString(mRandom.nextInt(9999999)));
                ps.setInt(16, mRandom.nextInt(2048));
                ps.setString(17, getNextLocation());
                ps.setString(18, getNextStatus());
                ps.setString(19, getNextOwner());
                ps.execute();
            }
            ps.close();
            helper.commit();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    /**
     * @return a String from the agent array
     */
    private String getNextAgent()
    {
        return mAgents[mCount++ % mAgents.length];
    }

    /**
     * @return a String from the broker array
     */
    private String getNextBroker()
    {
        return mBrokers[mCount++ % mBrokers.length];
    }

    /**
     * @return a Date from the date array
     */
    private Date getNextDate()
    {
        return mDate[mCount++ % mDate.length];
    }

    /**
     * @return
     */
    private String getNextListingType()
    {
        return mListingTypes[mCount++ % mListingTypes.length];
    }

    /**
     * @return
     */
    private String getNextLocation()
    {
        return mLocations[mCount++ % mLocations.length];
    }

    /**
     * @return
     */
    private String getNextOwner()
    {
        return mOwners[mCount++ % mOwners.length];
    }

    /**
     * @return ant int of the next school
     */
    private int getNextSchool()
    {
        return mESchools[mCount++ % mESchools.length];
    }

    /**
     * @return
     */
    private String getNextStatus()
    {
        return mStatus[mCount++ % mStatus.length];
    }

    /**
     * @return a string with the street
     */
    private String getNextStreet()
    {
        return mStreets[mCount++ % mStreets.length];
    }

    /**
     * @return a String from the URL array
     */
    private String getNextURL()
    {
        return mUrls[mCount++ % mUrls.length];
    }


    private static final Logger LOG =
        Logger.getLogger(CreatePropertiesCommand.class);
    private int mNumProperties;
    private String[] mAgents;
    private String[] mBrokers;
    private int mCount;
    private Date[] mDate;
    private int[] mESchools;
    private String[] mListingTypes;
    private String[] mLocations;
    private String[] mOwners;
    private Random mRandom;
    private String[] mStatus;
    private String[] mStreets;
    private String[] mUrls;
}
