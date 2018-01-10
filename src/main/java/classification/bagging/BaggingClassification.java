package classification.bagging;

import java.util.List;
import java.util.Map;


import main.Start;
import org.apache.log4j.Logger;
import util.PrintUtil;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Instance;
import weka.core.Instances;
import classification.BasicClassification;

public class BaggingClassification extends BasicClassification {
    public static Logger logger = Logger.getLogger(BaggingClassification.class);

    public BaggingClassification(Instances data,
                                 Map<Instance, List<Integer>> ins_Loc) {
        super(data, ins_Loc);
    }

    public String getClassificationResult(Classifier classifier,
                                          String classifier_name, int times) throws Exception {
        Bagging bag_classifier = new Bagging();
        bag_classifier.setClassifier(classifier);
        logger.info("bagging");
        PrintUtil.appendResult("bagging", Start.CUR_DETAIL_FILENAME);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("bag", classifier_name, validationResult, times);
    }

}