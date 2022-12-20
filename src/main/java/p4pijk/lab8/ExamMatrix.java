package p4pijk.lab8;

import p4pijk.image.Image;
import p4pijk.lab3.BinaryTrainingMatrix;
import p4pijk.lab4.ReferenceGeometricVector;
import p4pijk.lab5.CodeDistances;
import p4pijk.lab6.InformationalCriteria;
import p4pijk.util.ImageTools;

public class ExamMatrix {

    private final int[][] matrix;
    private final int[][] binaryMatrix;
    private final double[] u1;
    private final double[] u2;


    public ExamMatrix(Image first, Image second, int delta) {
        BinaryTrainingMatrix btm = new BinaryTrainingMatrix(first, second, delta);
        ReferenceGeometricVector rgv = new ReferenceGeometricVector(btm);
        CodeDistances cd = new CodeDistances(btm, rgv);
        InformationalCriteria ic1 = new InformationalCriteria(cd, cd.getSk1(), cd.getSk2());
        InformationalCriteria ic2 = new InformationalCriteria(cd, cd.getSkPara1(), cd.getSkPara2());

        matrix = fillExamMatrix(first, second);
        binaryMatrix = setExamBinaryMatrix(btm);
        double[] sk1 = cd.setSK(binaryMatrix, rgv.getFirstGeometricVector());
        double[] sk2 = cd.setSK(binaryMatrix, rgv.getSecondGeometricVector());
        u1 = setU(sk1, ic1.getOptRadius(ImageTools.SHENON_CRITERIA));
        u2 = setU(sk2, ic2.getOptRadius(ImageTools.SHENON_CRITERIA));
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getBinaryMatrix() {
        return binaryMatrix;
    }

    public double[] getU1() {
        return u1;
    }

    public double[] getU2() {
        return u2;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("The result of checking the effectiveness of systems shows us that:");
        if (check(u1, ">", 0) && check(u2, "<", 0)) {
            sb.append(" ")
                    .append("belonging of implementations to the first class");
        } else if (check(u1, "<", 0) && check(u2, ">", 0)) {
            sb.append(" ")
                    .append("belonging of implementations to the second class");
        } else if (check(u1, "<", 0) && check(u2, "<", 0)) {
            sb.append(" ")
                    .append("does not belong to any of the classes");
        } else if (mean(u1) > 0 && mean(u2) > 0 && mean(u1) > mean(u2)) {
            sb.append(" ")
                    .append("belonging of implementations to the first class");
        } else if (mean(u1) > 0 && mean(u2) > 0 && mean(u2) > mean(u1)) {
            sb.append(" ")
                    .append("belonging of implementations to the second class");
        }
        return sb.toString();
    }

    private boolean check(double[] u, String sign, int val) {
        int count = 0;
        switch (sign) {
            case ">":
                for (double num : u) {
                    if (num > val) {
                        count++;
                    }
                }
                return u.length == count;
            case "<":
                for (double num : u) {
                    if (num < val) {
                        count++;
                    }
                }
                return u.length == count;
            default:
                return false;
        }
    }

    public double mean(double[] u) {
        double sum = 0;
        for (double val : u) {
            sum += val;
        }
        return sum / u.length;
    }

    private double[] setU(double[] sk, int[] optRadius) {
        double[] u = new double[sk.length];
        for (int i = 0; i < u.length; ++i) {
            u[i] = 1 - (sk[i] / optRadius[0]);
        }
        return u;
    }

    private int[][] fillExamMatrix(Image first, Image second) {
        int[][] matrix = new int[first.getWidth()][second.getHeight()];
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = first.getPixelsMatrix()[i][j];
            }
        }
        for (int i = 40; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = second.getPixelsMatrix()[i][j];
            }
        }
        return matrix;
    }

    private int[][] setExamBinaryMatrix(BinaryTrainingMatrix btm) {
        double[] lct = btm.getLowerControlTolerance();
        double[] uct = btm.getUpperControlTolerance();
        int[][] binaryMatrix = new int[lct.length][lct.length];
        for (int i = 0, j = 0; i < binaryMatrix.length; j++) {
            binaryMatrix[i][j] = (matrix[i][j] >= lct[j] && matrix[i][j] <= uct[j]) ? 1 : 0;
            if (j == binaryMatrix.length - 1) {
                ++i;
                j = -1;
            }
        }

        return binaryMatrix;
    }
}
