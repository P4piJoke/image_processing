package p4pijk.lab7;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import p4pijk.image.Image;
import p4pijk.lab3.BinaryTrainingMatrix;
import p4pijk.lab4.ReferenceGeometricVector;
import p4pijk.lab5.CodeDistances;
import p4pijk.lab6.InformationalCriteria;
import p4pijk.util.ImageTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SystemOptimization {

    private final static double CONST = 0.5;
    private final int optimalDelta;
    private final int[] robObl;
    private final double[] shenonDelta;
    private final double shenonMaxDelta;

    public SystemOptimization(Image first, Image second) {
        ArrayList<Integer> tempRobObl = new ArrayList<>();
        ArrayList<Double> tempShenonDelta = new ArrayList<>();

        findDelta(first, second, tempRobObl, tempShenonDelta);
        robObl = tempRobObl.stream().mapToInt(Integer::intValue).toArray();
        shenonDelta = tempShenonDelta.stream().mapToDouble(Double::doubleValue).toArray();
        shenonMaxDelta = findMaxDelta();
        optimalDelta = findOptimalDelta();
    }

    public void saveArea(double[] arr, int[] rob_obl, String... names) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < arr.length; i++) {
            dataset.addValue(arr[i], names[0] + "_all", "" + i + 1);
        }
        for (int i : rob_obl) {
            dataset.addValue(arr[i - 1], names[0] + "_optimal", "" + (i - 1) + 1);
        }

        JFreeChart areaChart = ChartFactory.createAreaChart(
                "Delta's", // Chart title
                names[1] + " values", // X-Axis Label
                "Shenon's max", // Y-Axis Label
                dataset // Dataset for the Chart
        );

        ChartUtils.saveChartAsPNG(new File(ImageTools.LAB7_PATH.value() + names[2]), areaChart, 600, 400);
    }

    public int getOptimalDelta() {
        return optimalDelta;
    }

    public double[] getShenonDelta() {
        return shenonDelta;
    }

    public int[] getRobObl() {
        return robObl;
    }

    private int findOptimalDelta() {
        int delta = 0;
        for (int i : robObl) {
            if (shenonDelta[i - 1] == shenonMaxDelta) {
                delta = i;
            }
        }
        return delta;
    }

    private double findMaxDelta() {
        double tempMax = Double.MIN_VALUE;
        for (int i : robObl) {
            tempMax = Math.max(shenonDelta[i - 1], tempMax);
        }
        return tempMax;
    }

    private void findDelta(Image first, Image second, ArrayList<Integer> robObl, ArrayList<Double> shenonDelta) {
        for (int delta = 1; delta <= 100; delta++) {
            BinaryTrainingMatrix btm = new BinaryTrainingMatrix(first, second, delta);
            ReferenceGeometricVector rgv = new ReferenceGeometricVector(btm);
            CodeDistances cd = new CodeDistances(btm, rgv);
            InformationalCriteria ic1 = new InformationalCriteria(cd, cd.getSk1(), cd.getSk2());
            InformationalCriteria ic2 = new InformationalCriteria(cd, cd.getSkPara1(), cd.getSkPara2());

            double d1 = findD(ic1, ic1.getD1());
            double d2 = findD(ic1, ic1.getD2());
            double max1 = findMax(ic1);

            double dD1 = findD(ic2, ic2.getD1());
            double dD2 = findD(ic2, ic2.getD2());
            double max2 = findMax(ic2);
            if (checkD(d1) && checkD(d2) && checkD(dD1) && checkD(dD2)) {
                robObl.add(delta);
            }
            if (delta == 88) {
                System.out.println();
            }
            shenonDelta.add((max1 + max2) / 2);
        }
    }

    private double findMax(InformationalCriteria ic) {
        if (ic.getRobObl().length == 0) {
            return Arrays.stream(ic.getCriteria(ImageTools.SHENON_CRITERIA)).max().getAsDouble();
        }
        return ic.getMax(ImageTools.SHENON_CRITERIA);
    }

    private double findD(InformationalCriteria ic, double[] d) {
        double[] kulbak = ic.getCriteria(ImageTools.KULBAK_CRITERIA);
        if (ic.getRobObl().length == 0) {
            double max = Arrays.stream(kulbak).max().getAsDouble();
            int[] optKulbak = find(kulbak, max);
            return d[optKulbak[0]];
        } else {
            double max = ic.getMax(ImageTools.KULBAK_CRITERIA);
            int[] optRadius = find(trim(kulbak, ic.getRobObl()), max);
            optRadius[0] = ic.getRobObl()[optRadius[0]];
            return d[optRadius[0]];
        }
    }

    private double[] trim(double[] criteria, int[] robObl) {
        ArrayList<Double> trimmed = new ArrayList<>();
        for (int i : robObl) {
            trimmed.add(criteria[i - 1]);
        }
        return trimmed.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private int[] find(double[] kulbak, double max) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < kulbak.length; ++i) {
            if (kulbak[i] == max) {
                ans.add(i);
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }

    private boolean checkD(double d) {
        return d > CONST;
    }

}
