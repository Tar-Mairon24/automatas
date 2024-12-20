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
        //iteramos por todos los tokens del VCI
        for (int i = 0; i < vci.size(); i++) {
            //si el token es un read se ejecuta el metodo read
            if(vci.get(i).getValorTablaTokens() == -4)
                read(ejecucion.pop());

            //si el token es un write se ejecuta el metodo write
            if(vci.get(i).getValorTablaTokens() == -5) 
                write(ejecucion.pop());

            //si el token es un operador aritmetico se ejecuta la operacion correspondiente
            if(opAritmeticos(vci.get(i).getValorTablaTokens()) || opRelacionales(vci.get(i).getValorTablaTokens()) || opLogicos(vci.get(i).getValorTablaTokens())) {
                switch (vci.get(i).getValorTablaTokens()) {
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
                    case -31:
                        menorQue(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -32:
                        menorIgualQue(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -33:
                        mayorQue(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -34:
                        mayorIgualQue(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -35:
                        igualQue(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -36:
                        diferenteQue(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -41:
                        comparacionAnd(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -42:
                        comparacionOr(ejecucion.pop(), ejecucion.pop());
                        break;
                    case -43:
                        negacion(ejecucion.pop());
                        break;
                }
            }

            //si el token es un if se comprueba el resultado de la condicion y se decide si se salta o no
            if(vci.get(i).getValorTablaTokens() == -6) {
                Token direccion = ejecucion.pop();
                Token condicion = ejecucion.pop();
                if(condicion.getValorTablaTokens() == 0) {
                    i = saltarDireccion(direccion, vci);
                    continue;
                }
            }
            
            //si el token es un else se salta el else
            if(vci.get(i).getValorTablaTokens() == -7) {
                Token direccion = ejecucion.pop();
                i = saltarDireccion(direccion, vci);
            }

            if(vci.get(i).getValorTablaTokens() == -8) {
                Token direccion = ejecucion.pop();
                Token condicion = ejecucion.pop();
                if(condicion.getValorTablaTokens() == 0) {
                    i = saltarDireccion(direccion, vci);
                    continue;
                }
            }

            if(vci.get(i).getValorTablaTokens() == 3) {
                Token direccion = ejecucion.pop();
                i = saltarDireccion(direccion, vci);
                continue;
            }

            if(vci.get(i).getValorTablaTokens() == 4) {
                Token direccion = ejecucion.pop();
                Token condicion = ejecucion.pop();
                if(condicion.getValorTablaTokens() == 1) {
                    i = saltarDireccion(direccion, vci);
                    continue;
                }
            }

            //si el token es un operador de asignacion se actualiza el valor del simbolo
            if(vci.get(i).getValorTablaTokens() == -26){
                String valorNuevo = ejecucion.pop().getLexema();
                String simboloActualizar = ejecucion.pop().getLexema();
                actualizarValorSimbolo(simboloActualizar, valorNuevo);
            }
            
            //si el token es una constante o un identificador se agrega a la pila de ejecucion
            if(isConstante(vci.get(i).getValorTablaTokens()) || vci.get(i).getEsIdentificador() == -2 || vci.get(i).getValorTablaTokens() == 0)
                ejecucion.push(vci.get(i));
        }
        escribirTablaSimbolos("src/build/TablaSimbolos.dat", simbolos);
        terminarPrograma();
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
            String valor1 = "", valor2 = "", resultado = "";
            if(token1.getEsIdentificador() == -2) {
                valor1 = getValorSimbolo(token1.getLexema());
            }
            if(token2.getEsIdentificador() == -2) {
                valor2 = getValorSimbolo(token2.getLexema());
            }
            if(token1.getEsIdentificador() == -1) {
                valor1 = token1.getLexema().replaceAll("\"", "");
            }
            if(token2.getEsIdentificador() == -1) {
                valor2 = token2.getLexema().replaceAll("\"", "");
            }
            resultado = valor2 + valor1;
            ejecucion.push(new Token("\"" + resultado + "\"", -63, -1, -1));
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
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
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

    private void menorQue(Token token1, Token token2){
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        // Compara 2 enteros y las guarda en token
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor2 < valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 reales y las guarda en token
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double valor1 = parseValorReal(token1);
            double valor2 = parseValorReal(token2);
            if(valor2 < valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 strings y las guarda en token
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            String valor1 = token1.getLexema().replaceAll("\"", "");
            String valor2 = token2.getLexema().replaceAll("\"", "");
            if(valor2.compareTo(valor1) < 0) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //si los compardores son logicos da un error
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void menorIgualQue(Token token1, Token token2){
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        // Compara 2 enteros y las guarda en token
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor2 <= valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 reales y las guarda en token
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double valor1 = parseValorReal(token1);
            double valor2 = parseValorReal(token2);
            if(valor2 <= valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 strings y las guarda en token
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            String valor1 = token1.getLexema().replaceAll("\"", "");
            String valor2 = token2.getLexema().replaceAll("\"", "");
            if(valor2.compareTo(valor1) <= 0) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //si los compardores son logicos da un error
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void mayorQue(Token token1, Token token2){
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        // Compara 2 enteros y las guarda en token
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor2 > valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 reales y las guarda en token
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double valor1 = parseValorReal(token1);
            double valor2 = parseValorReal(token2);
            if(valor2 > valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 strings y las guarda en token
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            String valor1 = token1.getLexema().replaceAll("\"", "");
            String valor2 = token2.getLexema().replaceAll("\"", "");
            if(valor2.compareTo(valor1) > 0) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //si los compardores son logicos da un error
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void mayorIgualQue(Token token1, Token token2){
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        // Compara 2 enteros y las guarda en token
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor2 >= valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 reales y las guarda en token
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double valor1 = parseValorReal(token1);
            double valor2 = parseValorReal(token2);
            if(valor2 >= valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 strings y las guarda en token
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            String valor1 = token1.getLexema().replaceAll("\"", "");
            String valor2 = token2.getLexema().replaceAll("\"", "");
            if(valor2.compareTo(valor1) >= 0) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //si los compardores son logicos da un error
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void igualQue(Token token1, Token token2){
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        // Compara 2 enteros y las guarda en token
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor2 == valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 reales y las guarda en token
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double valor1 = parseValorReal(token1);
            double valor2 = parseValorReal(token2);
            if(valor2 == valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 strings y las guarda en token
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            String valor1 = token1.getLexema().replaceAll("\"", "");
            String valor2 = token2.getLexema().replaceAll("\"", "");
            if(valor2.equals(valor1)) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -65 || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -54) {
            String valor1 = token1.getLexema();
            String valor2 = getValorSimbolo(token2.getLexema());
            if(valor2.equals(valor1)) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    private void diferenteQue(Token token1, Token token2){
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        // Compara 2 enteros y las guarda en token
        if(token1.getValorTablaTokens() == -61 || token1.getValorTablaTokens() == -51 && token2.getValorTablaTokens() == -61 || token2.getValorTablaTokens() == -51) {
            int valor1 = parseValorEntero(token1);
            int valor2 = parseValorEntero(token2);
            if(valor2 != valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 reales y las guarda en token
        if(token1.getValorTablaTokens() == -62  || token1.getValorTablaTokens() == -52 && token2.getValorTablaTokens() == -62 || token2.getValorTablaTokens() == -52) {
            double valor1 = parseValorReal(token1);
            double valor2 = parseValorReal(token2);
            if(valor2 != valor1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //compara 2 strings y las guarda en token
        if(token1.getValorTablaTokens() == -63  || token1.getValorTablaTokens() == -53 && token2.getValorTablaTokens() == -63 || token2.getValorTablaTokens() == -53) {
            String valor1 = token1.getLexema().replaceAll("\"", "");
            String valor2 = token2.getLexema().replaceAll("\"", "");
            if(!valor2.equals(valor1)) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
            return;
        }
        //si los compardores son logicos da un error
        if(token1.getValorTablaTokens() == -64  || token1.getValorTablaTokens() == -54 && token2.getValorTablaTokens() == -64 || token2.getValorTablaTokens() == -54) {
            error("LogicalOperationNotSupportedExcepton at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
        }
        error("UnsupportedOperationException at line: " + token1.getNumeroLinea(), simbolos.get(0).getAmbito());
    }

    //compara con un and dos tokens logicos
    private void comparacionAnd(Token token1, Token token2) {
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        if(token1.getValorTablaTokens() == 1 || token1.getValorTablaTokens() == 0 && token2.getValorTablaTokens() == 1 || token2.getValorTablaTokens() == 0) {
            if(token1.getValorTablaTokens() == 1 && token2.getValorTablaTokens() == 1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
        }
        return;
    }

    //comprara con un or dos tokens logicos
    private void comparacionOr(Token token1, Token token2) {
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        if(token1.getValorTablaTokens() == 1 || token1.getValorTablaTokens() == 0 && token2.getValorTablaTokens() == 1 || token2.getValorTablaTokens() == 0) {
            if(token1.getValorTablaTokens() == 1 || token2.getValorTablaTokens() == 1) {
                ejecucion.push(verdadero);
            } else {
                ejecucion.push(falso);
            }
        }
        return;
    }

    //hace la negacion de un token logico
    private void negacion(Token token) {
        Token verdadero = new Token("1", 1, -1, -1);
        Token falso = new Token("0", 0, -1, -1);
        if(token.getValorTablaTokens() == 1 || token.getValorTablaTokens() == 0) {
            if(token.getValorTablaTokens() == 1) {
                ejecucion.push(falso);
            } else {
                ejecucion.push(verdadero);
            }
        }
        return;
    }

    //lee lo que se escriba en la consola y lo guarda en una variable
    private void read(Token token) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //se guarda lo de la consola en una variable
            String valor = reader.readLine();
            //se checa si el valor no es nulo
            if (valor != null) {
                //si la variable es un entero se guarda en la tabla de simbolos
                if(token.getValorTablaTokens() == -51) {
                    Pattern patron = Pattern.compile("^\\d+$");
                    Matcher matcher = patron.matcher(valor);
                    if(matcher.matches()) {
                        actualizarValorSimbolo(token.getLexema(), valor);
                    } else {
                        error("IntegerFormatException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
                    }
                }
                //si la variable es un real se guarda en la tabla de simbolos
                if(token.getValorTablaTokens() == -52) {
                    Pattern patron = Pattern.compile("^-?\\d+\\.\\d+$");
                    Matcher matcher = patron.matcher(valor);
                    if(matcher.matches()) {
                        actualizarValorSimbolo(token.getLexema(), valor);
                    } else {
                        error("RealNumberFormatException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
                    }
                }
                //si la variable es un string se guarda en la tabla de simbolos
                if(token.getValorTablaTokens() == -53) {
                    Pattern patron = Pattern.compile(".*");
                    Matcher matcher = patron.matcher(valor);
                    if(matcher.matches()) {
                        actualizarValorSimbolo(token.getLexema(), valor);
                    } else {
                        error("StringFormatException at line: " + token.getNumeroLinea(), simbolos.get(0).getAmbito());
                    }
                }
                //si la variable es un booleano se gurada en la tabla de simbolos
                if(token.getValorTablaTokens() == -54) {
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
            lexema = lexema.replaceAll("\\\\n", "\n"); // Replace escaped \n with actual newline
            System.out.print(lexema);
            return;
        }
        if(token.getValorTablaTokens() == -53) {
            lexema = token.getLexema();
            lexema = lexema.replaceAll("\"", "");
            lexema = lexema.replaceAll("\\\\n", "\n"); // Replace escaped \n with actual newline
            System.out.print(lexema);
            return;
        }
        if(token.getEsIdentificador() == -2){
            System.out.print(getValorSimbolo(token.getLexema()));
            return;
        }
        if(isConstante(token.getValorTablaTokens()))
            System.out.print(token.getLexema());
            return; 
    }

    //actualiza el valor de un simbolo
    private void actualizarValorSimbolo(String lexema, String valor) {
        for (Simbolo simbolo : simbolos) {
            if(simbolo.getID().equals(lexema)) {
                simbolo.setValor(valor);
            }
        }
    }

    //salta a la direccion que se le indique
    private int saltarDireccion(Token token, ArrayList<Token> vci) {
        int index = 0;
        index = Integer.parseInt(token.getLexema()) - 2;
        if(index >= vci.size()) {
            terminarPrograma();
        }
        return index;
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

    private boolean opRelacionales(int token) {
        return token == -31 || token == -32 || token == -33 || token == -34 || token == -35 || token == -36;
    }

    private boolean opLogicos(int token) {
        return token == -41 || token == -42 || token == -43;
    }

    private void error(String mensaje, String ambito) {
        System.out.println((char) 27 + "[31m" + "Exception in thread: " + ambito + "\n" + mensaje + (char) 27 + "[0m");
        System.out.println((char) 27 + "[31m" + "Program terminated with code 1" + ambito + "\n" + mensaje + (char) 27 + "[0m");
        System.exit(1);
    }

    private void terminarPrograma() {
        System.out.println("\n" + (char) 27 + "[32m" + "Program terminated with code 0" + (char) 27 + "[0m");
        System.exit(0);
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
