package classification.boosting;

import java.util.List;
import java.util.Map;

import main.Start;
import org.apache.log4j.Logger;
import util.PrintUtil;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import Classifier.OverBoosting;
import Classifier.SmoteBoosting;
import Classifier.UnderBoosting;
import classification.BasicClassification;

public class ResampleInBoostingClassification extends BasicClassification {

    private static Logger logger = Logger.getLogger(ResampleInBoostingClassification.class);

    public ResampleInBoostingClassification(Instances data,
                                            Map<Instance, List<Integer>> ins_Loc) {
        super(data, ins_Loc);
    }

    public String getClassificationResult(Classifier classifier,
                                          String classifier_name, int times) throws Exception {
        String predictResult = "";
        predictResult = getOverBoostClassificationResult(classifier,
                classifier_name, times);
        predictResult += getUnderBoostClassificationResult(classifier,
                classifier_name, times);
        predictResult += getSmoteBoostClassificationResult(classifier,
                classifier_name, times);
        return predictResult;
    }

    private String getSmoteBoostClassificationResult(Classifier classifier,
                                                     String classifier_name, int times) throws Exception {
        SmoteBoosting boost_classifier = new SmoteBoosting();
        boost_classifier.setClassifier(classifier);
        boost_classifier.setUseResampling(true);
        logger.info("smoteboost");
        PrintUtil.appendResult("smoteboost", Start.CUR_DETAIL_FILENAME);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(boost_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult(",smoteinboost", classifier_name, validationResult,
                times);
    }

    public String getUnderBoostClassificationResult(Classifier classifier,
                                                    String classifier_name, int times) throws Exception {
        UnderBoosting boost_classifier = new UnderBoosting();
        logger.info("underboost");
        PrintUtil.appendResult("underboost", Start.CUR_DETAIL_FILENAME);
        boost_classifier.setClassifier(classifier);
        boost_classifier.setUseResampling(true);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(boost_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult(",underinboost", classifier_name, validationResult,
                times);
    }

    private String getOverBoostClassificationResult(Classifier classifier,
                                                    String classifier_name, int times) throws Exception {
        OverBoosting boost_classifier = new OverBoosting();
        logger.info("overboost");
        PrintUtil.appendResult("overboost", Start.CUR_DETAIL_FILENAME);
        boost_classifier.setClassifier(classifier);
        boost_classifier.setUseResampling(true);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(boost_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("overinboost", classifier_name, validationResult,
                times);
    }

}
