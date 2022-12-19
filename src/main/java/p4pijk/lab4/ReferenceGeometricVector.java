package p4pijk.lab4;

import p4pijk.lab3.BinaryTrainingMatrix;
import p4pijk.util.ImageTools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReferenceGeometricVector {

    private static final double CONST = 0.5;
    private final int[] firstGeometricVector;
    private final int[] secondGeometricVector;

    public ReferenceGeometricVector(BinaryTrainingMatrix btm) {
        firstGeometricVector = getRGV(getMean(btm.getFirstBinaryMatrix()));
        secondGeometricVector = getRGV(getMean(btm.getSecondBinaryMatrix()));
    }

    public int[] getFirstGeometricVector() {
        return firstGeometricVector;
    }

    public int[] getSecondGeometricVector() {
        return secondGeometricVector;
    }

    private double[] getMean(int[][] binaryMatrix) {
        double[] mean = new double[100];
        for (int i = 0, j = 0; i < mean.length; ++j) {
            mean[j] += binaryMatrix[i][j];
            if (j == mean.length - 1) {
                ++i;
                j = -1;
            }
        }
        for (int i = 0; i < mean.length; ++i) {
            mean[i] = mean[i] / mean.length;
        }
        return mean;
    }

    private int[] getRGV(double[] meanBinaryMatrix) {
        ArrayList<Integer> rgv = new ArrayList<>();
        for (double binaryMatrix : meanBinaryMatrix) {
            rgv.add((binaryMatrix > CONST) ? 1 : 0);
        }
        return rgv.stream().mapToInt(Integer::intValue).toArray();
    }

    public void saveDataToFile(int[] firstVector, int[] secondVector) {
        try (FileWriter result = new FileWriter(ImageTools.LAB4_PATH.value() + ImageTools.RESULT_FILE.value())) {
            result.write(getVector(firstVector, ImageTools.FIRST_GEOMETRIC_VECTOR.value()));
            result.write(getVector(secondVector, ImageTools.SECOND_GEOMETRIC_VECTOR.value()));
        } catch (IOException e) {
            System.out.println("$ Data saving was failed. Try again. $");
            throw new RuntimeException(e);
        }
        System.out.println("! Data saving was successful. Check file '" +
                ImageTools.LAB4_PATH.value() +
                ImageTools.RESULT_FILE.value() + "' !");
    }

    public String getVector(int[] vector, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================\t")
                .append(name)
                .append("\t=========================")
                .append("\n");
        for (int j : vector) {
            sb.append(j)
                    .append(" ");
        }
        sb.append("\n");
        sb.append("==================================================")
                .append("\n");
        return sb.toString();
    }
}
