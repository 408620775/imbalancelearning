package classification.bagging;

import java.util.List;
import java.util.Map;

import classification.ResampleSimpleClassification;
import main.Start;
import org.apache.log4j.Logger;
import util.PrintUtil;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import Classifier.OverBagging;
import Classifier.SmoteBagging;
import Classifier.UnderBagging;
import classification.BasicClassification;
import evaluation.MyEvaluation;

public class ResampleInBaggingClassification extends BasicClassification {
    public static Logger logger = Logger.getLogger(ResampleSimpleClassification.class);

    public ResampleInBaggingClassification(Instances data,
                                           Map<Instance, List<Integer>> ins_Loc) {
        super(data, ins_Loc);
    }

    public String getClassificationResult(Classifier classifier,
                                          String classifier_name, int times) throws Exception {
        String predictResult = "";
        predictResult += getOverBagClassificationResult(classifier,
                classifier_name, times);
        predictResult += getUnderBagClassificationResult(classifier,
                classifier_name, times);
        predictResult += getSmoteBagClassificationResult(classifier,
                classifier_name, times);
        return predictResult;
    }

    public String getSmoteBagClassificationResult(Classifier classifier,
                                                  String classifier_name, int times) throws Exception {
        SmoteBagging bag_classifier = new SmoteBagging();
        bag_classifier.setClassifier(classifier);
        logger.info("smotebag");
        PrintUtil.appendResult("smotebag", Start.CUR_DETAIL_FILENAME);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult(",smotebag", classifier_name, validationResult, times);

    }

    public String getUnderBagClassificationResult(Classifier classifier,
                                                  String classifier_name, int times) throws Exception {
        UnderBagging bag_classifier = new UnderBagging();
        bag_classifier.setClassifier(classifier);
        logger.info("underbag");
        PrintUtil.appendResult("underbag", Start.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult("underbag", Start.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        validationResult = new double[MyEvaluation.EVALUATION_INDEX_NUM];
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= 10; randomSeed++) {
            MyEvaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
            updateCostEffective(eval);
        }
        if (calcutionCost) {
            double[] cost = getCostEffective(times);
            PrintUtil.appendResult(PrintUtil.arrayStringFormat(cost, BIT_NUM_AFTER_DECIMAL), Start.CUR_COST_EFFECTIVE_RECORD);
            PrintUtil.appendResult(PrintUtil.formatDouble(BIT_NUM_AFTER_DECIMAL, cost[PENCENTAGE_OF_CONCERN]) + "", Start
                    .CUR_COST_EFFECTIVE_RECORD);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult(",underbag", classifier_name, validationResult, times);

    }


    private String getOverBagClassificationResult(Classifier classifier,
                                                  String classifier_name, int times) throws Exception {
        OverBagging bag_classifier = new OverBagging();
        bag_classifier.setClassifier(classifier);
        logger.info("overbag");
        PrintUtil.appendResult("overbag", Start.CUR_DETAIL_FILENAME);
        startTime = System.currentTimeMillis();
        validationResult = new double[4];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            Evaluation eval = evaluate(bag_classifier, randomSeed, "none");
            updateResult(validationResult, eval);
        }
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));
        return getResult("overbag", classifier_name, validationResult, times);
    }
}