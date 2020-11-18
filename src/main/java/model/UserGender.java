package model;

public enum UserGender {
    male,
    female,
    helikopter_szturmowy;


    public static UserGender getGender(int x) {
        return values()[x];
    }

    }
