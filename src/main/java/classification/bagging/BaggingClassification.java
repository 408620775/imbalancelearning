package classification.bagging;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import evaluation.MyEvaluation;
import org.apache.log4j.Logger;
import util.PrintUtil;
import util.PropertyUtil;
import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.core.Instance;
import weka.core.Instances;
import classification.BasicClassification;

public class BaggingClassification extends BasicClassification {
    public static Logger logger = Logger.getLogger(BaggingClassification.class);
    public static List<String> METHOD_NAMES = Arrays.asList("Bag");

    public BaggingClassification(Instances data,
                                 Map<Instance, List<Integer>> ins_Loc) {
        super(data, ins_Loc);
    }

    public String getClassificationResult(Classifier classifier,
                                          String classifier_name, int times) throws Exception {
        Bagging bag_classifier = new Bagging();
        bag_classifier.setClassifier(classifier);
        logger.info(METHOD_NAMES.get(0));
        PrintUtil.appendResult(METHOD_NAMES.get(0), PropertyUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(METHOD_NAMES.get(0), PropertyUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult(METHOD_NAMES.get(0), classifier_name, validationResult, times);
    }

}
