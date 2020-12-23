package statistics;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.POI;
import model.Trace;
import model.User;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Statistics {
    private final Map<User, Map<POI, Double>> lengthOfStayPerUserPerPOI = new HashMap<>();
    private final Map<User, Pair<Trace, Double>> longestRoutePerUser = new HashMap<>();
    private final List<POIPopularity> poiPopularityList = new LinkedList<>();

    private User getUserById(String userId, Set<User> userSet) {
        UUID userUUID;

        try {
            userUUID = UUID.fromString(userId);
        } catch(Exception e) {
            return null;
        }

        for (User u : userSet) {
            if (u.getUserID().equals(userUUID)) {
                return u;
            }
        }

        return null;
    }

    private POI getPOIById(String poiID, Set<POI> poiSet) {
        UUID poiUUID;
        try {
            poiUUID = UUID.fromString(poiID);
        } catch(Exception e) {
            return null;
        }

        for (POI poi : poiSet) {
            if (poi.getId().equals(poiUUID)) {
                return poi;
            }
        }

        return null;
    }

    public String getLengthOfStayText(String userId, String poiId) {
        User user = getUserById(userId, lengthOfStayPerUserPerPOI.keySet());
        StringBuilder sb = new StringBuilder("User was not included before");

        if (user != null) {
            POI poi = getPOIById(poiId, lengthOfStayPerUserPerPOI.get(user).keySet());
            sb.delete(0, sb.length());
            sb.append("POI was not included before");

            if (poi != null) {
                Map<POI, Double> POIStayLength = lengthOfStayPerUserPerPOI.get(user);
                sb.delete(0, sb.length());
                sb.append("I (").append(user.getUserID()).append(") was here (").append(poi.getId()).append(") for ")
                    .append(POIStayLength.get(poi)).append(" minutes");
            }
        }

        return sb.toString();
    }

    public double getLengthOfStay(User user, POI poi) {
        if (lengthOfStayPerUserPerPOI.containsKey(user)) {
            Map<POI, Double> POIStayLength = lengthOfStayPerUserPerPOI.get(user);
            if (POIStayLength.containsKey(poi)) {
                return POIStayLength.get(poi);
            }
        }

        return 0.0;
    }

    public String getLongestRouteText(String userId) {
        User user = getUserById(userId, longestRoutePerUser.keySet());
        StringBuilder sb = new StringBuilder("User was not included before");

        if (user != null) {
            sb.delete(0, sb.length());
            sb.append("My (")
                .append(user.getUserID())
                .append(") longest route: ")
                .append(longestRoutePerUser.get(user).getValue());
        }

        return sb.toString();
    }

    public double getLongestRoute(User user) {
        if (longestRoutePerUser.containsKey(user)) {
            return longestRoutePerUser.get(user).getValue();
        }

        return 0.0;
    }

    public String getMostPopularPOIText(String searchedPOIId) {
        Set<POI> poiSet = poiPopularityList.stream().map(POIPopularity::getOldPOI).collect(Collectors.toSet());

        POI searchedPOI = getPOIById(searchedPOIId, poiSet);

        StringBuilder sb = new StringBuilder("POI was not included before");
        if (searchedPOI != null) {
            sb.delete(0, sb.length());
            sb.append("I did not visit anything from POI " + searchedPOI.getId());

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
        }

        return sb.toString();
    }

    public List<POI> getMostPopularPOI(POI searchedPOI) {
        List<POI> popularPOIs = new LinkedList<>();

        for (POIPopularity poiPopularity : poiPopularityList) {
            if (poiPopularity.oldPOI.getId() == searchedPOI.getId()) {
                int max = poiPopularity.getMaxVisits();
                for (POI poi : poiPopularity.popularityCounter.keySet()) {
                    if (poiPopularity.popularityCounter.get(poi) == max) {
                        popularPOIs.add(poi);
                    }
                }

                break;
            }
        }

        return popularPOIs;
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
        // Get user's POIs
        Map<POI, Double> lengthOfStayPerPOI = lengthOfStayPerUserPerPOI.computeIfAbsent(
            trace.getUser(),
            k -> new HashMap<>()
        );

        // Calculate length of stay in trace [min]
        double minutes = ChronoUnit.MINUTES.between(
            trace.getEntryTime(),
            trace.getExitTime());

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
        double newDistance = 0.0;

        // Get (oldTrace, longestRoute) pair if exists or
        // create new one
        Pair<Trace, Double> longestRoute = longestRoutePerUser.get(trace.getUser());
        if (longestRoute == null) {
            longestRoute = new Pair<>(null, newDistance);
        }

        // If previous trace exists, calculate distance between new and old one
        Trace oldTrace = longestRoute.getKey();
        if (oldTrace != null) {
            newDistance = oldTrace.getPointOfInterest().getGeolocalization()
                .getDistance(trace.getPointOfInterest().getGeolocalization());
        }

        // Create pair with new trace and currently longest route
        double oldDistance = longestRoute.getValue();
        double distance = newDistance > oldDistance ? newDistance : oldDistance;
        Pair<Trace, Double> newLongestRoute = new Pair<>(trace, distance);

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
                Integer newCounter = poiPopularity.popularityCounter.get(trace.getPointOfInterest());
                if (newCounter != null) {
                    counter = newCounter + 1;
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
        @Getter
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