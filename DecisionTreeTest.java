package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.Row;
import src.AttributeSelection;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {
    String trainingPath = "data/fruits-and-vegetables.csv";
    TreeGenerator testGenerator;


    /**
     * Testing getAttributeList
     */
    @Test
    public void testGetAttributeList() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();

        List<String> l1 = new ArrayList<>();
        l1.add("highProtein");
        l1.add("calories");
        l1.add("color");
        l1.add("foodType");
        assertEquals(l1, training.getAttributeList());
    }

    /**
     * Testing getDataObjects
     */
    @Test
    public void testGetDataObjects() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();

        assertEquals(7, training.getDataObjects().size());
        System.out.println(training.getDataObjects());
    }

    /**
     * Testing getSelectionType
     */
    @Test
    public void testGetSelectionType() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.RANDOM);
        this.testGenerator = new TreeGenerator();

        assertEquals(AttributeSelection.RANDOM, training.getSelectionType());
    }

    /**
     * Testing size
     */
    @Test
    public void testSize() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.RANDOM);
        this.testGenerator = new TreeGenerator();

        assertEquals(7, training.size());
    }

    /**
     * This test checks that node, value, leaf, and row are working.
     */
    @Test
    public void testDesign() {
        List<String> ls1 = new ArrayList<>();
        ls1.add("Color");
        ls1.add("Calories");
        ls1.add("Protein");
        List<Row> lr1 = new ArrayList<>();
        lr1.add(new Row("apple"));
        lr1.add(new Row("banana"));
        Dataset df1 = new Dataset(ls1, lr1, AttributeSelection.RANDOM);
        assertEquals(2, df1.size());
    }

    /**
     * Tests Random selectionType of getAttributeToSplitOn by printing random attribute types.
     */
    @Test
    public void testRandom() {
        List<String> ls1 = new ArrayList<>();
        ls1.add("Color");
        ls1.add("Calories");
        ls1.add("Protein");
        List<Row> lr1 = new ArrayList<>();
        lr1.add(new Row("apple"));
        lr1.add(new Row("banana"));
        Dataset df1 = new Dataset(ls1, lr1, AttributeSelection.RANDOM);
        System.out.println(df1.getAttributeToSplitOn());
        System.out.println(df1.getAttributeToSplitOn());
        System.out.println(df1.getAttributeToSplitOn());
        System.out.println(df1.getAttributeToSplitOn());
        System.out.println(df1.getAttributeToSplitOn());
    }

    /**
     * Tests partition method to ensure dataset is being filtered correctly
     */
    @Test
    public void testPartition() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();
        assertEquals(3, training.partition("color").size());
        assertEquals(3, training.partition("color").get(0).size());
        assertEquals(1, training.partition("color").get(2).size());
    }

    /**
     * Tests default method to ensure default value is correct
     */
    @Test
    public void testDefault() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();
        assertEquals("green", training.findDefault(training.partition("color"), "color"));
        assertEquals("true", training.findDefault(training.partition("highProtein"), "highProtein"));
        assertEquals("high", training.findDefault(training.partition("calories"), "calories"));
    }

    /**
     * Tests getDecision method
     */
    @Test
    public void testGetDecision() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();

        DecisionLeaf dl1 = new DecisionLeaf("fruit");
        DecisionLeaf dl2 = new DecisionLeaf("vegetable");
        ValueEdge v1 = new ValueEdge("yellow", dl1);
        List<ValueEdge> l1 = new ArrayList<>();
        l1.add(v1);

        List<ValueEdge> l2 = new ArrayList<>();
        ValueEdge v2 = new ValueEdge("low", dl2);
        ValueEdge v3 = new ValueEdge("high", dl1);
        l2.add(v2);
        l2.add(v3);
        AttributeNode a2 = new AttributeNode("calories", "vegetable", l2);
        ValueEdge v4 = new ValueEdge("green", a2);
        l1.add(v4);
        AttributeNode a1 = new AttributeNode("color", "vegetable", l1);

        assertEquals("fruit", a2.getDecision(training.getDataObjects().get(1)));
        assertEquals("vegetable", a1.getDecision(training.getDataObjects().get(0)));
    }

    /**
     * Tests generateTree method by testing the helper methods it uses (findUniqueValues, createEdges)
     */
    @Test
    public void testGenerateTree() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator = new TreeGenerator();

        List<String> l1 = new ArrayList<>();
        l1.add("green");
        l1.add("orange");
        l1.add("yellow");
        assertEquals(l1, training.findUniqueValues("color"));

        List<String> l2 = new ArrayList<>();
        l2.add("low");
        l2.add("high");
        l2.add("medium");
        assertEquals(l2, training.findUniqueValues("calories"));
    }
}