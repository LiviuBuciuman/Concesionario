import java.io.*;

public class Fichero {
    public void readFile() {
        File file;
        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            file = new File("coches.txt");
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String s;

            while ((s = bufferedReader.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            System.out.println("Imposible abrir el archivo para leer");
        }
    }
}
