package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class ReturnNode extends StmtNode {

    private final ExprNode expr;
    
    public ReturnNode(ExprNode expr) {
        this.expr = expr;
    }
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
