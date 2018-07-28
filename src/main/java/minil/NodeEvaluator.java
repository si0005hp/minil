package minil;

import static minil.MinilParser.ADD;
import static minil.MinilParser.DIV;
import static minil.MinilParser.MUL;
import static minil.MinilParser.SUB;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import minil.NodeEvaluator.StmtEvaResult;
import minil.ast.BinOpNode;
import minil.ast.FuncCallNode;
import minil.ast.FuncDefNode;
import minil.ast.IfNode;
import minil.ast.IntNode;
import minil.ast.LetNode;
import minil.ast.Node;
import minil.ast.PrintNode;
import minil.ast.ProgramNode;
import minil.ast.ReturnNode;
import minil.ast.StmtNode;
import minil.ast.VarRefNode;

public class NodeEvaluator implements NodeVisitor<Integer, StmtEvaResult> {

    private final Map<String, Integer> gVarMap = new HashMap<>();
    private final LinkedList<Map<String, Integer>> lVarMapStack = new LinkedList<>();
    private final Map<String, FuncDefNode> funcDefMap = new HashMap<>();
    
    @Data
    @AllArgsConstructor(staticName = "of")
    @RequiredArgsConstructor(staticName = "of")
    static class StmtEvaResult {
        private final Class<?> stmtType;
        private Object value;
    }
    
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
        throw new RuntimeException("Illegal binop type: " + n.getOpType());
    }

    @Override
    public StmtEvaResult visit(PrintNode n) {
        int val = n.getExpr().accept(this);
        System.out.println(val);
        return StmtEvaResult.of(PrintNode.class);
    }

    @Override
    public StmtEvaResult visit(ProgramNode n) {
        for (Node t : n.getTopLevels()) {
            if (isIllegalTopLevel(t)) {
                throw new RuntimeException("Illegal topLevel node: " + t);
            }
            t.accept(this);
        }
        return StmtEvaResult.of(ProgramNode.class);
    }
    
    private boolean isIllegalTopLevel(Node n) {
        if (n instanceof ReturnNode) {
            return true;
        }
        return false;
    }

    @Override
    public StmtEvaResult visit(LetNode n) {
        if (lVarMapStack.isEmpty()) {
            gVarMap.put(n.getVname(), n.getExpr().accept(this));
        } else {
            lVarMapStack.getLast().put(n.getVname(), n.getExpr().accept(this));
        }
        return StmtEvaResult.of(LetNode.class);
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
    public StmtEvaResult visit(FuncDefNode n) {
        funcDefMap.put(n.getFname(), n);
        return StmtEvaResult.of(FuncDefNode.class);
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
            StmtEvaResult res = s.accept(this);
            if (res.getStmtType() == ReturnNode.class) {
                lVarMapStack.removeLast();
                return (int)res.getValue();
            }
        }
        
        lVarMapStack.removeLast();
        return 0;
    }

    @Override
    public StmtEvaResult visit(ReturnNode n) {
        int retVal = n.getExpr().accept(this);
        return StmtEvaResult.of(ReturnNode.class, retVal);
    }

    @Override
    public StmtEvaResult visit(IfNode n) {
        int cond = n.getCondExpr().accept(this);
        List<StmtNode> body = cond != 0 ? n.getThenBody() : n.getElseBody(); 
        for (StmtNode s : body) {
            StmtEvaResult res = s.accept(this);
            if (res.getStmtType() == ReturnNode.class) {
                return res;
            }
        }
        return StmtEvaResult.of(IfNode.class);
    }
}
