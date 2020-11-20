package comparators;

import java.util.Comparator;

import javafx.scene.chart.XYChart;

public class ChartXComparator implements Comparator<XYChart.Data<Double, Double>> {
    @Override
    public int compare(XYChart.Data<Double, Double> a, XYChart.Data<Double, Double> b) {
        double aValue = a.getXValue().doubleValue();
        double bValue = b.getXValue().doubleValue();
        if (aValue > bValue) return 1;
        else if (aValue < bValue) return -1;
        else return 0;
    }
}
