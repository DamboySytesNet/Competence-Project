package repository;

import model.Geolocalization;
import model.POI;
import model.POIType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static connectors.JavaDatabaseConnector.getConnection;

public class POIRepository implements RepositorySaver<POI>{

    public static POI getById(UUID id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `competence-schema`.`poi` WHERE id=?");
        statement.setString(1, id.toString());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        POI poi = mapResultSetToPOI(resultSet);

        connection.close();
        return poi;
    }

    public POI getById(String id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM `competence-schema`.`poi` WHERE id=?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        POI poi = POI.builder().description(resultSet.getString("description"))
                .experimentId(resultSet.getString("experiment_id"))
                .geolocalization(new Geolocalization(resultSet.getDouble("x"),
                        resultSet.getDouble("y")))
                .name(resultSet.getString("name"))
                .type(POIType.valueOf(resultSet.getString("type"))).build();

        connection.close();
        return poi;
    }

    public static List<POI> getAll() throws SQLException {
        Connection connection = getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(
                "SELECT * FROM `competence-schema`.`poi`");
        List<POI> pois = new ArrayList<>();
        while (resultSet.next()) {
            pois.add(mapResultSetToPOI(resultSet));
        }
        connection.close();
        return pois;
    }

    public static boolean save(POI poi) throws SQLException {
        Connection connection = getConnection();

        String poiID = poi.getId() == null ? UUID.randomUUID().toString() : poi.getId().toString();

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `competence-schema`.`poi` (id, name, description, x, y, type, experiment_id)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, poiID);
        statement.setString(2, poi.getName());
        statement.setString(3, poi.getDescription());
        statement.setDouble(4, poi.getGeolocalization().getLatitude());
        statement.setDouble(5, poi.getGeolocalization().getLongitude());
        statement.setString(6, poi.getType().name());
        statement.setString(7, poi.getExperimentId());

        boolean isFinished = statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }

    public static boolean saveAll(List<POI> pois) throws SQLException {
        Connection connection = getConnection();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `competence-schema`.`poi` "
                + "(id, name, description, x, y, type, experiment_id)"
                + " VALUES ");

        for (int i = 0; i < pois.size() - 1; i++) {
            sb.append("(");
            sb.append(getPOIValuesString(pois.get(i)));
            sb.append("), ");
        }
        sb.append("(");
        sb.append(getPOIValuesString(pois.get(pois.size() - 1)));
        sb.append(");");

        PreparedStatement statement = connection.prepareStatement(sb.toString());

        boolean isFinished = statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }

    /**
     * Creates string made of POI's fields separated by ", ";
     * @param poi with all fields set
     * @return string made of POI's fields
     */
    private static String getPOIValuesString (POI poi) {
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        sb.append(poi.getId().toString());
        sb.append("', '");
        sb.append(poi.getName());
        sb.append("', '");
        sb.append(poi.getDescription());
        sb.append("', ");
        sb.append(poi.getGeolocalization().getLatitude());
        sb.append(", ");
        sb.append(poi.getGeolocalization().getLongitude());
        sb.append(", '");
        sb.append(poi.getType().toString());
        sb.append("', '");
        sb.append(poi.getExperimentId());
        sb.append("'");
        return sb.toString();
    }


    @Override
    public boolean saveAllGeneric(List<POI> objects) {
        try {
            return saveAll(objects);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    public static boolean updateByName(POI poi) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE `competence-schema`.`poi` SET description=?, x=?, y=?, type=? WHERE name=?");
        statement.setString(1, poi.getDescription());
        statement.setDouble(2, poi.getGeolocalization().getLatitude());
        statement.setDouble(3, poi.getGeolocalization().getLongitude());
        statement.setString(4, poi.getType().name());
        statement.setString(5, poi.getName());

        boolean isFinished = statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }

    public static boolean delete(String name) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM `competence-schema`.`poi` WHERE name=?");
        statement.setString(1, name);

        boolean isFinished =  statement.executeUpdate() > 0;
        connection.close();
        return isFinished;
    }

    public static long getTotalNumberOfPOI() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) AS total FROM `competence-schema`.`poi`");
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        long total = Long.parseLong(resultSet.getString("total"));
        connection.close();
        return total;
    }

    private static POI mapResultSetToPOI(ResultSet resultSet) throws SQLException {
        return POI.builder()
                .id(UUID.fromString(resultSet.getString("id")))
                .description(resultSet.getString("description"))
                .experimentId(resultSet.getString("experiment_id"))
                .geolocalization(new Geolocalization(resultSet.getDouble("x"),
                        resultSet.getDouble("y")))
                .name(resultSet.getString("name"))
                .type(POIType.valueOf(resultSet.getString("type")))
                .build();
    }
}
