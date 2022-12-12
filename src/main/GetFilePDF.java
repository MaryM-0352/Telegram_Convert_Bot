package src.main.java;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetFilePDF {
    public static Long GetFilePDF(String file_name, String key, List<String[]> dictionary) throws IOException {
        List<String> text = new ArrayList<>();
        List<List<String>> text1 = new ArrayList<>();
        if (key == null){
            if (ReadFile.ReadTXTFile(file_name) == null){
                System.out.println(' ');
                //text = ReadFile.ReadDOCXFile(document);
            }
            else{
                text = ReadFile.ReadTXTFile(file_name);
            }
        }
        else {
            text1 = CollectGroup.collectGroup(dictionary);
        }
        String name = "";

        PDDocument pdf_doc = new PDDocument();
        if (Objects.equals(key, "group")){
            int count = 0;
            for (List<String> file: text1){
                System.out.println("Here we go again");
                pdf_doc.addPage(new PDPage());
                PDPage page = pdf_doc.getPage(count);
                PDPageContentStream stream = new PDPageContentStream(pdf_doc, page);
                stream.beginText();
                stream.setFont(PDType1Font.TIMES_ROMAN, 12);
                stream.setLeading(14.5f);
                stream.newLineAtOffset(25, 700);
                for (String s : file) {
                    stream.showText(s);
                    stream.newLine();
                }
                stream.endText();
                stream.close();
                count += 1;
            }
            name = "text.txt.pdf";
        } else if (text != null){
            pdf_doc.addPage(new PDPage());
            PDPage page = pdf_doc.getPage(0);
            PDPageContentStream stream = new PDPageContentStream(pdf_doc, page);
            stream.beginText();
            stream.setFont(PDType1Font.TIMES_ROMAN, 12);
            stream.setLeading(14.5f);
            stream.newLineAtOffset(25, 700);
            for (String s : text) {
                stream.showText(s);
                stream.newLine();
            }
            stream.endText();
            stream.close();
            name = file_name + ".pdf";
        }

        pdf_doc.save(new File(name));
        pdf_doc.close();
        System.out.println("pdf converter is final");
        return pdf_doc.getDocumentId();
    }
}
