package semantico;

import java.util.ArrayList;

import utils.Token;

public class AnalizadorSemantico {
    ArrayList<Token> tokens;
    ArrayList<Simbolo> simbolos;
    Direccion programa;
    boolean salirDeclaratoria = false;
    int indice;
    Token proximoToken;
    String ambito;

    public AnalizadorSemantico(ArrayList<Token> tokens){
        this.tokens = tokens;
        indice = 0;
        proximoToken = null;
    }

    public void analizar(){
        Token tokenActual = tokens.get(indice);
        boolean entero = false, real = false, string = false, bool = false;
        while(tokenActual.getValorTablaTokens() != -15){
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
                                            0, 0, 0, null, ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador de enteros para tipo int (debe terminar con &) en linea:" + tokenActual.getNumeroLinea());
                }
            }
            
            //checar que los identificadores sean de tipo real antes de agregarlos a la tabla de simbolos
            if(tokenActual.getEsIdentificador() == -2 && real){ 
                if(tokenActual.getValorTablaTokens() == -52){
                    simbolo = new Simbolo(tokenActual.getLexema(), tokenActual.getValorTablaTokens(),
                                            0, 0, 0, null, ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador de reales para tipo real (debe terminar con %) en linea:" + tokenActual.getNumeroLinea());
                }
            }

            //checar que los identificadores sean de tipo string antes de agregarlos a la tabla de simbolos
            if(tokenActual.getEsIdentificador() == -2 && string){ 
                if(tokenActual.getValorTablaTokens() == -52){
                    simbolo = new Simbolo(tokenActual.getLexema(), tokenActual.getValorTablaTokens(),
                                            0, 0, 0, null, ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador de cadenas para tipo string (debe terminar con %) en linea:" + tokenActual.getNumeroLinea());
                }
            }

            //checar que los identificadores sean de tipo boolean antes de agregarlos a la tabla de simbolos
            if(tokenActual.getEsIdentificador() == -2 && bool){ 
                if(tokenActual.getValorTablaTokens() == -52){
                    simbolo = new Simbolo(tokenActual.getLexema(), tokenActual.getValorTablaTokens(),
                                            0, 0, 0, null, ambito);
                    simbolos.add(simbolo);
                } else {
                    error("usar un identificador boolean para tipo bool (debe terminar con %) en linea:" + tokenActual.getNumeroLinea());
                }
            }

            if(tokenActual.getEsIdentificador() == -77){
                entero = false;
                real = false;
                string = false;
                bool = false;
            }

            avanza();
        }
    }

    private boolean tipoDato(int token) {
        return token == -11 || token == -12 || token == -13 || token == -14;
    }

    private void avanza() {
        if (indice < tokens.size()) {
            indice++;
            if (indice < tokens.size() - 1)
                proximoToken = tokens.get(indice + 1);
            else
                proximoToken = null;
        } else {
            proximoToken = null; // Ya no hay más tokens para analizar
        }
    }

    private void error(String mensaje) {
        System.out.println((char) 27 + "[31m" + "ERROR SEMANTICO! " + mensaje);
        System.exit(1);
    }


}
