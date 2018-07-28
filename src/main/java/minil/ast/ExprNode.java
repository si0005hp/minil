package minil.ast;

import minil.NodeVisitor;

public abstract class ExprNode extends Node {
    public abstract <E, S> E accept(NodeVisitor<E, S> v);
}
