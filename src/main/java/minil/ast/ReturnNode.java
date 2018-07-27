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
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
}
