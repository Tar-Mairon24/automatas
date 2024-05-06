package sintactico;

import lexico.Token;

import java.util.List;



public class AnalizadorSintactico {
    private int puntero;
    private final List<Token> tokens;
    private boolean ultimoToken = false;
    private int token, tokenSiguiente;

    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
        puntero = 0;
        token = tokens.get(puntero).getValorTablaTokens();
        tokenSiguiente = tokens.get(puntero + 1).getValorTablaTokens();
    }

    public boolean analizar() {
        return programa();
    }

    public void avanzar() {
        if (puntero < tokens.size() - 1) {
            puntero++;
            token = tokens.get(puntero).getValorTablaTokens();
        }
        else
            ultimoToken = true;
    }

    private boolean programa() {
        if (token == -1) {
            avanzar();
            if (token == -55) {
                avanzar();
                if (token == -75) {
                    avanzar();
                    if(token == -15) {
                        avanzar();
                        if (declarativa()) {
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
        return (tokens.get(puntero).getValorTablaTokens() != -3 || tokens.get(puntero).getValorTablaTokens() != -75) && puntero == 2;
    }

    private boolean declarativa() {
        if(tipo()) {
            avanzar();
            if (token == -77) {
                avanzar();
                if(identificadores()) {
                    if(token == -75) {
                        avanzar();
                        if(token == -11 || token == -12 || token == -13 || token == -14)
                            return declarativa();
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
        if(token == -2) {
            avanzar();
            if (token != -3){
                return switch (token) {
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
        if (token == -11 || token == -12 || token == -13 || token == -14)
            return true;
        else {
            System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es un tipo valido");
            return false;
        }
    }

    private boolean identificador() {
        if(token == -51 || token == -52 || token == -53 || token == -54)
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
            if(token == -76) {
                avanzar();
                return identificadores();
            }
            else return true;
        }
        else
            return false;
    }
}
