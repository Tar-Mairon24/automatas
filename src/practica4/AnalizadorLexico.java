package practica4;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
    private final List<String> lexemas = new ArrayList<>();

    /*
    Metodo que analiza cada una de las lineas y guarda los resultados en un objeto token, los cuales se van almacenando en
    un array list de tokens para imprimirlos posteriormente
    @param es el objeto linea donde se encuentra la linea del archivo junto con su numero de linea
    @return devuelve una lista en el cual se guardan los lexemas para su comparacion posterior
     */
    public List<String> analizador(String cadena) {
        if(!cadena.isEmpty()){
            StringBuilder lexema = new StringBuilder();
            int[][] tablaLexica = {
                    // a-z 0-9  _   #   %   &   $   ?   ,   ;   :   (   )   /   "   >   <   =   !   |   -   .   *   +  otro  {  }
                    {6, 20, 28, 28, 28, 16, 28, 28, 1, 1, 26, 1, 1, 2, 8, 10, 10, 12, 14, 18, 21, 28, 25, 25, 28, 1, 1}, //0
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //1
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 3, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28 }, //2
                    {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}, //3
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 5, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //4
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //5
                    {6, 6, 6, 7, 7, 7, 7, 7, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //6
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //7
                    {8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8}, //8
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //9
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 11, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //10
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //11
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 13, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //12
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //13
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 15, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //14
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //15
                    {28, 28, 28, 28, 28, 17, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //16
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //17
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 19, 28, 28, 28, 28, 28, 28, 28}, //18
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //19
                    {28, 20, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 23, 28, 28, 28, 28, 28}, //20
                    {28, 22, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //21
                    {28, 22, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 23, 28, 28, 28, 28, 28}, //22
                    {28, 24, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //23
                    {28, 24, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //24
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //25
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 27, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //26
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //27
                    {28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //28
                    {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3} //29
            };
            int actual = 0, anterior, i = 0, posicion = 0;
            char[] c = cadena.toCharArray();
            do{
                anterior = actual;
                actual = tablaLexica[actual][pertenece(c[posicion])];
                if(c[posicion] == ' ' && actual != 8){
                    actual = 28;
                }
                if(actual != 28) {
                    lexema.append(c[posicion]);
                    if(posicion+1 == cadena.length()){
                        lexemas.add(lexema.toString());
                        lexema.setLength(0);
                        posicion = -2;
                    }
                    posicion++;
                }
                if(actual == 28 && esFinal(anterior)){
                    if(!lexema.toString().isEmpty()){
                        lexemas.add(lexema.toString());
                        i++;
                    }
                    lexema.setLength(0);
                    actual = 0;
                }
                if(posicion == -1)
                    break;
                if(actual == 28) {
                    posicion++;
                    actual = 0;
                }
            }while(i < cadena.length());
        }
        return lexemas;
    }

    private boolean esFinal(int q){
        switch (q){
            case 1, 2, 5, 6, 7, 9, 10, 11, 13, 14, 15, 17, 19, 20, 21, 24, 25, 26, 27 -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    //Metodo que recibe un caracter y checa en que grupo de caracteres entra dependiendo de su columna en la tabla de trancisiones
    private int pertenece(char c){
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') return 0;
        else if (c >= '0' && c <= '9') return 1;
        else {
            switch (c) {
                case '_' -> {
                    return 2;
                }
                case '#' -> {
                    return 3;
                }
                case '%' -> {
                    return 4;
                }
                case '&' -> {
                    return 5;
                }
                case '$' -> {
                    return 6;
                }
                case '?' -> {
                    return 7;
                }
                case ',' -> {
                    return 8;
                }
                case ';' -> {
                    return 9;
                }
                case ':' -> {
                    return 10;
                }
                case '(' -> {
                    return 11;
                }
                case ')' -> {
                    return 12;
                }
                case '/' -> {
                    return 13;
                }
                case '"' -> {
                    return 14;
                }
                case '>' -> {
                    return 15;
                }
                case '<' -> {
                    return 16;
                }
                case '=' -> {
                    return 17;
                }
                case '!' -> {
                    return 18;
                }
                case '|' -> {
                    return 19;
                }
                case '-' -> {
                    return 20;
                }
                case '.' -> {
                    return 21;
                }
                case '*' -> {
                    return 22;
                }
                case '+' -> {
                    return 23;
                }
                case '{' -> {
                    return 25;
                }
                case '}' -> {
                    return 26;
                }
            }
        }
        return 24;
    }
}