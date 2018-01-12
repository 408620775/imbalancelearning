package util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DataProcessUtil {
    private static Logger logger = Logger.getLogger(DataProcessUtil.class);

    public static void covertDetailFileToSK_ESDFile(String detaileFilePath, String SK_ESDFoldPath, int detailNum,
                                                    List<String> method_names, List<String> evaluation_names)
            throws Exception {
        Map<String, Map<String, String[]>> evaluation_method_values = new LinkedHashMap<>();
        for (int i = 0; i < evaluation_names.size(); i++) {
            Map<String, String[]> method_values = new LinkedHashMap<>();
            for (int j = 0; j < method_names.size(); j++) {
                String[] values = new String[detailNum];
                method_values.put(method_names.get(j), values);
            }
            evaluation_method_values.put(evaluation_names.get(i), method_values);
        }
        readDetailFileToMap(evaluation_method_values, detaileFilePath, method_names, evaluation_names, detailNum);
        writeDetailMapToFile(evaluation_method_values, SK_ESDFoldPath, detailNum, detaileFilePath.substring
                (detaileFilePath.lastIndexOf("/") + 1));
    }

    private static void writeDetailMapToFile(Map<String, Map<String, String[]>> evaluation_method_values, String
            SK_ESDFoldPath, int detailNum, String originFileName) throws IOException {
        for (String evaluation : evaluation_method_values.keySet()) {
            File sk_detail_file = new File(SK_ESDFoldPath + "/" + evaluation + "_" + originFileName + ".csv");
            if (!sk_detail_file.exists()) {
                System.out.println(sk_detail_file);
                sk_detail_file.createNewFile();
            }
            Map<String, String[]> method_values = evaluation_method_values.get(evaluation);
            BufferedWriter bWrite = new BufferedWriter(new FileWriter(sk_detail_file));
            StringBuffer title = new StringBuffer();
            for (String method : method_values.keySet()) {
                title.append(method + ",");
            }
            bWrite.append(title.substring(0, title.length() - 1) + "\n");
            for (int i = 0; i < detailNum; i++) {
                StringBuffer line = new StringBuffer();
                for (String method : method_values.keySet()) {
                    line.append(method_values.get(method)[i] + ",");
                }
                bWrite.append(line.substring(0, line.length() - 1) + "\n");
            }
            bWrite.flush();
            bWrite.close();
        }
    }

    private static void readDetailFileToMap(Map<String, Map<String, String[]>> evaluation_method_values,
                                            String detaileFilePath, List<String> method_names,
                                            List<String> evaluation_names, int detailNum) throws Exception {
        BufferedReader bReader = new BufferedReader(new FileReader(new File(detaileFilePath)));
        int curMethdoIndex = 0;
        String line;
        while ((line = bReader.readLine()) != null) {
            if (!line.equals(method_names.get(curMethdoIndex))) {
                logger.error("covertDetailFileToMap Error!");
                throw new Exception("covertDetailFileToMap Error!");
            }
            for (int i = 0; i < detailNum; i++) {
                line = bReader.readLine();
                String[] array = line.split(",");
                for (int j = 0; j < evaluation_names.size(); j++) {
                    evaluation_method_values.get(evaluation_names.get(j)).get(method_names.get(curMethdoIndex))[i] =
                            array[j];
                }
            }
            curMethdoIndex++;
        }
        bReader.close();
    }

    public static void covertAllDetailFileToSK_ESDFile(String detailFloderPath, String SK_ESDFoldPath, int detailNum,
                                                       List<String> method_names, List<String> evaluation_names) throws Exception {
        File detaildFloder = new File(detailFloderPath);
        if (!detaildFloder.exists()) {
            return;
        }
        File[] detaildFiles = detaildFloder.listFiles();
        for (int i = 0; i < detaildFiles.length; i++) {
            File curFile = detaildFiles[i];
            File saveFloder = new File(SK_ESDFoldPath + "/" + Character.toUpperCase(curFile.getName().charAt(0)) + "_SK_ESD");
            if (!saveFloder.exists()) {
                saveFloder.mkdirs();
            }
            covertDetailFileToSK_ESDFile(curFile.getAbsolutePath(), saveFloder.getAbsolutePath(), detailNum,
                    method_names, evaluation_names);
        }
    }
}
