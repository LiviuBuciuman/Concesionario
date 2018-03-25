import java.sql.*;

public class Concesionario {

    private String[] coches = null;

    // connexió
    private Connection conn = null;

    private void connecta() throws SQLException {
        if (conn == null) {
            // components de la cadena de connexió
            String usuari = "postgres";
            String password = "1234";
            String host = "localhost";
            String bd = "concesionario";
            String url = "jdbc:postgresql://" + host + "/" + bd;
            conn = DriverManager.getConnection(url, usuari, password);
            System.out.println("Connectat amb " + url);
        }
    }

    // tanca la connexió en cas que estigui connectada
    private void desconnecta() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
            System.out.println("Desconnectat");
            conn = null;
        }
    }

    private void creaTablaCoches() throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            String sql
                    = "CREATE TABLE  COCHES ("
                    + " matricula VARCHAR(8) PRIMARY KEY,"
                    + " marca VARCHAR(40) NOT NULL,"
                    + " modelo VARCHAR(40) NOT NULL,"
                    + " color VARCHAR(40) NOT NULL,"
                    + " anio INTEGER NOT NULL,"
                    + " precio INTEGER NOT NULL)";
            st.executeUpdate(sql);
            System.out.println("Creada taula COCHES");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    private void cargaTablacoches() throws SQLException {

    }

    public static void main(String[] args) {

        Concesionario concesionario = null;
        Fichero fichero = new Fichero();

        fichero.readFile();

        try {
            concesionario = new Concesionario();
            concesionario.connecta();
            concesionario.creaTablaCoches();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (concesionario != null) {
                concesionario.desconnecta();
            }
        }
    }
}
