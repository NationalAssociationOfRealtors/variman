package org.realtors.rets.server.testing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;
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

        mECount = 0;
        mESchools = new int[] { 306, 300, 42, 304, 303 };
        mACount = 0;
        mAgents = new String[] { "P345", "P123", "M123" };
        mBCount = 0;
        mBrokers = new String[] { "Laffalot Realty", "Tex Mex Real Estate",
            "Yellow Armadillo Realty", "Retzilla Realty" };
        mSCount = 0;
        mStreets = new String[] { "Buckingham Dr.", "Main St.",
            "Knoll Creek Dr.", "Randall Rd.", "Exeter Ct.", "Anderson Blvd.",
            "Westminster Circle" };

        mDCount = 0;
        mDate = new Date[4];
        Calendar cal = Calendar.getInstance();
        cal.set(2003,6,10);
        mDate[mDCount++] = new Date(cal.getTime().getTime());
        cal.set(2003,6,22);
        mDate[mDCount++] = new Date(cal.getTime().getTime());
        cal.set(2002,11,5);
        mDate[mDCount++] = new Date(cal.getTime().getTime());
        mDate[mDCount++] = new Date(new java.util.Date().getTime());
    }

    private void createData(int props) throws HibernateException, SQLException
    {
        Session session = null;
        Connection con = null;
        try
        {
            session = mSessions.openSession();
            con = session.connection();
            con.setAutoCommit(false);

            for (int i = 0; i < props; i++)
            {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO " +
                    "rets_property_res(id,lp,broker,agent_id,ln,zip_code,ld," +
                    "                  sqft,e_school,m_school,h_school," +
                    "                  stname)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);"
                );
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
        return mAgents[mACount++ % mAgents.length];
    }

    /**
     * @return a String from the broker array
     */
    private String getNextBroker()
    {
        return mBrokers[mBCount++ % mBrokers.length];
    }

    /**
     * @return a Date from the date array
     */
    private Date getNextDate()
    {
        return mDate[mDCount++ % mDate.length];
    }

    /**
     * @return ant int of the next school
     */
    private int getNextSchool()
    {
        return mESchools[mECount++ % mESchools.length];
    }

    /**
     * @return a string with the street
     */
    private String getNextStreet()
    {
        return mStreets[mSCount++ % mStreets.length];
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

    private int mACount;
    private String[] mAgents;
    private int mBCount;
    private String[] mBrokers;
    private Date[] mDate;
    private int mDCount;
    private int mECount;
    private int[] mESchools;
    private int mHCount; 
    private int[] mHSchools;
    private Random mRandom;
    private int mSCount;
    private String[] mStreets;
}