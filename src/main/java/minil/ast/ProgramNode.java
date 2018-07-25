package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class ProgramNode extends Node {

    private final List<StmtNode> stmts;
    
    public ProgramNode(List<StmtNode> stmts) {
        this.stmts = stmts;
    }
    
    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
}
