header {
package org.realtors.rets.server.dmql;
import java.util.*;
}

class DmqlParser extends Parser;

options
{
    defaultErrorHandler = false;
    buildAST = true;
    k = 3;
}

tokens 
{
    FIELD_NAME;
    LOOKUP_LIST; LOOKUP_OR; LOOKUP_AND; LOOKUP_NOT; LOOKUP;
    STRING_LIST; STRING;
    RANGE_LIST; BETWEEN; GREATER; LESS;
}

{
    private void assertValidField(Token t)
        throws SemanticException
    {
        String fieldName = t.getText();
        if (!mMetadata.isValidFieldName(fieldName)) {
            throw newSemanticException("No such field [" + fieldName + "]", t);
        }
    }

    private boolean isLookupField(String fieldName) {
        return mMetadata.isValidLookupName(fieldName);
    }

    private boolean isStringField(String fieldName) {
        return mMetadata.isValidStringName(fieldName);
    }

    private void assertValidLookupValue(AST field, Token t)
        throws SemanticException
    {
        String lookupName = field.getText();
        String lookupValue = t.getText();
        if (!mMetadata.isValidLookupValue(lookupName, lookupValue)) {
            throw newSemanticException("No such lookup value [" +
                                       lookupName + "]: " + lookupValue, t);
        }
    }

    public void setMetadata(DmqlParserMetadata metadata) {
        mMetadata = metadata;
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

    private SemanticException newSemanticException(String message, Token t) {
        return new SemanticException(message, t.getFilename(), t.getLine(),
                                     t.getColumn());
    }

    
    private boolean mTrace = false;
    private DmqlParserMetadata mMetadata;
}

query
    : search_condition e:EOF {#e.setText("");}
    ;

search_condition
    : query_clause (p:PIPE^ {#p.setType(OR);} query_clause)*
    ;

query_clause
    : query_element (c:COMMA^ {#c.setType(AND);} query_element)*
    ;

query_element
    : field_criteria
    | LPAREN! search_condition RPAREN!
    ;

field_criteria!
    : LPAREN n:field_name EQUAL v:field_value[#n] RPAREN
        {#field_criteria = #v;}
    ;

field_name
    {Token t;}
    : t=text_token {assertValidField(t);}
    ;

field_value [AST name]
    : {isLookupField(name.getText())}? lookup_list[name]
    | {isStringField(name.getText())}? string_list[name]
    | range_list[name]
    ;

range_list [AST name]
    : range (COMMA! range)*
        {#range_list = #([RANGE_LIST], name, #range_list);}
    ;

range
    : between
    | less
    | greater
    ;

between
    : (period | number) m:MINUS^ {#m.setType(BETWEEN);} (period | number)
    ;

less
    : (period | number) m:MINUS^ {#m.setType(LESS);}
    ;

greater
    : (period | number) p:PLUS^ {#p.setType(GREATER);}
    ;

number
    : NUMBER
    ;

period
    : date
    | datetime
    | TIME
    ;

date
    : DATE
    | TODAY
    ;

datetime
    : DATETIME
    | NOW
    ;

lookup_list [AST name]
    : o:lookup_or[name]
    | a:lookup_and[name]
    | n:lookup_not[name]
    ;

lookup_or [AST name]
    : p:PIPE! l1:lookups[name]
        {#lookup_or = #([LOOKUP_OR, "|"], name, l1);}
    // This is the "implied" OR
    |! l2:lookup[name]
        {#lookup_or = #([LOOKUP_OR, "|"], name, l2);}
    ;

lookup_and [AST name]
    : p:PLUS! {#p.setType(LOOKUP_AND);} l:lookups[name]
        {#lookup_and = #([LOOKUP_AND, "+"], name, l);}
    ;

lookup_not [AST name]
    : t:TILDE! {#t.setType(LOOKUP_NOT);} l:lookups[name]
        {#lookup_not = #([LOOKUP_NOT, "~"], name, l);}
    ;

lookups [AST name]
    : lookup[name] (COMMA! lookup[name])*
    ;

lookup [AST name]
    { Token t; }
    : t=t:text_token {assertValidLookupValue(name,t); #t.setType(LOOKUP);}
    | n:NUMBER       {assertValidLookupValue(name,n); #n.setType(LOOKUP);}
    ;

string_list [AST name]
    : string (COMMA! string)*
        {#string_list = #([STRING_LIST], name, #string_list);}
    ;

string
    : string_eq
    | string_start
    | string_contains
    | string_char1
    | string_char2
    ;

string_eq
    : text {#string_eq = #([STRING], #string_eq);}
    ;

string_start
    : text STAR
        {#string_start = #([STRING], #string_start);}
    ;

string_contains
    : STAR text STAR
        {#string_contains = #([STRING], #string_contains);}
    ;

// Need to split string_char into 2 separate rules, otherwise ANTLR
// cannot differentiate between a string_eq and a string_char
// string_char
//     : (TEXT)? QUESTION (TEXT)?
//     ;

string_char1
    : text QUESTION (text)?
        {#string_char1 = #([STRING], #string_char1);}
    ;

string_char2
    : QUESTION (text)?
        {#string_char2 = #([STRING], #string_char2);}
    ;

text
    { Token t; }
    : t=text_token
    ;

text_token returns [Token t]
    { t = null; }
    : txt:TEXT      {t=txt;}
    | or:OR         {t=or;      #or.setType(TEXT);}
    | and:AND       {t=and;     #and.setType(TEXT);}
    | not:NOT       {t=not;     #not.setType(TEXT);}
    | today:TODAY   {t=today;   #today.setType(TEXT);}
    | now:NOW       {t=now;     #now.setType(TEXT);}
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

class DmqlTreeParser extends TreeParser;

options
{
    defaultErrorHandler = false;
}

{
    public void traceIn(String text, AST t) {
        if (mTrace) super.traceIn(text, t);
    }

    public void traceOut(String text, AST t) {
        if (mTrace) super.traceOut(text, t);
    }

    public void setTrace(boolean trace) {
        mTrace = trace;
    }

    public void setMetadata(DmqlParserMetadata metadata) {
        mMetadata = metadata;
    }

    private LookupList newLookupOr(String field, List lookups) {
        LookupList list = new LookupList(LookupListType.OR,
                                         mMetadata.fieldToColumn(field));
        addLookups(list, field, lookups);
        return list;
    }

    private LookupList newLookupAnd(String field, List lookups) {
        LookupList list = new LookupList(LookupListType.AND,
                                         mMetadata.fieldToColumn(field));
        addLookups(list, field, lookups);
        return list;
    }

    private LookupList newLookupNot(String field, List lookups) {
        LookupList list = new LookupList(LookupListType.NOT,
                                         mMetadata.fieldToColumn(field));
        addLookups(list, field, lookups);
        return list;
    }

    private void addLookups(LookupList list, String field, List lookups) {
        for (int i = 0; i < lookups.size(); i++) {
            String lookup = (String) lookups.get(i);
            String dbValue = mMetadata.getLookupDbValue(field, lookup);
            list.addLookup(dbValue);
        }
    }

    private DmqlStringList newStringList(String field, List strings) {
        String column = mMetadata.fieldToColumn(field);
        DmqlStringList list = new DmqlStringList(column);
        for (int i = 0; i < strings.size(); i++) {
            DmqlString string = (DmqlString) strings.get(i);
            list.add(string);
        }
        return list;
    }

    private boolean mTrace = false;
    private DmqlParserMetadata mMetadata;
}

query returns [SqlConverter sql]
    : sql=compound EOF
    ;

compound returns [SqlConverter sql]
    { SqlConverter l; SqlConverter r; }
    : #(OR  l=compound r=compound)  {sql = new OrClause (l, r);}
    | #(AND l=compound r=compound)  {sql = new AndClause(l, r);}
    | #(NOT l=compound)             {sql = new NotClause(l);}
    | sql=field_criteria
    ;

field_criteria returns [SqlConverter sql]
    : sql=lookup_list
    | sql=string_list
    | sql=range_list
    | sql=string_literal
    | sql=number_value
    | sql=period_value
    ;

field returns [String text]
    : t:TEXT {text = t.getText();}
    ;

lookup_list returns [LookupList list]
    { String f; List l; }
    : #(LOOKUP_OR  f=field l=lookups)   {list = newLookupOr (f, l);}
    | #(LOOKUP_AND f=field l=lookups)   {list = newLookupAnd(f, l);}
    | #(LOOKUP_NOT f=field l=lookups)   {list = newLookupNot(f, l);}
    ;

lookups returns [List lookups]
    { lookups = new ArrayList();}
    : (l:LOOKUP {lookups.add(l.getText());}
        )+
    ;

string_list returns [DmqlStringList list]
    { String f; List s; }
    : #(STRING_LIST f=field s=strings) {list = newStringList(f, s);}
    ;

strings returns [List list]
    { list = new ArrayList(); DmqlString s;}
    : (s=string {list.add(s);}
        )+
    ;

string returns [DmqlString string]
    { string = new DmqlString(); }
    : #(STRING
            (t:TEXT     {string.add(t.getText());}
            | STAR      {string.add(DmqlString.MATCH_ZERO_OR_MORE);}
            | QUESTION  {string.add(DmqlString.MATCH_ZERO_OR_ONE);}
            )+
        )
    ;

range_list returns [OrClause or]
    { or = new OrClause(); String f; SqlConverter r;}
    : #(RANGE_LIST f=field
            (r=range[f] {or.add(r);}
            )+
        )
    ;

range [String field] returns [SqlConverter sql]
    { sql = null; SqlConverter c1; SqlConverter c2;}
    : #(BETWEEN c1=range_component c2=range_component)
        {sql = new BetweenClause(field, c1, c2);}
    | #(LESS c1=range_component)
    | #(GREATER c1=range_component)
    ;

range_component returns [SqlConverter sql]
    : p:period  {sql = new StringSqlConverter(p.getText());}
    | n:NUMBER  {sql = new StringSqlConverter(n.getText());}
    | TEXT      {sql = null;}
    ;

period
    : DATE | TODAY | DATETIME | NOW | TIME
    ;

string_literal returns [SqlConverter sql]
    { sql = null; }
    : STRING_LITERAL
    ;

number_value returns [SqlConverter sql]
    { sql = null; }
    : NUMBER
    ;

period_value returns [SqlConverter sql]
    { sql = null; }
    : PERIOD
    ;
