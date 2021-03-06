package minil.ast;

import minil.NodeVisitor;
import lombok.Getter;

@Getter
public final class BinOpNode extends ExprNode {
    
    private final int opType;
    private final ExprNode left;
    private final ExprNode right;
    
    public BinOpNode(int opType, ExprNode left, ExprNode right) {
        this.opType = opType;
        this.left = left;
        this.right = right;
    }

    @Override
    public <E, S> E accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
