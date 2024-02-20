import java.io.*;
import static com.zzy.main.Main.run;

public class Main {
    public static void apache() {
        org.apache.felix.main.Main.class.getName();
    }

    // javac Main.java && jar -cvfm felix.jar META-INF/MANIFEST.MF Main.class com/zzy/main/Main.class org/apache/felix/main/Main.class
    public static void main(String []args) {
        run(args);
    }
}
