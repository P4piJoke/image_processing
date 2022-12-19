package p4pijk.image;

import java.awt.image.BufferedImage;

public interface Image {

    void setImage(BufferedImage image);
    BufferedImage getImage();
    void setWidth(int width);
    void setHeight(int height);
    int getWidth();
    int getHeight();

    void setPixels(byte[] pixels);
    byte[] getPixels();

    int[][] getPixelsMatrix();
}
