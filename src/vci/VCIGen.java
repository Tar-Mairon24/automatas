package vci;

import utils.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class VCIGen {
    private ArrayList<Token> vci;
    private Stack<Token> operadores;
    private Stack<Token> direcciones;
    private Stack<Token> estatutos;
    public VCIGen() {
        this.vci = new ArrayList<>();
        this.operadores = new Stack<>();
        this.direcciones = new Stack<>();
        this.estatutos = new Stack<>();
    }

    public ArrayList<Token> getVci() {
        return vci;
    }
    
    // Método principal para generar el VCI
    public void generarVCI(ArrayList<Token> tokens) {
        boolean IOFlag = false, inCode = false;
        boolean isFirstToken = true;
        int valor;
        for (Token token : tokens) {
            valor = token.getValorTablaTokens();
            if (inCode) {
                if (isFirstToken && valor == -6) {
                    direcciones.push(token);
                    vci.add(token);
                    isFirstToken = false;
                    continue;
                }
                isFirstToken = false;

                if (isConstante(valor) || identificador(valor)) {
                    vci.add(token);
                }
                if (opAritmeticos(valor) || valor == -26) {
                    if (operadores.isEmpty()) {
                        operadores.push(token);
                    } else {
                        if (getPrioridad(valor) > getPrioridad(operadores.peek().getValorTablaTokens())) {
                            operadores.push(token);
                        } else {
                            while (getPrioridad(valor) <= getPrioridad(operadores.peek().getValorTablaTokens())) {
                                vci.add(operadores.pop());
                            }
                            operadores.push(token);
}
                    }
                }
                if (valor == -73) {
                    operadores.push(token);
                }
                if (valor == -74) {
                    while (operadores.peek().getValorTablaTokens() != -73) {
                        vci.add(operadores.pop());
                    }
                    operadores.pop();
                }
                if (valor == -75) {
                    while (!operadores.isEmpty()) {
                        vci.add(operadores.pop());
                    }
                }
                if (valor == -4 || valor == -5) {
                    estatutos.push(token);
                    IOFlag = true;
                }
                if (IOFlag) {
                    if (isPrintable(valor)) {
                        vci.add(estatutos.pop());
                        IOFlag = false;
                    }
                }
                // Manejo de la sentencia if
                if (valor == -6) {
                    direcciones.push(token);
                    estatutos.push(token);
                    vci.add(token); // Agregar el token if al VCI
                }
                // Manejo de la sentencia else
                if (valor == -7) {
                    if (!direcciones.isEmpty() && direcciones.peek().getValorTablaTokens() == -6) {
                        while (!direcciones.isEmpty() && direcciones.peek().getValorTablaTokens() != -6) {
                            vci.add(direcciones.pop());
                        }
                        direcciones.push(token);
                        vci.add(token); // Agregar el token else al VCI
                    } else {
                        // Error: else sin if correspondiente
                        System.err.println("Error: 'else' sin 'if' correspondiente.");
                    }
                }

                // Validación y movimiento de direcciones
                if (valor == -3) {
                    while (!direcciones.isEmpty() && direcciones.peek().getValorTablaTokens() != -6) {
                        vci.add(direcciones.pop());
                    }
                    if (!direcciones.isEmpty()) {
                        vci.add(direcciones.pop());
                    }
                }
            }
            if (valor == -2) {
                inCode = true;
            }
        }

        guardarVCI("src/build/salida.vci", vci);
    }

    private int getPrioridad(int token){
        switch (token){
            case -21, -22, -27 -> {
                return 60;
            }
            case -24, -25 -> {
                return 50;
            }
            case -31, -32, -33, -34, -35, -36 -> {
                return 40;
            }
            case -43 -> {
                return 30;
            }
            case -41 -> {
                return 20;
            }
            case -42 -> {
                return 10;
            }
            case -26, -73 -> {
                return 0;
            }
            default -> {
                return -1;
            }
        }
    }

    private boolean opAritmeticos(int token) {
        return token == -21 || token == -22 || token == -23 || token == -24 || token == -25 || token == -27;
    }

    private boolean isPrintable(int token) {
        return identificador(token) || isConstante(token);
    }

    private boolean isConstante(int token) {
        return token >= -65 && token <= -61;
    }

    private boolean identificador(int token) {
        return token <= -51 && token >= -54;
    }

    public static void guardarVCI(String archivoSalida, ArrayList<Token> vci) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
            for (Token token : vci) 
                bw.write(token.getLexema() + "\n");
            System.out.println("VCI guardado en 'salida.vci'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}