package practica4;

public class Token {
    private String lexema;
    private int token, posicion, numeroLinea;

    public Token(String lexema, int token, int posicion, int numeroLinea){
        this.lexema = lexema;
        this.token = token;
        this.posicion = posicion;
        this.numeroLinea = numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public int getToken() {
        return token;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getLexema() {
        return lexema;
    }

    @Override
    public String toString(){
        return lexema + " " + token + " " + posicion + " " + numeroLinea;
    }
}
