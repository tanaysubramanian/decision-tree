package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode {

    private String decision;

    /**
     * Constructor for DecisionLeaf
     * @param decision - name of decision as string
     */
    public DecisionLeaf(String decision) {
        this.decision = decision;
        //System.out.println("Leaf Decision: " + this.decision);
    }

    @Override
    public String getDecision(Row forDatum) {
        return this.decision;
    }
}