header {
package org.realtors.rets.server.dmql;
import java.util.*;
}

class DmqlParser extends Parser;

options
{
    defaultErrorHandler = false;
	exportVocab = DmqlParser;
	k = 3;
}

{
    public String validateFieldName(Token t)
        throws SemanticException
    {
        String fieldName = t.getText();
        if (mMetadata.isValidFieldName(fieldName)) {
            return fieldName;
        }
        else {
            throw newSemanticException("Unknown field name: " + fieldName, t);
        }
    }

    public boolean isLookup(String fieldName) {
        return mMetadata.isValidLookupName(fieldName);
    }

    public boolean isStringList(String fieldName) {
        return mMetadata.isValidStringName(fieldName);
    }

    public void addLookup(LookupList list, Token t)
        throws SemanticException
    {
        String lookupName = list.getField();
        String lookupValue = t.getText();
        if (mMetadata.isValidLookupValue(lookupName, lookupValue)) {
            list.addLookup(lookupValue);
        }
        else {
            throw newSemanticException("No such lookup value [" +
                                       lookupName + "]: " + lookupValue, t);
        }
    }

    public void setMetadata(DmqlParserMetadata metadata) {
        mMetadata = metadata;
    }

    public void print(String s) {
        System.out.println(s);
    }

    public void print(Token t) {
        System.out.println(t);
    }

    public void traceIn(String text) throws TokenStreamException {
        if (mTrace) super.traceIn(text);
    }

    public void traceOut(String text) throws TokenStreamException {
        if (mTrace) super.traceOut(text);
    }

    public void setTrace(boolean trace) {
        mTrace = trace;
    }

    private SemanticException newSemanticException(String message, Token t)
    {
        return new SemanticException(message, t.getFilename(), t.getLine(),
                                     t.getColumn());
    }

    
    private boolean mTrace = false;
    private DmqlParserMetadata mMetadata;
}

query returns [SqlConverter sql]
    { sql = null; }
//    : query_clause ((PIPE | "OR" ) query)? EOF!
    : sql=query_element EOF!
    ;

query_clause
    : boolean_element ((COMMA | "AND" ) query_clause)?
    ;

boolean_element
    { SqlConverter sql = null; }
    : (NOT)? sql=query_element
    ;

query_element returns [SqlConverter sql]
    { sql = null; }
    : sql=field_criteria
//    | LPAREN query RPAREN
    ;

field_criteria returns [SqlConverter sql]
    {
        sql = null;
        String field;
    }
    : LPAREN field=field_name EQUAL sql=field_value[field] RPAREN
    ;

field_name returns [String field]
    { field = null; }
    : t:TEXT {field=validateFieldName(t);}
    ;

field_value [String name] returns [SqlConverter sql]
    { sql = null; }
    : {isLookup(name)}? sql=lookup_list[name]
    | {isStringList(name)}? string_list
    | {isStringList(name)}? string_literal
    | number
    | period
    | range_list
    ;

range_list
    : range (COMMA range)*
    ;

range
    : between
    | less
    | greater
    ;

between
    : (period | number | string_eq) MINUS (period | number | string_eq)
    ;

less
    : (period | number | string_eq) MINUS
    ;

greater
    : (period | number | string_eq) PLUS
    ;

number : n:NUMBER {print("n"); print(n);} ;

period
    : d:DATE {print("d"); print(d);}
    | dt:DATETIME {print("dt"); print(dt);}
    | t:TIME {print("t"); print(t);}
    ;

lookup_list [String name] returns [SqlConverter sql]
    { sql = null;}
    : sql=lookup_or[name]
    | sql=lookup_and[name]
    | sql=lookup_not[name]
    ;

lookup_or [String name] returns [SqlConverter sql]
    { LookupList list = new LookupList(LookupListType.OR, name); sql = list; }
    : PIPE lookups[list]
    // This is the "implied" OR
    | lookup[list]
    ;

lookup_and [String name] returns [SqlConverter sql]
    { LookupList list = new LookupList(LookupListType.AND, name); sql = list; }
    : PLUS lookups[list]
    ;

lookup_not [String name] returns [SqlConverter sql]
    { LookupList list = new LookupList(LookupListType.NOT, name); sql = list; }
    : TILDE lookups[list]
    ;

lookups [LookupList list]
    : lookup[list] (COMMA lookup[list])*
    ;

lookup [LookupList list]
    : t:TEXT {addLookup(list, t);}
    ;

string_list
    : string (COMMA string)*
    ;

string
    : string_eq
    | string_start
    | string_contains
    | string_char
    ;

string_eq
    : TEXT
    ;

string_start
    : TEXT STAR
    ;

string_contains
    : STAR TEXT STAR
    ;

string_char
    : TEXT QUESTION TEXT
    ;

string_literal
    : STRING_LITERAL
    ;

class DmqlLexer extends Lexer;

options
{
    k = 2;
	testLiterals = false;
}


{
    public void traceIn(String text) throws CharStreamException {
        if (mTrace) super.traceIn(text);
    }

    public void traceOut(String text) throws CharStreamException {
        if (mTrace) super.traceOut(text);
    }

    public void setTrace(boolean trace) {
        mTrace = trace;
    }
    
    private boolean mTrace = false;
}

LPAREN : '(';
RPAREN : ')';
COMMA : ',';
EQUAL : '=';
STAR : '*';
PLUS : '+';
MINUS : '-';
PIPE : '|';
TILDE : '~';
QUESTION : ('?')+;
SEMI : ';';

protected DATETIME : YMD 'T' HMS;
protected DATE : YMD;
protected TIME : HMS;

// Year-Month-Day
protected
YMD 
    : DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT;

// Hour:Minute:Second[.Fraction]
protected
HMS : DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT DIGIT)?;

protected
DIGIT : ('0' .. '9');

protected
ALPHANUM : ('a'..'z' | 'A'..'Z' | DIGIT);

protected
TEXT
	options { testLiterals = true; }
	: (ALPHANUM)+;

protected
NUMBER
    : (DIGIT)+ ('.' (DIGIT)*)* ;

// Since these all basically have overlapping patterns, we need to use
// backtracking to try them in order.
TEXT_OR_NUMBER_OR_PERIOD
    : (YMD 'T') => DATETIME {$setType(DATETIME);}
    | (DATE) => DATE {$setType(DATE);}
    | (TIME) => TIME {$setType(TIME);}
    | (NUMBER) => NUMBER {$setType(NUMBER);}
    | TEXT {$setType(TEXT);}
    ;

STRING_LITERAL
	options { testLiterals = true; }
    : '"'! (~'"')* ('"'! '"' (~'"')*)* '"'!;

WS  :   (' ' | '\t' | '\n' | '\r')
        { _ttype = Token.SKIP; }
    ;
