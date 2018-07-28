package minil.ast;

import minil.NodeVisitor;

public abstract class Node {
    public abstract <E, S> Object accept(NodeVisitor<E, S> v);
}
