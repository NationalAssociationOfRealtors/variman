header {
package org.realtors.rets.server.dmql;
import java.util.*;
}

class Dmql2Parser extends DmqlParser;

options
{
    defaultErrorHandler = false;
	k = 3;
    buildAST = true;
}


query
    : search_condition  e:EOF {#e.setText("");}
    ;

search_condition
    : query_clause (
            (OR^ | p:PIPE^ {#p.setType(OR);})
            query_clause)*
    ;

query_clause
    : boolean_element (
            (AND^ | c:COMMA^ {#c.setType(AND);} )
            boolean_element)*
    ;

boolean_element
    : (NOT^ | t:TILDE^ {#t.setType(NOT);}) query_element
    | query_element
    ;

field_value [AST name]
    : {isLookupField(name.getText())}? lookup_list[name]
    | {isStringField(name.getText())}? string_list[name]
    | {isStringField(name.getText())}? STRING_LITERAL^
    | number {#field_value = #([NUMBER], #field_value);}
    | period {#field_value = #([PERIOD], #field_value);}
    | range_list[name]
    ;

between
    : ((period) => period | number | text) m:MINUS^ {#m.setType(BETWEEN);}
        ((period) => period | number | text)
    ;

less
    : ((period) => period | number | text) m:MINUS^ {#m.setType(LESS);}
    ;

greater
    : ((period) => period | number | text) p:PLUS^ {#p.setType(GREATER);}
    ;
