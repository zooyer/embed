import static com.zzy.main.Main.run;

// 编译命令：javac Main.java && jar -cvfm felix.jar META-INF/MANIFEST.MF Main.class com/zzy/main/Main.class org/apache/felix/main/Main.class
public class Main {
    public static void main(String []args) {
        run(args);
    }
}
