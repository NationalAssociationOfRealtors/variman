package org.realtors.rets.server.dmql;

import java.io.StringReader;

import antlr.ANTLRException;
import antlr.BaseAST;
import antlr.RecognitionException;
import antlr.Parser;
import antlr.collections.AST;

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
        parser.query();
        return walkTreeParser(parser, traceParser, parser.getAST());
    }

    private static SqlConverter walkTreeParser(Parser parser,
                                               boolean traceParser, AST ast)
        throws RecognitionException
    {
        BaseAST.setVerboseStringConversion(true, parser.getTokenNames());
        if (traceParser)
        {
            System.out.println("ast: " + ast.toStringList());
        }
        DmqlTreeParser treeParser = new DmqlTreeParser();
        treeParser.setTrace(traceParser);
        return treeParser.query(ast);
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
        parser.query();
        return walkTreeParser(parser, traceParser, parser.getAST());
    }
}

