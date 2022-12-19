package p4pijk.lab6;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import p4pijk.lab3.BinaryTrainingMatrix;
import p4pijk.lab4.ReferenceGeometricVector;
import p4pijk.lab5.CodeDistances;
import p4pijk.util.ImageTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class InformationalCriteria {

    private static double[] r = setR();
    private static double[] k1;
    private static double[] k2;
    private static double[] d1;
    private static double[] d2;
    private static double[] beta;
    private static double[] alfa;

    public static void main(String[] args) throws IOException {

        // Doted characteristics 1
        double[] sk1 = CodeDistances.getSK(
                ReferenceGeometricVector.getFirstGeometricVector(),
                BinaryTrainingMatrix.getFirstBinaryMatrix());
        double[] sk2 = CodeDistances.getSK(
                ReferenceGeometricVector.getFirstGeometricVector(),
                BinaryTrainingMatrix.getSecondBinaryMatrix());

        k1 = setK(sk1);
        k2 = setK(sk2);

        d1 = setD1OrBeta(k1);
        beta = setD1OrBeta(k2);
        alfa = setD2OrAlfa(d1);
        d2 = setD2OrAlfa(beta);

        double[] sum1 = setSum(alfa, alfa, d2);
        double[] sum2 = setSum(d2, alfa, d2);
        double[] sum3 = setSum(beta, beta, d1);
        double[] sum4 = setSum(d1, beta, d1);

        // Shenon 1
        double[] e1 = setE(sum1, sum2, sum3, sum4);
        int[] rob_obl1 = setRobObl(d1, d2, r, CodeDistances.getDc());
        double emax1 = setMax(e1, rob_obl1);
        int[] opt_radius = setOptRadius(e1, emax1);

        // Kulbak 1
        double[] kulb1 = setKulbak(d1, d2, alfa, beta);
        double kul_max1 = setMax(kulb1, rob_obl1);
        int[] opt_radius_kul1 = setOptRadius(kulb1, kul_max1);

        saveArea(e1, rob_obl1, "Shenon1","Shenon's","Shenon1.png");
        saveArea(kulb1, rob_obl1, "Kulbak1","Kulbak's","Kulbak1.png");

        // Doted characteristics 1
        double[] skPara1 = CodeDistances.getSK(ReferenceGeometricVector.getSecondGeometricVector(),
                BinaryTrainingMatrix.getSecondBinaryMatrix());
        double[] skPara2 = CodeDistances.getSK(ReferenceGeometricVector.getSecondGeometricVector(),
                BinaryTrainingMatrix.getFirstBinaryMatrix());

        k1 = setK(skPara1);
        k2 = setK(skPara2);

        d1 = setD1OrBeta(k1);
        beta = setD1OrBeta(k2);
        alfa = setD2OrAlfa(d1);
        d2 = setD2OrAlfa(beta);

        double[] sum5 = setSum(alfa, alfa, d2);
        double[] sum6 = setSum(d2, alfa, d2);
        double[] sum7 = setSum(beta, beta, d1);
        double[] sum8 = setSum(d1, beta, d1);

        // Shenon 2
        double[] e2 = setE(sum5, sum6, sum7, sum8);
        int[] rob_obl2 = setRobObl(d1, d2, r, CodeDistances.getDc());
        double emax2 = setMax(e2, rob_obl2);
        int[] opt_radius2 = setOptRadius(e2, emax2);

        // Kulbak 2
        double[] kulb2 = setKulbak(d1, d2, alfa, beta);
        double kul_max2 = setMax(kulb2, rob_obl2);
        int[] opt_radius_kul2 = setOptRadius(kulb2, kul_max2);

        saveArea(e2, rob_obl2, "Shenon2","Shenon's","Shenon2.png");
        saveArea(kulb2, rob_obl2, "Kulbak2","Kulbak's","Kulbak2.png");
    }

    private static void saveArea(double[] arr, int[] rob_obl, String... names) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < arr.length; i++) {
            dataset.addValue(arr[i], names[0] + "_all", "" + i + 1);
        }
        for (int i : rob_obl) {
            dataset.addValue(arr[i], names[0] + "_mid", "" + i + 1);
        }

        JFreeChart areaChart = ChartFactory.createAreaChart(
                "Containers' optimization area", // Chart title
                names[1] + " criteria", // X-Axis Label
                "Container's radius", // Y-Axis Label
                dataset // Dataset for the Chart
        );

        ChartUtils.saveChartAsPNG(new File(ImageTools.LAB6_PATH.value() + names[2]), areaChart, 600, 400);
    }

    private static int[] setOptRadius(double[] arr, double max) {
        ArrayList<Integer> tempOptRadius = new ArrayList<>();
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] == max) {
                tempOptRadius.add(i);
            }
        }
        return tempOptRadius.stream().mapToInt(Integer::intValue).toArray();
    }

    private static double setMax(double[] e, int[] rob_obl1) {
        double tempMax = e[rob_obl1[0]];
        for (int i : rob_obl1) {
            tempMax = Math.max(e[i], tempMax);
        }
        return tempMax;
    }

    private static int[] setRobObl(double[] d1, double[] d2, double[] r, int dc) {
        ArrayList<Integer> tempRobObl = new ArrayList<>();
        for (int i = 0; i < d1.length; i++) {
            if (d1[i] > 0.5 && d2[i] > 0.5 && r[i] <= dc) {
                tempRobObl.add(i);
            }
        }
        return tempRobObl.stream().mapToInt(Integer::intValue).toArray();
    }

    private static double[] setE(double[] sum1, double[] sum2, double[] sum3, double[] sum4) {
        double[] tempE = new double[sum1.length];
        for (int i = 0; i < tempE.length; i++) {
            tempE[i] = 1 + 0.5 * (sum1[i] + sum2[i] + sum3[i] + sum4[i]);
        }
        return tempE;
    }

    private static double[] setSum(double[] f, double[] s, double[] t) {
        double[] tempSum = new double[f.length];
        for (int i = 0; i < tempSum.length; i++) {
            tempSum[i] = f[i] / (s[i] + t[i]);
        }
        for (int i = 0; i < tempSum.length; i++) {
            tempSum[i] *= log2(tempSum[i]);
            tempSum[i] = (Double.isNaN(tempSum[i])) ? 0 : tempSum[i];
        }
        return tempSum;
    }

    private static double[] setKulbak(double[] d1, double[] d2, double[] alfa, double[] beta) {
        double[] tempKulbak = new double[d1.length];
        for (int i = 0; i < tempKulbak.length; ++i) {
            tempKulbak[i] = 0.5 * log2((d1[i] + d2[i])
                    /
                    (alfa[i] + beta[i])
            )
                    * (d1[i] + d2[i] - alfa[i] - beta[i]);
        }
        return tempKulbak;
    }

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    private static double[] setK(double[] sk) {
        double[] tempK = new double[r.length];
        for (int i = 0; i < tempK.length; i++) {
            int count = 0;
            for (int j = 0; j < sk.length; j++) {
                if (sk[j] <= r[i]) {
                    count++;
                }
            }
            tempK[i] = count;
        }
        return tempK;
    }

    private static double[] setD1OrBeta(double[] k) {
        double[] temp = new double[k.length];
        for (int i = 0; i < temp.length; i++) {
            if (k[i] == 0) {
                temp[i] = 0;
            } else {
                temp[i] = k[i] / 100;
            }
        }
        return temp;
    }

    private static double[] setD2OrAlfa(double[] arr) {
        double[] temp = new double[arr.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = 1 - arr[i];
        }
        return temp;
    }

    private static double[] setR() {
        double[] temp = new double[100];
        for (int i = 0; i < temp.length; ++i) {
            temp[i] = i + 1;
        }
        return temp;
    }
}
