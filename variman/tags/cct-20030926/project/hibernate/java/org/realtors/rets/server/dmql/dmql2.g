header {
package org.realtors.rets.server.dmql;
import java.util.*;
}

class Dmql2Parser extends DmqlParser;

options
{
    defaultErrorHandler = false;
	k = 3;
}


query returns [SqlConverter sql]
    : sql=search_condition EOF
    ;

search_condition returns [SqlConverter sql]
    { SqlConverter sc;}
    : sql=query_clause (or sc=query_clause)*
    ;

query_clause returns [SqlConverter sql]
    { SqlConverter be;}
    : sql=boolean_element (and be=boolean_element)*
    ;

boolean_element returns [SqlConverter sql]
    : (not)? sql=query_element
    ;

or
    : PIPE
    | OR
    ;

and
    : COMMA
    | AND
    ;

not
    : NOT
    | TILDE
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

between
    : ((period) => period | number | string_eq) MINUS
        ((period) => period | number | string_eq)
    ;

less
    : ((period) => period | number | string_eq) MINUS
    ;

greater
    : ((period) => period | number | string_eq) PLUS
    ;
