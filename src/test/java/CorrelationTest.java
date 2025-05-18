import org.junit.Test;

import pg.gda.edu.lsea.analysis.Correlation;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CorrelationTest {

    private static final double DELTA = 0.0001;


    /**
     * Test for perfect positive correlation (coef =1.0)
     */
    @Test
    public void testCalculatePearson_PerfectPositiveCorrelation(){

        //Arrange
        List<Integer> xList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> yList = Arrays.asList(2, 4, 6, 8, 10);

        //Act
        double correlation = Correlation.calculatePearson(xList, yList);
        //Assert
        assertEquals(1.0, correlation, DELTA);
    }

    /**
     * Test for perfect negative correlation (coef =-1.0)
     */
    @Test
    public void testCalculatePearson_PerfectNegativeCorrelation(){
        //Arrange
        List<Integer> xList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> yList = Arrays.asList(10, 8, 6, 4, 2);

        //Act
        double correlation = Correlation.calculatePearson(xList, yList);

        //Assert
        assertEquals(-1.0, correlation, DELTA);


    }


    /**
     * Test for no correlation (random values)
     */
    @Test
    public void testCalculatePearson_NoCorrelation(){
        //Arrange
        List<Integer> xList = Arrays.asList(2, 1, 17, 2, 13);
        List<Integer> yList = Arrays.asList(5, 8, 10, 12, 3);

        //Act
        double correlation = Correlation.calculatePearson(xList, yList);

        //Assert
        assertEquals(0.0, correlation, 0.3);
    }

    /**
     * Test for correlation with one list as const. values
     */
    @Test
    public void testCalculatePearson_ConstantValues(){
        //Arrange
        List<Integer> xList = Arrays.asList(5, 5, 5, 5, 5);
        List<Integer> yList = Arrays.asList(10, 20, 30, 40, 50);

        //Act
        double correlation = Correlation.calculatePearson(xList, yList);

        //Assert
        assertEquals(0.0, correlation, DELTA);
    }

    /**
     * Test for both lists as const. values
     */
    @Test
    public void testCalculatePearson_BothConstantValues(){
        //Arrange
        List<Integer> xList = Arrays.asList(5, 5, 5, 5, 5);
        List<Integer> yList = Arrays.asList(10, 10, 10, 10, 10);


        //Act
        double correlation = Correlation.calculatePearson(xList, yList);
        //Assert
        assertEquals(0.0, correlation, DELTA);
    }


    /**
     * Test for simulated real-world example statistics data
     */
    @Test
    public void testCalculatePearson_SportsStatisticsData(){
        //Arrange
        List<Integer> goalsScored = Arrays.asList(45,32,56,27,38,42,51,29,33,47);
        List<Integer> wins = Arrays.asList(18,12,22,9,14,16,20,11,13,19);

        //Act
        double correlation = Correlation.calculatePearson(goalsScored, wins);
        //Assert
        assertTrue("Goals scored should be positively correlated with wins", correlation > 0.8 );
    }


    /**
     * Test with empty lists
     */
    @Test
    public void testCalculatePearson_EmptyLists(){
        //Arrange
        List<Integer> xList = Arrays.asList();
        List<Integer> yList = Arrays.asList();


        try{
            //Act
            double correlation = Correlation.calculatePearson(xList, yList);

            //Assert
            //Check if it returns a valid number
            assertFalse(Double.isNaN(correlation));
        } catch (IllegalArgumentException e){
            //Assert
            //Or throws an exception
            assertTrue(true);
        }
    }

    /**
     * Test with null pointers to test exception handling
     */
    @Test(expected = NullPointerException.class)
    public void testCalculatePearson_NullList(){
        //Arrange, Act and Assert
        Correlation.calculatePearson(null, null);
    }

}
