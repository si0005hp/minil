package minil.ast;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class ContinueNode extends StmtNode {
    
    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
}
