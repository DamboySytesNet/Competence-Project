package generation;

import model.User;
import model.UserGender;
import model.UserType;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Random;
import java.util.UUID;

public class UserFactory {
    private NormalDistribution ageNormalDistribution;
    private UniformIntegerDistribution typeUniformDistribution;
    private UniformIntegerDistribution genderUniformDistribution;
    private Random random ;
    private static UserFactory instance = new UserFactory();

    private UserFactory() {
        ageNormalDistribution = new NormalDistribution(35, 16);
        typeUniformDistribution = new UniformIntegerDistribution(0,2);
        genderUniformDistribution = new UniformIntegerDistribution(0,2);
        random = new Random();
    }

    public static UserFactory getInstance() {
        return instance;
    }

    public User generate(){
        int userAge;
        UUID userID = UUID.randomUUID();
        String phoneNumber = "";
        phoneNumber = Integer.toString(random.nextInt(899999999) + 100000000) ;
        do{
            userAge = (int) ageNormalDistribution.sample();
        }while(userAge < 19 || userAge > 80);

        UserType userType = UserType.getUserType(typeUniformDistribution.sample());
        UserGender userGender = UserGender.getGender(genderUniformDistribution.sample());
        return User.builder()
                .userID(userID)
                .phoneNumber(phoneNumber)
                .userAge(userAge)
                .userType(userType)
                .userGender(userGender)
                .build();
    }


}

