package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public static String formatDouble(int decimal, double d) {
        String str = String.format("%." + decimal + "f", d);
        return str;
    }

    public static void saveResult(String result, String file) throws IOException {
        FileWriter fw=new FileWriter(file, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(result);
        bw.flush();
        bw.close();
    }

    public static void appendResult(String result, String file) throws IOException{
        FileWriter fa=new FileWriter(file, true);
        BufferedWriter ba = new BufferedWriter(fa);
        ba.write(result);
        ba.flush();
        ba.close();
    }
}
