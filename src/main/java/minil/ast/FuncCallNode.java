package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class FuncCallNode extends ExprNode {

    private final String fname;
    private final List<ExprNode> exprs;

    public FuncCallNode(String fname, List<ExprNode> exprs) {
        this.fname = fname;
        this.exprs = exprs;
    }

    @Override
    public <E, S> E accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
