package Data.Utils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageUtils {
    public static BufferedImage toBuffered(Image image) {
        if (image instanceof BufferedImage)
            return (BufferedImage) image;

        BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = (Graphics2D) temp.getGraphics();
        g2d.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);

        g2d.dispose();
        return temp;
    }

    public static Image circleImage(Image image, int size, boolean outline) {
        Objects.requireNonNull(image);

        int offset = 4;

        Image scaled = image.getScaledInstance(size - offset*2, size - offset*2, Image.SCALE_SMOOTH);

        BufferedImage temp = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Ellipse2D.Double clip = new Ellipse2D.Double(offset, offset, size - offset*2, size - offset*2);

        Graphics2D g2d = temp.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setClip(clip);

        g2d.drawImage(scaled, offset, offset, scaled.getWidth(null), scaled.getHeight(null), null);

        if (outline) {
            GradientPaint paint = new GradientPaint(
                    0, size/2f, new Color(255, 201, 14),
                    size, size/2f, new Color(246, 141, 0)
            );

            g2d.setClip(null);
            g2d.setPaint(paint);
            g2d.setStroke(new BasicStroke(4));
            g2d.draw(clip);
        }

        g2d.dispose();
        return temp;
    }

    public static Image grayScale(Image image) {
        int width = image.getWidth(null), height = image.getHeight(null);
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g2d = temp.createGraphics();

        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        return temp;
    }

    public static Image fade(Image image, float alpha) {
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("Alpha value must be between 0.0 and 1.0");
        }

        int width = image.getWidth(null), height = image.getHeight(null);
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = temp.createGraphics();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(ac);
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        return temp;
    }
}
