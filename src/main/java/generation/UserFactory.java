package generation;

import model.Geolocalization;
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

    private static UserFactory instance = new UserFactory();

    private UserFactory() {
        ageNormalDistribution = new NormalDistribution(35, 16);
        typeUniformDistribution = new UniformIntegerDistribution(0,2);
        genderUniformDistribution = new UniformIntegerDistribution(0,1);
    }

    public static UserFactory getInstance() {
        return instance;
    }

    public User generate(){
        Random random = new Random();
        UUID userID = UUID.randomUUID();
        StringBuilder phoneNumber = new StringBuilder();
        for(int i = 0 ; i < 9 ; i++){
            phoneNumber.append((char) (random.nextInt(9) + 48));
        }
        int userAge;
        do{
            userAge = (int) ageNormalDistribution.sample();
        }while(userAge < 19 || userAge > 80);
        UserType userType = UserType.getUserType(typeUniformDistribution.sample());
        UserGender userGender = UserGender.getGender(genderUniformDistribution.sample());
        return User.builder()
                .userID(userID)
                .phoneNumber(phoneNumber.toString())
                .userAge(userAge)
                .userType(userType)
                .userGender(userGender)
                .build();
    }


}

