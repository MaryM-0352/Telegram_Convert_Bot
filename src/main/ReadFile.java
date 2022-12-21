package src.main;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    public static List<String> ReadTXTFile(String filename) throws IOException {
        Path content_path = Paths.get("C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot2" +
                                           "\\src\\main\\resources\\upl_files\\", filename);
        BufferedReader reader = Files.newBufferedReader(content_path);
        String result = "";
        int c, k = 0;
        List<String> result1 = new ArrayList<>();
        try {
            while ((c = reader.read()) != -1) {
                if (c == 9){
                    result += "    ";
                }
                else if (c != 13){
                    result += (char) c;
                }
                else{
                    result1.add(k, result);
                    k += 1;
                    result = "";
                    c = reader.read();
                }
            };
            result1.add(k, result);
            return result1;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> ReadDOCXFile(String filename) {
        List<String> text = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot2\\" +
                                          "src\\main\\resources\\upl_files\\" + filename);

            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                text.add(para.getText());
            }
            fis.close();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
