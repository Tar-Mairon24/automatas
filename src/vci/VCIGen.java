package vci;

import utils.Token;
import utils.TokenType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class VCIGen {
    private ArrayList<Token> vci;
    private Stack<Token> operadores;
    private Stack<Token> direcciones;
    private Stack<Token> estatutos;
    private boolean IOFlag = false;

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
        boolean inCode = false;
        int valor;
        int indiceActual = 0;
        for (Token token : tokens){
            valor = token.getValorTablaTokens();
            if(inCode) {
                if(isConstante(valor) || identificador(valor)){
                    vci.add(token);
                }
                if(opAritmeticos(valor) || valor == -26){
                    if(operadores.isEmpty()){
                        operadores.push(token);
                    } else {
                        if(getPrioridad(valor) > getPrioridad(operadores.peek().getValorTablaTokens())){
                            operadores.push(token);
                        } else {
                            while(getPrioridad(valor) <= getPrioridad(operadores.peek().getValorTablaTokens())){
                                vci.add(operadores.pop());
                            }
                            operadores.push(token);
                        }
                    }
                }
                if(valor == -73){
                    operadores.push(token);
                }
                if(valor == -74){
                    while(operadores.peek().getValorTablaTokens() != -73){
                        vci.add(operadores.pop());
                    }
                    operadores.pop();
                }
                if(valor == -75) {
                    while (!operadores.isEmpty()) {
                        vci.add(operadores.pop());
                    }
                }
                // Si el token es read or write se activa una flag que nos dice que hay que agregar la siguiete
                // instruccion al VCI
                if(valor == -4 || valor == -5){
                    estatutos.push(token);
                    IOFlag = true;
               }
                if(IOFlag){
                    if(isPrintable(valor)){
                        vci.add(estatutos.pop());
                        IOFlag = false;
                    }
                }
                // parte del if
                if (valor == TokenType.PALABRAS_RESERVADAS_IF.getValorTablaTokens()) {
                    if (!procesarIf(tokens, indiceActual)) {
                        System.out.println("Error en la estructura del if");
                        IOFlag = true;
                        return;
                    }
                }
            }
            if(valor == -2){
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

    private boolean esCondicionValida(List<Token> tokens, int indiceActual) {
        //Aqui vemos la logica de expresiones logicas
        int tipoToken = tokens.get(indiceActual).getValorTablaTokens();
        return tipoToken == TokenType.IDENTIFICADORES_ID_GENERAL.getValorTablaTokens() 
            || tipoToken == TokenType.CONSTANTES_VERDADERO.getValorTablaTokens()
            || tipoToken == TokenType.CONSTANTES_FALSO.getValorTablaTokens();
    }

    private boolean esSentenciaValida(Token token) {
        // Implementar lógica de validación de una sentencia básica (ejemplo: asignación, operación, etc.)
        int valor = token.getValorTablaTokens();
        // Ejemplo básico: considerar válidos los identificadores y constantes
        return identificador(valor) || isConstante(valor) || opAritmeticos(valor);
    }

    private boolean esBloqueValido(List<Token> tokens, int indice) {
        // Lógica para validar el bloque; puede depender de la gramática específica.
        // Por ahora, asumimos que un bloque puede ser una secuencia de tokens válidos hasta un delimitador.
        while (indice < tokens.size() && tokens.get(indice).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_ELSE.getValorTablaTokens()
                && tokens.get(indice).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_END.getValorTablaTokens()) {
            // Validar cada sentencia individual
            if (!esSentenciaValida(tokens.get(indice))) {
                return false;
            }
            indice++;
        }
        return true;
    }

    private boolean procesarIf(List<Token> tokens, int indiceActual){
        // Vemos que el primer token sea if
        if(tokens.get(indiceActual).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_IF.getValorTablaTokens()){
            IOFlag = true;
            return false;
        }
        indiceActual++;

        // Validar despues que venga una condicion valida 
        if (!esCondicionValida(tokens, indiceActual)) {
            IOFlag = true;
            return false;
        }

        // Saltar token que componene la condicion
        while(indiceActual < tokens.size() && tokens.get(indiceActual).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_THEN.getValorTablaTokens()){
            indiceActual++;
        }

        // Validar then
        if(tokens.get(indiceActual).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_THEN.getValorTablaTokens()){
            IOFlag = true;
            return false;
        }
        indiceActual++;

        // Vallidar que el then en bloque sea correcto
        if(tokens.get(indiceActual).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_THEN.getValorTablaTokens()){
                IOFlag = true;
                return false;
        }

        // en caso de que haya un segundo else
        indiceActual = saltarBloque(tokens, indiceActual);

        // Validar si hay un else
        if(indiceActual < tokens.size() && tokens.get(indiceActual).getValorTablaTokens() == TokenType.PALABRAS_RESERVADAS_ELSE.getValorTablaTokens()){
            indiceActual++;
            // Validar que el else en bloque sea correcto
            if(tokens.get(indiceActual).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_ELSE.getValorTablaTokens()){
                IOFlag = true;
                return false;
            }
            // Terminamos el bloque
            indiceActual = saltarBloque(tokens, indiceActual);
        }
        // En caso de llegar aqui pues es verdadero
        return true;
    }

    private int saltarBloque(List<Token> tokens, int indice) {
        while (indice < tokens.size() && tokens.get(indice).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_ELSE.getValorTablaTokens()
                && tokens.get(indice).getValorTablaTokens() != TokenType.PALABRAS_RESERVADAS_END.getValorTablaTokens()) {
            indice++;
        }
        return indice;
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
