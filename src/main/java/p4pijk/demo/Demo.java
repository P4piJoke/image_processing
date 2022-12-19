package p4pijk.demo;

import p4pijk.image.Image;
import p4pijk.image.ImageImpl;
import p4pijk.lab2.TrainingMatrix;
import p4pijk.lab3.BinaryTrainingMatrix;
import p4pijk.lab4.ReferenceGeometricVector;
import p4pijk.util.ImageTools;

import java.io.File;
import java.io.IOException;

public class Demo {

    public static void main(String[] args) throws IOException {
        Image firstImage = new ImageImpl(new File(ImageTools.FIRST_IMAGE.value()));
        Image secondImage = new ImageImpl(new File(ImageTools.SECOND_IMAGE.value()));

        //lab2
        TrainingMatrix trainingMatrix = new TrainingMatrix(firstImage, secondImage);

        System.out.println(trainingMatrix.getPixelMatrix(firstImage, ImageTools.FIRST_IMAGE.value()));
        System.out.println(trainingMatrix.getPixelMatrix(secondImage, ImageTools.SECOND_IMAGE.value()));
        trainingMatrix.saveDataToFile();

        // lab3
        BinaryTrainingMatrix btm = new BinaryTrainingMatrix(firstImage, secondImage, 80);

        int[][] firstBinaryMatrix = btm.getFirstBinaryMatrix();
        int[][] secondBinaryMatrix = btm.getSecondBinaryMatrix();
        btm.createNewImage(firstImage, firstBinaryMatrix, "1_binary", "bmp");
        btm.createNewImage(secondImage, secondBinaryMatrix, "2_binary", "bmp");
        btm.saveDataToFile(firstBinaryMatrix, secondBinaryMatrix);

        // lab4
        ReferenceGeometricVector rgv = new ReferenceGeometricVector(btm);

        int[] firstVector = rgv.getFirstGeometricVector();
        int[] secondVector = rgv.getSecondGeometricVector();
        rgv.saveDataToFile(firstVector, secondVector);
        rgv.getVector(firstVector, ImageTools.FIRST_GEOMETRIC_VECTOR.value());
        rgv.getVector(secondVector, ImageTools.SECOND_GEOMETRIC_VECTOR.value());

    }
}
