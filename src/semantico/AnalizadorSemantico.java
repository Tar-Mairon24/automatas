package semantico;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.Token;

public class AnalizadorSemantico {
    ArrayList<Token> tokens;
    ArrayList<Simbolo> simbolos;
    Direccion programa;
    boolean salirDeclaratoria = false, errorSemantico = false, enLinea = false;
    int indice;
    Token proximoToken;
    String ambito;

    public AnalizadorSemantico(ArrayList<Token> tokens){
        this.tokens = tokens;
        this.simbolos = new ArrayList<>();
        indice = 0;
        proximoToken = null;
        this.programa = null;
        this.ambito = null;
    }

    public void analizar(){
        Token tokenActual = tokens.get(indice);
        boolean entero = false, real = false, string = false, bool = false;
        while(tokenActual.getValorTablaTokens() != -2){
            tokenActual = tokens.get(indice);
            //System.out.println(tokenActual.toString());
            Simbolo simbolo;
            if(tokenActual.getValorTablaTokens() == -55){
                programa = new Direccion(tokenActual.getLexema(), -55, 
                                    tokenActual.getNumeroLinea(), 0);
                ambito = tokenActual.getLexema();
            }

            if(tipoDato(tokenActual.getValorTablaTokens())){
                switch (tokenActual.getValorTablaTokens()) {
                    case -11 -> entero = true;
                    case -12 -> real = true;
                    case -13 -> string = true;
                    case -14 -> bool = true;
                }
            }

            //checar que los identificadores sean de tipo entero antes de agregarlos a la tabla de simbolos
            if(tokenActual.getEsIdentificador() == -2 && entero){ 
                if(tokenActual.getValorTablaTokens() == -51){
                    simbolo = new Simbolo(tokenActual.getLexema(), tokenActual.getValorTablaTokens(),
                                            0, 0, 0, "", ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador de enteros para tipo int (debe terminar con &) en linea:" + tokenActual.getNumeroLinea());
                }
            }
            
            //checar que los identificadores sean de tipo real antes de agregarlos a la tabla de simbolos
            if(tokenActual.getEsIdentificador() == -2 && real){ 
                if(tokenActual.getValorTablaTokens() == -52){
                    simbolo = new Simbolo(tokenActual.getLexema(), tokenActual.getValorTablaTokens(),
                                            0, 0, 0, "", ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador de reales para tipo real (debe terminar con %) en linea:" + tokenActual.getNumeroLinea());
                }
            }

            //checar que los identificadores sean de tipo string antes de agregarlos a la tabla de simbolos
            if(tokenActual.getEsIdentificador() == -2 && string){ 
                if(tokenActual.getValorTablaTokens() == -53){
                    simbolo = new Simbolo(tokenActual.getLexema(), tokenActual.getValorTablaTokens(),
                                            0, 0, 0, "", ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador de cadenas para tipo string (debe terminar con %) en linea:" + tokenActual.getNumeroLinea());
                }
            }

            //checar que los identificadores sean de tipo boolean antes de agregarlos a la tabla de simbolos
            if(tokenActual.getEsIdentificador() == -2 && bool){ 
                if(tokenActual.getValorTablaTokens() == -54){
                    simbolo = new Simbolo(tokenActual.getLexema(), tokenActual.getValorTablaTokens(),
                                            0, 0, 0, "", ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador boolean para tipo bool (debe terminar con $) en linea:" + tokenActual.getNumeroLinea());
                }
            }

            if(tokenActual.getValorTablaTokens() == -75){
                entero = false;
                real = false;
                string = false;
                bool = false;
            }

            // Move to the next token
            indice++;
            if (indice >= tokens.size()) {
                break; // Avoid IndexOutOfBoundsException
            }
        }
        

        for(Token token : tokens){
            if(token.getEsIdentificador() == -2 ){
                if(token.getValorTablaTokens() != -55){
                    if(!isTokenInSimbolos(token.getLexema())){
                        error("variable no declarada: " + token.getLexema() + " en linea:" + token.getNumeroLinea());
                    }
                }
            }
            if(salirDeclaratoria){
                if(token.getEsIdentificador() == -2){
                    switch(token.getValorTablaTokens()){
                        case -51 -> entero = true;
                        case -52 -> real = true;
                        case -53 -> string = true; 
                        case -54 -> bool = true;
                    }
                    enLinea = true;
                }
                if(token.getValorTablaTokens() == -75){
                    entero = false;
                    real = false;
                    string = false;
                    bool = false;
                    enLinea = false;
                }
                if(enLinea){
                    if(token.getEsIdentificador() == -2 && entero){
                        if(token.getValorTablaTokens() != -51){
                            error("solo se pueden asignar identificadores tipo entero. En Linea:" + token.getNumeroLinea());
                        }
                        if(token.getValorTablaTokens() != -61 || token.getValorTablaTokens() != -62){
                            error("solo se pueden asignar numeros a identificadores tipo entero. En Linea:" + token.getNumeroLinea());
                        }
                    if(token.getValorTablaTokens() == -2 && real){
                        if(token.getValorTablaTokens() != -52){
                            error("solo se pueden asignar identificadores tipo real. En Linea:" + token.getNumeroLinea());
                        }
                        if(token.getValorTablaTokens() != -61 || token.getValorTablaTokens() != -62){
                            error("solo se pueden asignar numeros a identificadores tipo real. En Linea:" + token.getNumeroLinea());
                        }
                    }
                    if(token.getEsIdentificador() == -2 && string){
                        if(token.getValorTablaTokens() != -53){
                            error("solo se pueden asignar identificadores tipo string. En Linea:" + token.getNumeroLinea());
                        }
                        if(token.getValorTablaTokens() != -63){
                            error("solo se pueden asignar cadenas a identificadores tipo string. En Linea:" + token.getNumeroLinea());
                        }
                    }
                    if(token.getEsIdentificador() == -2 && bool){
                        if(token.getValorTablaTokens() != -54){
                            error("solo se pueden asignar identificadores tipo boolean. En Linea:" + token.getNumeroLinea());
                        }
                        if(token.getValorTablaTokens() != -64 || token.getValorTablaTokens() != -65){
                            error("solo se pueden asignar valores logicos a identificadores tipo real. En Linea:" + token.getNumeroLinea());
                        }
                    }
                    
                }
            }
        }
    }

        if(!errorSemantico){
            escribirTablaSimbolos("TablaSimbolos.txt", simbolos);
            escribirTablaDirecciones("TablaDirecciones.txt", programa);
        } else {
            System.out.println((char) 27 + "[31m" + "No se puede compilar el programa por errores semanticos");
        }
    }

    public boolean isTokenInSimbolos(String lexema) {
        for (Simbolo simbolo : simbolos) {
            if (simbolo.getID().equals(lexema)) {
                return true;
            }
        }
        return false;
    }

    private boolean tipoDato(int token) {
        return token == -11 || token == -12 || token == -13 || token == -14;
    }


    private void error(String mensaje) {
        System.out.println((char) 27 + "[31m" + "ERROR SEMANTICO! " + mensaje + (char) 27 + "[0m");
        errorSemantico = true;
        return;
    }

    public static void escribirTablaSimbolos(String archivoSalida, ArrayList<Simbolo> tablaSimbolos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
            for (Simbolo simbolo : tablaSimbolos) {
                bw.write(simbolo.toString() + "\n");
            }
            System.out.println("Tabla de simbolos guardada en 'TablaSimbolos.txt'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void escribirTablaDirecciones(String archivoSalida, Direccion direccion) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
            bw.write(direccion.toString() + "\n");
            System.out.println("Tabla de direcciones guardada en 'TablaDirecciones.txt'");
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
}
