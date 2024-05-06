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
    
    private boolean operador_logico(){
        if (tokens.get(puntero).getValorTablaTokens() == -31 || tokens.get(puntero).getValorTablaTokens() == -32 ||
                tokens.get(puntero).getValorTablaTokens() == -33 || tokens.get(puntero).getValorTablaTokens() == -34 ||
                		tokens.get(puntero).getValorTablaTokens() == -35 || tokens.get(puntero).getValorTablaTokens() == -36 ||
                				tokens.get(puntero).getValorTablaTokens() == -41 || tokens.get(puntero).getValorTablaTokens() == -42 ||
                						tokens.get(puntero).getValorTablaTokens() == -43)
            return true;
        else {
            if (!ultimo)
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es un operador logico valido");
            return false;
        }
    }

    private boolean operador_aritmetico(){
        if (tokens.get(puntero).getValorTablaTokens() == -21 || tokens.get(puntero).getValorTablaTokens() == -22 ||
                tokens.get(puntero).getValorTablaTokens() == -24 || tokens.get(puntero).getValorTablaTokens() == -25)
            return true;
        else {
            if (!ultimo)
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es un operador aritmetico valido");
            return false;
        }
    }
    
    private boolean constante(){
        if (tokens.get(puntero).getValorTablaTokens() == -61 || tokens.get(puntero).getValorTablaTokens() == -62 ||
                tokens.get(puntero).getValorTablaTokens() == -63 || tokens.get(puntero).getValorTablaTokens() == -64 ||
                		tokens.get(puntero).getValorTablaTokens() == -65)
            return true;
        else {
            if (!ultimo)
                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
                        tokens.get(puntero).getLexema() + " no es una constante valida");
            return false;
        }
    }
    
    private boolean asignacion(){
    	if(identificador() && recorrer() && !ultimo) {
    		if(tokens.get(puntero).getValorTablaTokens() == -26 && recorrer() && !ultimo) {
    			if(expresion() && !ultimo) {
    				if(tokens.get(puntero).getValorTablaTokens() == -75) {
    					return true;
    				} else {
    					if (!ultimo)
    						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
    				}
    			}
    		} else {
    			if (!ultimo)
    				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ':='");
    		}
    	}
    	return false;
    }
    
    private boolean expresion() {
    	if(termino() && !ultimo) {
    		if(operador_logico() && recorrer() && !ultimo) {
    			if(termino() && !ultimo) {
    				return true;
    			}
    		}
    	}
    	return false;
    }


	private boolean termino() {
    	if(factor() && !ultimo) {
    		if(operador_aritmetico() && recorrer() && !ultimo) {
    			return termino();
    		}
    		return true;
    	}
    	return false;
    }
    
    private boolean factor() {
    	if((constante() && recorrer() && !ultimo) || (identificador() && recorrer() && !ultimo)){
    		return true;
    	} else if(tokens.get(puntero).getValorTablaTokens() == -73 && recorrer() && !ultimo) {
    		if(expresion() && recorrer() && !ultimo) {
    			if(tokens.get(puntero).getValorTablaTokens() == -74 && recorrer() && !ultimo) {
    				return true;
    			} else {
    				if (!ultimo)
    					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ')'");
    			}
    		}
    	} else if(tokens.get(puntero).getValorTablaTokens() == -43 && recorrer() && !ultimo) {
    		if(factor()) {
    			return true;
    		}
    	} else {
    		if (!ultimo)
    			System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un '(' o un '!'");
    	}
    	return false;
    }
}
