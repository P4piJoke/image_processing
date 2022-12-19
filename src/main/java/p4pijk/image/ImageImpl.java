package p4pijk.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class ImageImpl implements Image {

    BufferedImage image;
    int width;
    int height;
    byte[] pixels;

    public ImageImpl(File image) throws IOException {
        this.image = ImageIO.read(image);
        width = this.image.getWidth();
        height = this.image.getHeight();
        pixels = ((DataBufferByte) this.image.getRaster().getDataBuffer()).getData();
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setPixels(byte[] pixels) {
        this.pixels = pixels;
    }

    @Override
    public byte[] getPixels() {
        return pixels;
    }

    @Override
    public int[][] getPixelsMatrix() {
        int[][] pixelsMatrix = new int[height][width];

        int i = 0;
        for (int row = 0; row < height; row++){
            for (int col = 0; col < width; col++){
                if (pixels[i] < 0){
                    pixelsMatrix[row][col] = pixels[i++] + 256;
                }
                else{
                    pixelsMatrix[row][col] = pixels[i++];
                }
            }
        }

        return pixelsMatrix;
    }
}
