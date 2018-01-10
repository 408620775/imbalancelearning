import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.PrintUtil;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import classification.Classification;

public class Start {

    public static void main(String argv[]) throws Exception {
        String LOCFilePath = "LOCFiles";
        String arffPath = "Arffs_old_paper";
        // String projects[] = { "MyLucene", "MyTomcat", "MyJedit", "MyAnt",
        // "MySynapse", "MyVoldemort", "MyItextpdf", "MyBuck", "MyFlink",
        // "MyHadoop" };
        String[] projects = {"MyAnt", "MyBuck", "MyFlink", "MyHadoop",
                "MyItextpdf", "MyJedit", "MyLucene", "MySynapse", "MyTomcat",
                "MyVoldemort"};
        // String[] projects = { "MyBuck" };
        String predict_result = "";
        // String[] bases = { "j48", "RF", "naivebayes", "smo" };
        String[] bases = {"j48"};
        System.out.println("Arff Fold is :" + arffPath);
        for (String base : bases) {
            String output_file = base + "Result.csv";
            String measure_name = "project, method, recall-1, precision-1, fMeasure-1, auc \n";
            PrintUtil.saveResult(measure_name, output_file);
            System.out.println(base + " for detail");
            System.out.println();
            for (int i = 0; i < projects.length; i++) {
                String project = projects[i];
                System.out.println(project);
                // read in the input arff file
                String inputfile = arffPath + "/" + project + ".arff";
                // String inputfile = arffPath + "/" + project + ".arff";
                FileReader fr = new FileReader(inputfile);
                BufferedReader br = new BufferedReader(fr);
                Instances data = new Instances(br);
                data.setClassIndex(data.numAttributes() - 1);
                // print out number of instances
                System.out.println("Total number of instances in Arff file : "
                        + data.numInstances());
                AttributeStats as = data
                        .attributeStats(data.numAttributes() - 1);
                int count[] = as.nominalCounts;
                System.out.println("Number of buggy instances: " + count[1]);

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
                    System.out
                            .println("Error! The number in LOC File is different "
                                    + "with the number in Arff File!");
                    continue;
                }
                Map<Instance, List<Integer>> ins_Loc = new LinkedHashMap<>();
                for (int j = 0; j < data.numInstances(); j++) {
                    ins_Loc.put(data.instance(j), changedLineList.get(j));
                }
                Classification classification = new Classification(data);
                predict_result = classification.predict(base, project, 10,
                        ins_Loc);
                PrintUtil.appendResult(predict_result, output_file);
            }
        }

    }
}
