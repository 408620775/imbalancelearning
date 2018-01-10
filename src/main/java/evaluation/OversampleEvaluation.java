package evaluation;

import java.util.List;
import java.util.Map;
import java.util.Random;

import main.Start;
import org.apache.log4j.Logger;
import resample.OverSubsample;
import util.PrintUtil;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import dataprocess.ClassificationResult;

public class OversampleEvaluation extends MyEvaluation {
    private static Logger logger = Logger.getLogger(OversampleEvaluation.class);
    public OversampleEvaluation(Instances data,
            Map<Instance, List<Integer>> ins_Loc) throws Exception {
        super(data, ins_Loc);
    }

    public void crossValidateModel(Classifier classifier, Instances data,
            int numFolds, Random random, Object... forPredictionsPrinting)
            throws Exception {

        // Make a copy of the data we can reorder
        data = new Instances(data);
        data.randomize(random);
        if (data.classAttribute().isNominal()) {
            data.stratify(numFolds);
        }

        int num_inst = 0;
        double num_correct = 0;
        double num_tp1 = 0;
        double num_tp2 = 0;
        int numclass1 = 0;
        int numclass2 = 0;
        int numPredictClass1 = 0;
        int numPredictClass2 = 0;
        NominalPrediction np = null;
        FastVector predictions = null;
        FastVector cur_predictions = new FastVector();
        ClassificationResult cr = null;
        crs = new FastVector();
        // Do the folds
        for (int i = 0; i < numFolds; i++) {
            Instances train = data.trainCV(numFolds, i, random);

            OverSubsample oversubsample;
            oversubsample = new OverSubsample();
            oversubsample.setInputFormat(train);
            oversubsample.setDistributionSpread(1);// set the ratio of the major
                                                    // class sample to the minor
                                                    // clas
            train = Filter.useFilter(train, oversubsample);

            setPriors(train);
            Classifier copiedClassifier = Classifier.makeCopy(classifier);
            copiedClassifier.buildClassifier(train);
            Instances test = data.testCV(numFolds, i);
            // evaluateModel(copiedClassifier, test, forPredictionsPrinting);
            evaluateModel(copiedClassifier, test);
            predictions = predictions();
            num_inst = test.numInstances();
            cr = new ClassificationResult();
            for (int n = predictions.size() - num_inst; n < predictions.size(); n++) {
                cur_predictions.addElement(predictions.elementAt(n));
                double[] actual_predict = new double[2];
                np = (NominalPrediction) predictions.elementAt(n);
                actual_predict[0] = np.actual();
                actual_predict[1] = np.predicted();
                ins_actual_predict.put(
                        test.instance(n - (predictions.size() - num_inst)),
                        actual_predict);
            }
            cr.setAuc(areaUnderROC(0, cur_predictions));
            for (int n = 0; n < cur_predictions.size(); n++) {
                np = (NominalPrediction) cur_predictions.elementAt(n);
                if (np.actual() == 0) {
                    numclass1++;
                } else {
                    numclass2++;
                }
                if (np.predicted() == 0) {
                    numPredictClass1++;
                } else {
                    numPredictClass2++;
                }
                if (np.actual() == np.predicted()) {
                    num_correct++;
                    if (np.actual() == 0) {
                        num_tp1++;
                    } else {
                        num_tp2++;
                    }
                }
            }
            cr.setAccuracy(num_correct / num_inst);
            cr.setRecall1(num_tp1 / numclass1);
            cr.setRecall2(num_tp2 / numclass2);
            cr.setPrecision1(num_tp1 / numPredictClass1);
            cr.setPrecision2(num_tp2 / numPredictClass2);
            crs.addElement(cr);
            String detaileString = PrintUtil.formatDouble(
                    PrintUtil.CROSSVAILD_OUTPUT_DECIMAL, cr.getRecall2())
                    + ","
                    + PrintUtil.formatDouble(
                            PrintUtil.CROSSVAILD_OUTPUT_DECIMAL,
                            cr.getPrecision2())
                    + ","
                    + PrintUtil.formatDouble(
                            PrintUtil.CROSSVAILD_OUTPUT_DECIMAL,
                            cr.getfMeasure2())
                    + ","
                    + PrintUtil.formatDouble(
                            PrintUtil.CROSSVAILD_OUTPUT_DECIMAL, cr.getAuc());
            logger.info(detaileString);
            PrintUtil.appendResult(detaileString, Start.CUR_DETAIL_FILENAME);
            numclass1 = 0;
            numclass2 = 0;
            cur_predictions.removeAllElements();
            num_correct = 0;
            num_tp1 = 0;
            num_tp2 = 0;
            numPredictClass1 = 0;
            numPredictClass2 = 0;
        }
        m_NumFolds = numFolds;

        /*
         * if (classificationOutput != null) classificationOutput.printFooter();
         */

    }

}