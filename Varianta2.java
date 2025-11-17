import java.io.IOException;
public class Varianta2 {
    public static void executa() {
        try {
            String path = "C:\\Windows\\System32\\notepad.exe";
            String addToStartup = "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run "
                    + "/v Laborator5 /t REG_SZ /d \"" + path + "\" /f";

            Process process = Runtime.getRuntime().exec(addToStartup);
            process.waitFor();
            System.out.println("Aplicatia a fost adaugata in pornirea automata");

            Thread.sleep(5000);

            String restartCommand = "shutdown /r /t 10";
            Runtime.getRuntime().exec(restartCommand);

            System.out.println("Sistemul va fi repornit in 10 secunde");
        } catch (IOException | InterruptedException e) {
            System.err.println("Eroare: " + e.getMessage());
            e.printStackTrace();
        }
    }
}