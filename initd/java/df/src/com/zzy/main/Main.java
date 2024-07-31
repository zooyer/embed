package com.zzy.main;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Main {
    // 获取本程序文件位置
    private static String thisPath() {
        return org.apache.felix.main.Main.class.getProtectionDomain().getCodeSource().getLocation().toString();
    }

    // 获取本程序文件目录
    private static String thisDir() {
        // 获取本程序文件位置
        String location = thisPath();

        // 获取本程序路径、文件名
        int start = location.indexOf(":") + 1;
        int end = location.lastIndexOf(File.separator);

        return location.substring(start, end);
    }

    // 获取本程序文件名称
    private static String thisFile() {
        // 获取本程序文件位置
        String location = thisPath();

        // 获取本程序路径、文件名
        int start = location.indexOf(":") + 1;
        int end = location.lastIndexOf(File.separator);

        return location.substring(end + 1);
    }

    // 获取java二进制路径
    public static String javaBin() {
        return String.join(File.separator, System.getProperty("java.home"), "bin", "java");
    }

    // 获取进程ID：从JVM名称中获取
    private static int getProcessIDByJVM() {
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

    // 获取进程ID：启动第三方进程，在第三方进程中获取父级pid并输出到stdout返回
    private static int getProcessIDByCmd() throws IOException, InterruptedException {
        String cmd = String.join(File.separator, thisDir(), "getppid");
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

    // 获取进程ID：从/proc/self/stat中获取
    private static int getProcessIDByStat() throws IOException {
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

    // 从输入流中读取字符串
    public static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    // 向文件追加字符串内容
    public static void appendFile(String filename, String data) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
        out.write(data);
        out.close();
    }

    // 读取文件内容为字符串
    public static String readFile(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream s = new FileInputStream(file);

        String data = readFromInputStream(s);

        s.close();

        return data;
    }

    // 阻塞等待执行init.sh初始化脚本
    public static void execInitSH() throws IOException, InterruptedException {
        // 启动Hook初始化init脚本
        String initSH = String.join(File.separator, thisDir(), "init.sh");
        Runtime.getRuntime().exec(initSH).waitFor();
    }

    // 获取Jar包主class名称
    private static String getJarMainClassName(String jarPath) throws IOException {
        try (JarFile jarFile = new JarFile(jarPath)) {
            Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                Attributes attributes = manifest.getMainAttributes();
                return attributes.getValue(Attributes.Name.MAIN_CLASS);
            }
        }
        return null;
    }

    // 文件路径拼接
    private static String pathJoin(String... paths) {
        return String.join(File.separator, paths);
    }

    // 记录日志
    private static void log(String line) throws IOException {
        appendFile(pathJoin("/usr/data/zzy", "init.log"), line + "\n");
    }

    // 执行流程1：
    // 1.阻塞等待执行init.sh脚本执行完毕。
    // 2. 通过JVM获取运行参数，获取java二进制绝对路径拼接到运行参数中，把*-native.jar拼接到运行参数。
    public static void run1(String[] args) throws IOException, InterruptedException {
        // 启动Hook初始化init脚本
        execInitSH();

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
        // 获取本程序路径、文件名
        String thisDir = thisDir();
        String thisFile = thisFile();

        // Hook前原程序名
        String nativeFile = thisFile.replaceAll(".jar", "-native.jar");

        // 获取java进程启动jvm命令行参数
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        // java进程参数（JVM参数）
        List<String> javaArgs = bean.getInputArguments();

        // 获取java目录
        String javaHome = System.getProperty("java.home");

        // 组合运行参数
        List<String> nativeArgs = new ArrayList<>(Collections.singletonList(javaHome + "/bin/java"));
        // java参数
        nativeArgs.addAll(javaArgs);
        // jar参数
        nativeArgs.addAll(Arrays.asList("-jar", String.join(File.separator, thisDir, nativeFile)));
        nativeArgs.addAll(Arrays.asList(args));

        // 生产要执行的命令行
        String[] cmdline = nativeArgs.toArray(new String[0]);
        System.out.println(Arrays.toString(cmdline));

        Process process = Runtime.getRuntime().exec(cmdline);
        process.waitFor();
    }

    // 执行流程2：
    // 1. 阻塞等待执行init.sh脚本执行完毕。
    // 2. 获取当前进程ID，通过/proc/self/stat中获取。
    // 3. 解析/proc/当前进程ID/cmdline，获取运行参数。（这里可以考虑直接从/proc/self/cmdline获取）
    // 4. 把运行参数中的*.jar文件替换成*-native.jar，如果第0个参数是java，则获取java绝对路径执行修改完的运行参数。
    public static void run2(String[] args) throws IOException, InterruptedException {
        execInitSH();

        int pid = getProcessIDByStat();

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
            }
        }

        if (Objects.equals(fields[0], "java")) {
            fields[0] = javaBin();
        }

        Process process = Runtime.getRuntime().exec(fields);
        process.waitFor();
    }

    // 执行流程3：
    // 1. 阻塞等待执行init.sh脚本执行完毕。
    // 2. 获取
    public static void run3(String[] args) throws IOException, InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 阻塞执行当前目录下init.sh脚本执行完毕。
        execInitSH();

        // 原生jar包路径
        String jarPath = pathJoin(thisDir(), thisFile().replaceAll(".jar", "-native.jar"));

        // 创建URLClassLoader来加载原生jar包
        File file = new File(jarPath);
        URL url = file.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, null);

        // 读取jar的MANIFEST.MF文件以获取Main-Class
        String mainClassName = getJarMainClassName(jarPath);
        if (mainClassName == null) {
            throw new IllegalArgumentException("Main-Class attribute is missing in " + jarPath);
        }

        // 加载jar的主类
        Class<?> mainClass = classLoader.loadClass(mainClassName);

        // 获取主类的main方法
        Method mainMethod = mainClass.getMethod("main", String[].class);

        // 调用main方法运行原生jar中的代码，传递当前的命令行参数
        mainMethod.invoke(null, (Object) args);
    }

    // 运行hook程序
    public static void run(String[] args) {
        try {
            run3(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
