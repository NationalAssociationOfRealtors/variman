package org.realtors.rets.server.dmql;

public class RangeNode extends DmtAST
{

    private DmtAST end1, end2;

    public RangeNode(antlr.collections.AST end1, antlr.collections.AST end2)
    {
        TextNode text;

        this.end1 = (DmtAST) end1;
        this.end2 = (DmtAST) end2;

        if (end1 != null && end1 instanceof TextNode)
            try
            {
                text = (TextNode) end1;
                Integer.parseInt(text.text());
            }
            catch (NumberFormatException e)
            {
                throw new RuntimeException("non-number in range list");
            }
        if (end2 != null && end2 instanceof TextNode)
            try
            {
                text = (TextNode) end2;
                Integer.parseInt(text.text());
            }
            catch (NumberFormatException e)
            {
                throw new RuntimeException("non-number in range list");
            }
    }

    public DmtAST end1()
    {
        return end1;
    }

    public DmtAST end2()
    {
        return end2;
    }

}

