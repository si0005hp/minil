grammar Minil;

@header {
import minil.ast.*;
}

@parser::members {
}

program returns [ProgramNode n]
	: v=stmts[new java.util.ArrayList<StmtNode>()] { $n = new ProgramNode($v.n); }
	;



stmts[List<StmtNode> ns] returns [List<StmtNode> n]
	: ( v=stmt { $ns.add($v.n); } )+ { $n = $ns; }
	;

stmt returns [StmtNode n]
	: PRINT LPAREN expr RPAREN SEMICOLON { $n = new PrintNode($expr.n); }
	| var EQ expr SEMICOLON { $n = new LetNode($var.text, $expr.n); }
	;

expr returns [ExprNode n]
	: l=expr op=('*'|'/') r=expr  { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| l=expr op=('+'|'-') r=expr  { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| INTVAL                      { $n = new IntNode($INTVAL.int); }
	| var                         { $n = new VarRefNode($var.text); }
	| LPAREN expr RPAREN          { $n = $expr.n; }
	;

var : IDT ;

PRINT : 'print' ;

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;

LBRACE : '{' ;	
RBRACE : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;

SEMICOLON : ';' ;
EQ : '=' ;

IDT : [a-z]+ ;
INTVAL : [0-9]+ ;
NEWLINE : ('\r' '\n'?|'\n') -> skip ;
WS : [ \t]+ -> skip ;