import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Fichero {

    // llegir l'arxiu coches.txt
    public static ArrayList<String> leerFichero() {
        ArrayList<String> listaCoches = new ArrayList<>();

        File file;
        FileReader fileReader;
        BufferedReader bufferedReader;
        try {
            file = new File("coches.txt");
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String s;

            //Recórrer l'arxiu i guardar línia per línia en un ArrayList
            while ((s = bufferedReader.readLine()) != null) {
                listaCoches.add(s);
            }
        } catch (IOException e) {
            System.out.println("Imposible abrir el archivo para leer");
        }
        return listaCoches;
    }
}
