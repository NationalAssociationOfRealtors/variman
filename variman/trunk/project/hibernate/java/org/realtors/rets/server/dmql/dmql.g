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

    private SemanticException newSemanticException(String message, Token t)
    {
        return new SemanticException(message, t.getFilename(), t.getLine(),
                                     t.getColumn());
    }

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
    : t:TEXT
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

