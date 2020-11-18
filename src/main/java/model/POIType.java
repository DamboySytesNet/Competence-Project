package model;

public enum POIType {
    indoor,
    outdoor,
    other;

    public static POIType getPOIType(int x) {
        return values()[x];
    }
}
