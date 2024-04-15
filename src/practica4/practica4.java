package practica4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class practica4 {
    public static void main(String[] args) throws IOException {
        ArrayList<Linea> lineas;
        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico analizador = new AnalizadorLexico();
        List<String> lexemas;

        lineas = ma.leer();
        for(Linea linea : lineas) {
            lexemas = analizador.analizador(linea.getLinea());
            for(String lexema : lexemas){
                System.out.println(lexema);
            }
            lexemas.clear();
        }
    }
}
