package sol;

import org.junit.Assert;
import org.junit.Test;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

/**
 * A class to test basic decision tree functionality on a basic training dataset
 */
public class BasicDatasetTest {
    String trainingPath = "data/fruits-and-vegetables.csv";
    //fruits-and-vegetables.csv //data1.csv  //mushrooms/testing.csv
    String targetAttribute = "foodType";
//    String trainingPath = "data/basicdata.csv";
//    String targetAttribute = "outcome";
    TreeGenerator testGenerator;

    /**
     * Constructs the decision tree for testing based on the input file and the target attribute.
     */
    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        // builds a TreeGenerator object and generates a tree for "foodType"
        this.testGenerator = new TreeGenerator();
        //System.out.println(training.partition(attributeList.get(0)).toString());
        this.testGenerator.generateTree(training, this.targetAttribute);
        System.out.println();
        System.out.println("Tree Built!");
        System.out.println("____________________________");
    }

    /**
     * Tests the expected classification of the "tangerine" row is a fruit
     */
    @Test
    public void testFruitsAndVegetablesClassification() {
        // makes a new (partial) Row representing the tangerine from the example
        Row tangerine = new Row("test row (tangerine)");
        tangerine.setAttributeValue("color", "orange");
        tangerine.setAttributeValue("highProtein", "false");
        tangerine.setAttributeValue("calories", "high");

        Row banana = new Row("test row (banana)");
        banana.setAttributeValue("color", "yellow");
        banana.setAttributeValue("highProtein", "true");
        banana.setAttributeValue("calories", "high");
        //banana.setAttributeValue("foodType", "fruit");
        Assert.assertEquals("fruit", this.testGenerator.getDecision(banana));
        //Assert.assertEquals("fruit", this.testGenerator.getDecision(tangerine));

    }


    /**
     * Tests the basic 1 node and 2 leaves case
     */
    @Test
    public void testBasic() {
        Row one = new Row("test row (one)");
        one.setAttributeValue("order", "gzero");
        one.setAttributeValue("outcome", "positive");

        Assert.assertEquals("positive", this.testGenerator.getDecision(one));
    }
}