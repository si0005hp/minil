grammar Minil;

@header {
import minil.ast.*;
}

@parser::members {
}

program returns [ProgramNode n]
	: s=stmts[new ArrayList<StmtNode>()] f=funcDefs[new ArrayList<FuncDefNode>()] { $n = new ProgramNode($s.n, $f.n); }
	;

funcParams[List<String> ns] returns [List<String> n]
	: var { $ns.add($var.text); } (',' var { $ns.add($var.text); } )* { $n = $ns; }
	;

funcDefs[List<FuncDefNode> ns] returns [List<FuncDefNode> n]
	: ( f=funcDef { $ns.add($f.n); } )+ { $n = $ns; }
	;

funcDef returns [FuncDefNode n]
	: DEF IDT LPAREN a=funcParams[new ArrayList<String>()] RPAREN s=stmts[new ArrayList<StmtNode>()] END { $n = new FuncDefNode($IDT.text, $a.n, $s.n); }
	;

stmts[List<StmtNode> ns] returns [List<StmtNode> n]
	: ( v=stmt { $ns.add($v.n); } )+ { $n = $ns; }
	;

stmt returns [StmtNode n]
	: PRINT LPAREN expr RPAREN SEMICOLON   { $n = new PrintNode($expr.n); } // print
	| var EQ expr SEMICOLON                { $n = new LetNode($var.text, $expr.n); } // let
	| IDT LPAREN a=funcArgs[new ArrayList<ExprNode>()] RPAREN SEMICOLON { $n = new FuncCallNode($IDT.text, $a.n); } // funcCall
	;

funcArgs[List<ExprNode> ns] returns [List<ExprNode> n]
	: expr { $ns.add($expr.n); } (',' expr { $ns.add($expr.n); } )* { $n = $ns; }
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
DEF : 'def' ;
END : 'end' ;

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