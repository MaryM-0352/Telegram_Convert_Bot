package src.main.java;

import org.telegram.telegrambots.meta.api.objects.Document;
import java.io.IOException;

public class DownFileDocx {
    public static void DownFileDocx(Document doc) throws IOException {
        if (doc != null){
            final String fileId = doc.getFileId();
            final String fileName = doc.getFileName();
            UploadFile.uploadFile(fileName, fileId);
        }
    }
}
