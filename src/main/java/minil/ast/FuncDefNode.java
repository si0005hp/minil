package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public final class FuncDefNode extends Node {
    
    private final String fname;
    private final List<String> params;
    private final List<StmtNode> stmts;
    
    public FuncDefNode(String fname, List<String> args, List<StmtNode> stmts) {
        this.fname = fname;
        this.params = args;
        this.stmts = stmts;
    }
    
    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
    
}
