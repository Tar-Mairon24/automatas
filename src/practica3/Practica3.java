package practica3;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
                    if (clasificacion == null) clasificacion = clasificarOperadorLogico(token);
                    if (er.identificadoresJava(token) && clasificacion == null) clasificacion = "Identificador de Java";
                    if (clasificacion == null) clasificacion = clasificarOperadorAritmetico(token);
                    if (clasificacion == null) clasificacion = clasificarOperadorRelacional(token);
                    if (clasificacion == null) clasificacion = "No válido";
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
    
    /** La siguiente funcion evalua una cadena para ver si es un Operador Logico valido en Python al simular 
     *  el funcionamiento de un AFD a partir de su tabla de transiciones
     * @param cadena	La cadena a evaluar
     * @return			El tipo de componente lexico y su definicion
     * 					null en caso de que no sea un componente lexico valido
     */
	public static String clasificarOperadorLogico(String cadena) {
		//Definir alfabeto y estados finales
		final Set<Character> ALFABETO = Set.of('a', 'n', 'd', 'o', 'r', 't');
		final Set<String> ESTADOS_FINALES = Set.of("q3", "q6", "q8");
		//Definir tabla de transiciones
		final Map<String, Map<Character, String>> TABLA_TRANSICIONES = new HashMap<>();
		TABLA_TRANSICIONES.put("q0", Map.of('a', "q4", 'n', "q1", 'd', "q9", 'o', "q7", 'r', "q9", 't', "q9"));
		TABLA_TRANSICIONES.put("q1", Map.of('a', "q9", 'n', "q9", 'd', "q9", 'o', "q2", 'r', "q9", 't', "q9"));
		TABLA_TRANSICIONES.put("q2", Map.of('a', "q9", 'n', "q9", 'd', "q9", 'o', "q9", 'r', "q9", 't', "q3"));
		TABLA_TRANSICIONES.put("q3", Map.of('a', "q9", 'n', "q9", 'd', "q9", 'o', "q9", 'r', "q9", 't', "q9"));
		TABLA_TRANSICIONES.put("q4", Map.of('a', "q9", 'n', "q5", 'd', "q9", 'o', "q9", 'r', "q9", 't', "q9"));
		TABLA_TRANSICIONES.put("q5", Map.of('a', "q9", 'n', "q9", 'd', "q6", 'o', "q9", 'r', "q9", 't', "q9"));
		TABLA_TRANSICIONES.put("q6", Map.of('a', "q9", 'n', "q9", 'd', "q9", 'o', "q9", 'r', "q9", 't', "q9"));
		TABLA_TRANSICIONES.put("q7", Map.of('a', "q9", 'n', "q9", 'd', "q9", 'o', "q9", 'r', "q8", 't', "q9"));
		TABLA_TRANSICIONES.put("q8", Map.of('a', "q9", 'n', "q9", 'd', "q9", 'o', "q9", 'r', "q9", 't', "q9"));
		TABLA_TRANSICIONES.put("q9", Map.of('a', "q9", 'n', "q9", 'd', "q9", 'o', "q9", 'r', "q9", 't', "q9"));
		//Estado inicial de la transicion y sobre el cual se iterara
		String estadoActual = "q0";
		//Iterar sobre cada caracter en la cadena y realizar la transicion debida
		for (char c : cadena.toCharArray()) {
			if (!ALFABETO.contains(c)) {
				return null;
			}
			estadoActual = TABLA_TRANSICIONES.get(estadoActual).get(c);
		}
		
		//Comparar el estado de la ultima transicion y definir componente lexico
		switch (estadoActual) {
		case "q3":
			return "Operador Logico not";
		case "q6":
			return "Operador Logico and";
		case "q8":
			return "Operador Logico or";
		default:
			return null;
		}
	}
	
	 /** La siguiente funcion evalua una cadena para ver si es un Operador Aritmetico valido en Python al simular 
     *  el funcionamiento de un AFD a partir de su tabla de transiciones
     * @param cadena	La cadena a evaluar
     * @return			El tipo de componente lexico y su definicion
     * 					null en caso de que no sea un componente lexico valido
     */
	public static String clasificarOperadorAritmetico(String cadena) {
		//Definir alfabeto y estados finales
		final Set<Character> ALFABETO = Set.of('+', '-', '*', '/', '%');
		final Set<String> ESTADOS_FINALES = Set.of("q1", "q2", "q3", "q4", "q5", "q6", "q7");
		//Definir tabla de transiciones
		final Map<String, Map<Character, String>> TABLA_TRANSICIONES = new HashMap<>();
		TABLA_TRANSICIONES.put("q0", Map.of('+', "q1", '-', "q3", '*', "q6", '/', "q4", '%', "q2"));
		TABLA_TRANSICIONES.put("q1", Map.of('+', "q8", '-', "q8", '*', "q8", '/', "q8", '%', "q8"));
		TABLA_TRANSICIONES.put("q2", Map.of('+', "q8", '-', "q8", '*', "q8", '/', "q8", '%', "q8"));
		TABLA_TRANSICIONES.put("q3", Map.of('+', "q8", '-', "q8", '*', "q8", '/', "q8", '%', "q8"));
		TABLA_TRANSICIONES.put("q4", Map.of('+', "q8", '-', "q8", '*', "q8", '/', "q5", '%', "q8"));
		TABLA_TRANSICIONES.put("q5", Map.of('+', "q8", '-', "q8", '*', "q8", '/', "q8", '%', "q8"));
		TABLA_TRANSICIONES.put("q6", Map.of('+', "q8", '-', "q8", '*', "q7", '/', "q8", '%', "q8"));
		TABLA_TRANSICIONES.put("q7", Map.of('+', "q8", '-', "q8", '*', "q8", '/', "q8", '%', "q8"));
		TABLA_TRANSICIONES.put("q8", Map.of('+', "q8", '-', "q8", '*', "q8", '/', "q8", '%', "q8"));
		//Estado inicial de la transicion y sobre el cual se iterara
		String estadoActual = "q0";
		//Iterar sobre cada caracter en la cadena y realizar la transicion debida
		for (char c : cadena.toCharArray()) {
			if (!ALFABETO.contains(c)) {
				return null;
			}
			estadoActual = TABLA_TRANSICIONES.get(estadoActual).get(c);
		}
		//Comparar el estado de la ultima transicion y definir componente lexico
		switch (estadoActual) {
		case "q1":
			return "Operador Aritmetico Suma";
		case "q2":
			return "Operador Aritmetico Modulo";
		case "q3":
			return "Operador Aritmetico Resta";
		case "q4":
			return "Operador Aritmetico Division";
		case "q5":
			return "Operador Aritmetico Division Entera";
		case "q6":
			return "Operador Aritmetico Multiplicacion";
		case "q7":
			return "Operador Aritmetico Exponente";
		default:
			return null;
		}
	}
	
	 /** La siguiente funcion evalua una cadena para ver si es un Operador Relacional valido en Python al simular 
     *  el funcionamiento de un AFD a partir de su tabla de transiciones
     * @param cadena	La cadena a evaluar
     * @return			El tipo de componente lexico y su definicion
     * 					null en caso de que no sea un componente lexico valido
     */
	public static String clasificarOperadorRelacional(String cadena) {
		//Definir alfabeto y estados finales
		final Set<Character> ALFABETO = Set.of('<', '>', '=', '!');
		final Set<String> ESTADOS_FINALES = Set.of("q1", "q2", "q3", "q4", "q6", "q8");
		//Definir tabla de transiciones
		final Map<String, Map<Character, String>> TABLA_TRANSICIONES = new HashMap<>();
		TABLA_TRANSICIONES.put("q0", Map.of('<', "q1", '>', "q3", '=', "q5", '!', "q7"));
		TABLA_TRANSICIONES.put("q1", Map.of('<', "q9", '>', "q9", '=', "q2", '!', "q9"));
		TABLA_TRANSICIONES.put("q2", Map.of('<', "q9", '>', "q9", '=', "q9", '!', "q9"));
		TABLA_TRANSICIONES.put("q3", Map.of('<', "q9", '>', "q9", '=', "q4", '!', "q9"));
		TABLA_TRANSICIONES.put("q4", Map.of('<', "q9", '>', "q9", '=', "q9", '!', "q9"));
		TABLA_TRANSICIONES.put("q5", Map.of('<', "q9", '>', "q9", '=', "q6", '!', "q9"));
		TABLA_TRANSICIONES.put("q6", Map.of('<', "q9", '>', "q9", '=', "q9", '!', "q9"));
		TABLA_TRANSICIONES.put("q7", Map.of('<', "q9", '>', "q9", '=', "q8", '!', "q9"));
		TABLA_TRANSICIONES.put("q8", Map.of('<', "q9", '>', "q9", '=', "q9", '!', "q9"));
		TABLA_TRANSICIONES.put("q9", Map.of('<', "q9", '>', "q9", '=', "q9", '!', "q9"));
		//Estado inicial de la transicion y sobre el cual se iterara
		String estadoActual = "q0";
		//Iterar sobre cada caracter en la cadena y realizar la transicion debida
		for (char c : cadena.toCharArray()) {
			if (!ALFABETO.contains(c)) {
				return null;
			}
			estadoActual = TABLA_TRANSICIONES.get(estadoActual).get(c);
		}
		//Comparar el estado de la ultima transicion y definir componente lexico
		switch (estadoActual) {
		case "q1":
			return "Operador Relacional Menor que";
		case "q2":
			return "Operador Relacional Menor o igual que";
		case "q3":
			return "Operador Relacional Mayor que";
		case "q4":
			return "Operador Relacional Mayor o igual que";
		case "q6":
			return "Operador Relacional Igual que";
		case "q8":
			return "Operador Relacional Diferente que";
		default:
			return null;
		}
	}
}
