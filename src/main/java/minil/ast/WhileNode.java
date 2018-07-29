package minil.ast;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import minil.NodeVisitor;

@Getter
@Setter
public class WhileNode extends StmtNode {

    private ExprNode condExpr;
    private List<StmtNode> body;
    
    public WhileNode(ExprNode condExpr, List<StmtNode> body) {
        this.condExpr = condExpr;
        this.body = body;
    }
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
