package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class VarLetNode extends StmtNode {

    private final VarRefNode var;
    private final ExprNode expr;
    
    public VarLetNode(VarRefNode var, ExprNode expr) {
        this.var = var;
        this.expr = expr;
    }
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
