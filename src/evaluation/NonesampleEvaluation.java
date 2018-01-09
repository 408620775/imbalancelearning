package evaluation;

import java.util.List;
import java.util.Map;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Range;
import dataprocess.ClassificationResult;

public class NonesampleEvaluation extends MyEvaluation {

	public NonesampleEvaluation(Instances data,
			Map<Instance, List<Integer>> ins_loc) throws Exception {
		super(data, ins_loc);
	}

	public void crossValidateModel(Classifier classifier, Instances data,
			int numFolds, Random random, Object... forPredictionsPrinting)
			throws Exception {
		data = new Instances(data);
		data.randomize(random);
		if (data.classAttribute().isNominal()) {
			data.stratify(numFolds);
		}
		if (forPredictionsPrinting.length > 0) {
			// print the header first
			StringBuffer buff = (StringBuffer) forPredictionsPrinting[0];
			Range attsToOutput = (Range) forPredictionsPrinting[1];
			boolean printDist = ((Boolean) forPredictionsPrinting[2])
					.booleanValue();
			printClassificationsHeader(data, attsToOutput, printDist, buff);
		}
		int num_inst = 0;
		double num_correct = 0;
		double num_tp1 = 0;
		double num_tp2 = 0;
		int numclass1 = 0;
		int numclass2 = 0;
		NominalPrediction np = null;
		FastVector predictions = null;
		FastVector cur_predictions = new FastVector();
		ClassificationResult cr = null;
		crs = new FastVector();
		// Do the folds
		for (int i = 0; i < numFolds; i++) {
			Instances train = data.trainCV(numFolds, i, random);
			setPriors(train);
			Classifier copiedClassifier = Classifier.makeCopy(classifier);
			copiedClassifier.buildClassifier(train);
			Instances test = data.testCV(numFolds, i);
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
			crs.addElement(cr);
			numclass1 = 0;
			numclass2 = 0;
			cur_predictions.removeAllElements();
			num_correct = 0;
			num_tp1 = 0;
			num_tp2 = 0;
		}
		m_NumFolds = numFolds;
	}

}
