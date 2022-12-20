package p4pijk.lab5;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import p4pijk.lab3.BinaryTrainingMatrix;
import p4pijk.lab4.ReferenceGeometricVector;
import p4pijk.util.ImageTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CodeDistances {

    private final int dc;

    private final double[] sk1;
    private final double[] sk2;
    private final double[] skPara1;
    private final double[] skPara2;
    private final double[] sk;
    private final double[] skPara;
    private final double[] x;
    private final double[] xX;
    private final double[] y;
    private final double[] yY;

    public CodeDistances(BinaryTrainingMatrix btm, ReferenceGeometricVector rgv) {
        dc = getSum(doABS(doMinus(rgv.getFirstGeometricVector(),
                rgv.getSecondGeometricVector())));

        sk1 = setSK(rgv.getFirstGeometricVector(),
                btm.getFirstBinaryMatrix());
        sk2 = setSK(rgv.getFirstGeometricVector(),
                btm.getSecondBinaryMatrix());

        skPara1 = setSK(rgv.getSecondGeometricVector(),
                btm.getSecondBinaryMatrix());
        skPara2 = setSK(rgv.getSecondGeometricVector(),
                btm.getFirstBinaryMatrix());

        sk = createSK(sk1, sk2);
        skPara = createSK(skPara1, skPara2);

        x = setX(sk1, skPara2);
        xX = setX(skPara1, sk2);

        y = setY(sk1, x);
        doReverse(50, 100, y);
        yY = setY(skPara1, x);
        doReverse(50, 100, yY);
    }

    public void createScatterPlot() throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries centres = new XYSeries("Centres");
        centres.add(0, 0, true);
        centres.add(dc, 0, true);

        dataset.addSeries(fillData("Red", x, y));
        dataset.addSeries(centres);
        dataset.addSeries(fillData("Green", dcMinusX(), yY));

        JFreeChart scatterPlot = ChartFactory.createScatterPlot(
                "Code Distances", // Chart title
                "X", // X-Axis Label
                "Y", // Y-Axis Label
                dataset // Dataset for the Chart
        );

        ChartUtils.saveChartAsPNG(new File(
                        ImageTools.LAB5_PATH.value() + "scatterplot.png"),
                scatterPlot,
                800,
                600);
    }

    private double[] dcMinusX() {
        double[] temp = new double[xX.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = dc - xX[i];
        }
        return temp;
    }

    private XYSeries fillData(String name, double[] x, double[] y) {
        XYSeries data = new XYSeries(name);
        for (int i = 0; i < x.length; i++) {
            data.add(x[i], y[i]);
        }
        return data;
    }

    public int getDc() {
        return dc;
    }

    public double[] getSk1() {
        return sk1;
    }

    public double[] getSk2() {
        return sk2;
    }

    public double[] getSkPara1() {
        return skPara1;
    }

    public double[] getSkPara2() {
        return skPara2;
    }

    public double[] getSk() {
        return sk;
    }

    public double[] getSkPara() {
        return skPara;
    }

    public double[] getX() {
        return x;
    }

    public double[] getxX() {
        return xX;
    }

    public double[] getY() {
        return y;
    }

    public double[] getyY() {
        return yY;
    }

    private double[] setY(double[] first, double[] second) {
        double[] result = new double[first.length];
        for (int i = 0; i < result.length; i++) {
            double val = Math.pow(first[i], 2) - Math.pow(second[i], 2);
            if (val < 0) {
                result[i] = 0;
            } else {
                result[i] = Math.sqrt(val);
            }
        }
        return result;
    }

    private void doReverse(int start, int end, double[] src) {
        for (int i = start - 1; i < end; i++) {
            src[i] = -src[i];
        }
    }

    private double[] setX(double[] first, double[] second) {
        double[] result = new double[first.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (Math.pow(first[i], 2) - Math.pow(second[i], 2) + Math.pow(dc, 2)) / (2 * dc);
        }
        return result;
    }

    public double[] setSK(int[] vector, int[][] binaryMatrix) {
        ArrayList<Double> sk = new ArrayList<>();
        for (int i = 0; i < vector.length; ++i) {
            sk.add((double) getSum(doABS(doMinus(vector, binaryMatrix[i]))));
        }

        return sk.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public double[] setSK(int[][] binaryMatrix, int[] vector) {
        ArrayList<Double> sk = new ArrayList<>();
        for (int[] matrix : binaryMatrix) {
            sk.add((double) getSum(doMinus(doABS(matrix), vector)));
        }

        return sk.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private double[] createSK(double[] firstSK, double[] secondSK) {
        double[] resultedSK = new double[firstSK.length + secondSK.length];
        System.arraycopy(firstSK, 0, resultedSK, 0, firstSK.length);
        System.arraycopy(secondSK, 0, resultedSK, secondSK.length, secondSK.length);
        return resultedSK;
    }

    private int getSum(int[] array) {
        int sum = 0;
        for (int val : array) {
            sum += val;
        }
        return sum;
    }

    private int[] doABS(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = Math.abs(array[i]);
        }
        return array;
    }

    private int[] doMinus(int[] first, int[] second) {
        int[] result = new int[first.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = first[i] - second[i];
        }
        return result;
    }

    public void saveDataToFile(double[] sk, double[] skPara, String name) {
        String skName, skParaName;
        switch (name) {
            case "1":
                skName = ImageTools.FIRST_SK.value();
                skParaName = ImageTools.FIRST_SK_PARA.value();
                break;
            case "2":
                skName = ImageTools.SECOND_SK.value();
                skParaName = ImageTools.SECOND_SK_PARA.value();
                break;
            default:
                skName = ImageTools.BOTH_SK.value();
                skParaName = ImageTools.BOTH_SK_PARA.value();
        }

        try (FileWriter result = new FileWriter(ImageTools.LAB5_PATH.value()
                + ImageTools.RESULT_FILE.value(), true)) {
            result.write(getSKAsString(sk, skName));
            result.write(getSKAsString(skPara, skParaName));
        } catch (IOException e) {
            System.out.println("$ Data saving was failed. Try again. $");
            throw new RuntimeException(e);
        }
        System.out.println("! Data saving was successful. Check file '" +
                ImageTools.LAB5_PATH.value() +
                ImageTools.RESULT_FILE.value() + "' !");
    }

    public String getSKAsString(double[] sk, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================\t")
                .append(name)
                .append("\t=========================")
                .append("\n");
        for (double v : sk) {
            sb.append(v)
                    .append(" ");
        }
        sb.append("\n");
        sb.append("==================================================")
                .append("\n");
        return sb.toString();
    }
}
