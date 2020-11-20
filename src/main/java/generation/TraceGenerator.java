package generation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.POI;
import model.Trace;
import model.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TraceGenerator {
    private List<UserTraceInformation> users;
    private List<POI> pointsOfInterest;
    private Random random;

    public TraceGenerator(List<User> users, List<POI> pointsOfInterest) {
        this.random = new Random();
        this.users = new LinkedList<>();

        this.pointsOfInterest = pointsOfInterest;
        for (User user: users) {
            this.users.add(new UserTraceInformation(
                        user,
                        this.pointsOfInterest.get(random.nextInt(this.pointsOfInterest.size()))
            ));
        }
    }

    public List<Trace> generateTrace(LocalDateTime time) {
        List<Trace> traces = new LinkedList<>();

        for (UserTraceInformation userTraceInformation: this.users) {

            Trace newTrace;

            if (userTraceInformation.getRemainingTravelTime() <= 0) {
                if (userTraceInformation.getWaitingTime() <= 0) {

                    // get unique destination POI
                    POI destination;
                    POI location = userTraceInformation.getNewPOI();
                    do {
                        destination = this.pointsOfInterest.get(
                                random.nextInt(this.pointsOfInterest.size())
                        );
                    }while (destination.equals(location));

                    // actualize data
                    userTraceInformation.updatePOIs(
                            location,
                            destination
                    );
                    long waitingTime = random.nextInt(10) + 5;
                    LocalDateTime exitTime = time.plusMinutes(waitingTime);
                    userTraceInformation.setWaitingTime(waitingTime);

                    // generate trace from data
                    newTrace = new Trace(
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

        public UserTraceInformation(User user, POI startPOI) {
            this.user = user;
            this.lastPOI = null;
            this.newPOI = startPOI;
            this.remainingTravelTime = 0;
            this.waitingTime = 0;
            this.velocity = (random.nextDouble() + 2.) * 10; // 20 - 30
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
