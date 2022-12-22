package src.main;

import com.lowagie.text.pdf.PdfEncodings;
import com.spire.pdf.graphics.PdfFont;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class PageCreating {
    public static PDPageContentStream creatingPage(PDDocument pdf_doc, int count) throws IOException {
        pdf_doc.addPage(new PDPage());
        PDPage page = pdf_doc.getPage(count);
        PDPageContentStream stream = new PDPageContentStream(pdf_doc, page);
        stream.beginText();
        //PDFont font = PDType0Font.load( pdf_doc, new File( "fonts/VREMACC_.TTF" ) );
        //System.out.println(font);
        stream.setFont(PDType1Font.TIMES_ROMAN, 12);
        //stream.setFont(font, 12);
        stream.setLeading(14.5f);
        stream.newLineAtOffset(40, 725);
        return stream;
    }
}
