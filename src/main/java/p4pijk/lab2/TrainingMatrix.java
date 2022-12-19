package p4pijk.lab2;

import p4pijk.image.Image;
import p4pijk.image.ImageImpl;
import p4pijk.util.ImageTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TrainingMatrix {

    private final Image first;
    private final Image second;

    public TrainingMatrix(Image first, Image second){
        this.first = first;
        this.second = second;
    }

    public void saveDataToFile() {
        try (FileWriter result = new FileWriter(ImageTools.LAB2_PATH.value() + ImageTools.RESULT_FILE.value())) {
            result.write(getPixelMatrix(first, ImageTools.FIRST_MATRIX.value()));
            result.write(getPixelMatrix(second, ImageTools.SECOND_MATRIX.value()));
        } catch (IOException e) {
            System.out.println("$ Data saving was failed. Try again. $");
            throw new RuntimeException(e);
        }
        System.out.println("! Data saving was successful. Check file '" +
                ImageTools.LAB2_PATH.value() +
                ImageTools.RESULT_FILE.value() + "' !");
    }

    public String getPixelMatrix(Image image, String name) {
        StringBuilder sb = new StringBuilder();
        int[][] tempMatrix = image.getPixelsMatrix();
        sb.append("=========================\t")
                .append(name)
                .append("\t=========================")
                .append("\n");
        for (int i = 0; i < image.getHeight(); ++i) {
            for (int j = 0; j < image.getWidth(); ++j) {
                sb.append(tempMatrix[i][j])
                        .append(" ");
            }
            sb.append("\n");
        }
        sb.append("==================================================")
                .append("\n");
        return sb.toString();
    }
}


