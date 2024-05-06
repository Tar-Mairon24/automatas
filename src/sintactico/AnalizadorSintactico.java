package sintactico;

import lexico.Token;

import java.util.List;



public class AnalizadorSintactico {
    private int puntero;
    private final List<Token> tokens;
    private boolean ultimoToken = false;

    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
        puntero = 0;
    }

    public boolean analizar() {
        return programa();
    }

    public void avanzar() {
        if (puntero < tokens.size() - 1) {
            puntero++;
        }
        else
            ultimoToken = true;
    }

    private boolean programa() {
        if (tokens.get(puntero).getValorTablaTokens() == -1) {
            avanzar();
            if (tokens.get(puntero).getValorTablaTokens() == -55) {
                avanzar();
                if (tokens.get(puntero).getValorTablaTokens() == -75) {
                    avanzar();
                    if(tokens.get(puntero).getValorTablaTokens() == -15) {
                        avanzar();
                        if (declarativa()) {
                            avanzar();
                            if (codigo()) {
                                avanzar();
                                return true;
                            }
                        }
                    } else
                        return true;
                }else {
                    System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
                    return false;
                }
            }else {
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es un identificador valido para el programa");
                return false;
            }
        }else {
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba la palabra reservada 'program'");
                return false;
        }
        return ultimoToken && (tokens.get(puntero).getValorTablaTokens() != -3 || tokens.get(puntero).getValorTablaTokens() != -75) && puntero == 2;
    }

    private boolean declarativa() {
        if(tipo()) {
            avanzar();
            if (tokens.get(puntero).getValorTablaTokens() == -77) {
                avanzar();
                if(identificadores()) {
                    if(tokens.get(puntero).getValorTablaTokens() == -75) {
                        avanzar();
                        if(tipo()){
                            return declarativa();
                        }
                        else
                            return true;
                        }
                    } else {
                        System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        return false;
        }

    private boolean codigo(){
        if(tokens.get(puntero).getValorTablaTokens() == -2) {
            avanzar();
            if (tokens.get(puntero).getValorTablaTokens() != -3){
                return switch (tokens.get(puntero).getValorTablaTokens()) {
                    case -4 -> read();
                    case -5 -> write();
                    case -6 -> if_else();
                    case -8 -> while_();
                    case -9 -> repeat_until();
                    case -51, -52, -53, -54 -> asignacion();
                    default -> codigo();
                };
            }
            else
                return true;
        } else {
            System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba la palabra reservada 'begin'");
            return false;
        }
    }

    private boolean read() {
        return true;
    }

    private boolean write() {
        return true;
    }

    private boolean if_else() {
        return true;
    }

    private boolean while_() {
        return true;
    }

    private boolean repeat_until() {
        return true;
    }

    private boolean asignacion() {
        return true;
    }

    private boolean tipo(){
        if (tokens.get(puntero).getValorTablaTokens() == -11 || tokens.get(puntero).getValorTablaTokens() == -12 ||
                tokens.get(puntero).getValorTablaTokens() == -13 || tokens.get(puntero).getValorTablaTokens() == -14)
            return true;
        else {
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
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es un identificador valido");
            return false;
        }

    }

    private  boolean identificadores() {
        if(identificador()) {
            avanzar();
            if(tokens.get(puntero).getValorTablaTokens() == -76) {
                avanzar();
                return identificadores();
            }
            else return tokens.get(puntero).getValorTablaTokens() != -76;
        }
        else
            return false;
    }
}
