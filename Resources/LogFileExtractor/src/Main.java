import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  private static final File f = new File("c:\\ownCloud\\Logs\\log5_office.txt");

  private static final Pattern p = Pattern.compile("in (\\d+) ms");
  private static final Pattern p2 = Pattern.compile("queue size is (\\d+)");

  public static void main(String[] args) throws IOException {
    String s = new String(Files.readAllBytes(f.toPath()));
    List<String> l = new ArrayList<>();
    List<String> l2 = new ArrayList<>();
    int mean = 0;

    Matcher m = p.matcher(s);
    while (m.find()) {
      l.add(m.group(1));
      mean = mean + new Integer(m.group(1));
    }

    Matcher m2 = p2.matcher(s);
    while (m2.find()) {
      l2.add(m2.group(1));
    }

    float result = mean / l.size();

    System.out.println("Upload-Latenz (ms): " + l.toString());
    System.out.println("Warteschlange (#): " + l2.toString());
    System.out.println("Durchschnitt " + result + " ms");

  }

}
