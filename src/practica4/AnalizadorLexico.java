package practica4;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexico {
     private ArrayList<Linea> lineas = new ArrayList<Linea>();
     private List<Token> tablaTokens = new ArrayList<Token>();

    public AnalizadorLexico(ArrayList<Linea> lineas){
        this.lineas = lineas;
    }

    /*
    Metodo que analiza cada una de las lineas y guarda los resultados en un objeto token, los cuales se van almacenando en
    un array list de tokens para imprimirlos posteriormente
    @param es el objeto linea donde se encuentra la linea del archivo junto con su numero de linea
    @return devuelve una lista en el cual se guardan los tokens de la linea, junto con su posicion en la tabla, su numero de
        token y su numero de linea
     */
    public void analizar(Linea linea){
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(linea.getLinea(), " ");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            tokens.add(token);
        }
        for(String token : tokens) {
            Token nt = null;
            if(esUnOperadorRelacional(token)) {
                switch(token) {
                    case "<":
                        nt = new Token(token, TokenType.OPERADORES_MENOR_QUE, linea.getNumeroLinea());
                        break;
                    case ">":
                        nt = new Token(token, TokenType.OPERADORES_MAYOR_QUE, linea.getNumeroLinea());
                        break;
                    case "<=":
                        nt = new Token(token, TokenType.OPERADORES_MENOR_IGUAL_QUE, linea.getNumeroLinea());
                        break;
                    case ">=":
                        nt = new Token(token, TokenType.OPERADORES_MAYOR_IGUAL_QUE, linea.getNumeroLinea());
                        break;
                    case "==":
                        nt = new Token(token, TokenType.OPERADORES_COMPARACION, linea.getNumeroLinea());
                        break;
                    case "!=":
                        nt = new Token(token, TokenType.OPERADORES_DIFERENTE, linea.getNumeroLinea());
                        break;
                }
            } else if(esUnOperadorLogico(token)) {
                switch(token) {
                    case "||":
                        nt = new Token(token, TokenType.OPERADORES_O, linea.getNumeroLinea());
                        break;
                    case "&&":
                        nt = new Token(token, TokenType.OPERADORES_Y, linea.getNumeroLinea());
                        break;
                    case "!":
                        nt = new Token(token, TokenType.OPERADORES_NO, linea.getNumeroLinea());
                        break;
                }
            } else if(esUnCaracterEspecial(token)) {
                switch(token) {
                    case "(":
                        nt = new Token(token, TokenType.CARACTERES_PARENTESIS_IZQUIERDO, linea.getNumeroLinea());
                        break;
                    case ")":
                        nt = new Token(token, TokenType.CARACTERES_PARENTESIS_DERECHO, linea.getNumeroLinea());
                        break;
                    case ",":
                        nt = new Token(token, TokenType.CARACTERES_COMA, linea.getNumeroLinea());
                        break;
                    case ";":
                        nt = new Token(token, TokenType.CARACTERES_PUNTO_COMA, linea.getNumeroLinea());
                        break;
                    case ":":
                        nt = new Token(token, TokenType.CARACTERES_DOS_PUNTOS, linea.getNumeroLinea());
                        break;
                } 
            } else { //Se arroja una excepcion si se encuentra una cadena que no coincida con ningun componente lexico
            	throw new ErrorLexico("Error léxico en la línea: " + linea.getNumeroLinea() +
                        ", Token no válido encontrado: " + token);
            }
            tablaTokens.add(nt);
        }
    }

    // Imprimir tabla de tokens
    public void imprimirTablaTokens() {
    	for(Token token : tablaTokens) {
    		System.out.println(token.toString());
    	}
    }
    
    // Método para verificar si una cadena es un operador relacional
    public static boolean esUnOperadorRelacional(String cadena) {
        String regexOperadoresRelacionales = "(<=?|>=?|(!=|==))";
        Pattern patternOperadoresRelacionales = Pattern.compile(regexOperadoresRelacionales);
        Matcher matcherOperadoresRelacionales = patternOperadoresRelacionales.matcher(cadena);
        return matcherOperadoresRelacionales.matches();
    }

    // Método para verificar si una cadena es un operador lógico
    public static boolean esUnOperadorLogico(String cadena) {
        String regexOperadoresLogicos = "(\\|\\||&&|!)";
        Pattern patternOperadoresLogicos = Pattern.compile(regexOperadoresLogicos);
        Matcher matcherOperadoresLogicos = patternOperadoresLogicos.matcher(cadena);
        return matcherOperadoresLogicos.matches();
    }

    // Método para verificar si una cadena es un carácter especial
    public static boolean esUnCaracterEspecial(String cadena) {
        String regexCaracteresEspeciales = "([();, :])";
        Pattern patternCaracteresEspeciales = Pattern.compile(regexCaracteresEspeciales);
        Matcher matcherCaracteresEspeciales = patternCaracteresEspeciales.matcher(cadena);
        return matcherCaracteresEspeciales.matches();
    }

}
