package minil;

import static minil.MinilParser.ADD;
import static minil.MinilParser.DIV;
import static minil.MinilParser.MUL;
import static minil.MinilParser.SUB;

import java.util.HashMap;
import java.util.Map;

import minil.ast.BinOpNode;
import minil.ast.FuncCallNode;
import minil.ast.FuncDefNode;
import minil.ast.IntNode;
import minil.ast.LetNode;
import minil.ast.PrintNode;
import minil.ast.ProgramNode;
import minil.ast.StmtNode;
import minil.ast.VarRefNode;

public class NodeEvaluator implements NodeVisitor<Integer> {

    private final Map<String, Integer> varMap = new HashMap<>();
    
    @Override
    public Integer visit(IntNode n) {
        return n.getValue();
    }

    @Override
    public Integer visit(BinOpNode n) {
        int l = n.getLeft().accept(this);
        int r = n.getRight().accept(this);
        switch (n.getOpType()) {
        case ADD: return l + r;
        case SUB: return l - r;
        case MUL: return l * r;
        case DIV: return l / r;
        }
        throw new RuntimeException();
    }

    @Override
    public Integer visit(PrintNode n) {
        int val = n.getExpr().accept(this);
        System.out.println(val);
        return 0;
    }

    @Override
    public Integer visit(ProgramNode n) {
        for (FuncDefNode f : n.getFuncDefs()) {
            f.accept(this);
        }
        for (StmtNode s : n.getStmts()) {
            s.accept(this);
        }
        return 0;
    }

    @Override
    public Integer visit(LetNode n) {
        varMap.put(n.getVname(), n.getExpr().accept(this));
        return 0;
    }
    
    @Override
    public Integer visit(VarRefNode n) {
        if (!varMap.containsKey(n.getVname())) {
            throw new RuntimeException("Undefined var: " + n.getVname());
        }
        return varMap.get(n.getVname());
    }

    @Override
    public Integer visit(FuncDefNode n) {
        System.out.println("Compiling " + n.getFname());
        return 0;
    }

    @Override
    public Integer visit(FuncCallNode n) {
        return null;
    }
}
