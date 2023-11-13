import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Project-04 – filewriter
 *
 *
 * @author Gunho Park, Seth Hartzler, Isaac Shane, Tru Bui; Lab - L16
 *

 * @version Nov. 13, 2023
 *
 */
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

