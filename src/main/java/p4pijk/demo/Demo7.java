package p4pijk.demo;

import p4pijk.image.Image;
import p4pijk.image.ImageImpl;
import p4pijk.lab2.TrainingMatrix;
import p4pijk.lab3.BinaryTrainingMatrix;
import p4pijk.lab4.ReferenceGeometricVector;
import p4pijk.lab5.CodeDistances;
import p4pijk.lab6.InformationalCriteria;
import p4pijk.lab7.SystemOptimization;
import p4pijk.util.ImageTools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Demo7 {

    public static void main(String[] args) throws IOException {
        Image firstImage = new ImageImpl(new File(ImageTools.FIRST_IMAGE.value()));
        Image secondImage = new ImageImpl(new File(ImageTools.SECOND_IMAGE.value()));
        SystemOptimization systemOptimization = new SystemOptimization(firstImage, secondImage);

        TrainingMatrix trainingMatrix = new TrainingMatrix(firstImage, secondImage);

        System.out.println(trainingMatrix.getPixelMatrix(firstImage, ImageTools.FIRST_IMAGE.value()));
        System.out.println(trainingMatrix.getPixelMatrix(secondImage, ImageTools.SECOND_IMAGE.value()));
        trainingMatrix.saveDataToFile();

        BinaryTrainingMatrix btm = new BinaryTrainingMatrix(firstImage, secondImage, systemOptimization.getOptimalDelta());

        int[][] firstBinaryMatrix = btm.getFirstBinaryMatrix();
        int[][] secondBinaryMatrix = btm.getSecondBinaryMatrix();
        btm.createNewImage(firstImage, firstBinaryMatrix, "1_binary", "bmp");
        btm.createNewImage(secondImage, secondBinaryMatrix, "2_binary", "bmp");

        System.out.println(btm.printBinaryMatrix(firstBinaryMatrix, ImageTools.FIRST_BINARY_MATRIX.value()));
        System.out.println(btm.printBinaryMatrix(secondBinaryMatrix, ImageTools.SECOND_BINARY_MATRIX.value()));
        btm.saveDataToFile(firstBinaryMatrix, secondBinaryMatrix);

        ReferenceGeometricVector rgv = new ReferenceGeometricVector(btm);

        int[] firstVector = rgv.getFirstGeometricVector();
        int[] secondVector = rgv.getSecondGeometricVector();
        rgv.saveDataToFile(firstVector, secondVector);
        rgv.getVector(firstVector, ImageTools.FIRST_GEOMETRIC_VECTOR.value());
        rgv.getVector(secondVector, ImageTools.SECOND_GEOMETRIC_VECTOR.value());

        CodeDistances cd = new CodeDistances(btm, rgv);

        double[] sk1 = cd.getSk1();
        double[] sk2 = cd.getSk2();

        double[] skPara1 = cd.getSkPara1();
        double[] skPara2 = cd.getSkPara2();

        double[] sk = cd.getSk();
        double[] skPara = cd.getSkPara();

        System.out.println(cd.getSKAsString(sk1, ImageTools.FIRST_SK.value()));
        System.out.println(cd.getSKAsString(skPara1, ImageTools.FIRST_SK_PARA.value()));
        System.out.println(cd.getSKAsString(sk2, ImageTools.SECOND_SK.value()));
        System.out.println(cd.getSKAsString(skPara2, ImageTools.SECOND_SK_PARA.value()));
        System.out.println(cd.getSKAsString(sk, ImageTools.BOTH_SK.value()));
        System.out.println(cd.getSKAsString(skPara, ImageTools.BOTH_SK_PARA.value()));
        Files.deleteIfExists(Paths.get(ImageTools.LAB5_PATH.value()
                + ImageTools.RESULT_FILE.value()));
        cd.saveDataToFile(sk1, skPara1, "1");
        cd.saveDataToFile(sk2, skPara2, "2");
        cd.saveDataToFile(sk, skPara, "");
        cd.createScatterPlot();

        InformationalCriteria ic1 = new InformationalCriteria(cd, cd.getSk1(), cd.getSk2());

        int[] robObl1 = ic1.getRobObl();

        double[] shenon1 = ic1.getCriteria(ImageTools.SHENON_CRITERIA);

        double[] kulbak1 = ic1.getCriteria(ImageTools.KULBAK_CRITERIA);

        ic1.saveArea(shenon1, robObl1, ImageTools.SHENON_CRITERIA.value() + "1",
                ImageTools.SHENON_CRITERIA.value() + "'s",
                ImageTools.SHENON_CRITERIA.value() + "1.png");
        ic1.saveArea(kulbak1, robObl1, ImageTools.KULBAK_CRITERIA.value() + "1",
                ImageTools.KULBAK_CRITERIA.value() + "'s",
                ImageTools.KULBAK_CRITERIA.value() + "1.png");

        InformationalCriteria ic2 = new InformationalCriteria(cd, cd.getSkPara1(), cd.getSkPara2());

        int[] robObl2 = ic2.getRobObl();

        double[] shenon2 = ic2.getCriteria(ImageTools.SHENON_CRITERIA);

        double[] kulbak2 = ic2.getCriteria(ImageTools.KULBAK_CRITERIA);

        ic1.saveArea(shenon2, robObl2, ImageTools.SHENON_CRITERIA.value() + "2",
                ImageTools.SHENON_CRITERIA.value() + "'s",
                ImageTools.SHENON_CRITERIA.value() + "2.png");
        ic1.saveArea(kulbak2, robObl2, ImageTools.KULBAK_CRITERIA.value() + "2",
                ImageTools.KULBAK_CRITERIA.value() + "'s",
                ImageTools.KULBAK_CRITERIA.value() + "2.png");
    }
}
