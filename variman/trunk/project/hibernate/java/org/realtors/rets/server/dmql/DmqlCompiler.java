package org.realtors.rets.server.dmql;

import java.io.StringReader;

import antlr.ANTLRException;

public class DmqlCompiler
{

    public static SqlConverter parseDmql(String dmql,
                                         DmqlParserMetadata metadata)
        throws ANTLRException
    {
        return parseDmql(dmql, metadata, false, false);
    }

    public static SqlConverter parseDmql(String dmql,
                                         DmqlParserMetadata metadata,
                                         boolean traceParser,
                                         boolean traceLexer)
        throws ANTLRException
    {
        DmqlLexer lexer = new DmqlLexer(new StringReader(dmql));
        lexer.setTrace(traceLexer);
        DmqlParser parser = new DmqlParser(lexer);
        parser.setDmqlLexer(lexer);
        parser.setMetadata(metadata);
        parser.setTrace(traceParser);
        return parser.query();
    }

    public static SqlConverter parseDmql2(String dmql,
                                          DmqlParserMetadata metadata)
        throws ANTLRException
    {
        return parseDmql2(dmql, metadata, false, false);
    }

    public static SqlConverter parseDmql2(String dmql,
                                          DmqlParserMetadata metadata,
                                          boolean traceParser,
                                          boolean traceLexer)
        throws ANTLRException
    {
        DmqlLexer lexer = new DmqlLexer(new StringReader(dmql));
        lexer.setTrace(traceLexer);
        Dmql2Parser parser = new Dmql2Parser(lexer);
        parser.setDmqlLexer(lexer);
        parser.setMetadata(metadata);
        parser.setTrace(traceParser);
        return parser.query();
    }
}

