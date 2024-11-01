package semantico;

public class Simbolo {
    String id;
    int token;  
    int valor;
    int d1;
    int d2;
    String ptr;
    String ambito;

    public Simbolo(String id, int token, int valor, int d1, int d2, String ptr, String ambito) {
        this.id = id;
        this.token = token;  
        this.valor = valor;
        this.d1 = d1;
        this.d2 = d2;
        this.ptr = ptr;
        this.ambito = ambito;
    }

    @Override
    public String toString() {
        return id + "\t" + token + "\t" + valor + "\t" + d1 + "\t" + d2 + "\t" + ptr + "\t" + ambito;
    }
}
