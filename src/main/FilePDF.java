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

    public static List<String> prepareText(List<String> text){
        System.out.println(text);
        List<String> result = new ArrayList<>();
        for (String s: text){
            System.out.println(s + " " + s.length());
            int lastSpace = -1;
            int next = 0;
            String fin = "";
            int charCount = 0;
            if (s.length() < 88) {
                result.add(s);
                System.out.println("Line is added: " + s);
            } else {
                for (int i = 0; i < s.length(); i++){
                    if (s.charAt(i) == ' '){
                        lastSpace = i;
                    }
                    if (charCount < 88){
                        fin += s.charAt(i);
                        charCount++;
                    } else {
                        System.out.println("Not Optimize string " + fin + " " + fin.length());
                        if (lastSpace == i){
                            result.add(fin);
                            System.out.println("Line is added: " + fin);
                            fin = "";
                            System.out.println("Next line ot work: " + fin);
                            charCount = 0;
                            next = lastSpace + 1;
                        }
                        else {
                            result.add(s.substring(next, lastSpace));
                            System.out.println("Line is added: " + s.substring(next, lastSpace));
                            fin = fin.substring(lastSpace - next + 1) + s.charAt(i);
                            next = lastSpace + 2;
                            System.out.println("Next line to work: " + fin);
                            charCount = fin.length();
                            }
                        }
                    }
                }
            }
        System.out.println("Done");
            return result;
        }

    public static Integer writeText(List<String> file, PDPageContentStream stream,
                                 int page_count, PDDocument pdf_doc) throws IOException {
        int string_count = 0;
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
        return page_count;
    }
    public static Long getTextPDF(String file_name, String key, List<String[]> dictionary, String type) throws IOException {
        List<String> text = new ArrayList<>();
        List<List<String>> text1 = new ArrayList<>();
        List<List<String>> allText = new ArrayList<>();
        if (key == null){
            if (Objects.equals(type, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
                text = ReadFile.ReadDOCXFile(file_name);
            }
            else{
                text = ReadFile.ReadTXTFile(file_name);
            }
            assert text != null;
            text = prepareText(text);
        }
        else {
            text1 = CollectGroup.collectFileGroup(dictionary);

            for (List<String> list: text1){
                list = prepareText(list);
                allText.add(list);
            }
        }
        String name = "";

        PDDocument pdf_doc = new PDDocument();
        if (Objects.equals(key, "group")){
            int page_count = 0;
            for (List<String> file: allText){
                PDPageContentStream stream = PageCreating.creatingPage(pdf_doc, page_count);
                page_count = writeText(file, stream, page_count, pdf_doc);
                stream.endText();
                stream.close();
                page_count += 1;
                System.out.println(page_count);
            }
            name = "text.txt.pdf";
        } else {
            int page_count = 0;
            PDPageContentStream stream = PageCreating.creatingPage(pdf_doc, 0);
            writeText(text,stream, page_count, pdf_doc);
            stream.endText();
            stream.close();
            name = file_name + ".pdf";
        }
        pdf_doc.save(new File(name));
        pdf_doc.close();
        System.out.println("pdf converter is final");
        return pdf_doc.getDocumentId();
    }

    public static void getPhotoPDF(List<String> names, String file_name, String key) throws IOException {
        PDDocument pdf_doc = new PDDocument();
        if (Objects.equals(key, "group")){
            int i = 0;
            for (String name: names){
                System.out.println(name);
                pdf_doc.addPage(new PDPage());
                PDPage page = pdf_doc.getPage(i);
                String imagePath = "C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot2\\src\\main\\resources\\upl_files\\" + name;
                int wigth; int heigth;
                if(Photo.resizeFile(imagePath, name)){
                    wigth = 0;
                    heigth = 0;
                } else {
                    wigth = 70;
                    heigth = 200;
                }
                PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath,pdf_doc);
                System.out.println("it is ok");
                PDPageContentStream contents = new PDPageContentStream(pdf_doc, page);

                contents.drawImage(pdImage, wigth, heigth);

                System.out.println("Image inserted");

                contents.close();
                i += 1;
            }
            pdf_doc.save("photos" + ".pdf");
            pdf_doc.close();
        } else {
            pdf_doc.addPage(new PDPage());
            PDPage page = pdf_doc.getPage(0);
            String imagePath = "C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot2\\src\\main\\resources\\upl_files\\" + file_name;
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, pdf_doc);
            PDPageContentStream contents = new PDPageContentStream(pdf_doc, page);

            contents.drawImage(pdImage, 70, 250);

            System.out.println("Image inserted");

            contents.close();
            pdf_doc.save(file_name + ".pdf");
            pdf_doc.close();
        }
    }

}
