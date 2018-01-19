package classification.boosting;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import evaluation.MyEvaluation;
import org.apache.log4j.Logger;
import util.PrintUtil;
import util.PropertyUtil;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import Classifier.OverBoosting;
import Classifier.SmoteBoosting;
import Classifier.UnderBoosting;
import classification.BasicClassification;

public class ResampleInBoostingClassification extends BasicClassification {

    private static Logger logger = Logger.getLogger(ResampleInBoostingClassification.class);
    public static List<String> METHOD_NAMES = Arrays.asList("ROSBoost", "RUSBoost", "SmoteBoost");

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
        logger.info(METHOD_NAMES.get(2));
        PrintUtil.appendResult(METHOD_NAMES.get(2), PropertyUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(METHOD_NAMES.get(2), PropertyUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(boost_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("," + METHOD_NAMES.get(2), classifier_name, validationResult,
                times);
    }

    public String getUnderBoostClassificationResult(Classifier classifier,
                                                    String classifier_name, int times) throws Exception {
        UnderBoosting boost_classifier = new UnderBoosting();
        logger.info(METHOD_NAMES.get(1));
        PrintUtil.appendResult(METHOD_NAMES.get(1), PropertyUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(METHOD_NAMES.get(1), PropertyUtil.CUR_COST_EFFECTIVE_RECORD);
        boost_classifier.setClassifier(classifier);
        boost_classifier.setUseResampling(true);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(boost_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("," + METHOD_NAMES.get(1), classifier_name, validationResult,
                times);
    }

    private String getOverBoostClassificationResult(Classifier classifier,
                                                    String classifier_name, int times) throws Exception {
        OverBoosting boost_classifier = new OverBoosting();
        logger.info(METHOD_NAMES.get(0));
        PrintUtil.appendResult(METHOD_NAMES.get(0), PropertyUtil.CUR_COST_EFFECTIVE_RECORD);
        PrintUtil.appendResult(METHOD_NAMES.get(0), PropertyUtil.CUR_DETAIL_FILENAME);
        boost_classifier.setClassifier(classifier);
        boost_classifier.setUseResampling(true);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(boost_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult(METHOD_NAMES.get(0), classifier_name, validationResult,
                times);
    }

}
