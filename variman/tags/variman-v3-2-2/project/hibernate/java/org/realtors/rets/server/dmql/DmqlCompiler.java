/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.dmql;

import java.io.StringReader;
import java.util.Collections;
import java.util.Set;

import antlr.ANTLRException;
import antlr.BaseAST;
import antlr.RecognitionException;
import antlr.collections.AST;

public class DmqlCompiler
{

    public static ParserResults parseDmql(String dmql,
                                         DmqlParserMetadata metadata)
        throws ANTLRException
    {
        return parseDmql(dmql, metadata, false, false);
    }

    public static ParserResults parseDmql(String dmql,
                                         DmqlParserMetadata metadata,
                                         boolean traceParser,
                                         boolean traceLexer)
        throws ANTLRException
    {
        DmqlLexer lexer = new DmqlLexer(new StringReader(dmql));
        lexer.setTrace(traceLexer);
        DmqlParser parser = new DmqlParser(lexer);
        parser.setMetadata(metadata);
        parser.setTrace(traceParser);
        parser.query();
        return walkTreeParser(parser.getTokenNames(), metadata, traceParser,
                              parser.getAST());
    }

    public static ParserResults parseDmql2(String dmql,
                                          DmqlParserMetadata metadata)
        throws ANTLRException
    {
        return parseDmql2(dmql, metadata, false, false);
    }

    public static ParserResults parseDmql2(String dmql,
                                          DmqlParserMetadata metadata,
                                          boolean traceParser,
                                          boolean traceLexer)
        throws ANTLRException
    {
        DmqlLexer lexer = new DmqlLexer(new StringReader(dmql));
        lexer.setTrace(traceLexer);
        Dmql2Parser parser = new Dmql2Parser(lexer);
        parser.setMetadata(metadata);
        parser.setTrace(traceParser);
        parser.query();
        return walkTreeParser(parser.getTokenNames(), metadata, traceParser,
                              parser.getAST());
    }

    private static ParserResults walkTreeParser(String[] tokenNames,
                                               DmqlParserMetadata metadata,
                                               boolean traceParser, AST ast)
        throws RecognitionException
    {
        BaseAST.setVerboseStringConversion(true, tokenNames);
        if (traceParser)
        {
            System.out.println("ast: " + ast.toStringList());
        }
        DmqlTreeParser treeParser = new DmqlTreeParser();
        treeParser.setTrace(traceParser);
        treeParser.setMetadata(metadata);
        SqlConverter sqlConverter = treeParser.query(ast);
        Set<String> foundFields = treeParser.getFoundFields();
        ParserResults parserResults = new ParserResults(sqlConverter, foundFields);
        return parserResults;
    }

    public static class ParserResults
    {
        private SqlConverter mSqlConverter;
        private Set<String> mFields = Collections.emptySet();
        
        public ParserResults(SqlConverter sqlConverter, Set<String> foundFields)
        {
            if (sqlConverter == null) {
                throw new NullPointerException("sqlConverter is null.");
            }
            mSqlConverter = sqlConverter;
            if (foundFields != null) {
                mFields = foundFields;
            }
        }

        /**
         * @return the fields (system names or standard names) found during the
         *         parse. Never returns <code>null</code>, however, may return
         *         an empty set.
         */
        public Set<String> getFoundFields() {
            return mFields;
        }

        /**
         * @return the SQL converter which can be used to translate the DMQL(2)
         *         passed to the parser into SQL.
         */
        public SqlConverter getSqlConverter() {
            return mSqlConverter;
        }
        
    }
}

