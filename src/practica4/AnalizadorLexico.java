package practica4;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {
     private ArrayList<Linea> lineas = new ArrayList<Linea>();
     private List<Token> tablaTokens = new ArrayList<Token>();

    public AnalizadorLexico(ArrayList<Linea> lineas){
        this.lineas = lineas;
    }

    /*
    Metodo que analiza cada una de las lineas y guarda los resultados en un objeto token, los cuales se van almacenando en
    un array list de tokens para imprimirlos posteriormente
    @param es el objeto linea donde se encuentra la linea del archivo junto con su numero de linea
    @return devuelve una lista en el cual se guardan los tokens de la linea, junto con su posicion en la tabla, su numero de
        token y su numero de linea
     */
    public void analizador(Linea linea){
        //lo primero es quitarle los espacios a la linea para su analisis
        linea.setLinea(linea.getLinea().replace("\\s+", ""));

        
    }


}
