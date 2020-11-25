package model;

import java.util.ArrayList;
import java.util.UUID;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {

    private UUID userID;
    private String phoneNumber;
    private int userAge;
    private UserType userType;
    private UserGender userGender;

    private String experimentId;
}
