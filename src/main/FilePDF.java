package src.main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilePDF {
    public static Long getTextPDF(String file_name, String key, List<String[]> dictionary, String type) throws IOException {
        List<String> text = new ArrayList<>();
        List<List<String>> text1 = new ArrayList<>();
        if (key == null){
            if (Objects.equals(type, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
                text = ReadFile.ReadDOCXFile(file_name);
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

    public static void getPhotoPDF(String file_name) throws IOException {
        PDDocument pdf_doc = new PDDocument();
        pdf_doc.addPage(new PDPage());
        PDPage page = pdf_doc.getPage(0);
        String imagePath = "C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot2\\src\\main\\resources\\upl_files\\" + file_name;
        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath,pdf_doc);
        PDPageContentStream contents = new PDPageContentStream(pdf_doc, page);

        contents.drawImage(pdImage, 70, 250);

        System.out.println("Image inserted");

        contents.close();
        pdf_doc.save(file_name + ".pdf");
        pdf_doc.close();
    }

}