package minil;

import static minil.MinilParser.ADD;
import static minil.MinilParser.DIV;
import static minil.MinilParser.EQEQ;
import static minil.MinilParser.GT;
import static minil.MinilParser.GTE;
import static minil.MinilParser.LT;
import static minil.MinilParser.LTE;
import static minil.MinilParser.MUL;
import static minil.MinilParser.NOTEQ;
import static minil.MinilParser.SUB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.google.common.base.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import minil.MinilValue.ValueType;
import minil.NodeEvaluator.StmtEvaResult;
import minil.ast.ArrayElemLetNode;
import minil.ast.ArrayElemRefNode;
import minil.ast.ArrayNode;
import minil.ast.BinOpNode;
import minil.ast.BreakNode;
import minil.ast.ExprNode;
import minil.ast.ExprStmtNode;
import minil.ast.FuncCallNode;
import minil.ast.FuncDefNode;
import minil.ast.IfNode;
import minil.ast.IntNode;
import minil.ast.VarLetNode;
import minil.ast.Node;
import minil.ast.PrintNode;
import minil.ast.ProgramNode;
import minil.ast.ReturnNode;
import minil.ast.StmtNode;
import minil.ast.StrNode;
import minil.ast.VarRefNode;
import minil.ast.WhileNode;

public class NodeEvaluator implements NodeVisitor<MinilValue, StmtEvaResult> {

    private final Map<String, MinilValue> gVarMap = new HashMap<>();
    private final LinkedList<Map<String, MinilValue>> lVarMapStack = new LinkedList<>();
    private final Map<String, FuncDefNode> funcDefMap = new HashMap<>();
    
    @Data
    @AllArgsConstructor(staticName = "of")
    @RequiredArgsConstructor(staticName = "of")
    static class StmtEvaResult {
        private final Class<?> stmtType;
        private MinilValue value;
    }
    
    @Override
    public MinilValue visit(IntNode n) {
        return new MinilValue(ValueType.INT, n.getValue());
    }
    
    @Override
    public MinilValue visit(StrNode n) {
        return new MinilValue(ValueType.STRING, n.getValue());
    }

    @Override
    public MinilValue visit(BinOpNode n) {
        MinilValue l = n.getLeft().accept(this);
        MinilValue r = n.getRight().accept(this);
        validBinOprandsType(l, r);
        
        switch (n.getOpType()) {
        case ADD: 
            if (l.getType() == ValueType.INT) {
                return new MinilValue(ValueType.INT, l.asInt() + r.asInt());
            }
            return new MinilValue(ValueType.STRING, l.asStr() + r.asStr());
        case SUB: 
            return new MinilValue(ValueType.INT, l.asInt() - r.asInt());
        case MUL: 
            return new MinilValue(ValueType.INT, l.asInt() * r.asInt());
        case DIV: 
            return new MinilValue(ValueType.INT, l.asInt() / r.asInt());
        case EQEQ: 
            return new MinilValue(ValueType.INT, 
                    Objects.equal(l.getValue(), r.getValue()) ? 1 : 0);
        case NOTEQ: 
            return new MinilValue(ValueType.INT, 
                    Objects.equal(l.getValue(), r.getValue()) ? 0 : 1);
        case GT: 
            return new MinilValue(ValueType.INT, 
                    l.asInt() > r.asInt() ? 1 : 0);
        case LT: 
            return new MinilValue(ValueType.INT, 
                    l.asInt() < r.asInt() ? 1 : 0);
        case GTE: 
            return new MinilValue(ValueType.INT, 
                    l.asInt() >= r.asInt() ? 1 : 0);
        case LTE: 
            return new MinilValue(ValueType.INT, 
                    l.asInt() <= r.asInt() ? 1 : 0);
        }
        throw new RuntimeException("Illegal binop type: " + n.getOpType());
    }
    
    private void validBinOprandsType(MinilValue l, MinilValue r) {
        if (l.getType() != r.getType()) {
            throw new RuntimeException(String.format("Invalid BinOprands' type. Left: %s, Right: %s", 
                    l.getType(), r.getType()));
        }
    }
    
    @Override
    public StmtEvaResult visit(PrintNode n) {
        MinilValue val = n.getExpr().accept(this);
        System.out.println(val.getValue());
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
    public StmtEvaResult visit(VarLetNode n) {
        if (lVarMapStack.isEmpty()) {
            gVarMap.put(n.getVar().getVname(), n.getExpr().accept(this));
        } else {
            lVarMapStack.getLast().put(n.getVar().getVname(), n.getExpr().accept(this));
        }
        return StmtEvaResult.of(VarLetNode.class);
    }
    
    @Override
    public MinilValue visit(VarRefNode n) {
        ListIterator<Map<String, MinilValue>> lit = lVarMapStack.listIterator(lVarMapStack.size());
        while (lit.hasPrevious()) {
            Map<String, MinilValue> lVarMap = lit.previous();
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
    public MinilValue visit(FuncCallNode n) {
        if (!funcDefMap.containsKey(n.getFname())) {
            throw new RuntimeException("Undefined function: " + n.getFname());
        }
        
        FuncDefNode f = funcDefMap.get(n.getFname());
        
        if (n.getExprs().size() != f.getParams().size()) {
            throw new RuntimeException(String.format("Inconsistent number of arguments -> Expected: %s Actual: %s", 
                    f.getParams().size(), n.getExprs().size()));
        }
        
        Map<String, MinilValue> lVarMap = new HashMap<>();
        for (int i = 0; i < f.getParams().size(); i++) {
            String pName = f.getParams().get(i);
            MinilValue arg = n.getExprs().get(i).accept(this);
            lVarMap.put(pName, arg);
        }
        lVarMapStack.add(lVarMap);
        
        for (StmtNode s : f.getStmts()) {
            StmtEvaResult res = s.accept(this);
            if (res.getStmtType() == ReturnNode.class) {
                lVarMapStack.removeLast();
                return res.getValue();
            }
        }
        
        lVarMapStack.removeLast();
        return null;
    }

    @Override
    public StmtEvaResult visit(ReturnNode n) {
        MinilValue retVal = n.getExpr().accept(this);
        return StmtEvaResult.of(ReturnNode.class, retVal);
    }

    @Override
    public StmtEvaResult visit(IfNode n) {
        List<StmtNode> body = isTrueCond(n.getCondExpr()) ? n.getThenBody() : n.getElseBody(); 
        for (StmtNode s : body) {
            StmtEvaResult res = s.accept(this);
            if (res.getStmtType() == ReturnNode.class
                    || res.getStmtType() == BreakNode.class) {
                return res;
            }
        }
        return StmtEvaResult.of(IfNode.class);
    }
    
    private boolean isTrueCond(ExprNode condExpr) {
        MinilValue iCond = condExpr.accept(this);
        return iCond.asInt() == 0 ? false : true; // 0 is false, otherwise true
    }

    @Override
    public StmtEvaResult visit(WhileNode n) {
        while (isTrueCond(n.getCondExpr())) {
            for (StmtNode s : n.getBody()) {
                StmtEvaResult res = s.accept(this);
                if (res.getStmtType() == ReturnNode.class
                        || res.getStmtType() == BreakNode.class) {
                    return res;
                }
            }
        }
        return StmtEvaResult.of(WhileNode.class);
    }

    @Override
    public StmtEvaResult visit(BreakNode n) {
        return StmtEvaResult.of(BreakNode.class);
    }

    @Override
    public MinilValue visit(ArrayNode n) {
        ArrayList<MinilValue> arr = new ArrayList<>();
        for (ExprNode e : n.getExprs()) {
            arr.add(e.accept(this));
        }
        return new MinilValue(ValueType.ARRAY, arr);
    }

    @Override
    public MinilValue visit(ArrayElemRefNode n) {
        ArrayList<MinilValue> arr = n.getArrname().accept(this).asArray();
        int idx = n.getIdx().accept(this).asInt();
        return arr.get(idx);
    }

    @Override
    public StmtEvaResult visit(ArrayElemLetNode n) {
        ArrayList<MinilValue> arr = n.getElem().getArrname().accept(this).asArray();
        int idx = n.getElem().getIdx().accept(this).asInt();
        arr.set(idx, n.getExpr().accept(this));
        return new StmtEvaResult(ArrayElemLetNode.class);
    }

    @Override
    public StmtEvaResult visit(ExprStmtNode n) {
        n.getExpr().accept(this); // Just evaluate the expression
        return new StmtEvaResult(ExprStmtNode.class);
    }

}
