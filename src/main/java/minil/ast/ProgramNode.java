package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class ProgramNode extends Node {

    private final List<Node> topLevels;
    
    public ProgramNode(List<Node> topLevels) {
        this.topLevels = topLevels;
    }
    
    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
}
