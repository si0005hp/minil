package minil;

import minil.ast.ArrayElemLetNode;
import minil.ast.ArrayElemRefNode;
import minil.ast.ArrayLengthNode;
import minil.ast.ArrayNode;
import minil.ast.BinOpNode;
import minil.ast.BreakNode;
import minil.ast.ContinueNode;
import minil.ast.ExprStmtNode;
import minil.ast.FuncCallNode;
import minil.ast.FuncDefNode;
import minil.ast.IfNode;
import minil.ast.IntNode;
import minil.ast.PrintNode;
import minil.ast.ProgramNode;
import minil.ast.ReturnNode;
import minil.ast.StrNode;
import minil.ast.VarLetNode;
import minil.ast.VarRefNode;
import minil.ast.WhileNode;

public interface NodeVisitor<E, S> {
    // Expr
    E visit(IntNode n);
    E visit(BinOpNode n);
    E visit(VarRefNode n);
    E visit(FuncCallNode n);
    E visit(StrNode n);
    E visit(ArrayNode n);
    E visit(ArrayElemRefNode n);
    E visit(ArrayLengthNode n);
    // Stmt
    S visit(PrintNode n);
    S visit(VarLetNode n);
    S visit(ReturnNode n);
    S visit(IfNode n);
    S visit(WhileNode n);
    S visit(BreakNode n);
    S visit(ArrayElemLetNode n);
    S visit(ExprStmtNode n);
    S visit(FuncDefNode n);
    S visit(ContinueNode n);
    // Others
    S visit(ProgramNode n);
}
