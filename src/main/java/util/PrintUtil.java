package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class PrintUtil {
    public static int CROSSVAILD_OUTPUT_DECIMAL = 4;

    public static void printListList(List<List<Double>> rankTable) {
        for (int i = 0; i < rankTable.size(); i++) {
            List<Double> list = rankTable.get(i);
            StringBuffer sBuffer = new StringBuffer();
            for (int j = 0; j < list.size() - 1; j++) {
                sBuffer.append(list.get(j) + " ");
            }
            sBuffer.append(list.get(list.size() - 1));
            System.out.println(sBuffer.toString());
        }
    }

    public static void printArray(double[] ratioes) {
        if (ratioes == null || ratioes.length == 0) {
            return;
        }
        for (int i = 0; i < ratioes.length - 1; i++) {
            System.out.print(ratioes[i] + " ");
        }
        System.out.print(ratioes[ratioes.length - 1]);
        System.out.println();
    }

    public static String arrayStringFormat(double[] ratioes, int dicimal) {
        if (ratioes == null || ratioes.length == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        String bitNum = "%." + dicimal + "f";
        stringBuffer.append(String.format(bitNum, ratioes[0]));
        for (int i = 1; i < ratioes.length; i++) {
            stringBuffer.append(",").append(String.format(bitNum, ratioes[i]));
        }
        return stringBuffer.toString();
    }

    //no change on origin array content.
    public static double[] formatDoubleArray(double[] doubleArray, int dicimal) {
        if (doubleArray == null || doubleArray.length == 0) {
            return doubleArray;
        }
        double[] res = new double[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++) {
            res[i] = formatDouble(dicimal, doubleArray[i]);
        }
        return res;
    }

    public static double formatDouble(int decimal, double d) {
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void saveResult(String result, String file) throws IOException {
        FileWriter fw = new FileWriter(file, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(result);
        bw.flush();
        bw.close();
    }

    public static void appendResult(String result, String file) throws IOException {
        FileWriter fa = new FileWriter(file, true);
        BufferedWriter ba = new BufferedWriter(fa);
        ba.write(result);
        ba.flush();
        ba.close();
    }
}
