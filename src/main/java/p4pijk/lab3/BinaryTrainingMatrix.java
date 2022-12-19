package p4pijk.lab3;

import p4pijk.image.Image;
import p4pijk.util.ImageTools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BinaryTrainingMatrix {

    private final int delta;
    private final int[][] firstBinaryMatrix;
    private final int[][] secondBinaryMatrix;

    public BinaryTrainingMatrix(Image first, Image second, int delta) {
        this.delta = delta;

        double[] lowerControlTolerance = getLCT(first.getPixelsMatrix());
        double[] upperControlTolerance = getUCT(first.getPixelsMatrix());

        firstBinaryMatrix = getBinaryMatrix(first.getPixelsMatrix(), lowerControlTolerance, upperControlTolerance);
        secondBinaryMatrix = getBinaryMatrix(second.getPixelsMatrix(), lowerControlTolerance, upperControlTolerance);
    }

    public int[][] getFirstBinaryMatrix() {
        return firstBinaryMatrix;
    }

    public int[][] getSecondBinaryMatrix() {
        return secondBinaryMatrix;
    }

    public void createNewImage(Image image, int[][] matrix, String name, String format) throws IOException {
        boolean[] binaryPixels = convertMatrixToBinary(matrix);
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int a, r, g, b;
                if (binaryPixels[i++]) {
                    a = r = g = b = 255;
                } else {
                    a = r = g = b = 0;
                }
                int p = (a << 24) | (r << 16) | (g << 8) | b;

                img.setRGB(x, y, p);
            }
        }
        String fullName = name.concat(".").concat(format);
        ImageIO.write(img,
                format,
                new File(ImageTools.LAB3_PATH.value() + fullName));
    }

    private static boolean[] convertMatrixToBinary(int[][] matrix) {
        ArrayList<Boolean> tempBinary = new ArrayList<>();
        for (int[] ints : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                tempBinary.add(ints[j] > 0);
            }
        }
        boolean[] binaryMatrix = new boolean[tempBinary.size()];
        for (int i = 0; i < tempBinary.size(); i++) {
            binaryMatrix[i] = tempBinary.get(i);
        }
        return binaryMatrix;
    }

    private static int[][] getBinaryMatrix(int[][] matrix, double[] lower, double[] upper) {
        int[][] binaryMatrix = new int[lower.length][lower.length];
        for (int i = 0, j = 0; i < binaryMatrix.length; j++) {
            binaryMatrix[i][j] = (matrix[i][j] >= lower[j] && matrix[i][j] <= upper[j]) ? 1 : 0;
            if (j == binaryMatrix.length - 1) {
                ++i;
                j = -1;
            }
        }

        return binaryMatrix;
    }

    private double[] getLCT(int[][] pixelsMatrix) {
        double[] lower = new double[100];
        for (int i = 0, j = 0; i < lower.length; ++j) {
            lower[j] += pixelsMatrix[i][j];
            if (j == lower.length - 1) {
                ++i;
                j = -1;
            }
        }
        for (int i = 0; i < lower.length; ++i) {
            lower[i] = (lower[i] / lower.length) - delta;
        }
        return lower;
    }

    private double[] getUCT(int[][] pixelsMatrix) {
        double[] upper = new double[100];
        for (int i = 0, j = 0; i < upper.length; ++j) {
            upper[j] += pixelsMatrix[i][j];
            if (j == upper.length - 1) {
                ++i;
                j = -1;
            }
        }
        for (int i = 0; i < upper.length; ++i) {
            upper[i] = (upper[i] / upper.length) + delta;
        }
        return upper;
    }

    public void saveDataToFile(int[][] first, int[][] second) {
        try (FileWriter result = new FileWriter(ImageTools.LAB3_PATH.value() + ImageTools.RESULT_FILE.value())) {
            result.write(printBinaryMatrix(first, ImageTools.FIRST_BINARY_MATRIX.value()));
            result.write(printBinaryMatrix(second, ImageTools.SECOND_BINARY_MATRIX.value()));
        } catch (IOException e) {
            System.out.println("$ Data saving was failed. Try again. $");
            throw new RuntimeException(e);
        }
        System.out.println("! Data saving was successful. Check file '" +
                ImageTools.LAB3_PATH.value() +
                ImageTools.RESULT_FILE.value() + "' !");
    }

    public String printBinaryMatrix(int[][] binary, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================\t")
                .append(name)
                .append("\t=========================")
                .append("\n");
        for (int[] ints : binary) {
            for (int j = 0; j < binary.length; ++j) {
                sb.append(ints[j])
                        .append(" ");
            }
            sb.append("\n");
        }
        sb.append("==================================================")
                .append("\n");
        return sb.toString();
    }
}


