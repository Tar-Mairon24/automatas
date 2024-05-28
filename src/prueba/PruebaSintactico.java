package prueba;

import sintactico.Token;

import java.util.ArrayList;

public class PruebaSintactico {
    private int puntero;
    private final ArrayList<Token> tokens;
    private int token;

    public PruebaSintactico(ArrayList<Token> tokens) {
        this.tokens = tokens;
        puntero = 0;
        token = tokens.get(puntero).getValorTablaTokens();
    }

    public boolean analizar() {
        return expresion();
    }

    public void avanzar() {
        if (puntero < tokens.size() - 1) {
            puntero++;
            token = tokens.get(puntero).getValorTablaTokens();
        }
    }

    private boolean expresion() {
        if(termino()) {
            if(operador_logico()) {
                avanzar();
            }
            return termino();
        }
        return false;
    }

    private boolean termino() {
        if(factor()) {
            if(operador_aritmetico()) {
                avanzar();
                return termino();
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean factor() {
        if(constante() || identificador()) {
            avanzar();
            return true;
        } else if(token == -73) {
            avanzar();
            if(expresion()) {
                if(token == -74) {
                    avanzar();
                    return true;
                } else {
                    System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba el caracter ')'");
                }
            }
        } else if(token == -43) {
            avanzar();
            return factor();
        } else {
            System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un identificador, constante o los caracteres '(' o '!'");
        }
        return false;
    }

    private boolean operador_logico(){
        return (token == -31 || token == -32 || token == -33 || token == -34 || token == -35 || token == -36 || token == -41 || token == -42 || token == -43);
    }

    private boolean operador_aritmetico(){
        return (token == -21 || token == -22 || token == -24 || token == -25);
    }

    private boolean constante(){
        return (token == -61 || token == -62 || token == -63 || token == -64 || token == -65);
    }

    private boolean identificador() {
        return token == -51 || token == -52 || token == -53 || token == -54;
//        else {
//                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
//                        tokens.get(puntero).getLexema() + " no es un identificador valido");
//            return false;
//        }
    }
}

