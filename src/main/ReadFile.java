package src.main.java;

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
import java.util.List;

public class ReadFile {
    public static String ReadTXTFile(Document document) throws IOException {
        Path content_path = Paths.get("C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot1\\src\\main\\resources\\upl_files\\", document.getFileName());
        System.out.println(content_path);
        BufferedReader reader = Files.newBufferedReader(content_path);
        String result = "";
        int c;
        try {
            while ((c = reader.read()) != -1) {
                result += (char) c;
            }
            ;
        } catch (IOException e){
            return null;
        }
        return result;
    }

    public static String ReadDOCXFile(Document document) {
        String text = "";
        try {

            File file = new File("C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot1\\src\\main\\resources\\upl_files\\" + document.getFileName());
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document1 = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document1.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                text += para.getText();
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
