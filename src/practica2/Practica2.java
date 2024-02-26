package practica2;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Practica2 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        String nombre;
        String menu = """
                
                -------------------------------------------------------------
                Menu
                1. Utilizar otro archivo
                2. Comparar con la Expresion Regular: (a-z)+(0-9)(0-9)(aa|xy)*
                3. Comparar con la Expresion Regular: _(f)+(1-8)+_
                4. Comparar con la Expresion Regular: (0|1)(77)*(a-z)+
                5. Terminar
                """;

        System.out.println("""

                Manejo de Expresiones regulares
                ----------------------------------""");

        ArrayList<String> texto;

        System.out.print("Nombre del archivo a utilizar (ej. Prueba): ");
        nombre = sc.next();

        while(opcion != 5){
            try{
                System.out.println(menu);
                System.out.print("Opcion del menu: ");
                opcion = sc.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("\n------------------\nOpcion invalida\n-------------------\n");
                //sc.nextLine();
            }
            if(opcion != 5){
                switch (opcion){
                    case 1 -> {
                        System.out.print("\nNombre del archivo a utilizar (ej. Prueba): ");
                        nombre = sc.next();
                    }
                    case 2 -> {
                        ArrayList<String> cadenasValidas = new ArrayList<>();
                        ArrayList<String> cadenasInvalidas = new ArrayList<>();
                        try{
                            texto = leer(nombre);

                            System.out.println("\n\nExpresion regular: (a-z)+(0-9)(0-9)(aa|xy)*\n");

                            // Evaluar cada cadena dentro del ArrayList si es valida se guarda
                            // en el ArrayList "cadenasValidas" en caso contrario en
                            // "cadenasInvalidas"
                            for (String string : texto) {
                                StringTokenizer st = new StringTokenizer(string, ",");
                                while (st.hasMoreTokens()){
                                    String token = st.nextToken();
                                    boolean valida = regexExpresion(token);
                                    if (valida)
                                        cadenasValidas.add(token);
                                    else
                                        cadenasInvalidas.add(token);
                                }
                            }
                            if(!cadenasValidas.isEmpty())
                                imprimir(cadenasValidas, cadenasInvalidas);
                            else{
                                System.out.println("\n------------------\nNo hay ninguna coincidencia\n-------------------\n");
                            }
                        }catch (FileNotFoundException e){
                            System.out.println("\nEl archivo no existe\n");
                        }
                    }
                    case 3 ->{
                    	//System.out.println("Proximamente");
                    	ArrayList<String> cadenasValidas = new ArrayList<>();
						ArrayList<String> cadenasInvalidas = new ArrayList<>();
						try {
							texto = leer(nombre);
							System.out.println("\n\nExpresion regular: _(f)+(1-8)+_\n");

                            // Evaluar cada cadena dentro del ArrayList si es valida se guarda
                            // en el ArrayList "cadenasValidas" en caso contrario en
                            // "cadenasInvalidas"
                            for (String string : texto) {
                                StringTokenizer st = new StringTokenizer(string, ",");
                                while (st.hasMoreTokens()){
                                    String token = st.nextToken();
                                    boolean valida = evaluarExpresion2(token);
                                    if (valida)
                                        cadenasValidas.add(token);
                                    else
                                        cadenasInvalidas.add(token);
                                }
                            }
                            if(!cadenasValidas.isEmpty())
                                imprimir(cadenasValidas, cadenasInvalidas);
                            else{
                                System.out.println("\n------------------\nNo hay ninguna coincidencia\n-------------------\n");
                            }
						} catch(FileNotFoundException e) {
							System.out.println("\nEl archivo no exite\n");
						}
                    }
					case 4 -> {
						ArrayList<String> cadenasValidas = new ArrayList<>();
						ArrayList<String> cadenasInvalidas = new ArrayList<>();

						try{
                            texto = leer(nombre);

                            System.out.println("\n\nExpresion regular: (0|1)(77)*(a-z)+\n");

                            // Evaluar cada cadena dentro del ArrayList si es valida se guarda
                            // en el ArrayList "cadenasValidas" en caso contrario en
                            // "cadenasInvalidas"
                            for (String string : texto) {
                                StringTokenizer st = new StringTokenizer(string, ",");
                                while (st.hasMoreTokens()){
                                    String token = st.nextToken();
                                    boolean valida = evaluarExpresion3(token);
                                    if (valida)
                                        cadenasValidas.add(token);
                                    else
                                        cadenasInvalidas.add(token);
                                }
                            }
                            if(!cadenasValidas.isEmpty())
                                imprimir(cadenasValidas, cadenasInvalidas);
                            else{
                                System.out.println("\n------------------\nNo hay ninguna coincidencia\n-------------------\n");
                            }
                        }catch (FileNotFoundException e){
                            System.out.println("\nEl archivo no exite\n");
                        }
					}
                    default -> System.out.println("Escriba una opcion valida");
                }
            }
        }
        sc.close();
    }

    /*
    Metodo para leer, devuelve un arraylist con todas las lineas del archivo, para poder usarlas individualmente
    @param nombre del archivo a leer
     */
    private static ArrayList<String> leer(String nombre) throws FileNotFoundException {
        ArrayList<String> lineas =  new ArrayList<>();
        String ruta = "src/practica2/archivosTxt/" + nombre + ".txt";
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
        return lineas;
    }

    /*
    Funcion que checa si la cadena cumple con las condiciones de la expresion regular (a-z)+(0-9)(0-9)(aa|xy)*
    @param cadena a evaluar
    @return true si la cadena hace match con la er o false si no lo haca
     */
    public static boolean regexExpresion(String cadena){
        String er = "^[a-z]+\\d{2}(aa|xy)*$";
        Pattern pattern = Pattern.compile(er);
        Matcher matcher = pattern.matcher(cadena);
        return matcher.matches();
    }
    
    /*
     * Este metodo evalua si una cadena cumple con las condiciones de la expresion regular (0| 1)(77)*(a-z)+
     * @param cadena	La cadena a evaluar.
     * @return 			true si la cadena cumple las condiciones de la expresion regular, falso de lo contrario.
     */
    public static boolean evaluarExpresion3(String cadena) {
    	if(!(cadena.charAt(0) == '0' || cadena.charAt(0) == '1') || cadena.length() == 1) return false;		//Verifica que el primer caracter sea 0 o 1
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
    
    public static boolean evaluarExpresion2(String cadena) { //
        if( !(cadena.charAt(0) == '_' || cadena.charAt(cadena.length()-1) == '_') || cadena.length() < 4) return false;
        if(!(cadena.charAt(1) == 'f')) return false;
        int pos1=2;
        for(int pos=2; pos < cadena.length(); pos++){
            if(!(cadena.charAt(pos) == 'f')) {
                pos=cadena.length();}
            else pos1++;
        }
        if(cadena.charAt(pos1) == '_') return false;
        while(pos1 < cadena.length()-1){
            if(cadena.charAt(pos1) > '8' || cadena.charAt(pos1) < '1') return false;
            else pos1++;
        }
        return true;
    }

    public static void imprimir (ArrayList<String> validas, ArrayList<String> invalidas){
        System.out.format("+-----------------+-----------------+-----------------+-----------------+%n");
        System.out.format("|      Valida     |     Cantidad    |     Invalida    |     Cantidad    |%n");
        System.out.format("+-----------------+-----------------+-----------------+-----------------+%n");
        String leftAlignment = "| %-15s | %-15s | %-15s | %-15s |%n";

        int max = Math.max(validas.size(), invalidas.size()), min = Math.min(validas.size(), invalidas.size()), i;

        System.out.format(leftAlignment, validas.getFirst(), validas.size(), invalidas.getFirst(), invalidas.size());
        System.out.format("+-----------------+-----------------+-----------------+-----------------+%n");

        for (i = 1; i < min; i++){
            System.out.format(leftAlignment, validas.get(i), "", invalidas.get(i), "");
            System.out.format("+-----------------+-----------------+-----------------+-----------------+%n");
        }
        if(validas.size() > invalidas.size()){
            for(; i < max; i++) {
                System.out.format(leftAlignment, validas.get(i), "", "", "");
                System.out.format("+-----------------+-----------------+-----------------+-----------------+%n");
            }
        }else
            for (; i < max; i++){
                System.out.format(leftAlignment, "", "", invalidas.get(i), "");
                System.out.format("+-----------------+-----------------+-----------------+-----------------+%n");
            }
    }
}
