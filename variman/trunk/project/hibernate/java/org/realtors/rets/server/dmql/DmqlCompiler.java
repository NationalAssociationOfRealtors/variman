package org.realtors.rets.server.dmql;

import java.io.DataInputStream;
import java.io.StringReader;

import antlr.ANTLRException;

public class DmqlCompiler
{
    public static String dmqlToSql(String dmql)
        throws ANTLRException
    {
        DmqlLexer lexer = new DmqlLexer(new StringReader(dmql));
        DmqlParser parser = new DmqlParser(lexer);
        parser.setASTFactory(new DmtAstFactory());
        parser.setASTNodeClass(DmtAST.class.getName());
        parser.query();
        DmtAST ast = (DmtAST) parser.getAST();
        return new SqlBackend().compile(ast);
    }

    public static void main(String[] argv)
    {
        try
        {
            DmqlLexer lexer = new DmqlLexer(new DataInputStream(System.in));
            DmqlParser parser = new DmqlParser(lexer);
            DmtAST ast;

            parser.setASTNodeClass("DmtAST");
            parser.query();
            ast = (DmtAST) parser.getAST();
            System.out.println(new SqlBackend().compile(ast));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

