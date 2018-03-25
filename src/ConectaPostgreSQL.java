import java.sql.*;
import java.util.ArrayList;

public class ConectaPostgreSQL {

    // connexió
    private Connection conn = null;

    public void connecta() throws SQLException {
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
    public void desconnecta() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
            System.out.println("Desconnectat");
            conn = null;
        }
    }

    // crea la taula cotxes
    public void creaTablaCoches() throws SQLException {
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

    // elimina la taula de cotxes si existeix
    public void eliminaTablaCoches() throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            String sql = "DROP TABLE IF EXISTS coches";
            st.executeUpdate(sql);
            System.out.println("Eliminada taula COCHES");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // carregar cotxes des del fitxer cotches.txt a base de dades
    public void cargaTablacoches() throws SQLException {

        ArrayList<String> listaLineasCoches = Fichero.leerFichero();
        ArrayList<Coche> cocheArrayList = new ArrayList<>();

        for (String listaLineasCoche : listaLineasCoches) {
            String[] stringCocheV = listaLineasCoche.split(" ");

            cocheArrayList.add(new Coche(
                    stringCocheV[0],
                    stringCocheV[1],
                    stringCocheV[2],
                    stringCocheV[3],
                    Integer.valueOf(stringCocheV[4]),
                    Integer.valueOf(stringCocheV[5]))
            );
        }

        for (int i = 0; i < cocheArrayList.size(); i++) {
            if (existeCoche(cocheArrayList.get(i).getMatricula())) {
                System.out.println("La matrícula existe!");
            } else {
                insertCoche(cocheArrayList, i);
            }
        }
    }

    private void insertCoche(ArrayList<Coche> cocheArrayList, int i) throws SQLException {
        String sql = "INSERT INTO coches (matricula, marca, modelo, color, anio, precio) VALUES (?,?,?,?,?,?)";

        // crea la sentència a executar (només un cop!)
        PreparedStatement ps = null;
        try {
            // obté l'estat anterior de l'autocommit.
            boolean anteriorAutoCommit = conn.getAutoCommit();

            ps = conn.prepareStatement(sql);
            try {
                // fem que no faci autocommit a cada execució
                conn.setAutoCommit(false);

                // afegim els valors a insertar
                ps.setString(1, cocheArrayList.get(i).getMatricula());
                ps.setString(2, cocheArrayList.get(i).getMarca());
                ps.setString(3, cocheArrayList.get(i).getModelo());
                ps.setString(4, cocheArrayList.get(i).getColor());
                ps.setInt(5, cocheArrayList.get(i).getAnio());
                ps.setInt(6, cocheArrayList.get(i).getPrecio());
                ps.executeUpdate();
                System.out.println("Afegit el cotxe " + cocheArrayList.get(i).getMarca());

                // si no hi ha problemes accepta tot
                conn.commit();
            } catch (SQLException e) {
                // trobat problemes amb la inserció: tot enrere
                conn.rollback();
            } finally {
                // tornem l'estat de autocomit tal i com estava
                conn.setAutoCommit(anteriorAutoCommit);
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    // comprovar si existeix matrícula
    public boolean existeCoche(String matricula) throws SQLException {
        String sql = "SELECT exists(SELECT matricula FROM coches WHERE matricula = '" + matricula + "')";
        Statement st = null;
        boolean exists = false;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                exists = rs.getBoolean("exists");
            }
            rs.close();
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return exists;
    }
}
