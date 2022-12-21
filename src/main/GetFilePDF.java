package src.main;

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
            int page_count = 0;
            for (List<String> file: text1){
                int string_count = 0;
                PDPageContentStream stream = PageCreating.creatingPage(pdf_doc, page_count);
                for (String s : file) {
                    if (string_count > 43){
                        stream.endText();
                        stream.close();
                        page_count += 1;
                        stream = PageCreating.creatingPage(pdf_doc, page_count);
                        stream.showText(s);
                        stream.newLine();
                        string_count = 1;
                    } else {
                        stream.showText(s);
                        stream.newLine();
                        string_count += 1;
                    }
                }
                stream.endText();
                stream.close();
                page_count += 1;
            }
            name = "text.txt.pdf";
        } else if (text != null){
            int page_count = 0;
            int string_count = 0;
            PDPageContentStream stream = PageCreating.creatingPage(pdf_doc, 0);
            for (String s : text) {
                if (string_count > 43){
                    stream.endText();
                    stream.close();
                    page_count += 1;
                    stream = PageCreating.creatingPage(pdf_doc, page_count);
                    stream.showText(s);
                    stream.newLine();
                    string_count = 1;
                } else {
                    stream.showText(s);
                    stream.newLine();
                    string_count += 1;
                }
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
