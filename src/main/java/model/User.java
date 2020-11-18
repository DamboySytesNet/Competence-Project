package model;

import java.util.ArrayList;
import java.util.UUID;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {

    private UUID userID;
    private String phoneNumber;
    private int userAge;
    private UserType userType;
    private UserGender userGender;
}
