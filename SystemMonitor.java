import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemMonitor {
    private static Thread monitorThread;
    private static volatile boolean running = false;

    public static void startMonitoring() {
        if (running) return;
        running = true;

        monitorThread = new Thread(() -> {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            System.out.println("Monitorizare pornită\n");

            while (running) {
                try {
                    afiseaza(osBean);
                    Thread.sleep(5000);
                } catch (InterruptedException e) { break; }
            }
            System.out.println("Monitorizare oprită\n");
        });

        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    public static void stopMonitoring() {
        running = false;
        if (monitorThread != null) monitorThread.interrupt();
    }

    public static SystemStats getStats() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        return new SystemStats(
            osBean.getCpuLoad() * 100,
            osBean.getSystemCpuLoad() * 100,
            osBean.getTotalMemorySize() - osBean.getFreeMemorySize(),
            osBean.getFreeMemorySize(),
            osBean.getTotalMemorySize()
        );
    }

    private static void afiseaza(OperatingSystemMXBean osBean) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        double cpu = osBean.getSystemCpuLoad() * 100;
        long free = osBean.getFreeMemorySize();
        long total = osBean.getTotalMemorySize();
        double memPercent = ((total - free) * 100.0) / total;

        System.out.printf("[%s] CPU: %5.1f%% %s | RAM: %5.1f%% (%s / %s) %s\n",
            time, cpu, bar(cpu), memPercent, format(total - free), format(total), bar(memPercent));
    }

    private static String format(long bytes) {
        double gb = bytes / (1024.0 * 1024 * 1024);
        return String.format("%.1f GB", gb);
    }

    private static String bar(double percent) {
        int filled = (int)(percent / 5);
        if (filled > 20) filled = 20;
        return "[" + "█".repeat(filled) + " ".repeat(20 - filled) + "]";
    }

    public static class SystemStats {
        public final double cpuProcess, cpuSystem;
        public final long usedMemory, freeMemory, totalMemory;

        public SystemStats(double cpuProcess, double cpuSystem, long used, long free, long total) {
            this.cpuProcess = cpuProcess;
            this.cpuSystem = cpuSystem;
            this.usedMemory = used;
            this.freeMemory = free;
            this.totalMemory = total;
        }

        @Override
        public String toString() {
            return String.format("CPU: %.1f%% | RAM: %.1f%%", cpuSystem, (usedMemory * 100.0) / totalMemory);
        }
    }
}
