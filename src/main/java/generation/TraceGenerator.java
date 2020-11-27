package generation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.POI;
import model.Trace;
import model.User;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TraceGenerator {
    private List<UserTraceInformation> users;
    private List<POI> pointsOfInterest;
    private int numberOfPois;

    private NormalDistribution velocityDistribution;
    private WeibullDistribution newPoiIndexDistribution;
    private NormalDistribution waitingTimeDistribution;

    public TraceGenerator(List<User> users, List<POI> pointsOfInterest) {
        this.users = new LinkedList<>();
        this.pointsOfInterest = pointsOfInterest;
        this.numberOfPois = pointsOfInterest.size();

        this.velocityDistribution = new NormalDistribution(1.127, 0.5324);
        this.newPoiIndexDistribution = new WeibullDistribution(1, 10);
        this.waitingTimeDistribution = new NormalDistribution(30, 20);

        for (User user: users) {
            POI poi = this.pointsOfInterest.get(this.randPoiIndex());
            UserTraceInformation userTraceInfo = new UserTraceInformation(user, poi, this.velocityDistribution.sample());

            this.users.add(userTraceInfo);
        }
    }

    private int randPoiIndex() {
        int poiIndex;

        do {
            poiIndex = (int) Math.round(this.newPoiIndexDistribution.sample());
        } while (poiIndex >= this.numberOfPois || poiIndex < 0);

        return poiIndex;
    }

    private int randWaitingTime() {
        int waitingTime;

        do {
            waitingTime = (int) Math.round(this.waitingTimeDistribution.sample());
        } while (waitingTime <= 0);

        return waitingTime;
    }

    public List<Trace> generateTrace(LocalDateTime time) {
        List<Trace> traces = new LinkedList<>();

        for (UserTraceInformation userTraceInformation: this.users) {
            if (userTraceInformation.getRemainingTravelTime() <= 0) {
                if (userTraceInformation.getWaitingTime() <= 0) {
                    // get unique destination POI
                    POI destination;
                    POI location = userTraceInformation.getNewPOI();
                    do {
                        destination = this.pointsOfInterest.get(this.randPoiIndex());
                    } while (destination.equals(location));

                    // update data
                    userTraceInformation.updatePOIs(location, destination);
                    long waitingTime = this.randWaitingTime();
                    LocalDateTime exitTime = time.plusMinutes(waitingTime);
                    userTraceInformation.setWaitingTime(waitingTime);

                    // generate trace from data
                    Trace newTrace = new Trace(
                            userTraceInformation.getUser(),
                            userTraceInformation.getNewPOI(),
                            time,
                            exitTime);

                    traces.add(newTrace);
                } else {
                    userTraceInformation.decreaseWaitingTime(GeneratorConsts.TIME_STEP);
                }
            } else {
                userTraceInformation.decreaseRemainingTravelTime(GeneratorConsts.TIME_STEP);
            }
        }
        return traces;
    }

    @Getter
    @Setter
    @ToString
    private class UserTraceInformation {
        private User user;
        private POI lastPOI;
        private POI newPOI;
        private long remainingTravelTime;
        private long waitingTime;
        private double velocity;

        public UserTraceInformation(User user, POI startPOI, double velocity) {
            this.user = user;
            this.lastPOI = null;
            this.newPOI = startPOI;
            this.remainingTravelTime = 0;
            this.waitingTime = 0;
            this.velocity = velocity;
        }

        public void decreaseRemainingTravelTime(long minutes) {
            this.remainingTravelTime -= minutes;
        }

        public void decreaseWaitingTime(long minutes) {
            this.waitingTime -= minutes;
        }

        public void updatePOIs(POI lastPOI, POI newPOI) {
            this.lastPOI = lastPOI;
            this.newPOI = newPOI;

            double distance;

            if (newPOI == null || lastPOI == null) {
                distance = 0;
            } else {
                distance = lastPOI.getGeolocalization().getDistance(newPOI.getGeolocalization());
            }

            this.remainingTravelTime = (long) (distance / velocity);
        }
    }
}
