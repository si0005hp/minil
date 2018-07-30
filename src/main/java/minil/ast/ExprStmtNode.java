package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

/**
 * Express the expression as statement
 */
@Getter
public class ExprStmtNode extends StmtNode {

    private final ExprNode expr;
    
    public ExprStmtNode(ExprNode expr) {
        this.expr = expr;
    }
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
