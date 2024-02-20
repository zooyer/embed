package com.zzy.main;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;

public class Main {
    private static String thisPath() {
        // 获取本程序文件位置
        String location = org.apache.felix.main.Main.class.getProtectionDomain().getCodeSource().getLocation().toString();

        // 获取本程序路径、文件名
        int start = location.indexOf(":") + 1;
        int end = location.lastIndexOf(File.separator);

        return location.substring(start, end);
    }

    private static String thisFile() {
        // 获取本程序文件位置
        String location = org.apache.felix.main.Main.class.getProtectionDomain().getCodeSource().getLocation().toString();

        // 获取本程序路径、文件名
        int start = location.indexOf(":") + 1;
        int end = location.lastIndexOf(File.separator);

        return location.substring(end + 1);
    }

    private static int getProcessId() {
        // Note: may fail in some JVM implementations
        // therefore fallback has to be provided

        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();

        System.out.println("jvmName:" + jvmName);

        final int index = jvmName.indexOf('@');

        if (index < 1) {
            // part before '@' empty (index = 0) / '@' not found (index = -1)
            return 0;
        }

        try {
            return Integer.parseInt(jvmName.substring(0, index));
        } catch (NumberFormatException e) {
            // ignore
        }
        return 0;
    }

    private static int getProcessId2() throws IOException, InterruptedException {
        String cmd = String.join(File.separator, thisPath(), "getppid");
        System.out.println("cmd:" + cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        process.waitFor();

        String line;
        List<String> strList = new ArrayList<String>();
        while ((line = input.readLine()) != null) {
            strList.add(line);
        }

        input.close();
        ir.close();

        System.out.println(strList);

        String output = String.join("\n", strList);

        System.out.println("output:" + output);

        return Integer.parseInt(output.trim());
    }

    private static int getProcessId3() throws IOException {
        byte[] bo = new byte[256];
        InputStream is = new FileInputStream("/proc/self/stat");
        is.read(bo);
        for (int i = 0; i < bo.length; i++) {
            if ((bo[i] < '0') || (bo[i] > '9')) {
                return Integer.parseInt(new String(bo, 0, i));
            }
        }

        return 0;
    }

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    public static void appendFile(String filename, String data) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
        out.write(data);
        out.close();
    }

    public static String readFile(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream s = new FileInputStream(file);

        String data = readFromInputStream(s);

        s.close();

        return data;
    }

    public static void initSH() throws IOException, InterruptedException {
        // 启动Hook初始化init脚本
        String initSh = String.join(File.separator, thisPath(), "init.sh");
        Runtime.getRuntime().exec(initSh).waitFor();
    }

    public static String javaBin() {
        return String.join(File.separator, System.getProperty("java.home"), "bin", "java");
    }

    public static void run1(String[] args) throws IOException, InterruptedException {
        // 获取java目录
        String javaHome = System.getProperty("java.home");

        // 获取本程序文件位置
        String location = org.apache.felix.main.Main.class.getProtectionDomain().getCodeSource().getLocation().toString();

        // 获取本程序路径、文件名
        int start = location.indexOf(":") + 1;
        int end = location.lastIndexOf(File.separator);
        String thisPath = location.substring(start, end);
        String thisFile = location.substring(end + 1);

        // Hook前原程序名
        String nativeFile = thisFile.replaceAll(".jar", "-native.jar");

        // 获取java进程启动jvm命令行参数
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List<String> javaArgs = bean.getInputArguments();

        // 启动Hook初始化init脚本
        String initSh = String.join(File.separator, thisPath, "init.sh");
        Runtime.getRuntime().exec(initSh).waitFor();

//        // 兴隆魁 - 移动光猫
//        String[] cmd = {
//                "java",
//                "-noverify",
//                "-Dfile.encoding=UTF-8",
//                "-Xcompactalways",
//                "-Dsun.zip.disableMemoryMapping=true",
//                "-Duser.timezone=GMT+8",
//                "-XX:ErrorFile=/usr/data/java_excp_log/java_error_6166.log",
//                "-Djava.security.policy=/usr/local/osgi/local/j2re/lib/security/private.policy",
//                "-Dorg.osgi.framework.security=osgi",
//                "-Xms48M",
//                "-Xmx128M",
//                "-Xss256K",
//                "-jar",
//                "/usr/local/osgi/local/osgi/felix/bin/felix.origin.jar",
//        };
//
//        // 张北 - 联通光猫
//        cmd = new String[]{
//                "java",
//                "-Xcompactalways",
//                "-Djava.net.preferIPv4Stack=true",
//                "-Duser.timezone=GMT+8",
//                "-Djava.security.policy=/usr/local/osgi/local/j2re/lib/security/private.policy",
//                "-Dorg.osgi.framework.security=osgi",
//                "-Xms48M",
//                "-Xmx128M",
//                "-Xss256K",
//                "-jar",
//                "/usr/local/osgi/local/osgi/felix/bin/felix.origin.jar",
//        };
//
//        List<String> list = new ArrayList<>(Arrays.asList(cmd));
//        list.addAll(Arrays.asList(args));
//
//        cmd = list.toArray(new String[0]);

        // 拼接原程序启动参数
        List<String> nativeArgs = new ArrayList<>(Collections.singletonList(javaHome + "/bin/java"));
        nativeArgs.addAll(javaArgs);
        nativeArgs.addAll(Arrays.asList("-jar", String.join(File.separator, thisPath, nativeFile)));
        nativeArgs.addAll(Arrays.asList(args));

        // 生产要执行的命令行
        String[] cmdline = nativeArgs.toArray(new String[0]);
        System.out.println(Arrays.toString(cmdline));

        Process process = Runtime.getRuntime().exec(cmdline);
        process.waitFor();
    }

    public static void run2(String[] args) throws IOException, InterruptedException {
        initSH();

        int pid = getProcessId3();

        String cmdline = readFile(String.join(File.separator, "/proc", Integer.toString(pid), "cmdline"));
        cmdline = cmdline.trim();
        String[] fields = cmdline.split("\0");

        for (int i = fields.length - 1; i >= 0; i--) {
            String field = fields[i];
            if (field.endsWith(".jar")) {
                int index = field.lastIndexOf(File.separator);
                if (index < 0) {
                    index = 0;
                }

                String path = field.substring(0, index);
                String nativeFilename = field.substring(index + 1, field.length() - 4) + "-native.jar";
                fields[i] = String.join(File.separator, path, nativeFilename);
                break;
            }
        }

        if (Objects.equals(fields[0], "java")) {
            fields[0] = javaBin();
        }

        Process process = Runtime.getRuntime().exec(fields);
        process.waitFor();
    }

    public static void run(String[] args) {
        try {
            run2(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
