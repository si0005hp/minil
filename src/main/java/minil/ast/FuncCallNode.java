package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class FuncCallNode extends StmtNode {

    private final String fname;
    private final List<ExprNode> exprs;
    
    public FuncCallNode(String fname, List<ExprNode> exprs) {
        this.fname = fname;
        this.exprs = exprs;
    }
    
    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
}
