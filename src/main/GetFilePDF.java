package src.main.java;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.File;
import java.io.IOException;

public class GetFilePDF {
    public static Long GetFilePDF(Document document) throws IOException {
        System.out.println("You are in PDFConverter");
        String text = "";
        if (ReadFile.ReadTXTFile(document) == null){
             text = ReadFile.ReadDOCXFile(document);
        }
        else{
             text = ReadFile.ReadTXTFile(document);
        }

        System.out.println(text);
        PDDocument pdf_doc = new PDDocument();
        pdf_doc.addPage(new PDPage());
        PDPage page = pdf_doc.getPage(0);
        PDPageContentStream stream = new PDPageContentStream(pdf_doc, page);
        stream.beginText();
        stream.newLineAtOffset(25, 700);
        stream.setFont(PDType1Font.TIMES_ROMAN, 12);
        stream.showText(text);
        stream.endText();
        stream.close();



        String name = "BlankPdf.pdf";
        pdf_doc.save(new File(name));
        pdf_doc.close();
        return pdf_doc.getDocumentId();
    }
}
