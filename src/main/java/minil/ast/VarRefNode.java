package minil.ast;

import minil.NodeVisitor;
import lombok.Getter;

@Getter
public final class VarRefNode extends ExprNode {
    
    private final String vname;
    
    public VarRefNode(String vname) {
        this.vname = vname;
    }
    
    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
    
}

