package minil.ast;

import minil.NodeVisitor;
import lombok.Getter;

@Getter
public final class IntNode extends ExprNode {
    
    private final int value;
    
    public IntNode(int value) {
        this.value = value;
    }
    
    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
    
}
