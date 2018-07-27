grammar Minil;

@header {
import minil.ast.*;
import java.util.*;
import java.util.stream.*;
}

@parser::members {
}

program returns [ProgramNode n]
	: v=topLevels[new ArrayList<>()]? 
	  { 
	  	$n = new ProgramNode($v.ctx == null ? Collections.emptyList() : $v.n);
	  }
	;

topLevels[List<Node> ns] returns [List<Node> n]
	: (
		funcDef  { $ns.add($funcDef.n); } | 
		stmt     { $ns.add($stmt.n); } |
		expr     { $ns.add($expr.n); }
	  )+ { $n = $ns; }
	;

funcParams[List<String> ns] returns [List<String> n]
	: var { $ns.add($var.text); } (',' var { $ns.add($var.text); } )* { $n = $ns; }
	;

funcDef returns [FuncDefNode n]
	: DEF IDT LPAREN a=funcParams[new ArrayList<String>()]? RPAREN s=stmts[new ArrayList<StmtNode>()] END
	  {
	  	$n = new FuncDefNode($IDT.text, $a.ctx == null ? Collections.emptyList() : $a.n, $s.n);
	  }
	;

stmts[List<StmtNode> ns] returns [List<StmtNode> n]
	: ( v=stmt { $ns.add($v.n); } )+ { $n = $ns; }
	;

stmt returns [StmtNode n]
	: PRINT LPAREN expr RPAREN   { $n = new PrintNode($expr.n); } // print
	| var EQ expr                { $n = new LetNode($var.text, $expr.n); } // let
	;

funcArgs[List<ExprNode> ns] returns [List<ExprNode> n]
	: expr { $ns.add($expr.n); } (',' expr { $ns.add($expr.n); } )* { $n = $ns; }
	;

expr returns [ExprNode n]
	: l=expr op=('*'|'/') r=expr  { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| l=expr op=('+'|'-') r=expr  { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| INTVAL                      { $n = new IntNode($INTVAL.int); }
	| IDT LPAREN a=funcArgs[new ArrayList<ExprNode>()]? RPAREN // funcCall
	  {
	  	$n = new FuncCallNode($IDT.text, $a.ctx == null ? Collections.emptyList() : $a.n); 
	  }
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