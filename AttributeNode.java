package sol;

import java.util.List;
import src.ITreeNode;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
public class AttributeNode implements ITreeNode {

    private String attribute;
    private String deflt;
    private List<ValueEdge> outgoingEdges;

    /**
     * Constructor for AttributeNode
     * @param attribute - name of attribute as string
     * @param deflt - name of default decision as string
     * @param outgoingEdges - list of children edges of node
     */
    public AttributeNode(String attribute, String deflt, List<ValueEdge> outgoingEdges) {
        this.attribute = attribute;
        this.deflt = deflt;
        this.outgoingEdges = outgoingEdges;
    }

    /**
     * Use recursion to traverse down tree until reaching a leaf where getDecision is called
     * @param forDatum the datum to look up a decision for
     * @return - string representing decision
     */
    @Override
    public String getDecision(Row forDatum) {
        for (int i = 0; i < this.outgoingEdges.size(); i++) {
            try { //if an error is thrown, this means that the current node is the target attribute,
                // meaning there won't be a value if we search for it.
                forDatum.getAttributeValue(this.attribute);
                //System.out.println("try call");
            } catch(RuntimeException exception) {
                //System.out.println("runtime");
                return this.deflt;
            }
            if (forDatum.getAttributeValue(this.attribute).equals(this.outgoingEdges.get(i).getValue())) {
                //System.out.println("if call");
                return this.outgoingEdges.get(i).getChild().getDecision(forDatum);
            }

        }
        return this.deflt;
    }
}