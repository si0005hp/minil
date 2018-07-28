package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class LetNode extends StmtNode {

    private final String vname;
    private final ExprNode expr;
    
    public LetNode(String vname, ExprNode expr) {
        this.vname = vname;
        this.expr = expr;
    }
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
