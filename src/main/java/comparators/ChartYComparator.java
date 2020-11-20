package comparators;

import java.util.Comparator;

import javafx.scene.chart.XYChart;

public class ChartYComparator implements Comparator<XYChart.Data<Double, Double>> {
    @Override
    public int compare(XYChart.Data<Double, Double> a, XYChart.Data<Double, Double> b) {
        double aValue = a.getYValue().doubleValue();
        double bValue = b.getYValue().doubleValue();
        if (aValue > bValue) return 1;
        else if (aValue < bValue) return -1;
        else return 0;
    }
}
