package org.realtors.rets.server.dmql;

import antlr.BaseAST;
import antlr.Token;
import antlr.collections.AST;

public class DmtAST extends BaseAST
{

    public void basicAccept(DmtVisitor vis)
    {
        DmtAST ast;

        ast = (DmtAST) getFirstChild();
        while (ast != null)
        {
            ast.accept(vis);
            ast = (DmtAST) ast.getNextSibling();
        }
    }

    public DmtAST left()
    {
        return (DmtAST) getFirstChild();
    }

    public DmtAST right()
    {
        return (DmtAST) left().getNextSibling();
    }

    public void accept(DmtVisitor vis)
    {
        basicAccept(vis);
    }

    public String toString()
    {
        return "(" + getClass().getName().toString() + ")";
    }

    public void initialize(int i, String text)
    {
        //System.out.println ("1: " + i + " " + text);
    }

    public void initialize(AST ast)
    {
        //System.out.println ("2: " + ast);
    }

    public void initialize(Token tok)
    {
        //System.out.println ("3: " + tok);
    }

    public String text()
    {
        return "";
    }

}

