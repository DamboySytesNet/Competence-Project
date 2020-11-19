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

public class TraceGenerator {
    private List<UserTraceInformation> users;
    private List<POI> pointsOfInterest;

    public TraceGenerator(List<User> users, List<POI> pointsOfInterest) {
        this.users = new LinkedList<>();
        this.pointsOfInterest = pointsOfInterest;
        for (User user: users) {
            this.users.add(new UserTraceInformation(
                    user,
                    this.pointsOfInterest.get((int) (Math.random() * this.pointsOfInterest.size())))
            );
        }
    }

    public List<Trace> generateTrace(LocalDateTime time) {
        List<Trace> traces = new LinkedList<>();
        for (UserTraceInformation userTraceInformation: this.users) {
            POI newPOI = userTraceInformation.getNewPOI();


            Trace newTrace;

            if (userTraceInformation.getRemainingTravelTime() <= 0) {



                if (userTraceInformation.getWaitingTime() > 0) {
                    userTraceInformation.decreaseWaitingTime(GeneratorConsts.TIME_SPEED);
                } else {
                    long waitingTime = (long) (Math.random() * 10 + 5);
                    LocalDateTime exitTime = time.plusMinutes(waitingTime);
                    newTrace = new Trace(userTraceInformation.getUser(), userTraceInformation.getNewPOI(), time, exitTime);
                    userTraceInformation.setWaitingTime(waitingTime);
                    traces.add(newTrace);
                    userTraceInformation.updatePOIs(
                            newPOI,
                            this.pointsOfInterest.get((int) (Math.random() * this.pointsOfInterest.size()))
                    );
                }
            }


            userTraceInformation.decreaseRemainingTravelTime(GeneratorConsts.TIME_SPEED);

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
            this.lastPOI = startPOI;
            this.newPOI = null;
            this.remainingTravelTime = 0;
            this.waitingTime = 0;
            this.velocity = Math.random();
        }

        public void decreaseRemainingTravelTime(long minutes) {
            this.remainingTravelTime =- minutes;
        }

        public void decreaseWaitingTime(long minutes) {
            this.waitingTime =- minutes;
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
