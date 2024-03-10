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

        System.out.print("Archivo a leer(ej. Prueba.txt): ");
        nombre = sc.nextLine();

        try{
            texto = ma.leer(nombre);
            for(String string: texto){
                //Divide cada linea del archivo en palabras indivuduales utilizando el espacio como delimitador
                st = new StringTokenizer(string);
                while(st.hasMoreTokens()){
                    token = st.nextToken();
                    if(er.identificadoresJava(token))
                        System.out.println(token + "\tIdentificador de java");
                    //aqui pongan un if para que cheque palabras con sus expresiones regulares y igual las imprimen
                }
            }

        }catch (FileNotFoundException e){
            System.out.println("No existe el archivo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
