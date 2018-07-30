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
	: IDT { $ns.add($IDT.text); } (',' IDT { $ns.add($IDT.text); } )* { $n = $ns; }
	;

funcDef returns [FuncDefNode n]
	: DEF IDT LPAREN a=funcParams[new ArrayList<String>()]? RPAREN s=stmts[new ArrayList<StmtNode>()] END
	  {
	  	$n = new FuncDefNode($IDT.text, $a.ctx == null ? Collections.emptyList() : $a.n, $s.n);
	  }
	;

stmts[List<StmtNode> ns] returns [List<StmtNode> n]
	: ( stmt { $ns.add($stmt.n); } )+ { $n = $ns; }
	;

stmt returns [StmtNode n]
	: PRINT LPAREN expr RPAREN   { $n = new PrintNode($expr.n); } // print
	| var EQ expr                { $n = new VarLetNode($var.n, $expr.n); } // var let
	| RETURN expr                { $n = new ReturnNode($expr.n); } // return
	| IF te=expr ts=stmts[new ArrayList<>()]        { List<IfNode> elifs = new ArrayList<>(); List<StmtNode> els = Collections.emptyList(); } // if
	  ( ELIF eie=expr eis=stmts[new ArrayList<>()]  { elifs.add(new IfNode($eie.n, $eis.n, Collections.emptyList())); } )* 
	  ( 
	  	ELSE es=stmts[new ArrayList<>()]            
	  	{
	        if (elifs.isEmpty()) {
	            els = $es.n;
	        } else {
	            elifs.get(elifs.size() - 1).setElseBody($es.n);
	        }
	  	} 
	  )? 
	  END
	  {
	  	IfNode root = new IfNode($te.n, $ts.n, Collections.emptyList());
        if (elifs.isEmpty()) {
            root.setElseBody(els);
        } else {
            root.setElseBody(Arrays.asList(IfNode.joinElifs(elifs)));
        }
	  	$n = root;
	  }
	| WHILE expr DO? stmts[new ArrayList<>()] END  { $n = new WhileNode($expr.n, $stmts.n); }  // while
	| BREAK                                        { $n = new BreakNode(); }  // break
	;

exprList[List<ExprNode> ns] returns [List<ExprNode> n]
	: expr { $ns.add($expr.n); } (',' expr { $ns.add($expr.n); } )* { $n = $ns; }
	;

expr returns [ExprNode n]
	: l=expr op=('*'|'/') r=expr                      { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| l=expr op=('+'|'-') r=expr                      { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| l=expr op=('=='|'!='|'>'|'<'|'>='|'<=') r=expr  { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| INTVAL                                          { $n = new IntNode($INTVAL.int); }
	| STRVAL                                          { $n = new StrNode($STRVAL.text); }
	| LBRACK es=exprList[new ArrayList<>()]? RBRACK // array
	  { 
	  	$n = new ArrayNode($es.ctx == null ? Collections.emptyList() : $es.n); 
	  } 
	| IDT LPAREN a=exprList[new ArrayList<ExprNode>()]? RPAREN // funcCall
	  {
	  	$n = new FuncCallNode($IDT.text, $a.ctx == null ? Collections.emptyList() : $a.n); 
	  }
	| var LBRACK expr RBRACK { $n = new ArrayElemRefNode($var.n, $expr.n); }
	| var                    { $n = $var.n; }
	| LPAREN expr RPAREN     { $n = $expr.n; }
	;

var returns [VarRefNode n]
	: IDT { $n = new VarRefNode($IDT.text); }
	;

RETURN : 'return' ;
PRINT : 'print' ;
DEF : 'def' ;
END : 'end' ;
IF : 'if' ;
ELIF : 'elif' ;
ELSE : 'else' ;
WHILE : 'while' ;
DO : 'do' ;
BREAK : 'break' ;

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;

LBRACE : '{' ;	
RBRACE : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;
LBRACK : '[' ;
RBRACK : ']' ;

SEMICOLON : ';' ;
EQ : '=' ;

EQEQ : '==' ;
NOTEQ : '!=' ;
GT : '>' ;
LT : '<' ;
GTE : '>=' ;
LTE : '<=' ;

IDT : [a-z]+ ;
INTVAL : [0-9]+ ;
STRVAL : '"' ('""'|~'"')* '"' ;
NEWLINE : ('\r' '\n'?|'\n') -> skip ;
WS : [ \t]+ -> skip ;