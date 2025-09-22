// Archivo: CasinoDados.java
import java.util.InputMismatchException;
import java.util.Scanner;

public class CasinoDados {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Casino casino = new Casino();

        System.out.println(" ¡Bienvenidos al Casino de Dados! ");

        int n = 0;
        while (n < 2 || n > 4) {
            try {
                System.out.print("¿Cuántos jugadores participarán? (2-4): ");
                n = scanner.nextInt();
                if (n < 2 || n > 4) {
                    System.out.println("Por favor, ingrese un número entre 2 y 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.next(); // Limpiar el buffer
            }
        }
        scanner.nextLine();

        for (int i = 1; i <= n; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            
            int tipo = 0;
            while (tipo < 1 || tipo > 3) {
                 try {
                    System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP): ");
                    tipo = scanner.nextInt();
                     if (tipo < 1 || tipo > 3) {
                         System.out.println("Por favor, ingrese 1, 2 o 3.");
                     }
                 } catch (InputMismatchException e) {
                     System.out.println("Entrada inválida. Por favor, ingrese un número.");
                     scanner.next(); // Limpiar el buffer
                 }
            }
            scanner.nextLine();

            Jugador jugador = casino.crearJugador(nombre, tipo);
            casino.agregarJugador(jugador);
        }

        casino.jugar(scanner);

        scanner.close();
    }
}