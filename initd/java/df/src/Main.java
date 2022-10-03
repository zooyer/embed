import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Runtime.getRuntime().exec("/usr/local/osgi/local/osgi/felix/bin/init.sh").waitFor();

        String[] cmd = {
                "java",
                "-noverify",
                "-Dfile.encoding=UTF-8",
                "-Xcompactalways",
                "-Dsun.zip.disableMemoryMapping=true",
                "-Duser.timezone=GMT+8",
                "-XX:ErrorFile=/usr/data/java_excp_log/java_error_6166.log",
                "-Djava.security.policy=/usr/local/osgi/local/j2re/lib/security/private.policy",
                "-Dorg.osgi.framework.security=osgi",
                "-Xms48M",
                "-Xmx128M",
                "-Xss256K",
                "-jar",
                "/usr/local/osgi/local/osgi/felix/bin/felix.origin.jar",
        };

        List<String> list = new ArrayList<>(Arrays.asList(cmd));
        list.addAll(Arrays.asList(args));

        cmd = list.toArray(new String[0]);

        Process runtime = Runtime.getRuntime().exec(cmd);
        runtime.waitFor();
    }
}
