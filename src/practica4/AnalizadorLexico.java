package practica4;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.IOException;

public class AnalizadorLexico {
    private final List<Token> tablaTokens = new ArrayList<>();
	private int contador = 1;

	public void imprimirTablaTokens(FileWriter writer) throws IOException {
        for (Token token : tablaTokens) {
            writer.write(token.toString() + "\n");
        }
    }
    
    public List<Token> getTablaTokens() {
        return tablaTokens;
    }

    // Función para verificar si una cadena es un número entero positivo
    public static boolean esEntero(String cadena) {
        Pattern patron = Pattern.compile("^\\d+$");
        Matcher matcher = patron.matcher(cadena);
        return matcher.matches();
    }

    // Función para verificar si una cadena es un número decimal positivo o negativo
    public static boolean esDecimal(String cadena) {
        Pattern patron = Pattern.compile("^-?\\d+\\.\\d+$");
        Matcher matcher = patron.matcher(cadena);
        return matcher.matches();
    }

    // Función para verificar si una cadena es una constante de tipo string
    public static boolean esString(String cadena) {
        // La expresión regular para una constante string es cualquier secuencia de caracteres entre comillas
        Pattern patron = Pattern.compile("^\".*\"$");
        Matcher matcher = patron.matcher(cadena);
        return matcher.matches();
    }
	public static boolean esIdentificador(String cadena) {
		// La expresión regular para una constante string es cualquier secuencia de caracteres entre comillas
		Pattern patron = Pattern.compile("^[a-zA-Z]([a-zA-Z]|[0-9]|_)*([#$%&?])$");
		Matcher matcher = patron.matcher(cadena);
		return matcher.matches();
	}
    
	public void analizar(List<Token> lista, Writer writer) throws IOException {
		for (Token lexema : lista) {
			if(esEntero(lexema.getLexema())) {
				lexema.setToken(TokenType.CONSTANTES_ENTERO);
				tablaTokens.add(lexema);
			} else if(esDecimal(lexema.getLexema())) {
				lexema.setToken(TokenType.CONSTANTES_REAL);
				tablaTokens.add(lexema);
			} else if(esString(lexema.getLexema())) {
				lexema.setToken(TokenType.CONSTANTES_CADENA);
				tablaTokens.add(lexema);
			} else if (esIdentificador(lexema.getLexema())){
				if (lexema.getLexema().charAt(lexema.getLexema().length() - 1) == '#') {
					lexema.setToken(TokenType.IDENTIFICADORES_TIPO_CADENA);
					tablaTokens.add(lexema);
				} else if (lexema.getLexema().charAt(lexema.getLexema().length() - 1) == '%') {
					lexema.setToken(TokenType.IDENTIFICADORES_TIPO_REAL);
					tablaTokens.add(lexema);
				} else if (lexema.getLexema().charAt(lexema.getLexema().length() - 1) == '&') {
					lexema.setToken(TokenType.IDENTIFICADORES_TIPO_ENTERO);
					tablaTokens.add(lexema);
				} else if (lexema.getLexema().charAt(lexema.getLexema().length() - 1) == '$') {
					lexema.setToken(TokenType.IDENTIFICADORES_TIPO_LOGICO);
					tablaTokens.add(lexema);
				} else if (lexema.getLexema().charAt(lexema.getLexema().length() - 1) == '?') {
					lexema.setToken(TokenType.IDENTIFICADORES_ID_GENERAL);
					tablaTokens.add(lexema);
				}
			}else {
				switch (lexema.getLexema()) {
					case "program" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_PROGRAM);
						tablaTokens.add(lexema);
					}
					case "begin" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_BEGIN	);
						tablaTokens.add(lexema);
					}
					case "end" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_END);
						tablaTokens.add(lexema);
					}
					case "read" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_WRITE);
						tablaTokens.add(lexema);
					}
					case "write" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_READ);
						tablaTokens.add(lexema);
					}
					case "if" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_IF);
						tablaTokens.add(lexema);
					}
					case "else" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_ELSE);
						tablaTokens.add(lexema);
					}
					case "while" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_WHILE);
						tablaTokens.add(lexema);
					}
					case "repeat" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_REPEAT);
						tablaTokens.add(lexema);
					}
					case "until" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_UNTIL);
						tablaTokens.add(lexema);
					}
					case "int" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_INT);
						tablaTokens.add(lexema);
					}
					case "real" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_REAL);
						tablaTokens.add(lexema);
					}
					case "string" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_STRING);
						tablaTokens.add(lexema);
					}
					case "bool" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_BOOL);
						tablaTokens.add(lexema);
					}
					case "var" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_VAR);
						tablaTokens.add(lexema);
					}
					case "then" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_THEN);
						tablaTokens.add(lexema);
					}
					case "do" -> {
						lexema.setToken(TokenType.PALABRAS_RESERVADAS_DO);
						tablaTokens.add(lexema);
					}
					case "*" -> {
						lexema.setToken(TokenType.OPERADORES_MULTIPLICACION);
						tablaTokens.add(lexema);
					}
					case "/" -> {
						lexema.setToken(TokenType.OPERADORES_DIVISION);
						tablaTokens.add(lexema);
					}
					case "+" -> {
						lexema.setToken(TokenType.OPERADORES_SUMA);
						tablaTokens.add(lexema);
					}
					case "-" -> {
						lexema.setToken(TokenType.OPERADORES_RESTA);
						tablaTokens.add(lexema);
					}
					case ":=" -> {
						lexema.setToken(TokenType.OPERADORES_ASIGNACION);
						tablaTokens.add(lexema);
					}
					case "<" -> {
						lexema.setToken(TokenType.OPERADORES_MENOR_QUE);
						tablaTokens.add(lexema);
					}
					case ">" -> {
						lexema.setToken(TokenType.OPERADORES_MAYOR_QUE);
						tablaTokens.add(lexema);
					}
					case "<=" -> {
						lexema.setToken(TokenType.OPERADORES_MENOR_IGUAL_QUE);
						tablaTokens.add(lexema);
					}
					case ">=" -> {
						lexema.setToken(TokenType.OPERADORES_MAYOR_IGUAL_QUE);
						tablaTokens.add(lexema);
					}
					case "==" -> {
						lexema.setToken(TokenType.OPERADORES_COMPARACION);
						tablaTokens.add(lexema);
					}
					case "!=" -> {
						lexema.setToken(TokenType.OPERADORES_DIFERENTE);
						tablaTokens.add(lexema);
					}
					case "||" -> {
						lexema.setToken(TokenType.OPERADORES_O);
						tablaTokens.add(lexema);
					}
					case "&&" -> {
						lexema.setToken(TokenType.OPERADORES_Y);
						tablaTokens.add(lexema);
					}
					case "!" -> {
						lexema.setToken(TokenType.OPERADORES_NO);
						tablaTokens.add(lexema);
					}
					case "(" -> {
						lexema.setToken(TokenType.CARACTERES_PARENTESIS_IZQUIERDO);
						tablaTokens.add(lexema);
					}
					case ")" -> {
						lexema.setToken(TokenType.CARACTERES_PARENTESIS_DERECHO);
						tablaTokens.add(lexema);
					}
					case "," -> {
						lexema.setToken(TokenType.CARACTERES_COMA);
						tablaTokens.add(lexema);
					}
					case ";" -> {
						lexema.setToken(TokenType.CARACTERES_PUNTO_COMA);
						tablaTokens.add(lexema);
					}
					case ":" -> {
						lexema.setToken(TokenType.CARACTERES_DOS_PUNTOS);
						tablaTokens.add(lexema);
					}
					case "true" -> {
						lexema.setToken(TokenType.CONSTANTES_VERDADERO);
						tablaTokens.add(lexema);
					}
					case "false" -> {
						lexema.setToken(TokenType.CONSTANTES_FALSO);
						tablaTokens.add(lexema);
					}
					default ->{
						writer.write(contador + " ," + lexema.getLexema() + " ," + lexema.getNumeroLinea() + "\n");
						contador++;
						System.err.println("Error encontrado en la linea: " + lexema.getNumeroLinea() + " Token: " + lexema.getLexema() + " no valido");
					}
				}
			}
		}
	}

	/*
    Metodo que analiza cada una de las lineas y guarda los resultados en un objeto token, los cuales se van almacenando en
    un array list de tokens para imprimirlos posteriormente
    @param es el objeto linea donde se encuentra la linea del archivo junto con su numero de linea
    @return devuelve una lista en el cual se guardan los lexemas para su comparacion posterior
     */
	public List<Token> analizador(Linea linea) {
		List<Token> tokens = new ArrayList<>();
		StringBuilder lexema = new StringBuilder();
		if(!linea.getLinea().isEmpty()){
			//StringBuilder lexema = new StringBuilder();
			int[][] tablaLexica = {
					// a-z 0-9  _   #   %   &   $   ?   ,   ;   :   (   )   /   "   >   <   =   !   |   -   .   *   +  otro
					{6, 20, 28, 28, 28, 16, 28, 28, 1, 1, 26, 1, 1, 2, 8, 10, 10, 12, 14, 18, 21, 28, 25, 25, 28}, //0
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //1
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 3, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //2
					{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}, //3
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 5, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //4
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //5
					{6, 6, 6, 7, 7, 7, 7, 7, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //6
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //7
					{8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8}, //8
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //9
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 11, 28, 28, 28, 28, 28, 28, 28}, //10
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //11
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 13, 28, 28, 28, 28, 28, 28, 28}, //12
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //13
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 15, 28, 28, 28, 28, 28, 28, 28}, //14
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //15
					{28, 28, 28, 28, 28, 17, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //16
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //17
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 19, 28, 28, 28, 28, 28}, //18
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //19
					{28, 20, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 23, 28, 28, 28}, //20
					{28, 22, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //21
					{28, 22, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 23, 28, 28, 28}, //22
					{28, 24, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //23
					{28, 24, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //24
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //25
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 27, 28, 28, 28, 28, 28, 28, 28}, //26
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //27
					{28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28}, //28
					{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3} //29
			};
			int actual = 0, anterior, i = 0, posicion = 0;
			char[] c = linea.getLinea().toCharArray();
			do{
				if(posicion == -1)
					break;
				anterior = actual;
				actual = tablaLexica[actual][pertenece(c[posicion])];
				if(c[posicion] == ' ' && actual != 8){
					actual = 28;
				}
				if(actual != 28) {
					lexema.append(c[posicion]);
					if(posicion+1 == linea.getLinea().length()){
						Token token = new Token(lexema.toString(), null, linea.getNumeroLinea());
						//lexemas.add(lexema.toString());
						tokens.add(token);
						lexema.setLength(0);
						posicion = -2;
					}
					posicion++;
				}
				if(actual == 28 && esFinal(anterior)){
					if(!lexema.toString().isEmpty()){
						//lexemas.add(lexema.toString());
						Token token = new Token(lexema.toString(), null, linea.getNumeroLinea());
						tokens.add(token);
						i++;
					}
					lexema.setLength(0);
					actual = 0;
				}
				if(actual == 28 && c[posicion] == '.'){
					if(posicion+1 == linea.getLinea().length())
						posicion = -2;
					actual = 0;
					posicion++;
				}
				if(actual == 28 && (c[posicion] == ' ')){
					if(lexema.isEmpty()) {
						actual = 0;
						posicion++;
					}
					else {
						Token token = new Token(lexema.toString(), null, linea.getNumeroLinea());
						tokens.add(token);
						lexema.setLength(0);
						posicion++;
						i++;
					}
				}
				if(actual == 28 && c[posicion] != ' ') {
					lexema.append(c[posicion]);
					if(posicion+1 != linea.getLinea().length())
						posicion++;
					else{
						Token token = new Token(lexema.toString(), null, linea.getNumeroLinea());
						tokens.add(token);
						i++;
						lexema.setLength(0);
						posicion = -1;
					}
					actual = 0;
				}
			}while(i < linea.getLinea().length());
		}
		return tokens;
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
            }
        }
        return 24;
    }
}