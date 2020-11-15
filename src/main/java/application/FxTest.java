package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import model.Geolocalization;
import model.POI;

import generation.POIFactory;

import comparators.ChartXComparator;
import comparators.ChartYComparator;

public class FxTest extends Application {
    final int windowWidth = 530;
    final int windowHeight = 450;

    @Override
    public void start(Stage stage) {
        XYChart.Series series = generateSeries();
        ScatterChart<String, Number> chart = createChart(series);

        Group root = new Group(chart);
        Scene scene = new Scene(root, windowWidth, windowHeight);
        stage.setScene(scene);
        stage.show();
    }

    private XYChart.Series generateSeries() {
        // Generation limit
        final int howMany = 100;

        // Generate POIs
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < howMany; i++) {
            POI poi = POIFactory.getInstance().generate();
            Geolocalization geolocalization = poi.getGeolocalization();

            series.getData().add(new XYChart.Data(geolocalization.getLatitude(), geolocalization.getLongitude()));
        }

        return series;
    }

    private ScatterChart<String, Number> createChart(XYChart.Series<Double, Double> series) {
        double maxX = series.getData().stream().max(new ChartXComparator()).get().getXValue();
        double minX = series.getData().stream().min(new ChartXComparator()).get().getXValue();
        double maxY = series.getData().stream().max(new ChartYComparator()).get().getYValue();
        double minY = series.getData().stream().min(new ChartYComparator()).get().getYValue();

        double margin = 0.3;
        double tick = 1;

        // X axis
        NumberAxis xAxis = new NumberAxis(minX - margin, maxX + margin, tick);
        xAxis.setLabel("X");

        // Y axis
        NumberAxis yAxis = new NumberAxis(minY - margin, maxY + margin, tick);
        yAxis.setLabel("Y");

        // Create chart
        ScatterChart<String, Number> scatterChart = new ScatterChart(xAxis, yAxis);

        // Setting the data to scatter chart
        scatterChart.getData().addAll((XYChart.Series)series);
        series.setName("Point of interest");

        return scatterChart;
    }

    public static void main(String[] args) {
        launch();
    }

}