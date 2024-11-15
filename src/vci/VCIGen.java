package vci;

import utils.Token;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import semantico.Simbolo;

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

    public void generarVCI(ArrayList<Token> tokens) {
        boolean IOFlag = false;
        for (Token token : tokens){
            // Si el token es read or write se activa una flag que nos dice que hay que agregar la siguiete
            // instruccion al VCI
            if(token.getValorTablaTokens() == -4 || token.getValorTablaTokens() == -5){
                estatutos.push(token);
                IOFlag = true;
           }
            if(IOFlag){
                if(isPrintable(token.getValorTablaTokens())){
                    vci.add(token);
                    vci.add(estatutos.pop());
                    IOFlag = false;
                }
            }
        }

        guardarVCI("src/build/salida.vci", vci);
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
            case -26 -> {
                return 0;
            }
            default -> {
                return -1;
            }
        }
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
