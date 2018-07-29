package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public final class StrNode extends ExprNode {
    
    private final String value;
    
    public StrNode(String value) {
        this.value = value.substring(1, value.length() - 1); // trim double quate("")
    }
    
    @Override
    public <E, S> E accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
