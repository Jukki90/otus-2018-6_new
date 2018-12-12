package util;

public class Utils {
    public static Integer getParameter(String[] args, String pattern) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(pattern)) {
                return Integer.valueOf(args[i + 1]);
            }
        }
        throw new RuntimeException();
    }
}
