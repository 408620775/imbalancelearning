package classification.baggingMax;

import classification.BasicClassification;
import classification.ResampleSimpleClassification;
import evaluation.MyEvaluation;
import org.apache.log4j.Logger;
import util.PrintUtil;
import util.PropertyUtil;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.util.List;
import java.util.Map;
import Classifier.SmoteBagMax;
import Classifier.ROSBagMax;
import Classifier.RUSBagMax;

public class ResampleInBaggingMaxClassification extends BasicClassification {
    public static Logger logger = Logger.getLogger(ResampleInBaggingMaxClassification.class);

    public ResampleInBaggingMaxClassification(Instances data, Map<Instance, List<Integer>> ins_Loc) {
        super(data, ins_Loc);
    }

    public String getSmoteBagClassificationResult(Classifier classifier,
                                                  String classifier_name, int times) throws Exception {
        SmoteBagMax bag_classifier = new SmoteBagMax();
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
        RUSBagMax bag_classifier = new RUSBagMax();
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
        ROSBagMax bag_classifier = new ROSBagMax();
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
