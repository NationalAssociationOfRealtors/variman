header {
package org.realtors.rets.server.dmql;
import java.util.*;
}

class DmqlParser extends Parser;

options
{
	exportVocab = DmqlParser;
	k = 3;
}

{
    public void checkFieldName(String fieldName) {
        if (fieldName.equals("STATUS")){
            System.out.println("Is status");
        }
        else {
            System.out.println("Is not status");
        }
    }

    public void checkFieldType(String fieldName) {
        if (fieldName.equals("AR")) {
            mLastWasLookup = true;
        }
        else {
            mLastWasLookup = false;
        }

    }

    public boolean lastWasLookup() {
        return mLastWasLookup;
    }

    private boolean mLastWasLookup = false;
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
    : LPAREN field=field_name {checkFieldType(field);} EQUAL sql=field_value[field] RPAREN
        {
            checkFieldName(field);
        }
    ;

field_name returns [String field]
    { field = null; }
    : t:TEXT {field=t.getText();}
    ;

field_value [String name] returns [SqlConverter sql]
    { sql = null; }
    : {lastWasLookup()}? sql=lookup_list[name]
//    | string_list
    ;

lookup_list [String name] returns [SqlConverter sql]
    { sql = null;}
    : sql=lookup_or[name]
//    | l=lookup_and      {System.out.println(l);}
//    | l=lookup_not      {System.out.println(l);}
//    | l=simple_lookup   {System.out.println(l);}
    ;

simple_lookup returns [List lookups]
    { String l; lookups = new ArrayList(); }
    : l=lookup {lookups.add(l);}
    ;

lookup_or [String name] returns [SqlConverter sql]
    { LookupList ll = new LookupList(LookupListType.OR, name); sql = ll; }
    : PIPE lookups[ll]
    ;

lookup_and
    : PLUS lookups[null]
    ;

lookup_not
    : TILDE lookups[null]
    ;

lookups [LookupList ll]
    { String l; }
    : l=lookup {ll.addLookup(l);} (COMMA l=lookup {ll.addLookup(l);})*
    ;

lookup returns [String lookup]
    { lookup = null; }
    : t:TEXT {lookup = t.getText();}
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
    : t:TEXT {System.out.println("string_eq: " + t.getText());}
    ;

string_start
    : STAR TEXT
    ;

string_contains
    : STAR TEXT STAR
    ;

string_char
    : TEXT QUESTION string_char
    ;

class DmqlLexer extends Lexer;

options
{
	k = 11;
	testLiterals = false;
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

DATETIME : ('0'..'9')('0'..'9')('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') 'T' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ('.' ('0'..'9')('0'..'9'))?;
DATE : ('0'..'9')('0'..'9')('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9');
TIME : ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ('.' ('0'..'9')('0'..'9'))?;

TEXT
	options { testLiterals = true; }
	: ('a'..'z' | 'A'..'Z' | '0'..'9')+;

WS  :   (' ' | '\t' | '\n' | '\r')
        { _ttype = Token.SKIP; }
    ;

