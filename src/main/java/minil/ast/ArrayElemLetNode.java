package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public final class ArrayElemLetNode extends StmtNode {

    private final ArrayElemRefNode elem;
    private final ExprNode expr;

    public ArrayElemLetNode(ArrayElemRefNode elem, ExprNode expr) {
        this.elem = elem;
        this.expr = expr;
    }

    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
