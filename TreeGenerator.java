package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {
    private ITreeNode root;

    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
        //The first attribute to split on; based off of trainingData's selectionType
        //System.out.println(trainingData.getSelectionType().toString());
        String attributeName = trainingData.getAttributeToSplitOn();

        //System.out.println("First Attribute: " + attributeName);

        //Creates first root node, then through creating the edges, recursively creates new edges and leaves until tree is built
        this.root = new AttributeNode(attributeName,
                trainingData.findDefault(trainingData.partition(attributeName), targetAttribute),
                this.createEdges(trainingData.findUniqueValues(attributeName), attributeName, targetAttribute, trainingData));

        //System.out.println(this.root);

    }

    /**
     * Creates list of value edges from an attribute
     * @param uniqueValues - list of strings representing distinct values
     * @param attributeToSplitOn - name of attribute we are partitioning on
     * @param targetAttribute - attribute with value edges we are targeting
     * @param data - dataset which contains rows
     * @return - list of value edges
     */
    public List<ValueEdge> createEdges(List<String> uniqueValues, String attributeToSplitOn, String targetAttribute, Dataset data) {
        List<ValueEdge> edges = new ArrayList<>();
        List<Dataset> subDatasets = data.partition(attributeToSplitOn);
        for(int i = 0; i < subDatasets.size(); i++) { //assumption is that findUniqueValues works and order is the same
            edges.add(new ValueEdge(uniqueValues.get(i), this.childDecider(targetAttribute, subDatasets.get(i))));
        }
        return edges;
    }

    /**
     * Creates next children nodes and edges after createEdges
     * @param targetAttribute - string of attribute name we are targeting
     * @param childDataset - dataset with rows of data of children
     * @return - ITreeNode representing successive children
     */
    public ITreeNode childDecider(String targetAttribute, Dataset childDataset) {
//        System.out.println();
//        System.out.println(childDataset.getAttributeList());
        if(childDataset.checkIfSameOutcome(targetAttribute)) {
            //If partition works correctly, all rows should have same value, so we take the first row's value for the targetAttribute
            //System.out.println("Leaf created");
            return new DecisionLeaf(childDataset.getDataObjects().get(0).getAttributeValue(targetAttribute));
        } else {
            String nextNodeAttribute = childDataset.getAttributeToSplitOn(); //always the first one...
            //System.out.println("childNode Attribute: " + nextNodeAttribute);
            //This means either
            /**
             *  a. childDataset's attribute list is incorrect !!!
             *
             *  b. getAttributeToSplitOn is broken.
             */
            return new AttributeNode(nextNodeAttribute,
                    childDataset.findDefault(childDataset.partition(nextNodeAttribute), targetAttribute),
                    this.createEdges(childDataset.findUniqueValues(nextNodeAttribute), nextNodeAttribute, targetAttribute, childDataset));
        }
    }

    @Override
    public String getDecision(Row datum) {
        return this.root.getDecision(datum);
    }

}