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
    public void checkFieldType(String fieldName) {
        if (mMetadata.isValidLookupName(fieldName)) {
            mLastLookupName = fieldName;
        }
        else {
            mLastLookupName = null;
        }
    }

    public void addLookup(LookupList list, String lookupValue)
        throws RecognitionException
    {
        if (mMetadata.isValidLookupValue(mLastLookupName, lookupValue)) {
            list.addLookup(lookupValue);
        }
        else {
            throw new SemanticException("No such lookup value [" +
                                        mLastLookupName + "]: " + lookupValue);
        }
    }

    public boolean lastWasLookup() {
        return (mLastLookupName != null);
    }

    public void setMetadata(DmqlParserMetadata metadata) {
        mMetadata = metadata;
    }

    private String mLastLookupName = null;
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
    : LPAREN field=field_name {checkFieldType(field);} EQUAL sql=field_value[field] RPAREN
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
    | sql=lookup_and[name]
    | sql=lookup_not[name]
    ;

simple_lookup returns [List lookups]
    { String l; lookups = new ArrayList(); }
    : l=lookup {lookups.add(l);}
    ;

lookup_or [String name] returns [SqlConverter sql]
    { LookupList ll = new LookupList(LookupListType.OR, name); sql = ll; }
    : PIPE lookups[ll]
    | single_lookup[ll]
    ;
lookup_and [String name] returns [SqlConverter sql]
    { LookupList ll = new LookupList(LookupListType.AND, name); sql = ll; }
    : PLUS lookups[ll]
    ;

lookup_not [String name] returns [SqlConverter sql]
    { LookupList ll = new LookupList(LookupListType.NOT, name); sql = ll; }
    : TILDE lookups[ll]
    ;

lookups [LookupList list]
    { String l; }
    : l=lookup {addLookup(list, l);} (COMMA l=lookup {addLookup(list, l);})*
    ;

single_lookup [LookupList list]
    { String l; }
    : l=lookup {addLookup(list, l);}
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

