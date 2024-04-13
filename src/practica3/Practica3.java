package practica3;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Practica3 {
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nombre, token;
        ArrayList<String> texto;
        ManejoArchivo ma = new ManejoArchivo();
        ExpresionRegular er = new ExpresionRegular();
        StringTokenizer st;

        System.out.print("Archivo a leer (ej. Prueba.txt): ");
        nombre = sc.nextLine(); 

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/practica3/salida.txt"))) {
            texto = ma.leer(nombre);
            writer.write(String.format("| %-20s| %-40s|%n", "Cadena", "Componente Léxico"));
            writer.write("|---------------------------------------------------------------|\n");
            for (String string : texto) {
                // Divide cada línea del archivo en palabras individuales utilizando el espacio como delimitador
                st = new StringTokenizer(string);
                while (st.hasMoreTokens()) {
                    token = st.nextToken();
                    String clasificacion = null;
                    if (clasificacion == null) clasificacion = er.clasificarOperadorLogico(token);
                    if (er.identificadoresJava(token) && clasificacion == null) clasificacion = "Identificador de Java";
                    if (clasificacion == null) clasificacion = er.clasificarOperadorAritmetico(token);
                    if (clasificacion == null) clasificacion = er.clasificarOperadorRelacional(token);
					if (clasificacion == null) clasificacion = er.regresanum(token);
                    if (clasificacion != null)
                        writer.write(String.format("| %-20s| %-40s|%n", token, clasificacion));
                }
            }
            writer.write("-----------------------------------------------------------------");
            System.out.println("Resultados escritos en el archivo salida.txt");
        } catch (FileNotFoundException e) {
            System.out.println("No existe el archivo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
