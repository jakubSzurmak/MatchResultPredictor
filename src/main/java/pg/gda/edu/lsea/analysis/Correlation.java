package pg.gda.edu.lsea.analysis;
import java.util.List;

/**
 * Utility class for calculating statistical correlation measures
 *
 * Class provides methods to calculate different types of correlation
 * coefficients between data sets
 */
public class Correlation{
    /**
     * Calculates the Pearson correlation coefficient between two data sets
     *
     * @param xList is the first data set as a list of Integer values
     * @param yList is the second data set as a list of Integer values
     * @return the Pearson correlation coefficient between the two data sets
     */
    public static double calculatePearson(List<Integer> xList, List<Integer> yList){
        int n = xList.size();
        double sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0, sumXY = 0;

        // Calculate the sums needed for the Pearson correlation formula
        for(int i =0; i< n; i++){
            int x = xList.get(i);
            int y = yList.get(i);
            sumX += x;
            sumY += y;
            sumX2 += x*x;
            sumY2 += y*y;
            sumXY += x*y;
        }

        // Calculate the correlation coefficient
        double numerator = n * sumXY - sumX* sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        // Avoid division by zero
        return (denominator == 0) ? 0 : numerator / denominator;
    }
}

