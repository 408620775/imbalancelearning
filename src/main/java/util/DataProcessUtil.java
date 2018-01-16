package util;

import classification.Classification;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DataProcessUtil {
    private static Logger logger = Logger.getLogger(DataProcessUtil.class);
    public static String SK_RESULT_FOLDER = "SK_RESULT";
    public static String FILENAME_DELIMITER = "_";
    public static String PAPER_TABLE_SAVE_PATH = "PaperTables";
    public static String AVERAGE_NAME = "Avg";

    public static void getProjectRankOfMethod(String resultFolder) throws IOException {
        for (String baseLearner : PropertySetUtil.BASE_LEARNERS) {
            Map<String, Map<String, Map<String, Double>>> method_evaluation_project_rank = getPaperTable(baseLearner);
            writePaperTableAccordBase(method_evaluation_project_rank, baseLearner);
        }
    }

    static void writePaperTableAccordBase(Map<String, Map<String, Map<String, Double>>>
                                                  method_evaluation_project_rank, String baseLearner) throws IOException {
        String savePath = PAPER_TABLE_SAVE_PATH + "/" + baseLearner + "_paperRank.csv";
        File saveFile = new File(savePath);
        StringBuffer line = new StringBuffer();
        line.append(",,");
        for (String project : PropertySetUtil.PROJECTS) {
            line.append(project + ",");
        }
        line.append(AVERAGE_NAME);
        PrintUtil.appendResult(line.toString(), savePath);
        for (String methodName : Classification.METHOD_NAMES) {
            for (int i = 0; i < Classification.EVALUATION_NAMES.size(); i++) {
                line = new StringBuffer();
                if (i == 0) {
                    line.append(methodName);
                }
                line.append("," + Classification.EVALUATION_NAMES.get(i));
                for (String project : PropertySetUtil.PROJECTS) {
                    line.append("," + method_evaluation_project_rank.get(methodName).get(Classification.EVALUATION_NAMES
                            .get(i)).get(project));
                }
                line.append("," + method_evaluation_project_rank.get(methodName).get(Classification.EVALUATION_NAMES
                        .get(i)).get(AVERAGE_NAME));
                PrintUtil.appendResult(line.toString(), savePath);
            }
        }
    }

    static Map<String, Map<String, Map<String, Double>>> getPaperTable(String baseLearner) throws IOException {
        Map<String, Map<String, Map<String, Double>>> method_evaluation_project_rank = initialPaperTableMap();
        for (String evaluation : Classification.EVALUATION_NAMES) {
            String fileName = SK_RESULT_FOLDER+"/"+Character.toUpperCase(baseLearner.charAt(0)) + FILENAME_DELIMITER +
                    evaluation + FILENAME_DELIMITER + "Rank";
            BufferedReader bReader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = bReader.readLine()) != null) {
                String projectName = "";
                String methodName = "";
                String evaluationName = evaluation;
                Double rankValue = 0.0;
                for (String project : PropertySetUtil.PROJECTS) {
                    if (!line.equals(project)) {
                        continue;
                    }
                    projectName = project;
                    line = bReader.readLine();
                    for (int i = 0; i < Classification.METHOD_NAMES.size(); i++) {
                        line = bReader.readLine();
                        for (int j = 0; j < Classification.METHOD_NAMES.size(); j++) {
                            if (!line.split(",")[0].replace("\"","").equals(Classification.METHOD_NAMES.get(j))) {
                                continue;
                            }
                            methodName = Classification.METHOD_NAMES.get(j);
                            rankValue = Double.parseDouble(line.split(",")[1]);
                            method_evaluation_project_rank.get(methodName).get(evaluation).put(projectName, rankValue);
                            break;
                        }
                    }
                    break;
                }
            }

        }
        AddAverageRankAccordProject(method_evaluation_project_rank);
        return method_evaluation_project_rank;
    }

    private static void AddAverageRankAccordProject(Map<String, Map<String, Map<String, Double>>>
                                                            method_evaluation_project_rank) {
        for (String methodName : Classification.METHOD_NAMES) {
            for (String evaluationName : Classification.EVALUATION_NAMES) {
                double avgRank = 0.0;
                int projectNum = 0;
                Map<String, Double> project_rank = method_evaluation_project_rank.get(methodName).get(evaluationName);
                for (String projectName : project_rank.keySet()) {
                    avgRank += project_rank.get(projectName);
                    projectNum++;
                }
                avgRank /= projectNum;
                method_evaluation_project_rank.get(methodName).get(evaluationName).put(AVERAGE_NAME, avgRank);
            }
        }
    }

    private static Map<String, Map<String, Map<String, Double>>> initialPaperTableMap() {
        Map<String, Map<String, Map<String, Double>>> method_evaluation_project_rank = new LinkedHashMap<>();
        for (String methodName : Classification.METHOD_NAMES) {
            Map<String, Map<String, Double>> evaluation_project_rank = new LinkedHashMap<>();
            for (String evaluationName : Classification.EVALUATION_NAMES) {
                Map<String, Double> project_rank = new LinkedHashMap<>();
                evaluation_project_rank.put(evaluationName, project_rank);
            }
            method_evaluation_project_rank.put(methodName, evaluation_project_rank);
        }
        return method_evaluation_project_rank;
    }

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
