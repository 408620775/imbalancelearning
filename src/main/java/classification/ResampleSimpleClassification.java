package classification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import util.PrintUtil;
import util.PropertySetUtil;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import evaluation.MyEvaluation;

public class ResampleSimpleClassification extends BasicClassification {
    private static Logger logger = Logger.getLogger(ResampleSimpleClassification.class);
    public static List<String> METHOD_NAMES = Arrays.asList("ROS", "RUS", "Smote");

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
        logger.info(METHOD_NAMES.get(0));
        PrintUtil.appendResult(METHOD_NAMES.get(0), PropertySetUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(METHOD_NAMES.get(0), PropertySetUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(classifier, randomSeed, "over");
            updateResult(validationResult1, eval);
            updateCostEffective(eval);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));

        logger.info(METHOD_NAMES.get(1));
        PrintUtil.appendResult(METHOD_NAMES.get(1), PropertySetUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(METHOD_NAMES.get(1), PropertySetUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(classifier, randomSeed, "under");
            updateResult(validationResult2, eval);
            updateCostEffective(eval);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));

        logger.info(METHOD_NAMES.get(2));
        PrintUtil.appendResult(METHOD_NAMES.get(2), PropertySetUtil.CUR_DETAIL_FILENAME);
        PrintUtil.appendResult(METHOD_NAMES.get(2), PropertySetUtil.CUR_COST_EFFECTIVE_RECORD);
        startTime = System.currentTimeMillis();
        ratioes = new double[MyEvaluation.COST_EFFECTIVE_RATIO_STEP];
        for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
            MyEvaluation eval = evaluate(classifier, randomSeed, "smote");
            updateResult(validationResult3, eval);
            updateCostEffective(eval);
        }
        writeCostEffective(times);
        endTime = System.currentTimeMillis();
        logger.info("Time:" + (endTime - startTime));

        return getResult(METHOD_NAMES.get(0), classifier_name, validationResult1,
                times)
                + getResult("," + METHOD_NAMES.get(1), classifier_name, validationResult2,
                times)
                + getResult("," + METHOD_NAMES.get(2), classifier_name, validationResult3,
                times);
    }
}