package src.main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.util.List;

public class GetPhotoPDF {
    public static void GetPhotoPDF(String file_name) throws IOException {

        PDDocument pdf_doc = new PDDocument();
        pdf_doc.addPage(new PDPage());
        PDPage page = pdf_doc.getPage(0);
        System.out.println("Page is created");
        String imagePath = "C:\\Users\\Maria\\IdeaProjects\\Telegram_Convert_Bot2\\src\\main\\resources\\upl_files\\" + file_name;
        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath,pdf_doc);
        System.out.println("Page is created2");
        PDPageContentStream contents = new PDPageContentStream(pdf_doc, page);

        contents.drawImage(pdImage, 70, 250);

        System.out.println("Image inserted");

        //Closing the PDPageContentStream object
        contents.close();

        //Saving the document
        pdf_doc.save(file_name + ".pdf");

        //Closing the document
        pdf_doc.close();
    }
}
