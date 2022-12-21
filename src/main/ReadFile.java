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
        Path content_path = Paths.get("C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot1" +
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
        } catch (IOException e){
            return null;
        }
        return result1;
    }

    public static String ReadDOCXFile(Document document) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File("C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot1\\src\\main\\resources\\upl_files\\" + document.getFileName());
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document1 = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document1.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                System.out.println(para.getText());
                text.append(para.getText());
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text.toString();
    }
}
