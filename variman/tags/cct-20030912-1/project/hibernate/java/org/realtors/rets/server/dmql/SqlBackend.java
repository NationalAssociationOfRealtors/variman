package org.realtors.rets.server.dmql;

public class SqlBackend extends AbstractBackend implements DmtVisitor
{
   public String compile(DmtAST ast)
    {
        sql = "";
        ast.accept(this);
        return sql;
    }

    public void visit(QueryNode node)
    {
        sql += " AND ";
    }

    public void preVisit(SubqueryNode node)
    {
        sql += "(";
    }

    public void postVisit(SubqueryNode node)
    {
        sql += ")";
    }

    public void preVisit(DisjunctionNode node)
    {
        sql += "(";
    }

    public void visit(DisjunctionNode node)
    {
        sql += " OR ";
    }

    public void postVisit(DisjunctionNode node)
    {
        sql += ")";
    }

    public void visit(FieldNameNode node)
    {
        curr_field = node.text();
        first_field = false;
        mode = OR;
    }

    public void visit(TextNode node)
    {
        try
        {
            Integer.parseInt(node.text());
            sql += node.text();
        }
        catch (NumberFormatException e)
        {
            sql += "'" + node.text() + "'";
        }
    }

    public void visit(TodayNode node)
    {
        sql += "'today'";
    }

    public void visit(NowNode node)
    {
        sql += "'now'";
    }

    public void visit(DateNode node)
    {
        sql += "'" + node.year() + "-" + node.month() + "-" + node.day() + "'";
    }

    public void visit(TimeNode node)
    {
        sql += "'" + node.hour() + ":" + node.minute() + ":" + node.second() +
            "." + node.frac() + "'";
    }

    public void visit(DateTimeNode node)
    {
        sql += "'" + node.year() + "-" + node.month() + "-" + node.day() +
            " " + node.hour() + ":" + node.minute() + ":" + node.second() +
            "." + node.frac() + "'";
    }

    private void connect()
    {
        if (first_field)
            if (mode == AND)
                sql += " AND ";
            else if (mode == OR)
                sql += " OR ";
            else if (mode == NOT)
                sql += " AND ";
        if (mode == NOT)
            sql += "NOT ";
    }

    public void visit(StringEqNode node)
    {
        connect();
        try
        {
            Integer.parseInt(node.text());
            sql += curr_field + " = " + node.text();
        }
        catch (NumberFormatException e)
        {
            sql += curr_field + " = '" + node.text() + "'";
        }
        first_field = true;
    }

    public void visit(StringStartNode node)
    {
        connect();
        try
        {
            Integer.parseInt(node.text());
            sql += "huh?";
        }
        catch (NumberFormatException e)
        {
            sql += curr_field + " LIKE '" + node.text() + "%'";
        }
        first_field = true;
    }

    public void visit(StringContainsNode node)
    {
        connect();
        try
        {
            Integer.parseInt(node.text());
            sql += "huh?";
        }
        catch (NumberFormatException e)
        {
            sql += curr_field + " LIKE '%" + node.text() + "%'";
        }
        first_field = true;
    }

    public void visit(StringCharNode node)
    {
        int i;

        connect();
        try
        {
            Integer.parseInt(node.text());
            sql += "huh?";
        }
        catch (NumberFormatException e)
        {
            sql += curr_field + " LIKE '" + node.text1();
            for (i = 0; i < node.qs(); i++)
                sql += "_";
            sql += node.text2() + "'";
        }
    }

    public void visit(RangeListNode node)
    {
        sql += " OR ";
    }

    public void visit(RangeGreaterNode node)
    {
        sql += curr_field + " >= ";
        node.end1().accept(this);
    }

    public void visit(RangeLessNode node)
    {
        sql += curr_field + " <= ";
        node.end1().accept(this);
    }

    public void visit(RangeBetweenNode node)
    {
        sql += "(" + curr_field + " >= ";
        node.end1().accept(this);
        sql += " AND " + curr_field + " <= ";
        node.end2().accept(this);
        sql += ")";
    }

    public void visit(LookupAndNode node)
    {
        // hmm....
        mode = AND;
    }

    public void visit(LookupOrNode node)
    {
        mode = OR;
    }

    public void visit(LookupNotNode node)
    {
        mode = NOT;
    }

    private static final int AND = 0;
    private static final int OR = 1;
    private static final int NOT = 2;

    private boolean first_field;
    private int mode;
    private String sql, curr_field;
 }
