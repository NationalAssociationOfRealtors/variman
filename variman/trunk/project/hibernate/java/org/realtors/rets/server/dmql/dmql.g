header {
package org.realtors.rets.server.dmql;
}

class OldDmqlParser extends Parser;

options
{
	exportVocab = DmqlParser;
	buildAST = true;
	k = 3;
}

query
	: LPAREN! subquery RPAREN!
			(options {greedy = true;}: COMMA^<AST=QueryNode> query)* EOF!
	;

subquery
	: atom_subquery
	| disj_subquery
	;

atom_subquery
	: TEXT<AST=FieldNameNode> EQUAL^<AST=SubqueryNode> field_value
	;

disj_subquery
	: LPAREN! atom_subquery RPAREN!
			(options {greedy = true;}: PIPE^<AST=DisjunctionNode>
				disj_subquery)?
	;

field_value
	: string_list
	| range_list
	| lookup_list
	;

string_list!
	: s1:string (options {greedy = true;}: COMMA^ s2:string_list)*
		{ #string_list = new StringListNode (#s1, #s2); }
	;

range_list!
	: r1:range (options {greedy = true;}: COMMA^ r2:range_list)*
		{ #range_list = new RangeListNode (#r1, #r2); }
	;

range
	: r_a_1:range_literal PLUS^
		{ #range = new RangeGreaterNode (#r_a_1, null); }
	| r_b_1:range_literal MINUS^
		{ #range = new RangeLessNode (#r_b_1, null); }
	| r_c_1:range_literal MINUS^ r_c_2:range_literal
		{ #range = new RangeBetweenNode (#r_c_1, #r_c_2); }
	;

range_literal
	: TEXT<AST=TextNode>
	| "TODAY"<AST=TodayNode>
	| "NOW"<AST=NowNode>
	| DATE<AST=DateNode>
	| TIME<AST=TimeNode>
	| DATETIME<AST=DateTimeNode>
	;

lookup_list!
	: PLUS^ s_a:string_list
	    { #lookup_list = new LookupAndNode (#s_a); }
	| PIPE^ s_b:string_list
	    { #lookup_list = new LookupOrNode (#s_b); }
	| TILDE^ s_c:string_list
	    { #lookup_list = new LookupNotNode (#s_c); }
	;

string
	: TEXT<AST=StringEqNode>
	| TEXT<AST=StringStartNode> STAR!
	| STAR! TEXT<AST=StringContainsNode> STAR!
	| q_a:QUESTION!
		{ #string = new StringCharNode (null, q_a, null); }
	| q_b:QUESTION! t_b_2:TEXT
		{ #string = new StringCharNode (null, q_b, t_b_2); }
	| t_c_1:TEXT q_c:QUESTION!
		{ #string = new StringCharNode (t_c_1, q_c, null); }
	| t_d_1:TEXT q_d:QUESTION! t_d_2:TEXT
		{ #string = new StringCharNode (t_d_1, q_d, t_d_2); }
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

DATETIME : ('0'..'9')('0'..'9')('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') 'T' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ('.' ('0'..'9')('0'..'9'))?;
DATE : ('0'..'9')('0'..'9')('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9');
TIME : ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ('.' ('0'..'9')('0'..'9'))?;

TEXT
	options { testLiterals = true; }
	: ('a'..'z' | 'A'..'Z' | '0'..'9')+;

WS  :   (' ' | '\t' | '\n' | '\r')
        { _ttype = Token.SKIP; }
    ;

