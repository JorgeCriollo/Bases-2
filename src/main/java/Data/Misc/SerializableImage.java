package Data.Misc;

import Data.Utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerializableImage implements Serializable {
    private static final long serialVersionUID = 15L;
    private byte[] img = null;

    public SerializableImage(byte[] imageBytes) {
        img = imageBytes;
    }

    public SerializableImage(Image image) {
        if (image == null)
            img = null;
        else {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(ImageUtils.toBuffered(image), "png", baos);
                img = baos.toByteArray();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Image getImage() {
        if (img == null)
            return null;

        ByteArrayInputStream bais = new ByteArrayInputStream(img);
        try {
            BufferedImage bi = ImageIO.read(bais);
            bais.close();
            return bi;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] getBytes() {
        return img;
    }
}
