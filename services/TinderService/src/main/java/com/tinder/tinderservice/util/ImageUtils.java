package com.tinder.tinderservice.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageUtils {

    /**
     * Resize and compress image
     * @param file
     * @param maxWidth
     * @param maxHeight
     * @param quality
     * @return
     * @throws IOException
     */

    public static byte[] compressImage(MultipartFile file, int maxWidth, int maxHeight, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(file.getInputStream())
                .size(maxWidth, maxHeight)
                .outputQuality(quality)
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

}
