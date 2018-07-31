package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public final class ArrayLengthNode extends ExprNode {

    private final VarRefNode arrname;

    public ArrayLengthNode(VarRefNode arrname) {
        this.arrname = arrname;
    }

    @Override
    public <E, S> E accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
