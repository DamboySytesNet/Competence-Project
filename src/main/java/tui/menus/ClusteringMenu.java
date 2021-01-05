package tui.menus;

import application.ClusteringWindow;
import model.POI;
import model.Trace;
import model.TraceData;
import ranking.POIDynamicRanking;
import repository.POIRepository;
import repository.TraceRepository;
import tui.Menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClusteringMenu implements Menu {
    private final Menu parent;
    private final Scanner scanner;
    private final TraceRepository traceRepository;
    private final POIRepository poiRepository;
    private final POIDynamicRanking dynamicRanking;

    private final ClusteringWindow clusteringWindow;

    public ClusteringMenu(Menu parent, Scanner scanner, TraceRepository traceRepository) {
        this.dynamicRanking = new POIDynamicRanking();
        this.poiRepository = new POIRepository();
        this.traceRepository = traceRepository;
        this.scanner = scanner;
        this.parent = parent;

        clusteringWindow = new ClusteringWindow();
    }

    @Override
    public void execute() {
        System.out.println("[1] - Cluster by localization");
        System.out.println("[2] - Cluster by frequent users");
        System.out.println("[3] - Cluster by length of stay");
        System.out.println("[4] - Back");

        int clusters = 0;

        String input = scanner.nextLine();

        if (input.equals("1") || input.equals("2") || input.equals("3")) {
            System.out.println("How many clusters:");

            try {
                clusters = Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Not an integer");
                execute();
            }
        }

        String[] args = new String[2];
        args[0] = Integer.toString(clusters);

        List<TraceData> tracesData = traceRepository.getTraces(0, traceRepository.getTotalNumberOfTraces());
        List<Trace> traces = new ArrayList<>();

        try {
            for (TraceData data : tracesData) {
                traces.add(
                        new Trace(null, poiRepository.getById(data.getPointOfInterestId().toString()), data.getEntryTime(), data.getExitTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch (input) {
            case "1":
                args[1] = "localization";
                break;
            case "2":
                args[1] = "frequent users";

                dynamicRanking.updateVisitorsCountRanking(traces, false);
                break;
            case "3":
                args[1] = "length of stay";

                dynamicRanking.updateTimeSpentRanking(traces, false);
                break;
            case "4":
                parent.execute();
                break;
            default:
                execute();
        }

        try {
            List<POI> pois = poiRepository.getAll();

            printChart(args, pois);
            System.out.println("Chart has been written to a file");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        parent.execute();
    }

    private void printChart(String[] args, List<POI> pois) {
        clusteringWindow.transferPOIsSetRanking(pois, dynamicRanking);
        clusteringWindow.printChartToFile(args);
    }
}
