package org.realtors.rets.server.testing;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

public class DataGenerator extends DataGenBase
{
    public DataGenerator() throws HibernateException
    {
        super();
        
        mRandom = new Random(System.currentTimeMillis());
        mRandom.nextInt();

        mCount = 0;
        mESchools = new int[] { 306, 300, 42, 304, 303 };
        mAgents = new String[] { "P345", "P123", "M123" };
        mBrokers = new String[] { "Laffalot Realty", "Tex Mex Real Estate",
            "Yellow Armadillo Realty", "Retzilla Realty" };
        mStreets = new String[] { "Buckingham Dr.", "Main St.",
            "Knoll Creek Dr.", "Randall Rd.", "Exeter Ct.", "Anderson Blvd.",
            "Westminster Circle" };
        mUrls = new String[] { "http://www.crt.realtors.org/",
            "http://www.realtors.org", "http://slashdot.org/" };
        mListingTypes = new String[] { "INC", "LND", "RENT", "RES" };
        mLocations = new String[] { "AUR", "BATV", "ELBRN", "ELGN", "GENVA",
            "SELGN", "STC", "WYNE", "WCHIC" };
        mStatus = new String[] { "A", "C", "X", "L", "O", "P", "S", "T", "U",
            "W" };
        mOwners = new String[] { "Keith Garner", "Dave Dribin",
            "Dave Terrell", "Mark Lesswing" };

        mDate = new Date[4];
        Calendar cal = Calendar.getInstance();
        cal.set(2003,6,10);
        mDate[mCount++] = new Date(cal.getTime().getTime());
        cal.set(2003,6,22);
        mDate[mCount++] = new Date(cal.getTime().getTime());
        cal.set(2002,11,5);
        mDate[mCount++] = new Date(cal.getTime().getTime());
        mDate[mCount++] = new Date(new java.util.Date().getTime());
    }

    private void createData(int props) throws HibernateException, SQLException
    {
        Session session = null;
        Connection con = null;
        PreparedStatement ps = null;
        try
        {
            session = mSessions.openSession();
            con = session.connection();
            con.setAutoCommit(false);

            for (int i = 0; i < props; i++)
            {
                ps = con.prepareStatement(
                    "INSERT INTO " +
                    "rets_property_res(id,lp,broker,agent_id,ln,zip_code,ld," +
                    "                  sqft,e_school,m_school,h_school," +
                    "                  stname, url, ltyp, stnum, vew, ar," +
                    "                  status,owner)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"
                );
                // todo: LookupMulti EF needs to be added.
                // todo: LookupMulti LIF needs to be added.
                ps.setInt(1, i);
                ps.setInt(2, mRandom.nextInt(1000000));

                ps.setString(3, getNextBroker());
                ps.setString(4, getNextAgent());

                String tmp = Long.toHexString(mRandom.nextInt(100000));
                ps.setString(5, tmp);
                
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
            con.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (con != null)
            {
                con.rollback();
            }
            if (session != null)
            {
                session.close();
            }
            throw e;
        }
        catch (SQLException e)
        {
            System.out.println(ps);
            if (con != null)
            {
                con.rollback();
            }
            if (session != null)
            {
                session.close();
            }
            throw e;
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
    
    public static void main(String[] args)
        throws HibernateException, SQLException
    {

        DataGenerator dg = new DataGenerator();
        long before = System.currentTimeMillis();
        dg.loadMetadata();
        long after = System.currentTimeMillis();
        System.out.print("Time to loadMetadata:");
        System.out.print(after - before);
        System.out.println("ms");

        before = System.currentTimeMillis();
        String tmp = System.getProperty("prop.count", "10");
        dg.createData(Integer.parseInt(tmp));
        after = System.currentTimeMillis();
        System.out.print("Time to create data:");
        System.out.print(after - before);
        System.out.println("ms");

    }

    private String[] mAgents;
    private String[] mBrokers;
    private int mCount;
    private Date[] mDate;
    private int[] mESchools;
    private int[] mHSchools;
    private String[] mListingTypes;
    private String[] mLocations;
    private String[] mOwners;
    private Random mRandom;
    private String[] mStatus;
    private String[] mStreets;
    private String[] mUrls;
    private int[] mViews;
}