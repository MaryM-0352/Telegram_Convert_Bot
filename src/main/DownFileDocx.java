package src.main;

import org.telegram.telegrambots.meta.api.objects.Document;
import java.io.IOException;

public class DownFileDocx {
    public static void DownFileDocx(String fileName, String fileId) throws IOException {
            UploadFile.uploadFile(fileName, fileId);
    }
}
