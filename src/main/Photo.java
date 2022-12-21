package src.main;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.List;

public class Photo {
    public static PhotoSize getPhoto(Update update){
        List<PhotoSize> photos = update.getMessage().getPhoto();
        return photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
    }
}
