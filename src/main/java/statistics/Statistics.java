package statistics;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import model.POI;
import model.Trace;
import model.User;

import java.time.temporal.ChronoUnit;
import java.util.*;

public class Statistics {
    private final Map<User, Map<POI, Double>> lengthOfStayPerUserPerPOI = new HashMap<>();
    private final Map<User, Pair<Trace, Double>> longestRoutePerUser = new HashMap<>();
    private final List<POIPopularity> poiPopularityList = new LinkedList<>();

    public String getLengthOfStay(User user, POI poi) {
        StringBuilder sb = new StringBuilder("I (" + user.getUserID() + ") have no route");

        if (lengthOfStayPerUserPerPOI.containsKey(user)) {
            Map<POI, Double> POIStayLength = lengthOfStayPerUserPerPOI.get(user);
            if (POIStayLength.containsKey(poi)) {
                sb.delete(0, sb.length());
                sb.append("I (" + user.getUserID() + ") was here (" + poi.getId() + ") for ").append(POIStayLength.get(poi)).append(" minutes");
            }
        }

        return sb.toString();
    }

    public String getLongestRoute(User user) {
        StringBuilder sb = new StringBuilder("I (" + user.getUserID() + ") have no route");

        if (longestRoutePerUser.containsKey(user)) {
            sb.delete(0, sb.length());
            sb.append("My (")
                .append(user.getUserID())
                .append(") longest route: ")
                .append(longestRoutePerUser.get(user).getValue());
        }

        return sb.toString();
    }

    public String getMostPopularPOI(POI searchedPOI) {
        StringBuilder sb = new StringBuilder("I did not visit anything from POI " + searchedPOI.getId());

        for (POIPopularity poiPopularity : poiPopularityList) {
            if (poiPopularity.oldPOI.getId() == searchedPOI.getId()) {
                sb.delete(0, sb.length());
                sb.append("Most visited POIs from ").append(searchedPOI.getId()).append(": \n");
                int max = poiPopularity.getMaxVisits();
                for (POI poi : poiPopularity.popularityCounter.keySet()) {
                    if (poiPopularity.popularityCounter.get(poi) == max) {
                        sb.append(poi.getId()).append("\n");
                    }
                }

                sb.append("with ").append(max).append(" visits");

                break;
            }
        }

        return sb.toString();
    }

    /**
     * Adds trace to statistics
     *
     * @param trace new trace to be added
     */
    public void addNewTrace(Trace trace, Trace oldTrace) {
        calculateLengthOfStay(trace);
        calculateLongestRoute(trace);
        calculatePOIPopularity(trace, oldTrace);
    }

    /**
     * Method to update length of stay for user
     *
     * @param trace new trace to be added
     */
    private void calculateLengthOfStay(Trace trace) {
        lengthOfStayPerUserPerPOI.computeIfAbsent(
            trace.getUser(),
            k -> new HashMap<>()
        );

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
     *
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

    /**
     * Method to calculate poi popularity
     *
     * @param trace new trace to be added
     */
    private void calculatePOIPopularity(Trace trace, Trace oldTrace) {
        if (oldTrace == null) {
            return;
        }

        // Look for poi source from old trace
        boolean POIFound = false;
        for (POIPopularity poiPopularity : poiPopularityList) {
            // If id matching
            if (poiPopularity.oldPOI.getId() == oldTrace.getPointOfInterest().getId()) {
                POIFound = true;

                // If dest POI already exists, increase counter, or else
                // set counter to 1
                int counter = 1;
                if (poiPopularity.popularityCounter.containsKey(trace.getPointOfInterest())) {
                    counter = poiPopularity.popularityCounter.get(trace.getPointOfInterest()) + 1;
                }

                // Update counter
                poiPopularity.popularityCounter.put(
                    trace.getPointOfInterest(),
                    counter
                );

                break;
            }
        }

        // If src POI not found
        if (!POIFound) {
            // Create new object with new poi dest counter
            int counter = 1;
            Map<POI, Integer> POIPopularityMap = new HashMap<>();
            POIPopularityMap.put(trace.getPointOfInterest(), counter);

            POIPopularity newPOIPopularity = new POIPopularity(oldTrace.getPointOfInterest(), POIPopularityMap);

            // Update counter
            poiPopularityList.add(newPOIPopularity);
        }
    }

    @AllArgsConstructor
    private static class POIPopularity {
        private final POI oldPOI;
        private final Map<POI, Integer> popularityCounter;

        public int getMaxVisits() {
            Optional<Map.Entry<POI, Integer>> a =
                popularityCounter.entrySet().stream().max(Map.Entry.comparingByValue());

            if (a.isPresent()) {
                return a.get().getValue();
            } else {
                return -1;
            }
        }
    }
}