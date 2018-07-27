grammar Work;

@header {
}

@parser::members {
}

program returns [Program n]
	: '(' v=funcParams[new ArrayList<String>()]? ')' { $n = new Program($v.ctx == null ? null : $v.n); }
	| foobars[new ArrayList<>()] { $n = new Program($foobars.n); }
	;

foobars[List<String> ns] returns [List<String> n]
	: (
	    foo { $ns.add($foo.text); } | 
		bar { $ns.add($bar.text); }
	  )+ { $n = $ns; }
	;

foo : 'foo' ;
bar : 'bar' ;

funcParams[List<String> ns] returns [List<String> n]
	: var { $ns.add($var.text); } (',' var { $ns.add($var.text); } )* { $n = $ns; }
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