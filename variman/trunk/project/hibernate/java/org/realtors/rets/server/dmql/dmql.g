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

    public void setDmqlLexer(DmqlLexer lexer) {
        mLexer = lexer;
    }

    public void print(String s) {
        // System.out.println(s);
    }

    public void print(Token t) {
        // System.out.println(t);
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
    private DmqlLexer mLexer;
}

query returns [SqlConverter sql]
    : sql=search_condition EOF
    ;

search_condition returns [SqlConverter sql]
    { SqlConverter sc;}
    : sql=query_clause (or sc=query_clause)*
    ;

query_clause returns [SqlConverter sql]
    { SqlConverter qe;}
    : sql=query_element (and qe=query_element)*
    ;

query_element returns [SqlConverter sql]
    { sql = null; SqlConverter sc;}
    : sql=field_criteria
    | LPAREN sc=search_condition RPAREN
    ;

or
    : PIPE
    ;

and
    : COMMA
    ;

field_criteria returns [SqlConverter sql]
    {
        sql = null;
        String field;
    }
    : LPAREN field=field_name EQUAL sql=field_value[field] RPAREN
    ;

field_name returns [String field]
    { field = null; Token t;}
    : t=text {field=validateFieldName(t);}
    ;

field_value [String name] returns [SqlConverter sql]
    { sql = null; }
    : {isLookup(name)}? sql=lookup_list[name]
    | {isStringList(name)}? string_list
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
    : (period | number) MINUS (period | number)
    ;

less
    : (period | number) MINUS
    ;

greater
    : (period | number) PLUS
    ;

number : n:NUMBER {print("n"); print(n);} ;

period
    : date
    | datetime
    | t:TIME {print("t"); print(t);}
    ;

date
    : DATE
    | TODAY
    ;

datetime
    : DATETIME
    | NOW
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
    {Token t;}
    : t=text {addLookup(list, t);}
    | n:NUMBER {addLookup(list, n);}
    ;

string_list
    : string (COMMA string)*
    ;

// string_char conflicts with string_eq due to text being TEXT or a
// reserved token.  We must use a syntactic predict to try matching
// string_char first, and if it's not a string_char, it must be one of
// the others.
string
    : string_eq
    | string_start
    | string_contains
    | string_char1
    | string_char2
    ;

string_eq
    { Token t; }
    : t=text
    ;

string_start
    { Token t; }
    : t=text STAR
    ;

string_contains
    { Token t; }
    : STAR t=text STAR
    ;

// Need to split string_char into 2 separate rules, otherwise ANTLR
// cannot differentiate between a string_eq and a string_char
// string_char
//     : (TEXT)? QUESTION (TEXT)?
//     ;

string_char1
    { Token t; }
    : t=text QUESTION (t=text)?
    ;

string_char2
    { Token t; }
    : QUESTION (t=text)?
    ;

string_literal
    : STRING_LITERAL
    ;

text returns [Token token]
    {token=null;}
    : text:TEXT     {token=text;}
    | or:OR         {token=or;}
    | and:AND       {token=and;}
    | not:NOT       {token=not;}
    | today:TODAY   {token=today;}
    | now:NOW       {token=now;}
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
    
    public void setInFieldCriteria(boolean inFieldCriteria) {
        mInFieldCriteria = inFieldCriteria;
    }

    public boolean isInFieldCriteria() {
        return mInFieldCriteria;
    }

    private boolean mTrace = false;
    private boolean mInFieldCriteria = false;
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
QUESTION : '?';
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
HMS : DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT
        ('.' DIGIT ((DIGIT)? DIGIT)?)?;

protected
DIGIT : ('0' .. '9');

protected
ALPHANUM
    : ('a'..'z' | 'A'..'Z' | DIGIT);

protected
TEXT
	: (ALPHANUM)+;

protected
NUMBER
    : (DIGIT)+ ('.' (DIGIT)*)* ;

protected OR : "OR" ;
protected AND : "AND" ;
protected NOT : "NOT" ;
protected TODAY : "TODAY" ;
protected NOW : "NOW" ;

// Since these all basically have overlapping patterns, we need to use
// backtracking to try them in order.
TEXT_OR_NUMBER_OR_PERIOD
    : (YMD 'T') => DATETIME {$setType(DATETIME);}
    | (DATE) => DATE {$setType(DATE);}
    | (TIME) => TIME {$setType(TIME);}
    | (NUMBER) => NUMBER {$setType(NUMBER);}
//     | {!isInFieldCriteria()}? OR {$setType(OR);}
//     | {!isInFieldCriteria()}? AND {$setType(AND);}
//     | {!isInFieldCriteria()}? NOT {$setType(NOT);}
    | (OR) => OR {$setType(OR);}
    | (AND) => AND {$setType(AND);}
    | (NOT) => NOT {$setType(NOT);}
    | (TODAY) => TODAY {$setType(TODAY);}
    | (NOW) => NOW {$setType(NOW);}
    | TEXT {$setType(TEXT);}
    ;

STRING_LITERAL
    : '"'! (~'"')* ('"'! '"' (~'"')*)* '"'!;

WS  :   (' ' | '\t' | '\n' {newline();} | '\r')
        { _ttype = Token.SKIP; }
    ;
