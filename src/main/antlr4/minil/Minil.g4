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
	: ( stmt { $ns.add($stmt.n); } )+ { $n = $ns; }
	;

stmt returns [StmtNode n]
	: PRINT LPAREN expr RPAREN   { $n = new PrintNode($expr.n); } // print
	| var EQ expr                { $n = new LetNode($var.text, $expr.n); } // let
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

funcArgs[List<ExprNode> ns] returns [List<ExprNode> n]
	: expr { $ns.add($expr.n); } (',' expr { $ns.add($expr.n); } )* { $n = $ns; }
	;

expr returns [ExprNode n]
	: l=expr op=('*'|'/') r=expr                      { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| l=expr op=('+'|'-') r=expr                      { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| l=expr op=('=='|'!='|'>'|'<'|'>='|'<=') r=expr  { $n = new BinOpNode($op.type, $l.n, $r.n); }
	| INTVAL                                          { $n = new IntNode($INTVAL.int); }
	| IDT LPAREN a=funcArgs[new ArrayList<ExprNode>()]? RPAREN // funcCall
	  {
	  	$n = new FuncCallNode($IDT.text, $a.ctx == null ? Collections.emptyList() : $a.n); 
	  }
	| var                 { $n = new VarRefNode($var.text); }
	| LPAREN expr RPAREN  { $n = $expr.n; }
	;

var : IDT ;

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
NEWLINE : ('\r' '\n'?|'\n') -> skip ;
WS : [ \t]+ -> skip ;