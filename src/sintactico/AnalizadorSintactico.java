package sintactico;

import lexico.Token;

import java.util.List;

public class AnalizadorSintactico {
    private int puntero;
    private final List<Token> tokens;
    private boolean ultimo = false;

    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
        puntero = 0;
    }

    public boolean analizar() {
        return programa();
    }

    public boolean recorrer() {
        if (puntero < tokens.size() - 1) {
            puntero++;
            return true;
        }
        if (puntero == tokens.size() - 1 && !ultimo) {
            ultimo = true;
        }
        return false;
    }

    private boolean programa() {
        if (tokens.get(puntero).getValorTablaTokens() == -1 && recorrer() && !ultimo) {
            if (tokens.get(puntero).getValorTablaTokens() == -55 && recorrer() && !ultimo) {
                if (tokens.get(puntero).getValorTablaTokens() == -75 && recorrer() && !ultimo) {
                    if(tokens.get(puntero).getValorTablaTokens() == -15 && recorrer() && !ultimo)
                        if (declarativa() && recorrer())
                            if (codigo() && recorrer())
                                return true;
                    return true;
                }else {
                    if (!ultimo)
                        System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
                    return false;
                }
            }else {
                if (!ultimo)
                    System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                            tokens.get(puntero).getLexema() + " no es un identificador valido para el programa");
                return false;
            }
        }else {
            if (!ultimo)
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba la palabra reservada 'program'");
            return false;
        }
    }

    private boolean declarativa() {
        if(tipo() && recorrer() && !ultimo) {
            if (tokens.get(puntero).getValorTablaTokens() == -77 && recorrer() && !ultimo) {
                if(identificadores() && !ultimo) {
                    if(tokens.get(puntero).getValorTablaTokens() == -75 && recorrer() && !ultimo) {
                        if(tipo()){
                            return declarativa();
                        }
                        else
                            return true;
                    } else {
                        if (!ultimo)
                            System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if (!ultimo)
                    System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ':'");
                return false;
            }
        }
        return false;
    }

    private boolean codigo(){
        return false;
    }

    private boolean tipo(){
        if (tokens.get(puntero).getValorTablaTokens() == -11 || tokens.get(puntero).getValorTablaTokens() == -12 ||
                tokens.get(puntero).getValorTablaTokens() == -13 || tokens.get(puntero).getValorTablaTokens() == -14)
            return true;
        else {
            if (!ultimo)
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es un tipo valido");
            return false;
        }
    }

    private boolean identificador() {
        if(tokens.get(puntero).getValorTablaTokens() == -51 || tokens.get(puntero).getValorTablaTokens() == -52
            || tokens.get(puntero).getValorTablaTokens() == -53 || tokens.get(puntero).getValorTablaTokens() == -54)
            return true;
        else {
            if (!ultimo)
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es un identificador valido");
            return false;
        }

    }

    private  boolean identificadores() {
        if(identificador() && recorrer() && !ultimo) {
            if(tokens.get(puntero).getValorTablaTokens() == -76 && recorrer() && !ultimo)
                return identificadores();
            else return tokens.get(puntero).getValorTablaTokens() != -76;
        }
        else
            return false;
    }
}
