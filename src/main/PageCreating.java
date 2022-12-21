package src.main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PageCreating {
    public static PDPageContentStream creatingPage(PDDocument pdf_doc, int count) throws IOException {
        pdf_doc.addPage(new PDPage());
        PDPage page = pdf_doc.getPage(count);
        PDPageContentStream stream = new PDPageContentStream(pdf_doc, page);
        stream.beginText();
        stream.setFont(PDType1Font.TIMES_ROMAN, 12);
        stream.setLeading(14.5f);
        stream.newLineAtOffset(40, 725);
        return stream;
    }
}
