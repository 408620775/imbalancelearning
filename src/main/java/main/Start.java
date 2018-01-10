package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import util.PrintUtil;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import classification.Classification;

public class Start {
    private static Logger logger = Logger.getLogger(Start.class);
    public static String[] indicators = {"recall-1", "precision-1", "fMeasure-1", "auc"};
    public static String CUR_DETAIL_FILENAME = "";
    public static String CUR_COST_EFFECTIVE_RECORD = "";
    public static void main(String argv[]) throws Exception {
        String LOCFilePath = "LOCFiles";
        String arffPath = "Arffs_old_paper";
        // String projects[] = { "MyLucene", "MyTomcat", "MyJedit", "MyAnt",
        // "MySynapse", "MyVoldemort", "MyItextpdf", "MyBuck", "MyFlink",
        // "MyHadoop" };
        String[] projects = {"MyAnt", "MyBuck", "MyFlink", "MyHadoop",
                "MyItextpdf", "MyJedit", "MyLucene", "MySynapse", "MyTomcat",
                "MyVoldemort"};
        //String[] projects = {"MyBuck"};
        String predict_result = "";
        String[] bases = { "j48", "RF", "naivebayes", "smo" };
        //String[] bases = {"j48"};
        logger.info("Arff Fold is :" + arffPath);
        for (String base : bases) {
            String output_file = base + "Result.csv";
            String measure_name = "project, method, recall-1, precision-1, fMeasure-1, auc \n";
            PrintUtil.saveResult(measure_name, output_file);
            logger.info(base + " for detail");
            for (int i = 0; i < projects.length; i++) {
                String project = projects[i];
                CUR_DETAIL_FILENAME = base + "_" + project+"_"+"DETAIL";
                CUR_COST_EFFECTIVE_RECORD = base+"_"+project+"_"+"COST";
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

                List<List<Integer>> changedLineList = new ArrayList<List<Integer>>();
                br = new BufferedReader(new FileReader(new File(LOCFilePath
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
                Map<Instance, List<Integer>> ins_Loc = new LinkedHashMap<>();
                for (int j = 0; j < data.numInstances(); j++) {
                    ins_Loc.put(data.instance(j), changedLineList.get(j));
                }
                Classification classification = new Classification(data);
                predict_result = classification.predict(base, project, 100,
                        ins_Loc);
                PrintUtil.appendResult(predict_result, output_file);
            }
        }

    }
}
