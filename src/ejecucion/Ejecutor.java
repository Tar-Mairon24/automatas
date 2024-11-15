package ejecucion;

import java.util.Stack;
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
            
            if(!isEstatuto(token.getValorTablaTokens()))
                ejecucion.push(token);
        }
        escribirTablaSimbolos("src/build/TablaSimbolos.dat", simbolos);
    }

    private void read(Token token) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String valor = reader.readLine();
            if (valor != null) {
            actualizarValorSimbolo(token.getLexema(), valor);
            } else {
            System.err.println("No input available");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Token token) {
        String lexema = "";
        if(token.getValorTablaTokens() == -63 || token.getValorTablaTokens() == -53) {
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

    private boolean isEstatuto(int token) {
        return token == -4 || token == -5;
    }

    private boolean isConstante(int token) {
        return token >= -65 && token <= -61;
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
