package pg.gda.edu.lsea.performance;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Utility class for generating performance graphs from test results
 */
public class GraphGenerator {

    /**
     * Generates a bar chart comparing average execution times of different operations
     *
     * @param results List of performance results
     * @param title Chart title
     * @param fileName File name for the saved chart
     * @throws IOException If file writing fails
     */
    public static void generateExecutionTimeBarChart(
            List<PerformanceTestUtil.PerformanceResult> results,
            String title,
            String fileName) throws IOException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add data points
        for (PerformanceTestUtil.PerformanceResult result : results) {
            dataset.addValue(
                    result.getAverageExecutionTime(),
                    "Execution Time (ms)",
                    result.getOperationName());
        }

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "Operation",
                "Average Execution Time (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize appearance
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);

        // Adjust bar renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189)); // Blue bars

        // Rotate labels if there are many operations
        CategoryAxis domainAxis = plot.getDomainAxis();
        if (results.size() > 5) {
            domainAxis.setCategoryLabelPositions(
                    org.jfree.chart.axis.CategoryLabelPositions.UP_45);
        }

        // Save to file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(fileName + "_" + timestamp + ".png");
        ChartUtils.saveChartAsPNG(file, chart, 800, 500);

        System.out.println("Bar chart saved to: " + file.getAbsolutePath());
    }

    /**
     * Generates a box plot showing distribution of execution times
     *
     * @param results List of performance results
     * @param title Chart title
     * @param fileName File name for the saved chart
     * @throws IOException If file writing fails
     */
    public static void generateExecutionTimeBoxPlot(
            List<PerformanceTestUtil.PerformanceResult> results,
            String title,
            String fileName) throws IOException {

        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        // Add data points
        for (PerformanceTestUtil.PerformanceResult result : results) {
            dataset.add(
                    result.getExecutionTimes(),
                    "Execution Time (ms)",
                    result.getOperationName()
            );
        }

        // Create chart
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                title,
                "Operation",
                "Execution Time (ms)",
                dataset,
                true
        );

        // Customize appearance
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);

        BoxAndWhiskerRenderer renderer = (BoxAndWhiskerRenderer) plot.getRenderer();
        renderer.setMeanVisible(true);
        renderer.setFillBox(true);
        renderer.setSeriesPaint(0, new Color(79, 129, 189)); // Blue boxes


        CategoryAxis domainAxis = plot.getDomainAxis();
        if (results.size() > 5) {
            domainAxis.setCategoryLabelPositions(
                    org.jfree.chart.axis.CategoryLabelPositions.UP_45);
        }

        // Save to file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(fileName + "_" + timestamp + ".png");
        ChartUtils.saveChartAsPNG(file, chart, 800, 500);

        System.out.println("Box plot saved to: " + file.getAbsolutePath());
    }

    /**
     * Generates a bar chart comparing memory usage of different operations
     *
     * @param results List of performance results
     * @param title Chart title
     * @param fileName File name for the saved chart
     * @throws IOException If file writing fails
     */
    public static void generateMemoryUsageBarChart(
            List<PerformanceTestUtil.PerformanceResult> results,
            String title,
            String fileName) throws IOException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add data points (convert to KB for readability)
        for (PerformanceTestUtil.PerformanceResult result : results) {
            dataset.addValue(
                    result.getAverageMemoryUsage() / 1024.0,
                    "Memory Usage (KB)",
                    result.getOperationName());
        }

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "Operation",
                "Average Memory Usage (KB)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize appearance
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.lightGray);

        // Adjust bar renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(192, 80, 77)); // Red bars

        // Rotate labels if there are many operations
        CategoryAxis domainAxis = plot.getDomainAxis();
        if (results.size() > 5) {
            domainAxis.setCategoryLabelPositions(
                    org.jfree.chart.axis.CategoryLabelPositions.UP_45);
        }

        // Save to file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(fileName + "_" + timestamp + ".png");
        ChartUtils.saveChartAsPNG(file, chart, 800, 500);

        System.out.println("Memory usage chart saved to: " + file.getAbsolutePath());
    }

}