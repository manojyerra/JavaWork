import java.io.File;
import java.text.NumberFormat;
import java.util.Scanner;

class Test
{
	public static void main(String args[]) throws Exception
	{
		systemInfo sInfo = new systemInfo();
		System.out.println("Info : "+sInfo.Info()+"\n\n");

		Process p=Runtime.getRuntime().exec("systeminfo");
		Scanner scan=new Scanner(p.getInputStream());
		
		//systeminfo |find "Available Physical Memory"
		
		while(scan.hasNext())
		{
			String temp=scan.nextLine();

			if(temp.indexOf("Available Physical Memory")  != -1)
			{
				String strVal = (temp.split(":")[1]).trim().split(" ")[0];
				strVal = strVal.replace(",","");
				float freeMemInMB = Float.parseFloat(strVal);
				System.out.println("Free Ram Mem : "+freeMemInMB+" MB");
				break;
			}
		}
	}
}

class systemInfo {

    private Runtime runtime = Runtime.getRuntime();

    public String Info() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.OsInfo());
        sb.append(this.MemInfo());
        sb.append(this.DiskInfo());
        return sb.toString();
    }

    public String OSname() {
        return System.getProperty("os.name");
    }

    public String OSversion() {
        return System.getProperty("os.version");
    }

    public String OsArch() {
        return System.getProperty("os.arch");
    }

    public long totalMem() {
        return Runtime.getRuntime().totalMemory();
    }

    public long usedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public String MemInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("Free memory: ");
        sb.append(format.format(freeMemory / 1024));
        sb.append("\n");
        sb.append("Allocated memory: ");
        sb.append(format.format(allocatedMemory / 1024));
        sb.append("\n");
        sb.append("Max memory: ");
        sb.append(format.format(maxMemory / 1024));
        sb.append("\n");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        sb.append("\n");
        return sb.toString();

    }

    public String OsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: ");
        sb.append(this.OSname());
        sb.append("\n");
        sb.append("Version: ");
        sb.append(this.OSversion());
        sb.append("\n");
        sb.append(": ");
        sb.append(this.OsArch());
        sb.append("\n");
        sb.append("Available processors (cores): ");
        sb.append(runtime.availableProcessors());
        sb.append("\n");
        return sb.toString();
    }

    public String DiskInfo() {
        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        StringBuilder sb = new StringBuilder();

        /* For each filesystem root, print some info */
        for (File root : roots) {
            sb.append("File system root: ");
            sb.append(root.getAbsolutePath());
            sb.append("\n");
            sb.append("Total space (bytes): ");
            sb.append(root.getTotalSpace());
            sb.append("\n");
            sb.append("Free space (bytes): ");
            sb.append(root.getFreeSpace());
            sb.append("\n");
            sb.append("Usable space (bytes): ");
            sb.append(root.getUsableSpace());
            sb.append("\n");
        }
        return sb.toString();
    }
}