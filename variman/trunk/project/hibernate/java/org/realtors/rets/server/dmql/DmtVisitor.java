package org.realtors.rets.server.dmql;

public interface DmtVisitor
{

    public void preVisit(DmtAST node);

    public void visit(DmtAST node);

    public void postVisit(DmtAST node);

    public void visit(TextNode node);

    public void visit(QueryNode node);

    public void preVisit(SubqueryNode node);

    public void postVisit(SubqueryNode node);

    public void preVisit(DisjunctionNode node);

    public void visit(DisjunctionNode node);

    public void postVisit(DisjunctionNode node);

    public void visit(FieldNameNode node);

    public void visit(TodayNode node);

    public void visit(NowNode node);

    public void visit(DateNode node);

    public void visit(TimeNode node);

    public void visit(DateTimeNode node);

    public void visit(StringEqNode node);

    public void visit(StringStartNode node);

    public void visit(StringContainsNode node);

    public void visit(StringCharNode node);

    public void visit(RangeListNode node);

    public void visit(RangeGreaterNode node);

    public void visit(RangeLessNode node);

    public void visit(RangeBetweenNode node);

    public void visit(LookupAndNode node);

    public void visit(LookupOrNode node);

    public void visit(LookupNotNode node);

}

