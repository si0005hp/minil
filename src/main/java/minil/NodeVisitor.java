package minil;

import minil.ast.BinOpNode;
import minil.ast.IntNode;
import minil.ast.LetNode;
import minil.ast.PrintNode;
import minil.ast.ProgramNode;
import minil.ast.VarRefNode;

public interface NodeVisitor<T> {
    T visit(IntNode n);
    T visit(BinOpNode n);
    T visit(PrintNode n);
    T visit(ProgramNode n);
    T visit(LetNode n);
    T visit(VarRefNode n);
}
