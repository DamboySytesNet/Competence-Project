package model;

public enum UserType {
    student,
    teacher,
    stuff;

    public static UserType getUserType(int x) {
        return values()[x];
    }
}