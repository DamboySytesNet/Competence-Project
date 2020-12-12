package connectors;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnector {

    private Cluster cluster;
    private Session session;

    private static final String NODE = "127.0.0.1";
    private static final Integer PORT = 9042;

    public void connect() {
        Cluster.Builder b = Cluster.builder().addContactPoint(NODE);
        b.withPort(PORT);
        cluster = b.build();

        session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }
}
