package sintactico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Token;

public class practica5 {
    public static void main(String[] args) {
        // Inicializamos el analizador sintáctico
        AnalizadorSintactico analizadorSintactico;

        // Creamos un ArrayList para almacenar los tokens
        ArrayList<Token> tokens = new ArrayList<>();

        // Definimos la ruta al archivo que contiene los tokens
        String ruta = "src/lexico/TablaTokens.txt";

        // Creamos un objeto File con la ruta especificada
        File file = new File(ruta);

        // Usamos una declaración try-with-resources para abrir el archivo y leer su contenido
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            // Declaramos un objeto Token y una cadena para contener cada línea del archivo
            Token token;
            String line;

            // Leemos el archivo línea por línea
            while ((line = br.readLine()) != null) {
                // Usamos una expresión regular para dividir la línea en partes, respetando las comillas
                Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
                Matcher matcher = pattern.matcher(line);
                
                List<String> parts = new ArrayList<>();
                while (matcher.find()) {
                    if (matcher.group(1) != null) {
                        // Parte entre comillas
                        parts.add(matcher.group(1));
                    } else {
                        // Parte sin comillas
                        parts.add(matcher.group(2));
                    }
                }

                // Creamos un nuevo objeto Token con las partes y lo añadimos a la lista de tokens
                token = new Token(parts.get(0), Integer.parseInt(parts.get(1)), Integer.parseInt(parts.get(2)), Integer.parseInt(parts.get(3)));
                tokens.add(token);
            }
        } catch (IOException e) {
            // Manejamos cualquier IOException que pueda ocurrir durante las operaciones de archivo
            System.out.println("Ocurrió un error");
        }

        // Creamos un nuevo analizador sintáctico con la lista de tokens
        analizadorSintactico = new AnalizadorSintactico(tokens);
        analizadorSintactico.analizar();
    }
}

