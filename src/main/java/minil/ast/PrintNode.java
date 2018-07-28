package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class PrintNode extends StmtNode {

    private final ExprNode expr;
    
    public PrintNode(ExprNode expr) {
        this.expr = expr;
    }
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
