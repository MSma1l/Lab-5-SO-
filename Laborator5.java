import java.util.Scanner;

public class Laborator5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int optiune;
        System.out.println("1. Porneste monitorizarea sistemului (SystemMonitor)");
        System.out.println("2. Executa varianta2 (adaugare in Startup + restart)");
        System.out.print("Alege optiunea: ");

        optiune = scanner.nextInt();

        switch (optiune) {
            case 1:
                SystemMonitor.startMonitoring();
                System.out.println("Apasa ENTER pentru a opri monitorizarea...");
                try { System.in.read(); } catch (Exception e) {}
                SystemMonitor.stopMonitoring();
                break;

            case 2:
                Varianta2.executa();
                break;

            default:
                System.out.println("Optiune invalida!");
        }

        scanner.close();
    }
}
