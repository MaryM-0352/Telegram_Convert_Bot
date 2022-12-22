package src.main;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Photo {
    public static PhotoSize getPhoto(Update update){
        List<PhotoSize> photos = update.getMessage().getPhoto();
        return photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
    }

    public static boolean resizeFile(String filepath, String filename)
            throws IOException {
        File file = new File(filepath);
        BufferedImage bufferedImageInput = ImageIO.read(file);
        System.out.println("fine1 " + bufferedImageInput.getWidth() + " " + bufferedImageInput.getHeight());
        if (bufferedImageInput.getWidth() > 570 || bufferedImageInput.getHeight() > 677){
            BufferedImage bufferedImageOutput = new BufferedImage(620,
                    877, bufferedImageInput.getType());

            Graphics2D g2d = bufferedImageOutput.createGraphics();
            g2d.drawImage(bufferedImageInput, 0, 0, 620, 877, null);
            g2d.dispose();
            String formatName = filepath.substring(filepath
                    .lastIndexOf(".") + 1);

            System.out.println("fine2 " + bufferedImageOutput.getWidth() + " " + bufferedImageOutput.getHeight());
            ImageIO.write(bufferedImageOutput, formatName, new File(filepath));
            System.out.println("fine");
            return true;
        }
        return false;
    }
}
