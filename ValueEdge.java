package sol;

import src.ITreeNode;

/**
 * A class that represents the edge of an attribute node in the decision tree
 */
public class ValueEdge {

    private String value;
    private ITreeNode child;

    /**
     * Constructor for ValueEdge
     * @param value - name of value as string
     * @param child - children of value edge as ITreeNode
     */
    public ValueEdge(String value, ITreeNode child) {
        this.value = value;
        this.child = child;
    }

    /**
     * Gets value from value edge
     * @return - name of value as string
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Gets child of value edge
     * @return - ITreeNode of child
     */
    public ITreeNode getChild() {
        return this.child;
    }
}