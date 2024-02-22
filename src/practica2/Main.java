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
					case 4 -> {
						// System.out.println("En desarrollo");
						ArrayList<String> cadenasValidas = new ArrayList<String>();
						ArrayList<String> cadenasInvalidas = new ArrayList<String>();

						texto = leer(nombre);

						for (String string : texto) { // Evaluar cada cadena dentro del ArrayList si es valida se guarda
														// en el ArrayList "cadenasValidas" en caso contrario en
														// "cadenasInvalidas"
							boolean valida = evaluarExpresion3(string);
							if (valida) {
								cadenasValidas.add(string);
							} else {
								cadenasInvalidas.add(string);
							}
						}
						//Imprime contenidos de las ArrayList
                        /*
                        System.out.println("Cadenas validas");
                        for(String string: cadenasValidas) {
                        	System.out.println(string);
                        }
                        System.out.println();
                        System.out.println("Cadenas invalidas");
                        for(String string: cadenasInvalidas) {
                        	System.out.println(string);
                        }
                        System.out.println();
                        */
					}
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
    
    /**
     * Este metodo evalua si una cadena cumple con las condiciones de la expresion regular (0| 1)(77)*(a-z)+
     * @param cadena	La cadena a evaluar.
     * @return 			true si la cadena cumple las condiciones de la expresion regular, falso de lo contrario.
     */
    public static boolean evaluarExpresion3(String cadena) {
    	if(!(cadena.charAt(0) == '0' || cadena.charAt(0) == '1') || cadena.length() <= 1) return false;		//Verifica que el primer caracter sea 0 o 1
    	if(!(cadena.charAt(1) == '7')) { // Si el caracter en la posicion 1 no es 7 checar que todas las letras siguientes sean de la a-z 
    		int i = 1;
    		while(i < cadena.length()) {
    			if(cadena.charAt(i) < 'a' || cadena.charAt(i) > 'z') return false;
    			i++;
    		}
    		return true;
    	} else { // En caso de que el caracter 1 sea 7 comprobar que la cantidad de 7 sea par y los siguientes caracteres de la a-z
    		int i = 1;
    		int cont = 0;
    		//Contar numeros de apariciones del caracter 7 
    		while(i < cadena.length() && cadena.charAt(i) == '7') {	
    			cont++;
    			i++;
    		}
    		if(!(cont % 2 == 0) || i == cadena.length()) return false; // Si no hay una cantidad par de 7 o termina en 7 regresar falso
    		// Verificar que los caracteres despues de los 7 pertenezcan a a-z
    		while(i < cadena.length()) {
    			if(cadena.charAt(i) < 'a' || cadena.charAt(i) > 'z') return false;
    			i++;
    		}
    		return true;
    	}
    }
}
