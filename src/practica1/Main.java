package practica1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	public static void main(String[] args) throws IOException {
		Scanner lector = new Scanner(System.in); 
		int opcion;

		String menu= "Menu \n";
		menu += "1.  Registrar persona \n";
        menu += "2.  Terminar  \n";
        ArrayList<Persona> arrayList = new ArrayList<Persona>();
        Write write = new Write();

		do{
            System.out.println(menu);
            System.out.print("Opcion del menu: ");
            opcion = lector.nextInt();
            lector.nextLine();
            switch(opcion)
            {
                case 1: System.out.print("Ingresar Nombre/s: ");
                    	String nombre=lector.next();
						System.out.print("Ingresar Apellido Paterno: ");
                    	String apellidoP=lector.next();
						System.out.print("Ingresar Apellido Materno: ");
                    	String apellidoM=lector.next();
						System.out.print("Ingresar Edad: ");
                    	String edad=lector.next();
						Persona per = new Persona(nombre, apellidoP, apellidoM, edad);
                        arrayList.add(per);
                     	break;
                case 2:
                     System.out.println("\nFin del Programa" );
                     break;
                default:
                    System.out.println("\nOpción no válida");
                    break;
            }
        }while (opcion != 2);

        for (Persona persona : arrayList) {
            //System.out.println(persona);
            write.escribir(persona);
        }
        write.cerrarBuffer();
    }
}
