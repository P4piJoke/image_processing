package p4pijk.lab6;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import p4pijk.lab5.CodeDistances;
import p4pijk.util.ImageTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InformationalCriteria {

    private final double[] r = setR();
    private final double[] d1;
    private final double[] d2;
    private final double[] beta;
    private final double[] alfa;
    private final int[] robObl;

    public InformationalCriteria(CodeDistances cd, double[] sk1, double[] sk2) {
        double[] k1 = setK(sk1);
        double[] k2 = setK(sk2);

        d1 = setD1OrBeta(k1);
        beta = setD1OrBeta(k2);
        alfa = setD2OrAlfa(d1);
        d2 = setD2OrAlfa(beta);
        robObl = setRobObl(d1, d2, r, cd.getDc());
    }

    public double[] getCriteria(ImageTools criteria) {
        return new Criteria(criteria.value()).getCriteria();
    }

    public double getMax(ImageTools criteria) {
        return new Criteria(criteria.value()).getMax();
    }

    public int[] getOptRadius(ImageTools criteria) {
        return new Criteria(criteria.value()).getOptRadius();
    }

    public double[] getD1() {
        return d1;
    }

    public double[] getD2() {
        return d2;
    }

    public double[] getBeta() {
        return beta;
    }

    public double[] getAlfa() {
        return alfa;
    }

    public int[] getRobObl() {
        return robObl;
    }

    public void saveArea(double[] arr, int[] rob_obl, String... names) throws IOException {
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

    private int[] setRobObl(double[] d1, double[] d2, double[] r, int dc) {
        ArrayList<Integer> tempRobObl = new ArrayList<>();
        for (int i = 0; i < d1.length; i++) {
            if (d1[i] > 0.5 && d2[i] > 0.5 && r[i] <= dc) {
                tempRobObl.add(i);
            }
        }
        return tempRobObl.stream().mapToInt(Integer::intValue).toArray();
    }

    private double[] setSum(double[] f, double[] s, double[] t) {
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

    private double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    private double[] setK(double[] sk) {
        double[] tempK = new double[r.length];
        for (int i = 0; i < tempK.length; i++) {
            int count = 0;
            for (double v : sk) {
                if (v <= r[i]) {
                    count++;
                }
            }
            tempK[i] = count;
        }
        return tempK;
    }

    private double[] setD1OrBeta(double[] k) {
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

    private double[] setD2OrAlfa(double[] arr) {
        double[] temp = new double[arr.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = 1 - arr[i];
        }
        return temp;
    }

    private double[] setR() {
        double[] temp = new double[100];
        for (int i = 0; i < temp.length; ++i) {
            temp[i] = i + 1;
        }
        return temp;
    }

    private class Criteria {

        private final double[] criteria;
        private final double max;
        private final int[] optRadius;

        public Criteria(String type) {
            switch (type) {
                case "Shenon":
                    criteria = setShenon();

                    break;
                case "Kulbak":
                    criteria = setKulbak();
                    break;
                default:
                    criteria = new double[]{0};
            }
            max = setMax();
            optRadius = setOptRadius();
        }

        private int[] setOptRadius() {
            ArrayList<Integer> tempOptRadius = new ArrayList<>();
            for (int i = 0; i < criteria.length; ++i) {
                if (criteria[i] == max) {
                    tempOptRadius.add(i);
                }
            }
            return tempOptRadius.stream().mapToInt(Integer::intValue).toArray();
        }

        private double[] setShenon() {
            double[] sum1 = setSum(alfa, alfa, d2);
            double[] sum2 = setSum(d2, alfa, d2);
            double[] sum3 = setSum(beta, beta, d1);
            double[] sum4 = setSum(d1, beta, d1);

            double[] tempE = new double[sum1.length];
            for (int i = 0; i < tempE.length; i++) {
                tempE[i] = 1 + 0.5 * (sum1[i] + sum2[i] + sum3[i] + sum4[i]);
            }
            return tempE;
        }

        private double[] setKulbak() {
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

        private double setMax() {
            double tempMax = criteria[robObl[0]];
            for (int i : robObl) {
                tempMax = Math.max(criteria[i], tempMax);
            }
            return tempMax;
        }

        public double[] getCriteria() {
            return criteria;
        }

        public double getMax() {
            return max;
        }

        public int[] getOptRadius() {
            return optRadius;
        }
    }
}
