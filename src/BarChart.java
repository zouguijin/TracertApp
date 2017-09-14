import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

/**
 * Created by starSea_AB on 2017/9/10.
 */
public class BarChart {
    ChartPanel frame;
    static int[] ylabel;
    static String[] xlabel;
    public BarChart(String[] websites, int[] compareResults) {
        ylabel = compareResults;
        xlabel = websites;
        CategoryDataset dataset = getDataSet();
        JFreeChart chart = ChartFactory.createBarChart3D(
                "Tracert Command Results(Test " + compareResults.length + " Times)", // title
                "Websites", // horizontal axis
                "Routing Different Times", // vertical axis
                dataset,
                PlotOrientation.VERTICAL, // direction of the chart
                true, // show the chart example or not
                false, // show the tools or not
                false // show the url or not
        );
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(new Font("黑体", Font.BOLD, 14));
        domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12));
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
        chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));

        //frame = new ChartPanel(chart,true);
        ChartFrame frame = new ChartFrame("Tracert Test", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private static CategoryDataset getDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 0; i < xlabel.length; i++) {
            dataset.addValue(ylabel[i],xlabel[i],xlabel[i]);
        }
        return dataset;
    }

    public ChartPanel getChartPanel() {
        return frame;
    }
}

