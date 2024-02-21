package ejercicio1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class main{
    public static void main(String[] Args){
        Scanner sc = new Scanner(System.in);
        String nombre = sc.nextLine();

        String ruta = "D:\\Users\\Mario\\Documents\\Sistemas\\6to semestre\\Automatas\\untitled\\src\\Nombre.txt";

        try{

            File file = new File(ruta);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nombre);
            bw.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}