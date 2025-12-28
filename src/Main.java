import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        String path = "./test.log";
        readFile(path);
    }

    public static void readFile(String path){
        try {
            Files.lines(Path.of(path)).forEach(line ->{
                String[] words = line.split(" ");

                for(String word:words){
                    if( word.length() < 8) continue;
                    if (word.toLowerCase().endsWith(".pdf") ||
                            word.toLowerCase().endsWith(".jpg") ||
                            word.toLowerCase().endsWith(".png")) continue;
                    // --- LAYER 1: REGEX SCAN ---
                    String regexMatch = RegexChecker.scan(word);
                    if (regexMatch != null) {
                        System.out.println("🚨 [REGEX DETECTED] Type: " + regexMatch);
                        System.out.println("   > Value: " + word);
                        System.out.println("   > Line: " + line.trim());
                        System.out.println("------------------------------------------------");
                        continue;
                    }

                    double score = EntropyUtil.calculate(word);
                    double requiredScore = getDynamicThreshold(word.length());

                    if (score > requiredScore) {
                        System.out.println("⚠️ [ENTROPY DETECTED] High Randomness (Score: " + String.format("%.2f", score) + ")");
                        System.out.println("   > Value: " + word);
                        System.out.println("   > Line: " + line.trim());
                        System.out.println("------------------------------------------------");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static double getDynamicThreshold(int length) {
        // Base threshold for an 8-char word
        double base = 3.2;

        // Increase requirement by 0.1 for every extra character
        double dynamic = base + (length - 8) * 0.1;

        // Cap the maximum requirement at 4.4 so super-long secrets aren't ignored
        return Math.min(4.4, dynamic);
    }
}