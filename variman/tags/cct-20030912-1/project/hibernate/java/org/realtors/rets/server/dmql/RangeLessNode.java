package org.realtors.rets.server.dmql;

public class RangeLessNode extends RangeNode
{

    public RangeLessNode(antlr.collections.AST end1, antlr.collections.AST end2)
    {
        super(end1, end2);
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

