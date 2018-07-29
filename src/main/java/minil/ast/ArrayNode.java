package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public final class ArrayNode extends ExprNode {
    
    private final List<ExprNode> exprs;
    
    public ArrayNode(List<ExprNode> exprs) {
        this.exprs = exprs;
    }
    
    @Override
    public <E, S> E accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
