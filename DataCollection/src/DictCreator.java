import java.io.*;

public class DictCreator {

    private File file;

    public DictCreator(File f) {
        file = f;
    }

    public static final double MAX_PAGE = 459;

    public void writeDict() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            int page = 1;
            String source;
            while (!WebCopier.isEmpty(source = WebCopier.getPageSource(page))) {
                for (String row : WebCopier.getRows(source)) {
                    writer.write(WebCopier.getWordString(row));
                    writer.newLine();
                }
                page++;
                System.out.println((float) (page / MAX_PAGE * 100) + "% completed");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DictCreator dc = new DictCreator(new File("data.txt"));
        dc.writeDict();

        System.out.println("\ndone");
    }
}
