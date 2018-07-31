package minil.ast;

import java.util.List;

import lombok.Getter;
import minil.NodeVisitor;

@Getter
public final class FuncDefNode extends StmtNode {

    private final String fname;
    private final List<String> params;
    private final List<StmtNode> stmts;

    public FuncDefNode(String fname, List<String> args, List<StmtNode> stmts) {
        this.fname = fname;
        this.params = args;
        this.stmts = stmts;
    }

    @Override
    public <E, S> S accept(NodeVisitor<E, S> v) {
        return v.visit(this);
    }

}
