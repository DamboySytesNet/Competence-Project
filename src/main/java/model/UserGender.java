package model;

public enum UserGender {
    male,
    female;


    public static UserGender getGender(int x) {
        return values()[x];
    }

    }
