package classification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import classification.bagging.BaggingClassification;
import classification.bagging.ResampleInBaggingClassification;
import classification.boosting.BoostingClassification;
import classification.boosting.ResampleInBoostingClassification;

public class Classification {

    Instances data;
    String classifier_name;
    Classifier classifier;
    String output_file;
    String output_file_matrix;
    //Fix me , not equal with SimpleClassification.java.
    public static List<String> METHOD_NAMES = Arrays.asList("Simple", "oversample", "undersample", "smotesample",
            "bagging", "overbag", "underbag", "smotebag", "boost", "overboost", "underboost", "smoteboost");
    public static List<String> EVALUATION_NAMES = Arrays.asList("R1", "P1", "f1", "AUC");
    public static int DETAIL_NUM = 1000;

    public Classification(Instances data) {
        this.data = data;

    }

    public void setClassifier(String classifier_name_input) {
        classifier_name = classifier_name_input;
        switch (classifier_name) {
            case "j48":
                classifier = new J48();
                break;
            case "naivebayes":
                classifier = new NaiveBayes();
                break;
            case "smo":
                classifier = new SMO();
                break;
            case "randomforest":
                classifier = new RandomForest();
                break;
            case "ripper":
                classifier = new JRip();
                break;
            case "IBk":
                classifier = new IBk();
                break;
            case "LR":
                classifier = new LinearRegression();
                break;
            case "RF":
                classifier = new RandomForest();
                break;
        }
    }

    public String predict(String classifier_name_input, String project,
                          int times, Map<Instance, List<Integer>> ins_Loc) throws Exception {

        setClassifier(classifier_name_input);
        DETAIL_NUM = times * 10;
        BasicClassification use_classification = new SimpleClassification(data,
                ins_Loc);
        String predict_result = "";

        predict_result = project
                + ","
                + use_classification.classify(times, classifier,
                classifier_name);
        use_classification = new ResampleSimpleClassification(data, ins_Loc);
        predict_result += ","
                + use_classification.classify(times, classifier,
                classifier_name);

        use_classification = new BaggingClassification(data, ins_Loc);
        predict_result += ","
                + use_classification.classify(times, classifier,
                classifier_name);
        use_classification = new ResampleInBaggingClassification(data, ins_Loc);
        predict_result += ","
                + use_classification.classify(times, classifier,
                classifier_name);
        use_classification = new BoostingClassification(data, ins_Loc);
        predict_result += ","
                + use_classification.classify(times, classifier,
                classifier_name);
        use_classification = new ResampleInBoostingClassification(data, ins_Loc);
        predict_result += ","
                + use_classification.classify(times, classifier,
                classifier_name);
        return predict_result;
    }
}
