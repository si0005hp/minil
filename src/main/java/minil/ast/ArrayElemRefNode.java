package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public final class ArrayElemRefNode extends ExprNode {
    
    private final VarRefNode arrname;
    private final ExprNode idx;
    
    public ArrayElemRefNode(VarRefNode arrname, ExprNode idx) {
        this.arrname = arrname;
        this.idx = idx;
    }
    
    @Override
    public <E, S> E accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
