package practica3;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExpresionRegular {
    public ExpresionRegular(){

    }

    public boolean identificadoresJava(String cadena) {
        //alfabeto = UNICODE
        //Q = {q0, q1,q2,q3, q4,q5}
        int [][] estadosTrancision = {
            {4, 4, 5, 1, 3, 5},
            {2, 2, 2, 2, 2, 5},
            {4, 4, 4, 2, 4, 4},
            {4, 4, 4, 4, 3, 5},
            {4, 4, 4, 4, 4, 5},
            {5, 5, 5, 5, 5, 5}
        };
        int actual = 0;
        for(char c: cadena.toCharArray()){
            actual = estadosTrancision[actual][pertenece(c)];
        }
        //S = {q0}
        //F = {q2, q3, q4}
        //Se compara el estado actual al que se llego al ultimo y si es uno de los finales(2, 3, 4) se devuelve true
        return actual == 2 || actual == 3 || actual == 4;
    }

    private int pertenece(char c){
        if(c >= 'A' && c <= 'Z') return 0;
        else if(c >= 'a' && c <= 'z') return 1;
        else if(c >= '0' && c <= '9') return 2;
        else if(c == '_') return 3;
        else if(c == '$') return 4;
        else return 5;
    }


    /** La siguiente funcion evalua una cadena para ver si es un Operador Logico valido en Python al simular
     *  el funcionamiento de un AFD a partir de su tabla de transiciones
     * @param cadena	La cadena a evaluar
     * @return			El tipo de componente lexico y su definicion
     * 					null en caso de que no sea un componente lexico valido
     */
    public String clasificarOperadorLogico(String cadena) {
        //Definir alfabeto y estados finales
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
    public String clasificarOperadorAritmetico(String cadena) {
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
        return switch (estadoActual) {
            case "q1" -> "Operador Aritmetico Suma";
            case "q2" -> "Operador Aritmetico Modulo";
            case "q3" -> "Operador Aritmetico Resta";
            case "q4" -> "Operador Aritmetico Division";
            case "q5" -> "Operador Aritmetico Division Entera";
            case "q6" -> "Operador Aritmetico Multiplicacion";
            case "q7" -> "Operador Aritmetico Exponente";
            default -> null;
        };
    }

    /** La siguiente funcion evalua una cadena para ver si es un Operador Relacional valido en Python al simular
     *  el funcionamiento de un AFD a partir de su tabla de transiciones
     * @param cadena	La cadena a evaluar
     * @return			El tipo de componente lexico y su definicion
     * 					null en caso de que no sea un componente lexico valido
     */
    public String clasificarOperadorRelacional(String cadena) {
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
        return switch (estadoActual) {
            case "q1" -> "Operador Relacional Menor que";
            case "q2" -> "Operador Relacional Menor o igual que";
            case "q3" -> "Operador Relacional Mayor que";
            case "q4" -> "Operador Relacional Mayor o igual que";
            case "q6" -> "Operador Relacional Igual que";
            case "q8" -> "Operador Relacional Diferente que";
            default -> null;
        };
    }

    public String regresanum(String cadena)
    {
        String x = evaluarExpresion(cadena);
        if(x==null)
            return null;
        for(int num=0; num < x.length(); num++)
        {
            if(x.charAt(num) == '.')
                return "Número Real";
        }
        return "Número Entero";
    }

    private String evaluarExpresion(String cadena)
    { //(-)?(0-9)+(.(0-9)+(f|L)?)?
        if(cadena.length() == 1)
        {
            if(cadena.charAt(0) >= '0' && cadena.charAt(0) <= '9')
                return cadena;
            else
                return null;
        }
        else if(cadena.charAt(0) == '-' || (cadena.charAt(0) >= '0' && cadena.charAt(0) <= '9'))
        {
            int pos1=1;
            while(cadena.charAt(pos1) >= '0' && cadena.charAt(pos1) <= '9')
            {
                pos1++;
                if(pos1 == cadena.length())
                    return cadena;
            }
            if(!(cadena.charAt(pos1) == '.'))
                return null;
            pos1++;
            if(pos1 == cadena.length())
                return null;
            while(cadena.charAt(pos1) >= '0' && cadena.charAt(pos1) <= '9')
            {
                pos1++;
                if(pos1 == cadena.length())
                    return cadena;
            }
            if((cadena.charAt(pos1) != 'f' || cadena.charAt(pos1) != 'L') && pos1 != cadena.length()-1)
                return null;
            return cadena;
        }
        else
            return null;
    }
}
