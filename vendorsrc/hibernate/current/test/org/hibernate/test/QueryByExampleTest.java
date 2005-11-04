package org.hibernate.test;

import java.util.List;

import org.hibernate.test.Component;
import org.hibernate.test.Componentizable;
import org.hibernate.test.SubComponent;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.dialect.HSQLDialect;
import net.sf.hibernate.expression.Example;
import net.sf.hibernate.expression.Expression;


/**
 * Query by example test to allow nested components
 * 
 * @author emmanuel
 */
public class QueryByExampleTest extends TestCase {

    /**
     * @param name test name
     */
    public QueryByExampleTest(String name) {
        super(name);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        initData();
    }
    
    public void tearDown() throws Exception {
        deleteData();
        super.tearDown();
    }

    /** 
     * @see org.hibernate.test.TestCase#getMappings()
     */
    protected String[] getMappings() {
        return new String[] { "Componentizable.hbm.xml" };
    }

    public void testSimpleQBE() throws Exception {
        
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", null, "ope%");
        Criteria crit = s.createCriteria(Componentizable.class);
        Example ex = Example.create(master).enableLike();
        crit.add(ex);
        List result = crit.list();
        assertNotNull(result);
        assertEquals(1, result.size());
        t.commit();
        s.close();
    }
    
    public void testJunctionNotExpressionQBE() throws Exception {
        
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", null, "ope%");
        Criteria crit = s.createCriteria(Componentizable.class);
        Example ex = Example.create(master).enableLike();
        
        crit.add(Expression.or(Expression.not(ex), ex));
        
        List result = crit.list();
        assertNotNull(result);
        if ( !(getDialect() instanceof HSQLDialect) ) assertEquals(2, result.size());
        t.commit();
        s.close();
        
    }
    
    public void testExcludingQBE() throws Exception {
        
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", null, "ope%");
        Criteria crit = s.createCriteria(Componentizable.class);
        Example ex = Example.create(master).enableLike()
            .excludeProperty("component.subComponent");
        crit.add(ex);
        List result = crit.list();
        assertNotNull(result);
        assertEquals(3, result.size());
        
        master = getMaster("hibernate", "ORM tool", "fake stuff");
        crit = s.createCriteria(Componentizable.class);
        ex = Example.create(master).enableLike()
            .excludeProperty("component.subComponent.subName1");
        crit.add(ex);
        result = crit.list();
        assertNotNull(result);
        assertEquals(1, result.size());
        t.commit();
        s.close();
                
        
    }
    
    private void initData() throws Exception {
        Session s = openSession();
        Transaction t = s.beginTransaction();
        Componentizable master = getMaster("hibernate", "ORM tool", "ORM tool1");
        s.saveOrUpdate(master);
        master = getMaster("hibernate", "open source", "open source1");
        s.saveOrUpdate(master);
        master = getMaster("hibernate", null, null);
        s.saveOrUpdate(master);
        t.commit();
        s.close();
    }
    
    private void deleteData() throws Exception {
        Session s = openSession();
        Transaction t = s.beginTransaction();
        s.delete("from Componentizable");
        t.commit();
        s.close();
    }

    private Componentizable getMaster(String name, String subname, String subname1) {
        Componentizable master = new Componentizable();
        if (name != null) {
            Component masterComp = new Component();
            masterComp.setName(name);
            if (subname != null || subname1 != null) {
                SubComponent subComponent = new SubComponent();
                subComponent.setSubName(subname);
                subComponent.setSubName1(subname1);
                masterComp.setSubComponent(subComponent); 
            }
            master.setComponent(masterComp);
        }
        return master;
    }
    
    public static Test suite() {
        return new TestSuite(QueryByExampleTest.class);
    }
    
    public static void main(String[] args) throws Exception {
        TestRunner.run( suite() );
    }
}
