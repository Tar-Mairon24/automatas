package practica2;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        String nombre;
        String menu = """
                1. Utilizar otro archivo
                2. Leer archivo en lenguaje 1
                3. Leer archivo en lenguaje 2
                4. Leer archivo en lenguaje 3
                5. Terminar
                """;

        System.out.println("""

                Manejo de Expresiones regulares
                ----------------------------------""");

        ArrayList<String> texto;

        System.out.print("Nombre del archivo a Utilizar (ej. Prueba.txt): ");
        nombre = sc.next();

        while(opcion != 5){
            System.out.println(menu);
            System.out.print("Opcion del menu: ");
            opcion = sc.nextInt();
            if(opcion != 5){
                switch (opcion){
                    case 1 -> {
                        System.out.print("\nNombre del archivo a Utilizar (ej. Prueba.txt): ");
                        nombre = sc.next();
                    }
                    case 2 -> {
                        //Se guarda el array list resultante y se muestran su contenido
                        texto = leer(nombre);
                        System.out.println("\n-------------------\n");
                        for(String string : texto){
                            //En vez de imprimir las lineas, se analizaran para ver si cumplen con la ER una por una
                            System.out.println(string);
                        }
                        enterParaContinuar();
                    }
                    case 3 ->
                        System.out.println("Proximamente");
                    case 4 ->
                        System.out.println("En desarrollo");
                    default -> System.out.println("Escriba una opcion valida");
                }
            }
        }
    }

    /*
    Metodo para leer, devuelve un arraylist con todas las lineas del archivo, para poder usarlas individualmente
     */
    private static ArrayList<String> leer(String nombre) {
        ArrayList<String> lineas =  new ArrayList<>();
        try{
            String ruta = "src/practica2/archivosTxt/" + nombre;
            File file = new File(ruta);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            try{
                String line;
                while((line = br.readLine()) != null){
                    lineas.add(line);
                }
                return lineas;
            }catch(IOException e){
                System.out.println("Ocurrio un error");
            }
        }catch(FileNotFoundException e){
            System.out.println("Ese archivo no existe");
            e.printStackTrace();
        }
        return lineas;
    }

    private static void enterParaContinuar(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\n-------------------\\n Fin del archivo (Pulse enter para continuar)");
        try
        {
            System.in.read();
            sc.nextLine();
        }
        catch(Exception e)
        {e.printStackTrace();}
    }
}
