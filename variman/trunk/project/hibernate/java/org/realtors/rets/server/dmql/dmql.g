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

    public boolean isLookupField(String fieldName) {
        return mMetadata.isValidLookupName(fieldName);
    }

    public boolean isStringField(String fieldName) {
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

    private void addStringEq(DmqlStringList list, Token t) 
        throws SemanticException
    {
        list.add(new DmqlString(t.getText()));
    }

    private void addStringStart(DmqlStringList list, Token t) 
        throws SemanticException
    {
        DmqlString string = new DmqlString();
        string.add(t.getText());
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
    }

    private void addStringContains(DmqlStringList list, Token t) 
        throws SemanticException
    {
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add(t.getText());
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
    }

    private void add(DmqlString string, Token t) {
        string.add(t.getText());
    }

    private void add(DmqlString string, DmqlStringComponent component) {
        string.add(component);
    }

    private void addStar(DmqlString string) {
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
    }

    private void addQuestion(DmqlString string) {
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
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
    : {isLookupField(name)}? sql=lookup_list[name]
    | {isStringField(name)}? sql=string_list[name]
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

string_list [String name] returns [SqlConverter sql]
    { DmqlStringList list = new DmqlStringList(name); sql = list; }
    : string[list] (COMMA string[list])*
    ;

string [DmqlStringList list]
    { Token t; DmqlString s = new DmqlString(); }
    : t=string_eq {add(s, t); list.add(s);}
    | string_start[s] {list.add(s);}
    | string_contains[s] {list.add(s);}
    | string_char1[s] {list.add(s);}
    | string_char2[s] {list.add(s);}
    ;

string_eq returns [Token t]
    : t=text
    ;

string_start [DmqlString s]
    { Token t; }
    : t=text {add(s, t);} STAR {addStar(s);}
    ;

string_contains [DmqlString s]
    { Token t; }
    : STAR {addStar(s);} t=text {add(s, t);} STAR {addStar(s);}
    ;

// Need to split string_char into 2 separate rules, otherwise ANTLR
// cannot differentiate between a string_eq and a string_char
// string_char
//     : (TEXT)? QUESTION (TEXT)?
//     ;

string_char1 [DmqlString s]
    { Token t; }
    : t=text {add(s, t);} QUESTION {addQuestion(s);} (t=text {add(s, t);})?
    ;

string_char2 [DmqlString s]
    { Token t; }
    : QUESTION {addQuestion(s);} (t=text {add(s, t);})?
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
