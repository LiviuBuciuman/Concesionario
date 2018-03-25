import java.io.IOException;

public class Concesionario {

    public static void main(String[] args) throws IOException {

        ConectaPostgreSQL conectaPostgreSQL = null;

        try {
            conectaPostgreSQL = new ConectaPostgreSQL();
            conectaPostgreSQL.connecta();
            conectaPostgreSQL.eliminaTablaCoches();
            conectaPostgreSQL.creaTablaCoches();
            conectaPostgreSQL.cargaTablacoches();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conectaPostgreSQL != null) {
                conectaPostgreSQL.desconnecta();
            }
        }
    }
}
