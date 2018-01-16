package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import util.DataProcessUtil;
import util.PrintUtil;
import weka.classifiers.Evaluation;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import classification.Classification;

public class Start {
    private static Logger logger = Logger.getLogger(Start.class);
    public static String[] indicators = {"recall-1", "precision-1", "fMeasure-1", "auc"};
    public static String DETAIL_FILES_PATH = "DetailFiles/";
    public static String COST_FILES_PATH = "CostFiles/";
    public static String RESULT_FILES_PATH = "ResultFiles/";
    public static String DEFAULT_DETAIL_FOLDER = "DetailFiles";
    public static String DEFAULT_SKESD_FOLDER = "SK_ESD";
    public static String CUR_DETAIL_FILENAME = "";
    public static String CUR_COST_EFFECTIVE_RECORD = "";
    public static String LOC_FILE_PATH = "LOCFiles";
    public static String ARFF_PATH = "Arffs_old_paper";
    public static String[] PROJECTS = {"MyAnt", "MyBuck", "MyFlink", "MyHadoop", "MyItextpdf", "MyJedit", "MyLucene",
            "MySynapse", "MyTomcat", "MyVoldemort"};
    public static String[] BASE_LEARNERS = {"j48", "RF", "naivebayes", "smo"};
    public static boolean COUNT_COST_EFFECTIVENESS = false;

    public static void main(String argv[]) throws Exception {
        //getClassificationResult(LOC_FILE_PATH, ARFF_PATH, PROJECTS, BASE_LEARNERS, 100);
//        DataProcessUtil.covertAllDetailFileToSK_ESDFile(DEFAULT_DETAIL_FOLDER, DEFAULT_SKESD_FOLDER,
//                Classification.DETAIL_NUM, Classification.METHOD_NAMES, Classification.EVALUATION_NAMES);
    }

    private static void getClassificationResult(String locFilePath, String arffPath, String[] projects,
                                                String[] baseLearners, int times) throws Exception {
        String predict_result = "";
        logger.info("Arff Fold is :" + arffPath);
        for (String base : baseLearners) {
            String output_file = RESULT_FILES_PATH + base + "Result.csv";
            String measure_name = "project, method, recall-1, precision-1, fMeasure-1, auc";
            PrintUtil.saveResult(measure_name, output_file);
            logger.info(base + " for detail");
            for (int i = 0; i < projects.length; i++) {
                String project = projects[i];
                CUR_DETAIL_FILENAME = DETAIL_FILES_PATH + base + "_" + project + "_" + "DETAIL";
                CUR_COST_EFFECTIVE_RECORD = COST_FILES_PATH + base + "_" + project + "_" + "COST";
                File cur_detail_file = new File(CUR_DETAIL_FILENAME);
                cur_detail_file.delete();
                File cur_cost_file = new File(CUR_COST_EFFECTIVE_RECORD);
                cur_detail_file.delete();
                logger.info(project);
                String inputfile = arffPath + "/" + project + ".arff";
                FileReader fr = new FileReader(inputfile);
                BufferedReader br = new BufferedReader(fr);
                Instances data = new Instances(br);
                data.setClassIndex(data.numAttributes() - 1);
                logger.info("Total number of instances in Arff file : "
                        + data.numInstances());
                AttributeStats as = data
                        .attributeStats(data.numAttributes() - 1);
                int count[] = as.nominalCounts;
                logger.info("Number of buggy instances: " + count[1]);
                Map<Instance, List<Integer>> ins_Loc = null;
                List<List<Integer>> changedLineList = null;
                if (COUNT_COST_EFFECTIVENESS) {
                    ins_Loc = new LinkedHashMap<>();
                    changedLineList = new ArrayList<>();
                    br = new BufferedReader(new FileReader(new File(locFilePath
                            + "/" + project + "LOC")));
                    String line;
                    while ((line = br.readLine()) != null && (!line.equals(""))) {
                        if (line.startsWith("commit_id")) {
                            continue;
                        }
                        String[] array = line.split(",");
                        List<Integer> tmp = new ArrayList<>();
                        for (int j = 0; j < array.length; j++) {
                            tmp.add(Integer.parseInt(array[j]));
                        }
                        changedLineList.add(tmp);
                    }
                    br.close();
                    if (changedLineList.size() != data.numInstances()) {
                        logger.error("Error! The number in LOC File is different "
                                + "with the number in Arff File!");
                        continue;
                    }
                    for (int j = 0; j < data.numInstances(); j++) {
                        ins_Loc.put(data.instance(j), changedLineList.get(j));
                    }
                }
                Classification classification = new Classification(data);
                predict_result = classification.predict(base, project, times, ins_Loc);
                PrintUtil.appendResult(predict_result, output_file);
            }
        }
    }
}
