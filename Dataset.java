package sol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset {

    private List<String> attributeList;
    private List<Row> dataObjects;
    private AttributeSelection selectionType;

    /**
     * Constructor for a Dataset object
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param selectionType - an enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection selectionType) {
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
        this.selectionType = selectionType;
    }

    /**
     * Determines attribute to split on in tree
     * @return - string of attribute name
     */
    public String getAttributeToSplitOn() {
        switch (this.selectionType) {
            case ASCENDING_ALPHABETICAL -> {
                System.out.println(this.attributeList.stream().sorted().toList());
                return this.attributeList.stream().sorted().toList().get(0);
            }
            case DESCENDING_ALPHABETICAL -> {
                System.out.println(this.attributeList.stream().sorted().toList());
                return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
            }
            case RANDOM -> {
                Collections.shuffle(this.attributeList);
                return this.attributeList.get(0);
            }
        }
        throw new RuntimeException("Non-Exhaustive Switch Case");
    }

    /**
     * Partitions dataset based on the ValueEdge it is travelling down
     * @param attributeToSplitOn - String of attribute name that is being partitioned
     * @return - list of datasets/rows filtered to each of the values for the attribute
     */
    public List<Dataset> partition(String attributeToSplitOn) {
        List<String> uniqueResponses = this.findUniqueValues(attributeToSplitOn);

        List<Dataset> subDatasets = new ArrayList<>(uniqueResponses.size());

        List<String> unusedAttributes = new ArrayList<>();
        for(String attribute : this.attributeList) {
            if(!attribute.equals(attributeToSplitOn)) {
                unusedAttributes.add(attribute);
            }
        }

        for(int i = 0; i < uniqueResponses.size(); i++) { //loop through subDatasets
            //Initialize the slot with a new Dataset
            subDatasets.add(new Dataset(unusedAttributes, new ArrayList<>(), this.selectionType));
            for(int j = 0; j < this.dataObjects.size(); j++) { //loop through dataObjects and add rows to the proper Dataset
                if(this.dataObjects.get(j).getAttributeValue(attributeToSplitOn).equals(uniqueResponses.get(i))) {
                    subDatasets.get(i).dataObjects.add(this.dataObjects.get(j));
                }
            }
        }
        return subDatasets;
    }

    /**
     * Determines default value
     * @param subDatasets - list of datasets produced by partition method
     * @param attributeName - string of name of attribute
     * @return - string of default value
     */
    public String findDefault(List<Dataset> subDatasets, String attributeName) {
        String modeValue = subDatasets.get(0).dataObjects.get(0).getAttributeValue(attributeName);
        int mode = 0;
        for(int i = 0; i < subDatasets.size(); i++) {
            if(subDatasets.get(i).size() > mode) {
                modeValue = subDatasets.get(i).dataObjects.get(0).getAttributeValue(attributeName);
                mode = subDatasets.get(i).size();
            }
        }
        return modeValue;
    }


    /**
     * Determines unique values for one attribute
     * @param attributeName - string representing AttributeNode name
     * @return - list of unique value edge names for one attribute
     */
    public List<String> findUniqueValues(String attributeName) {
        ArrayList<String> possibleResponses = new ArrayList<>();
        possibleResponses.add(this.dataObjects.get(0).getAttributeValue(attributeName));
        for(int i = 1; i < this.dataObjects.size(); i++) {
            if(!possibleResponses.contains(this.dataObjects.get(i).getAttributeValue(attributeName))) {
                possibleResponses.add(this.dataObjects.get(i).getAttributeValue(attributeName));
            }
        }
        //System.out.println(possibleResponses.toString());
        return possibleResponses;
    }

    /**
     * Determines if value of attribute is same
     * @param attributeName - string representing AttributeNode name
     * @return - boolean indicating if value of attribute is same
     */
    public boolean checkIfSameOutcome(String attributeName) {
        String stdAttributeValue = this.dataObjects.get(0).getAttributeValue(attributeName);
        for(int i = 1; i < this.dataObjects.size(); i++) {
            if(!this.dataObjects.get(i).getAttributeValue(attributeName).equals(stdAttributeValue)) {
                return false; //If any row is not equal to the attribute value of the first, then the list is not all same
            }
        }
        return true;
    }



    @Override
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    @Override
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    @Override
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }

    // Determined by number of rows in Dataset
    @Override
    public int size() {
        return this.dataObjects.size();
    }
}