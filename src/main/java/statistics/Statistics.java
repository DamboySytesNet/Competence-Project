package statistics;

import javafx.util.Pair;
import model.POI;
import model.Trace;
import model.User;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private Map<User, Map<POI, Double>> lengthOfStayPerUserPerPOI = new HashMap<>();
    private Map<User, Pair<Trace, Double>> longestRoutePerUser = new HashMap<>();

    /**
     * Adds trace to statistics
     * @param trace new trace to be added
     */
    public void addNewTrace(Trace trace) {
        calculateLengthOfStay(trace);
        calculateLongestRoute(trace);
    }

    /**
     * Method to update length of stay for user
     * @param trace new trace to be added
     */
    private void calculateLengthOfStay(Trace trace) {
        // Get user's POIs
        Map<POI, Double> lengthOfStayPerPOI = lengthOfStayPerUserPerPOI.get(trace.getUser());

        // Calculate length of stay in trace [min]
        double minutes = ChronoUnit.SECONDS.between(
            trace.getEntryTime(),
            trace.getExitTime()) / 60.0;

        // Add length of stay to existing key or
        // add new key with length of stay
        if (lengthOfStayPerPOI.containsKey(trace.getPointOfInterest())) {
            lengthOfStayPerPOI.put(
                trace.getPointOfInterest(),
                lengthOfStayPerPOI.get(trace.getPointOfInterest()) + minutes
            );
        } else {
            lengthOfStayPerPOI.put(
                trace.getPointOfInterest(),
                minutes
            );
        }

        // Update statistics
        lengthOfStayPerUserPerPOI.put(
            trace.getUser(),
            lengthOfStayPerPOI
        );
    }

    /**
     * Method to calculate longest route between two traces
     * @param trace new trace to be added
     */
    private void calculateLongestRoute(Trace trace) {
        Pair<Trace, Double> longestRoute;
        double newDistance = 0.0;

        // Get (oldTrace, longestRoute) pair if exists or
        // create new one
        if (longestRoutePerUser.containsKey(trace.getUser())) {
            longestRoute = longestRoutePerUser.get(trace.getUser());
        } else {
            longestRoute = new Pair<>(null, newDistance);
        }

        // If previous trace exists, calculate distance between new and old one
        Trace oldTrace = longestRoute.getKey();
        if (oldTrace != null) {
            newDistance = oldTrace.getPointOfInterest().getGeolocalization()
                .getDistance(trace.getPointOfInterest().getGeolocalization());
        }

        // Create pair with new trace and currently longest route
        Pair<Trace, Double> newLongestRoute;
        double oldDistance = longestRoute.getValue();
        if (newDistance > oldDistance) {
            newLongestRoute = new Pair<>(trace, newDistance);
        } else {
            newLongestRoute = new Pair<>(trace, oldDistance);
        }

        // Update statistics
        longestRoutePerUser.put(trace.getUser(), newLongestRoute);
    }
}
