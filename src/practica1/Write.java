package practica1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Write {
    public Write() throws IOException {

    }

    String ruta = "/home/tarmairon/Documents/sistemas/6to semestre/Automatas/Practicas/src/practica1/tabla.txt";
    File file = new File(ruta);

    FileWriter fw = new FileWriter(file);
    BufferedWriter bw = new BufferedWriter(fw);

    public void escribir(Persona persona) throws IOException{
        try {
            bw.write(persona.toString());

        }catch (IOException e) {
            System.out.println(e);
        }
    }

    public void cerrarBuffer() throws IOException {
        bw.close();
    }
}
