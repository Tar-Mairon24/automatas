package sintactico;

import java.util.ArrayList;
import utils.Token;

public class AnalizadorSintactico {
    private final ArrayList<Token> tokens;
    private int indice;
    private Token proximoToken;
    private boolean errorSintactico;

    public AnalizadorSintactico(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.indice = 0;
        this.proximoToken = null;
        this.errorSintactico = false;
    }

    public boolean getErrotSintactico() {
        return errorSintactico;
    }

    public void analizar() {
        try {
            encabezado();
            declaraciones(false);
            estructuraPrograma();
            //error("No se esperaba la palabra clave 'end' en la línea " + tokens.get(indice).getNumeroLinea());
        } catch (Exception e) {
            if (e instanceof IndexOutOfBoundsException || e instanceof NullPointerException)
                error("Se esperaba end para finalizar el programa en la linea "
                        + tokens.get(indice - 1).getNumeroLinea() + 1);
            error("Ocurrio un error catastrofico! " + e.getMessage());
        }
    }

    private boolean hayTokensRestantes() {
        return indice < tokens.size();
    }

    private void avanza() {
        if (indice < tokens.size()) {
            indice++;
            if (indice < tokens.size() - 1)
                proximoToken = tokens.get(indice + 1);
            else
                proximoToken = null;
        } else {
            proximoToken = null; // Ya no hay más tokens para analizar
        }
    }

    private void error(String mensaje) {
        System.out.println((char) 27 + "[31m" + "ERROR SINTACTICO! " + mensaje  + (char) 27 + "[0m");
        errorSintactico = true;
        System.exit(1);
    }

    private void aceptar() {
        System.out.println((char) 27 + "[32m" + "El análisis sintáctico ha finalizado sin errores." + (char) 27 + "[0m");
        //System.out.println("El análisis sintáctico ha finalizado sin errores.");
        System.out.printf("El programa es correcto\n");
    }

    private void encabezado() {
        // Verificamos si el próximo token es la palabra clave 'program'
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -1) {
            error("Se esperaba la palabra clave 'program'");
        }
        avanza(); // Avanzamos al próximo token

        // Verificamos si hay un identificador después de la palabra clave 'program'
        if (!hayTokensRestantes()) {
            error("Se esperaba un identificador después de la palabra clave 'program'");
        }
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -55) {
            error("Se esperaba un identificador de tipo general (?)");
        }
        avanza(); // Avanzamos al próximo token

        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -75) {
            error("Se esperaba un punto y coma ';' después del identificador ");
        }
        avanza(); // Avanzamos al próximo token
    }

    private void declaraciones(boolean varEncontrada) {
        if (!hayTokensRestantes() || tokens.get(indice).getValorTablaTokens() == -2) {
            return;
        }
        Token tokenActual = tokens.get(indice);

        if (tokenActual.getValorTablaTokens() == -15) {
            if (!varEncontrada) {
                avanza();
                varEncontrada = true;
            } else {
                error("La palabra clave 'var' ya fue declarada anteriormente en la línea " + tokenActual.getNumeroLinea());
            }
        } else {
            if (!varEncontrada) {
                error("Se esperaba la palabra clave 'var' en la línea " + tokenActual.getNumeroLinea());
            } else {
                if (!tipoDato(tokenActual.getValorTablaTokens()))
                    error("Se esperaba un tipo de dato valido o la palabra ´begin', en la linea: " + tokenActual.getNumeroLinea());
                avanza();
                tokenActual = tokens.get(indice);
                if (tokenActual.getValorTablaTokens() != -77)
                    error("Se esperaba ':' después del tipo de dato en la línea " + tokenActual.getNumeroLinea());
                avanza();
                declaracionVariable();
            }
        }

        declaraciones(varEncontrada);
    }

    private void declaracionVariable() {
        // Manejo de las declaraciones de variables
        Token tokenActual = tokens.get(indice);

        // Verificar y consumir identificadores
        tokenActual = tokens.get(indice);
        if (identificador(tokenActual.getValorTablaTokens())) {
            avanza();
            tokenActual = tokens.get(indice);
            if (tokenActual.getValorTablaTokens() == -76) {
                avanza();
            } else if (tokenActual.getValorTablaTokens() == -75) {
                avanza();
                return;
            } else {
                error("Se esperaba ',' o ';' después del identificador en la línea " + tokenActual.getNumeroLinea());
            }
        } else {
            error("Se esperaba un identificador válido en la línea " + tokenActual.getNumeroLinea());
        }
        declaracionVariable();
    }

    private void estructuraPrograma() {
        // Se verifica que comienze con un begin
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -2)
            error("Se esperaba la palabra clave 'begin' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        /* Originalmente era un while pero lo cambie por el metodo recursivo */
        estructuraSentencias();
        tokenActual = tokens.get(indice);
        // Se verifica que termine con un end
        if (indice == tokens.size() - 1 && tokenActual.getValorTablaTokens() != -3)
            error("Se esperaba la palabra clave 'end' en la línea " + tokenActual.getNumeroLinea());
        // Si termina con un end se termina el programa sin errores
        if (tokenActual.getValorTablaTokens() == -3 && indice == tokens.size() - 1) {
            aceptar();
            return;
        }
    }

    // este metodo sirve para el bloque de operaciones dentro del begin como por
    // ejmplo:
    // uno& := 10 + ( 39 * dos& ) ;
    // cinco$ := ( uno& <= 10 );
    private void asignacion() {
        Token tokenActual = tokens.get(indice);
        if (identificador(tokenActual.getValorTablaTokens())) {
            avanza();
            tokenActual = tokens.get(indice);
            if (tokenActual.getValorTablaTokens() == -26) { // Token de asignación :=
                avanza();
                if (expresion()) {
                    tokenActual = tokens.get(indice);
                    if (tokenActual.getValorTablaTokens() == -75) { // Token de ;
                        avanza();
                    } else {
                        error("Se esperaba ';' después de la expresión. Lexema: " + tokenActual.getLexema()
                                + ", Línea: " + tokenActual.getNumeroLinea());
                    }
                } else {
                    error("Error en la expresión. Lexema: " + tokenActual.getLexema() + ", Línea: "
                            + tokenActual.getNumeroLinea());
                }
            } else {
                error("Se esperaba ':=' después del identificador. Lexema: " + tokenActual.getLexema() + ", Línea: "
                        + tokenActual.getNumeroLinea());
            }
        } else {
            error("Se esperaba un identificador válido. Lexema: " + tokenActual.getLexema() + ", Línea: "
                    + tokenActual.getNumeroLinea());
        }
    }

    // este metodo valida que cualquier expresion(aritmetoca, logica, relacionales)
    // sea valida
    private boolean expresion() {
        if (termino()) {
            while (opRelacionales(tokens.get(indice).getValorTablaTokens()) || opAritmeticos(tokens.get(indice).getValorTablaTokens())
                    || opLogicos(tokens.get(indice).getValorTablaTokens())) { // Verificar cualquier operador relacional
                avanza();
                if (!termino()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean termino() {
        Token tokenActual; // Obtener el token actual
        if (factor()) {
            tokenActual = tokens.get(indice);
            while (opRelacionales(tokenActual.getValorTablaTokens()) || opAritmeticos(tokenActual.getValorTablaTokens())
                    || opLogicos(tokenActual.getValorTablaTokens())) {
                avanza();
                if (!factor()) {
                    return false;
                }
                tokenActual = tokens.get(indice);
            }
            return true;
        }
        return false;
    }

    private boolean factor() {
        Token tokenActual = tokens.get(indice);
        if (operandos(tokenActual.getValorTablaTokens())) { // Número o identificador
            avanza();
            return true;
        } else if (tokenActual.getValorTablaTokens() == -73) { // Paréntesis (
            avanza();
            if (expresion()) {
                tokenActual = tokens.get(indice);
                if (tokenActual.getValorTablaTokens() == -74) { // Paréntesis )
                    avanza();
                    return true;
                } else {
                    error("Se esperaba ')' después de la expresión. Lexema: " + tokenActual.getLexema() + ", Línea: "
                            + tokenActual.getNumeroLinea());
                    return false;
                }
            } else {
                error("Error en la expresión dentro del paréntesis. Lexema: " + tokenActual.getLexema() + ", Línea: "
                        + tokenActual.getNumeroLinea());
                return false;
            }
        } else if (tokenActual.getValorTablaTokens() == -43) {
            avanza();
            tokenActual = tokens.get(indice);
            if (identificador(tokenActual.getValorTablaTokens())) {
                avanza();
                return true;
            } else {
                error("Se esperaba un identificador '." + ", Línea: " + tokenActual.getNumeroLinea());
                return false;
            }
        } else {
            error("Se esperaba un factor válido. Lexema: " + tokenActual.getLexema() + ", Línea: "
                    + tokenActual.getNumeroLinea());
            return false;
        }
    }

    private void ifEstructura() {
        avanza();
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -73)
            error("Se esperaba '(' en la línea " + tokenActual.getNumeroLinea());
        condicion();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -74)
            error("Se esperaba ')' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -16)
            error("Se esperaba la palabra clave 'then' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -2)
            error("Se esperaba la palabra clave 'begin' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        estructuraSentencias();
        avanza();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() == -7)
            elseEstructura();
    }

    private void comprobarSentencias(Token tokenActual) {
        switch (tokenActual.getValorTablaTokens()) {
            case -4 -> lectura(); // Esto verifica la estructura de un read
            case -5 -> writeEstructura(); // Esto verifica la estructura de un write
            case -6 -> ifEstructura();
            case -7 -> error("Se esperaba un 'if' en la línea " + tokenActual.getNumeroLinea()); // Esto verifica la estructura
            // de un else
            case -8, -9 -> repetitivas(); // Esto verifica la estructura de un while
            case -51, -52, -53, -54 -> asignacion(); // Esto verifica la estructura de una asignacion
            default -> error("Se esperaba una sentencia válida en la línea " + tokenActual.getNumeroLinea());
        }
    }

    private void condicion() {
        avanza();
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() == -43 && identificador(proximoToken.getValorTablaTokens())) {
            avanza();
            tokenActual = tokens.get(indice);
        } else if (tokenActual.getValorTablaTokens() == -43 && !identificador(proximoToken.getValorTablaTokens())) {
            error("Se esperaba un identificador en la línea " + tokenActual.getNumeroLinea());
        }
        if (identificador(tokenActual.getValorTablaTokens()) || isConstante(tokenActual.getValorTablaTokens())) {
            avanza();
            tokenActual = tokens.get(indice);
            if (isOperando(tokenActual.getValorTablaTokens()))
                aritmetica();
            if (isLogical(tokenActual.getValorTablaTokens()))
                logica();
        } else if (tokenActual.getValorTablaTokens() == -73) {
            condicion();
            avanza();
            tokenActual = tokens.get(indice);
            if (tokenActual.getValorTablaTokens() == -74)
                return;
            if (tokenActual.getValorTablaTokens() == -73) {
                condicion();
                avanza();
                tokenActual = tokens.get(indice);
            }
        } else {
            error("Se esperaba un identificador o una constante en la línea " + tokenActual.getNumeroLinea());
        }
        if (tokenActual.getValorTablaTokens() == -74)
            avanza();
    }

    private void aritmetica() {
        Token tokenActual;
        avanza();
        tokenActual = tokens.get(indice);
        if (!identificador(tokenActual.getValorTablaTokens()) && !isConstante(tokenActual.getValorTablaTokens()))
            error("Se esperaba un identificador o una constante en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (isOperando(tokenActual.getValorTablaTokens())) {
            aritmetica();
            return;
        }
        if (tokenActual.getValorTablaTokens() != -35)
            error("Se esperaba un '==' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (!identificador(tokenActual.getValorTablaTokens()) && !isConstante(tokenActual.getValorTablaTokens()))
            error("Se esperaba un identificador o una constante en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (isOperando(tokenActual.getValorTablaTokens()))
            aritmetica();
        if (isLogical(tokenActual.getValorTablaTokens()))
            logica();
    }

    public void logica() {
        Token tokenActual;
        avanza();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() == -43) {
            avanza();
            tokenActual = tokens.get(indice);
            if (!identificador(tokenActual.getValorTablaTokens()))
                error("Se esperaba un identificador en la línea " + tokenActual.getNumeroLinea());
        }
        if (tokenActual.getValorTablaTokens() == -73) {
            condicion();
            avanza();
            return;
        }
        if (!identificador(tokenActual.getValorTablaTokens()) && !isConstante(tokenActual.getValorTablaTokens()))
            error("Se esperaba un identificador o una constante en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (isLogical(tokenActual.getValorTablaTokens()))
            logica();
        if (isOperando(tokenActual.getValorTablaTokens()))
            aritmetica();
    }

    private void elseEstructura() {
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -7)
            error("Se esperaba la palabra clave 'else' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -2)
            error("Se esperaba la palabra clave 'begin' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        estructuraSentencias();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -3)
            error("Se esperaba la palabra clave 'end' en la línea " + tokenActual.getNumeroLinea());
        avanza();
    }

    ////////// Estructuras repetitivas

    private void repetitivas() {
        Token tokenActual = tokens.get(indice);
        switch (tokenActual.getValorTablaTokens()) {
            case -8:
                whileEstructura();
                break;
            case -9:
                repeatEstructura();
                break;
            default:
                error("Se esperaba una estructura repetitiva en la linea " + tokenActual.getNumeroLinea());
        }
    }

    public void whileEstructura() {
        avanza();
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() == -73) {
            condicion();
            avanza();
            tokenActual = tokens.get(indice);
            if (tokenActual.getValorTablaTokens() == -17) {
                avanza();
                tokenActual = tokens.get(indice);
                if (tokenActual.getValorTablaTokens() == -2) {
                    avanza();
                    estructuraSentencias();
                    tokenActual = tokens.get(indice);
                    if (tokenActual.getValorTablaTokens() == -3) {
                        avanza();
                        // Aqui se regresa a la condicion
                    } else
                        error("Se esperaba la palabra clave 'end' en la linea " + tokenActual.getNumeroLinea());
                } else {
                    error("Se esperaba la palabra clave 'begin' en la linea " + tokenActual.getNumeroLinea());
                }
            } else {
                error("Se esperaba la palabra clave 'do' en la linea " + tokenActual.getNumeroLinea());
            }

        }
    }

    public void repeatEstructura() {
        avanza();
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() == -2) {
            avanza();
            estructuraSentencias();
            tokenActual = tokens.get(indice);
            if (tokenActual.getValorTablaTokens() == -3) {
                avanza();
                tokenActual = tokens.get(indice);
                if (tokenActual.getValorTablaTokens() == -10) {
                    avanza();
                    tokenActual = tokens.get(indice);
                    if (tokenActual.getValorTablaTokens() == -73) {
                        condicion();
                        tokenActual = tokens.get(indice);
                        if (tokenActual.getValorTablaTokens() == -74) {
                            avanza();
                            tokenActual = tokens.get(indice);
                            if (tokenActual.getValorTablaTokens() == -75) {
                                avanza();
                            } else {
                                error("Se esperaba la palabra clave ';' en la linea " + tokenActual.getNumeroLinea());
                            }
                        } else {
                            error("Se esperaba la palabra clave ')' en la linea " + tokenActual.getNumeroLinea());
                        }
                    } else {
                        error("Se esperaba la palabra clave '(' en la linea " + tokenActual.getNumeroLinea());
                    }
                } else {
                    error("Se esperaba la palabra clave 'until' en la linea " + tokenActual.getNumeroLinea());
                }
            } else {
                error("Se esperaba la palabra clave 'end' en la linea " + tokenActual.getNumeroLinea());
            }
        } else {
            error("Se esperaba la palabra clave 'begin' en la linea " + tokenActual.getNumeroLinea());
        }
    }

    private void lectura() {
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() == -4) {
            avanza();
            tokenActual = tokens.get(indice);
            if (tokenActual.getValorTablaTokens() == -73) {
                avanza();
                tokenActual = tokens.get(indice);
                if (isPrintable(tokenActual.getValorTablaTokens())) {
                    avanza();
                    tokenActual = tokens.get(indice);
                    if (tokenActual.getValorTablaTokens() == -74) {
                        avanza();
                        tokenActual = tokens.get(indice);
                        if (tokenActual.getValorTablaTokens() == -75) {
                            avanza();
                            // Aqui se regresa a la condicion
                        } else {
                            error("Se esperaba la palabra ';' en la linea " + tokenActual.getNumeroLinea());
                        }
                    } else {
                        error("Se esperaba la palabra ')' en la linea " + tokenActual.getNumeroLinea());
                    }
                }
            } else {
                error("Se esperaba la palabra '(' en la linea " + tokenActual.getNumeroLinea());
            }
        } else {
            error("Se esperaba la palabra 'read' en la linea " + tokenActual.getNumeroLinea());
        }
    }

    private boolean isOperando(int token) {
        return token <= -21 && token >= -25;
    }

    private boolean isLogical(int token) {
        return token <= -31 && token >= -42;
    }

    private void writeEstructura() {
        avanza();
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -73)
            error("Se esperaba '(' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (!isPrintable(tokenActual.getValorTablaTokens()))
            error("Se esperaba un identificador o un literal en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -74)
            error("Se esperaba ')' en la línea " + tokenActual.getNumeroLinea());
        avanza();
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() != -75)
            error("Se esperaba ';' en la línea " + tokenActual.getNumeroLinea());
        avanza();
    }

    private void estructuraSentencias() {
        Token tokenActual = tokens.get(indice);
        comprobarSentencias(tokenActual);
        tokenActual = tokens.get(indice);
        if (tokenActual.getValorTablaTokens() == -3)
            return;
        else
            estructuraSentencias();
    }

    private boolean isPrintable(int token) {
        return identificador(token) || isConstante(token);
    }

    private boolean isConstante(int token) {
        return token >= -65 && token <= -61;
    }

    private boolean tipoDato(int token) {
        return token == -11 || token == -12 || token == -13 || token == -14;
    }

    private boolean identificador(int token) {
        return token <= -51 && token >= -54;
    }

    private boolean operandos(int token) {
        return identificador(token) || token == -61 || token == -62 || token == -63 || token == -64 || token == -65;
    }

    private boolean opAritmeticos(int token) {
        return token == -21 || token == -22 || token == -23 || token == -24 || token == -25 || token == -27;
    }

    private boolean opRelacionales(int token) {
        return token == -31 || token == -32 || token == -33 || token == -34 || token == -35 || token == -36;
    }

    private boolean opLogicos(int token) {
        return token == -41 || token == -42;
    }
}//fin d ela clase