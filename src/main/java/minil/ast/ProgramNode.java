package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class ProgramNode extends Node {

    private final List<StmtNode> topLevels;
    
    public ProgramNode(List<StmtNode> topLevels) {
        this.topLevels = topLevels;
    }

    @Override
    public <E, S> Object accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }
 
}
