package minil;

import minil.ast.BinOpNode;
import minil.ast.BreakNode;
import minil.ast.FuncCallNode;
import minil.ast.FuncDefNode;
import minil.ast.IfNode;
import minil.ast.IntNode;
import minil.ast.LetNode;
import minil.ast.PrintNode;
import minil.ast.ProgramNode;
import minil.ast.ReturnNode;
import minil.ast.StrNode;
import minil.ast.VarRefNode;
import minil.ast.WhileNode;

public interface NodeVisitor<E, S> {
    // Expr
    E visit(IntNode n);
    E visit(BinOpNode n);
    E visit(VarRefNode n);
    E visit(FuncCallNode n);
    E visit(StrNode n);
    // Stmt
    S visit(PrintNode n);
    S visit(LetNode n);
    S visit(ReturnNode n);
    S visit(IfNode n);
    S visit(WhileNode n);
    S visit(BreakNode n);
    // Others
    S visit(ProgramNode n);
    S visit(FuncDefNode n);
}
