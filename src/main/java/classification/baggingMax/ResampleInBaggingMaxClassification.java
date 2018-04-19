package classification.baggingMax;

import bagging.ROSMaxBag;
import bagging.SmoteMaxBag;
import classification.BasicClassification;
import evaluation.MyEvaluation;
import org.apache.log4j.Logger;
import util.PrintUtil;
import util.PropertyUtil;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.List;
import java.util.Map;

import bagging.RUSMaxBag;

public class ResampleInBaggingMaxClassification extends BasicClassification {
    public static Logger logger = Logger.getLogger(ResampleInBaggingMaxClassification.class);

    public ResampleInBaggingMaxClassification(Instances data, Map<Instance, List<Integer>> ins_Loc) {
        super(data, ins_Loc);
    }

    public String getClassificationResult(Classifier classifier, String classifier_name, int times) throws Exception {
        String predictResult = "";
        if (PropertyUtil.METHOD_USE_MAP[13]) {
            predictResult += getOverBagClassificationResult(classifier, classifier_name, times);
        }
        if (PropertyUtil.METHOD_USE_MAP[14]) {
            predictResult += getUnderBagClassificationResult(classifier, classifier_name, times);
        }
        if (PropertyUtil.METHOD_USE_MAP[15]) {
            predictResult += getSmoteBagClassificationResult(classifier, classifier_name, times);
        }
        return predictResult;
    }

    public String getSmoteBagClassificationResult(Classifier classifier,
                                                  String classifier_name, int times) throws Exception {
        SmoteMaxBag bag_classifier = new SmoteMaxBag();
        bag_classifier.setClassifier(classifier);
        String methodName = PropertyUtil.METHOD_NAMES[15];
        logger.info(methodName);
        PrintUtil.appendResult(methodName, PropertyUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(methodName, PropertyUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval, methodName);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("," + methodName, classifier_name, validationResult, times);
    }

    public String getUnderBagClassificationResult(Classifier classifier,
                                                  String classifier_name, int times) throws Exception {
        RUSMaxBag bag_classifier = new RUSMaxBag();
        bag_classifier.setClassifier(classifier);
        String methodName = PropertyUtil.METHOD_NAMES[14];
        logger.info(methodName);
        PrintUtil.appendResult(methodName, PropertyUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(methodName, PropertyUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        validationResult = new double[MyEvaluation.EVALUATION_INDEX_NUM];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval, methodName);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("," + methodName, classifier_name, validationResult, times);

    }


    private String getOverBagClassificationResult(Classifier classifier,
                                                  String classifier_name, int times) throws Exception {
        ROSMaxBag bag_classifier = new ROSMaxBag();
        bag_classifier.setClassifier(classifier);
        String methodName = PropertyUtil.METHOD_NAMES[13];
        logger.info(methodName);
        PrintUtil.appendResult(methodName, PropertyUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(methodName, PropertyUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval, methodName);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("," + methodName, classifier_name, validationResult, times);
    }
}