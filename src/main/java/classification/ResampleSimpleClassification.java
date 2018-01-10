package classification;

import java.util.List;
import java.util.Map;

import main.Start;
import org.apache.log4j.Logger;
import util.PrintUtil;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import evaluation.MyEvaluation;

public class ResampleSimpleClassification extends BasicClassification {
    private static Logger logger = Logger.getLogger(ResampleSimpleClassification.class);

    public ResampleSimpleClassification(Instances data,
                                        Map<Instance, List<Integer>> ins_Loc) {
        super(data, ins_Loc);
    }

    // get the classification result without bagging
    public String getClassificationResult(Classifier classifier,
                                          String classifier_name, int times) throws Exception {
        double validationResult1[] = new double[4];
        double validationResult2[] = new double[4];
        double validationResult3[] = new double[4];
        logger.info("oversample");
        PrintUtil.appendResult("oversample", Start.CUR_DETAIL_FILENAME);
        startTime = System.currentTimeMillis();
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(classifier, randomSeed, "over");
            updateResult(validationResult1, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));

        logger.info("undersample");
        PrintUtil.appendResult("undersample",Start.CUR_DETAIL_FILENAME);
        startTime = System.currentTimeMillis();
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {// //////
            MyEvaluation eval = evaluate(classifier, randomSeed, "under");
            updateResult(validationResult2, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));

        logger.info("smotesample");
        PrintUtil.appendResult("smotesample",Start.CUR_DETAIL_FILENAME);
        startTime = System.currentTimeMillis();
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(classifier, randomSeed, "smote");
            updateResult(validationResult3, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));

        return getResult("oversample", classifier_name, validationResult1,
                times)
                + getResult(",undersample", classifier_name, validationResult2,
                times)
                + getResult(",smotesample", classifier_name, validationResult3,
                times);
    }
}