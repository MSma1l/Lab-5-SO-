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
            OperatingSystemMXBean osBean =
                    ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            System.out.println("Monitorizare pornită.\n");

            while (running) {
                try {
                    displayStats(osBean);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }

            System.out.println("\nMonitorizare oprită.");
        });

        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    public static void stopMonitoring() {
        running = false;
        if (monitorThread != null) monitorThread.interrupt();
    }

    public static SystemStats getStats() {
        OperatingSystemMXBean osBean =
                ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        long freeMem = osBean.getFreePhysicalMemorySize();
        long totalMem = osBean.getTotalPhysicalMemorySize();
        long usedMem = totalMem - freeMem;

        return new SystemStats(
                osBean.getProcessCpuLoad() * 100,
                osBean.getSystemCpuLoad() * 100,
                usedMem,
                freeMem,
                totalMem
        );
    }

    private static void displayStats(OperatingSystemMXBean osBean) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

        double cpu = osBean.getSystemCpuLoad() * 100;

        long free = osBean.getFreePhysicalMemorySize();
        long total = osBean.getTotalPhysicalMemorySize();
        long used = total - free;

        double memPercent = (used * 100.0) / total;

        System.out.printf("[%s] CPU: %5.1f%% %s | RAM: %5.1f%% (%s / %s) %s\n",
                time,
                cpu,
                bar(cpu),
                memPercent,
                format(used),
                format(total),
                bar(memPercent));
    }

    private static String format(long bytes) {
        double gb = bytes / (1024.0 * 1024 * 1024.0);
        return String.format("%.1f GB", gb);
    }

    private static String bar(double percent) {
        int filled = (int)(percent / 5);
        if (filled > 20) filled = 20;
        return "[" + "█".repeat(filled) + " ".repeat(20 - filled) + "]";
    }

    public static class SystemStats {

        public final double cpuProcess;
        public final double cpuSystem;
        public final long usedMemory;
        public final long freeMemory;
        public final long totalMemory;

        public SystemStats(double cpuProcess, double cpuSystem, long used, long free, long total) {
            this.cpuProcess = cpuProcess;
            this.cpuSystem = cpuSystem;
            this.usedMemory = used;
            this.freeMemory = free;
            this.totalMemory = total;
        }

        @Override
        public String toString() {
            double memPercent = (usedMemory * 100.0) / totalMemory;
            return String.format("CPU: %.1f%% | RAM: %.1f%%", cpuSystem, memPercent);
        }
    }
}