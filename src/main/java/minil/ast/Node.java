package minil.ast;

import minil.NodeVisitor;

public abstract class Node {
    
    public abstract <T> T accept(NodeVisitor<T> v);
    
}
