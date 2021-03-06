package org.realtors.rets.server.dmql;

import java.io.StringReader;

import antlr.ANTLRException;

public class DmqlCompiler
{
    public static String dmqlToSql(String dmql)
        throws ANTLRException
    {
        DmtAST ast = dmqlToAst(dmql);
        return new SqlBackend().compile(ast);
    }

    public static DmtAST dmqlToAst(String dmql)
        throws ANTLRException
    {
        DmqlLexer lexer = new DmqlLexer(new StringReader(dmql));
        DmqlParser parser = new DmqlParser(lexer);
        parser.setASTFactory(new DmtAstFactory());
        parser.setASTNodeClass(DmtAST.class.getName());
        parser.query();
        DmtAST ast = (DmtAST) parser.getAST();
        return ast;
    }

    public static SqlConverter parseDmql(String dmql,
                                         DmqlParserMetadata metadata)
        throws ANTLRException
    {
        DmqlLexer lexer = new DmqlLexer(new StringReader(dmql));
        DmqlParser parser = new DmqlParser(lexer);
        parser.setMetadata(metadata);
        return parser.query();
    }
}

