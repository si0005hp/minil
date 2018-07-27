package minil;

import static minil.MinilParser.ADD;
import static minil.MinilParser.DIV;
import static minil.MinilParser.MUL;
import static minil.MinilParser.SUB;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
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

    private final Map<String, Integer> gVarMap = new HashMap<>();
    private final LinkedList<Map<String, Integer>> lVarMapStack = new LinkedList<>();
    private final Map<String, FuncDefNode> funcDefMap = new HashMap<>();
    
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
        if (lVarMapStack.isEmpty()) {
            gVarMap.put(n.getVname(), n.getExpr().accept(this));
        } else {
            lVarMapStack.getLast().put(n.getVname(), n.getExpr().accept(this));
        }
        return 0;
    }
    
    @Override
    public Integer visit(VarRefNode n) {
        ListIterator<Map<String, Integer>> lit = lVarMapStack.listIterator(lVarMapStack.size());
        while (lit.hasPrevious()) {
            Map<String, Integer> lVarMap = lit.previous();
            if (lVarMap.containsKey(n.getVname())) {
                return lVarMap.get(n.getVname());
            }
        }
        if (!gVarMap.containsKey(n.getVname())) {
            throw new RuntimeException("Undefined var: " + n.getVname());
        }
        return gVarMap.get(n.getVname());
    }

    @Override
    public Integer visit(FuncDefNode n) {
        funcDefMap.put(n.getFname(), n);
        return 0;
    }

    @Override
    public Integer visit(FuncCallNode n) {
        if (!funcDefMap.containsKey(n.getFname())) {
            throw new RuntimeException("Undefined function: " + n.getFname());
        }
        
        FuncDefNode f = funcDefMap.get(n.getFname());
        
        if (n.getExprs().size() != f.getParams().size()) {
            throw new RuntimeException(String.format("Inconsistent number of arguments -> Expected: %s Actual: %s", 
                    f.getParams().size(), n.getExprs().size()));
        }
        
        Map<String, Integer> lVarMap = new HashMap<>();
        for (int i = 0; i < f.getParams().size(); i++) {
            String pName = f.getParams().get(i);
            int arg = n.getExprs().get(i).accept(this);
            lVarMap.put(pName, arg);
        }
        lVarMapStack.add(lVarMap);
        
        for (StmtNode s : f.getStmts()) {
            s.accept(this);
        }
        
        lVarMapStack.removeLast();
        return 0;
    }
}
