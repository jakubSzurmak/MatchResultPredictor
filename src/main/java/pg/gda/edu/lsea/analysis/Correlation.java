package pg.gda.edu.lsea.analysis;
import java.util.List;

public class Correlation{
    public static double calculatePearson(List<Integer> xList, List<Integer> yList){
        int n = xList.size();
        double sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0, sumXY = 0;

        for(int i =0; i< n; i++){
            int x = xList.get(i);
            int y = yList.get(i);
            sumX += x;
            sumY += y;
            sumX2 += x*x;
            sumY2 += y*y;
            sumXY += x*y;
        }

        double numerator = n * sumXY - sumX* sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        return (denominator == 0) ? 0 : numerator / denominator;
    }
}

