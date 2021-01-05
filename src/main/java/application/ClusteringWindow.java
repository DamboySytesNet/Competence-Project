package application;

import cluster.Cluster;
import cluster.clusterable.POIWrapper;
import model.POI;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ranking.POIDynamicRanking;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClusteringWindow {
    private List<POI> pois;
    private int clusters;
    private String category;

    private POIDynamicRanking dynamicRanking;

    public void printChartToFile(String[] args) {
        clusters = Integer.parseInt(args[0]);
        category = args[1];

        calculateChartAndPrint();
    }

    private void calculateChartAndPrint() {

        XYSeriesCollection dataset = new XYSeriesCollection();

        List<CentroidCluster<POIWrapper>> centroidClusters;
        Cluster<POI, POIWrapper> cluster;

        switch (category) {
            case "localization":
                cluster = new Cluster<>(clusters, 75);
                centroidClusters = cluster.KMean(pois, POIWrapper::new);
                break;
            case "frequent users":
                cluster = new Cluster<>(clusters, 75);
                for (POI poi : pois) {
                    dynamicRanking.getVisitorsCountRanking().putIfAbsent(poi, 0);
                }

                centroidClusters = cluster.KMean(pois, poi ->
                        new POIWrapper(poi, dynamicRanking.getVisitorsCountRanking().get(poi)));
                break;
            case "length of stay":
                cluster = new Cluster<>(clusters, 75);
                for (POI poi : pois) {
                    dynamicRanking.getTimeSpentRanking().putIfAbsent(poi, 0);
                }
                centroidClusters = cluster.KMean(pois, poi ->
                        new POIWrapper(poi, -1.0, dynamicRanking.getTimeSpentRanking().get(poi)));
                break;
            default:
                return;
        }

        assert centroidClusters != null;
        for (CentroidCluster<POIWrapper> wrappers : centroidClusters) {
            XYSeries newSeries = new XYSeries(wrappers.getCenter().toString());

            for (POIWrapper wrapper : wrappers.getPoints()) {
                newSeries.add(wrapper.getPoi().getGeolocalization().getLatitude(), wrapper.getPoi().getGeolocalization().getLongitude());
            }

            dataset.addSeries(newSeries);
        }

        JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                "Clustering points of interest by " + category, // Chart title
                "Latitude", // X-Axis Label
                "Longitude", // Y-Axis Label
                dataset // Dataset for the Chart
        );

        try {
            ChartUtils.saveChartAsPNG(new File(category + "ClusteringChart.jpeg" ), scatterPlot, 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transferPOIsSetRanking(List<POI> pois, POIDynamicRanking dynamicRanking) {
        this.pois = pois;
        this.dynamicRanking = dynamicRanking;
    }
}
