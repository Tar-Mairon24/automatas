package ejecucion;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import semantico.Simbolo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import utils.Token;

public class Ejecutor {
    private Stack<Token> ejecucion;
    private ArrayList<Simbolo> simbolos;

    public Ejecutor(ArrayList<Simbolo> simbolos) {
        this.ejecucion = new Stack<>();
        this.simbolos = simbolos;
    }

    public void ejecutar(ArrayList<Token> vci) {
        for (Token token : vci) {
            if(token.getValorTablaTokens() == -4)
                read(ejecucion.pop());

            if(token.getValorTablaTokens() == -5) 
                write(ejecucion.pop());

            if(opAritmeticos(token.getValorTablaTokens())) {
                switch (token.getValorTablaTokens()) {
                    case -21:
                        multiplicacion(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -22:
                        division(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -24:
                        suma(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -25:
                        resta(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -27:
                        modulo(ejecucion.pop(), ejecucion.pop());
                        break;
                }
            }

            if(token.getValorTablaTokens() == -26){
                String valorNuevo = ejecucion.pop().getLexema();
                String simboloActualizar = ejecucion.pop().getLexema();
                actualizarValorSimbolo(simboloActualizar, valorNuevo);
            }
            
            if(isConstante(token.getValorTablaTokens()) || token.getEsIdentificador() == -2)
                ejecucion.push(token);
        }
        escribirTablaSimbolos("src/build/TablaSimbolos.dat", simbolos);
    }

    private void suma(Token token1, Token token2) {
        // Suma 2 enteros y las guarda en una variable entera
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int resultado = parseValorEntero(token2) + parseValorEntero(token1);
            ejecucion.push(new Token(String.valueOf(resultado), -61, -1, -1));
            return;
        }
        // Suma 2 reales y las guarda en una variable real
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double resultado = parseValorReal(token2) + parseValorReal(token1);
            ejecucion.push(new Token(String.valueOf(resultado), -62, -1, -1));
            return;
        }
        // Suma 2 string y las concatena en una variable string
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            String valor1 = token1.getLexema().replaceAll("\"", "");
            String valor2 = token2.getLexema().replaceAll("\"", "");
            String resultado = valor2 + valor1;
            ejecucion.push(new Token(resultado, -62, -1, -1));
            return;
        }
        // Si se tratan de sumar 2 logicos lanza una excepcion
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Caso default lanza una excepcion
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void resta(Token token1, Token token2) {
        // Resta 2 enteros y las guarda en una variable entera
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int resultado = parseValorEntero(token2) - parseValorEntero(token1);
            if(resultado < 0) {
                error("NegativeNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
                System.exit(1);
            } else {
                ejecucion.push(new Token(String.valueOf(resultado), -61, -1, -1));
            }
            return;
        }
        // Resta 2 reales y las guarda en una variable real
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -62 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double resultado = parseValorReal(token2) - parseValorReal(token1);
            ejecucion.push(new Token(String.valueOf(resultado), -62, -1, -1));
            return;
        }
        // Si se trata de restar 2 strings lanza una excepcion
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            error("StringOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Si se tratan de sumar 2 logicos lanza una excepcion
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Caso default lanza una excepcion
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    
    }

    private void multiplicacion(Token token1, Token token2) {
        // Multiplica 2 enteros y las guarda en una variable entera
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int resultado = parseValorEntero(token2) * parseValorEntero(token1);
            ejecucion.push(new Token(String.valueOf(resultado), -61, -1, -1));
            return;
        }
        // Multiplica 2 reales y las guarda en una variable real
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double resultado = parseValorReal(token2) * parseValorReal(token1);
            ejecucion.push(new Token(String.valueOf(resultado), -62, -1, -1));
            return;
        }
        // Si se trata de multiplicar 2 strings lanza una excepcion
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            error("StringOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
            return;
        }
        // Si se tratan de sumar 2 logicos lanza una excepcion
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Caso default lanza una excepcion
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }


    private void division(Token token1, Token token2) {
        // Divide 2 enteros y las guarda en una variable entera
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor1 == 0) {
                error("DivisionByZeroException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
            }
            int resultado = valor2 / valor1;
            ejecucion.push(new Token(String.valueOf(resultado), -61, -1, -1));
            return;
        }
        // Divide 2 reales y las guarda en una variable real
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double valor1 = parseValorReal(token1);
            double valor2 = parseValorReal(token2);
            if(valor1 == 0) {
                error("DivisionByZeroException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
            }
            double resultado = valor2 / valor1;
            ejecucion.push(new Token(String.valueOf(resultado), -62, -1, -1));
            return;
        }
        // Si se trata de dividir 2 strings lanza una excepcion
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            error("StringOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Si se tratan de dividir 2 logicos lanza una excepcion
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        } 
        // Caso default lanza una excepcion
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void modulo(Token token1, Token token2) {
        // Modulo 2 enteros y las guarda en una variable entera
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor1 == 0) {
                error("DivisionByZeroException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
            }
            int resultado = valor2 % valor1;
            ejecucion.push(new Token(String.valueOf(resultado), -61, -1, -1));
            return;
        }
        // Si se trata de dividir 2 reales lanza una excepcion
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            error("RealOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Si se trata de dividir 2 strings lanza una excepcion
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            error("StringOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Si se tratan de dividir 2 logicos lanza una excepcion
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        // Caso default lanza una excepcion
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void read(Token token) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String valor = reader.readLine();
            if (valor != null) {
                if(token.getValorTablaTokens() == -51) {
                    Pattern patron = Pattern.compile("^\\d+$");
                    Matcher matcher = patron.matcher(valor);
                    if(matcher.matches()) {
                        actualizarValorSimbolo(token.getLexema(), valor);
                    } else {
                        error("IntegerFormatException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
                    }
                }
                if(token.getValorTablaTokens() == -52) {
                    Pattern patron = Pattern.compile("^-?\\d+\\.\\d+$");
                    Matcher matcher = patron.matcher(valor);
                    if(matcher.matches()) {
                        actualizarValorSimbolo(token.getLexema(), valor);
                    } else {
                        error("RealNumberFormatException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
                    }
                }
                if(token.getValorTablaTokens() == -53) {
                    Pattern patron = Pattern.compile("^\".*\"$");
                    Matcher matcher = patron.matcher(valor);
                    if(matcher.matches()) {
                        actualizarValorSimbolo(token.getLexema(), valor);
                    } else {
                        error("StringFormatException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
                    }
                }
                if(token.getValorTablaTokens() == -51) {
                    if(valor.equals("true") || valor.equals("false")) {
                        actualizarValorSimbolo(token.getLexema(), valor);
                    } else {
                        error("LogicalFormatException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
                    }
                }
            } else {
            error("IOException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Token token) {
        String lexema = "";
        if(token.getValorTablaTokens() == -63) {
            lexema = token.getLexema();
            lexema = lexema.replaceAll("\"", "");
            if(lexema.equals("\\n")) {
                System.out.println();
                return;
            } else {
                System.out.print(lexema);
                return;
            }
        }
        if(token.getValorTablaTokens() == -53) {
            lexema = getValorSimbolo(token.getLexema());
            lexema = lexema.replaceAll("\"", "");
            if(lexema.equals("\\n")) {
                System.out.println();
                return;
            } else {
                System.out.print(lexema);
                return;
            }
        }
        if(token.getEsIdentificador() == -2){
            System.out.print(getValorSimbolo(token.getLexema()));
            return;
        }
        if(isConstante(token.getValorTablaTokens()))
            System.out.print(token.getLexema());
            return; 
    }

    private void actualizarValorSimbolo(String lexema, String valor) {
        for (Simbolo simbolo : simbolos) {
            if(simbolo.getID().equals(lexema)) {
                simbolo.setValor(valor);
            }
        }
    }

    private String getValorSimbolo(String lexema) {
        for (Simbolo simbolo : simbolos) {
            if(simbolo.getID().equals(lexema)) {
                return simbolo.getValor();
            }
        }
        return null;
    }

    private int parseValorEntero(Token token) {
        if(token.getValorTablaTokens() == -61)
            return Integer.parseInt(token.getLexema());
        if(token.getValorTablaTokens() == -51)
            return Integer.parseInt(getValorSimbolo(token.getLexema()));
        return -1;
    }

    private double parseValorReal(Token token) {
        if(token.getValorTablaTokens() == -62)
            return Double.parseDouble(token.getLexema());
        if(token.getValorTablaTokens() == -52)
            return Double.parseDouble(getValorSimbolo(token.getLexema()));
        return -1;
    }

    private boolean isConstante(int token) {
        return token >= -65 && token <= -61;
    }

    private boolean opAritmeticos(int token) {
        return token == -21 || token == -22 || token == -23 || token == -24 || token == -25 || token == -27;
    }

    private void error(String mensaje, String ambito) {
        System.out.println((char) 27 + "[31m" + "Exception in thread: " + ambito + "\n" + mensaje + (char) 27 + "[0m");
        System.exit(1);
    }

    public static void escribirTablaSimbolos(String archivoSalida, ArrayList<Simbolo> tablaSimbolos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
            for (Simbolo simbolo : tablaSimbolos)
                bw.write(simbolo.toString() + "\n");
            //System.out.println("Tabla de simbolos guardada en 'TablaSimbolos.txt'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
