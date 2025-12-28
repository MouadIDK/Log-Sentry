import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    private static final Map<String, Pattern> rules = new HashMap<>();

    static {
        rules.put("EMAIL", Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"));

        rules.put("AWS_KEY", Pattern.compile("AKIA[0-9A-Z]{16}"));

        rules.put("IP_ADDRESS", Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b"));
    }

    public static String scan(String word) {
        for (Map.Entry<String, Pattern> entry : rules.entrySet()) {
            Matcher matcher = entry.getValue().matcher(word);
            if (matcher.find()) {
                return entry.getKey();
            }
        }
        return null;
    }

}

