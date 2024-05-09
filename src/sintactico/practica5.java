package sintactico;

import java.io.*;
import java.util.ArrayList;

public class practica5 {
    public static void main(String[] args) {
        AnalizadorSintactico analizadorSintactico;
        ArrayList<Token> tokens = new ArrayList<>();

        String ruta = "src/sintactico/TablaTokens.txt";
        File file = new File(ruta);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            Token token;
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                token = new Token(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                tokens.add(token);
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error");
        }

        analizadorSintactico = new AnalizadorSintactico(tokens);
        if(analizadorSintactico.analizar()) {
            System.out.println("El programa se ha compilado correctamente");
        }
        else {
            System.out.println("No se puede compilar el programa por errores sintacticos");
        }


    }
}

