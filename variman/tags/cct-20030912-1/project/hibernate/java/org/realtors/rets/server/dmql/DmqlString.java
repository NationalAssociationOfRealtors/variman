/*
 */
package org.realtors.rets.server.dmql;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DmqlString implements SqlConverter
{
    public static final DmqlStringComponent MATCH_ZERO_OR_MORE =
        new MatchZeroOrMore();

    public static final DmqlStringComponent MATCH_ZERO_OR_ONE =
        new MatchZeroOrOne();

    public DmqlString(String string)
    {
        this();
        add(string);
        mString = string;
    }

    public DmqlString()
    {
        mComponents = new ArrayList();
        mContainsWildcards = false;
    }

    public String toString()
    {
        return mString;
    }

    public void add(DmqlStringComponent component)
    {
        mComponents.add(component);
        if (component.conatinsWildcards())
        {
            mContainsWildcards = true;
        }
    }

    public void add(String string)
    {
        mComponents.add(new ConstantStringComponent(string));
    }

    public List getComponents()
    {
        return mComponents;
    }

    public boolean containsWildcards()
    {
        return mContainsWildcards;
    }

    public void toSql(PrintWriter out)
    {
        out.print("'");
        Iterator components = mComponents.iterator();
        while (components.hasNext())
        {
            DmqlStringComponent component =
                (DmqlStringComponent) components.next();
            if (component == DmqlString.MATCH_ZERO_OR_MORE)
            {
                out.print("%");
            }
            else if (component == DmqlString.MATCH_ZERO_OR_ONE)
            {
                out.print("_");
            }
            else
            {
                out.print(component);
            }

        }
        out.print("'");
    }

    private String mString;
    private List mComponents;
    private boolean mContainsWildcards;
}
