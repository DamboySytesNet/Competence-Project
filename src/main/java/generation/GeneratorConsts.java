package generation;

public final class GeneratorConsts {

    // lodz latitude and longitude
    public static final double LATITUDE_CENTER = 51.0759445;
    public static final double LONGITUDE_CENTER = 19.457216;

    // lodz latitude and longitude rectangle boundary
    // |LATITUDE_C - geolocalization latitude| < LATITUDE_B
    public static final double LATITUDE_BOUNDARY = 1.0;
    public static final double LONGITUDE_BOUNDARY = 1.0;

    // time propagation of tick in minutes
    public static final long TIME_STEP = 1;


}
