import java.util.Scanner;
public class Laborator5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int optiune;
        System.out.println("1. Adaugă aplicație in Startup + restart sistem (Ana)");
        System.out.println("2. Porneste monitorizarea sistemului (Maxim)");
        System.out.print("Alege optiunea: ");

        optiune = scanner.nextInt();
        switch (optiune) {
            case 1:
                Varianta2.executa();
                break;
            case 2:
                SystemMonitor.startMonitoring();
                System.out.println("Apasa ENTER pentru a opri monitorizarea");
                try { System.in.read(); } catch (Exception e) {}
                SystemMonitor.stopMonitoring();
                break;
            default:
                System.out.println("Optiunea nu este valida");
        }
        scanner.close();
    }
}
