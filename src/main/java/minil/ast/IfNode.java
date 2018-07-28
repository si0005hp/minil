package minil.ast;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import minil.NodeVisitor;

@Getter
@Setter
public class IfNode extends StmtNode {

    private ExprNode condExpr;
    private List<StmtNode> thenBody;
    private List<StmtNode> elseBody;
    
    public IfNode(ExprNode condExpr, List<StmtNode> thenBody, List<StmtNode> elseBody) {
        this.condExpr = condExpr;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
    
    public static IfNode joinElifs(List<IfNode> elifs) {
        if (elifs.isEmpty()) {
            throw new IllegalArgumentException("Not supposed to receive empty elif list.");
        } else if (elifs.size() < 2) {
            return elifs.get(0);
        }
        
        IfNode n = elifs.get(0);
        n.setElseBody(Arrays.asList(joinElifs(elifs.subList(1, elifs.size()))));
        return n;
    }
    
}
