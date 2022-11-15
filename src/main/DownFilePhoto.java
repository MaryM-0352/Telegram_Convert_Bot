package src.main.java;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.util.List;

public class DownFilePhoto {
    public static void DownFilePhoto(List<PhotoSize> doc) throws IOException {
        if (doc != null){
            final String fileId = doc.get(0).getFileId();
            //final String fileId = doc.get(1).getFileId();
            final String fileName = doc.get(0).getFileUniqueId();
            UploadFile.uploadFile(fileId, fileId);
        }
    }

//херня, а не код


}
