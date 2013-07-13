header {
package org.realtors.rets.server.dmql;
import java.util.*;
}

class Dmql2Parser extends DmqlParser;

options
{
    importVocab = DMQL;
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
    | DOT_ANY {#field_value = #([DOT_ANY], name, #field_value);}
    | DOT_EMPTY {#field_value = #([DOT_EMPTY], name, #field_value);}
    | {isCharacterField(name.getText())}? string_list[name]
    | {isCharacterField(name.getText())}? string_literal[name]
    | (range_list[name]) => range_list[name]
    | number {#field_value = #([NUMBER], name, #field_value);}
    | period {#field_value = #([PERIOD], name, #field_value);}
    ;
    
number
      {boolean isUnaryMinus = false; }
    : (MINUS! {isUnaryMinus = true;})? 
        n:NUMBER {if (isUnaryMinus) n_AST.setText("-" + n_AST.getText());}
    ;

string_literal [AST name]
    : s:STRING_LITERAL {#string_literal =  #([STRING_LITERAL], name, s); }
    ;

between [AST name]
    : {isNumericField(name.getText())}? between_number
    | (period) => between_period
    | between_text
    ;

between_text
    : text m:MINUS^ {#m.setType(BETWEEN);} text
    ;
    
range [AST name]
    : (less[name]) => less[name]
    | (greater[name]) => greater[name]
    | (between[name]) => between[name]
    ;

less [AST name]
    : {isNumericField(name.getText())}? (number MINUS RPAREN) => less_number
    | (period) => less_period
    | (less_text) => less_text
    ;

less_text
    : text m:MINUS^ {#m.setType(LESS);}
    ;

greater [AST name]
    : {isNumericField(name.getText())}? greater_number
    | (period) => greater_period
    | greater_text
    ;

greater_text
    : text p:PLUS^ {#p.setType(GREATER);}
    ;
