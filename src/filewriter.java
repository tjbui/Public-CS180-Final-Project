import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class filewriter {
    public filewriter(String fileName, String text) throws IOException {
        BufferedWriter br = new BufferedWriter(
                new FileWriter(fileName));
        br.write(text);
    }

    public static void main(String[] args) throws IOException {
        new filewriter("products.csv", "2\n2\n3\n2");
    }
}

