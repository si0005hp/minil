package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public class ProgramNode extends Node {

    private final List<StmtNode> stmts;
    private final List<FuncDefNode> funcDefs;
    
    public ProgramNode(List<StmtNode> stmts, List<FuncDefNode> funcDefs) {
        this.stmts = stmts;
        this.funcDefs = funcDefs;
    }
    
    @Override
    public <T> T accept(NodeVisitor<T> v) {
        return v.visit(this);
    }
}
