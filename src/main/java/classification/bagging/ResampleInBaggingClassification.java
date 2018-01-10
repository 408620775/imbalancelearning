package classification.bagging;

import java.util.List;
import java.util.Map;

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
		SmoteBagging bag_classifier = new SmoteBagging(); // set the classifier
															// as bagging
		bag_classifier.setClassifier(classifier); // set the basic classifier of
		System.out.println("smotebag"); // bagging
		startTime = System.currentTimeMillis();
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
			Evaluation eval = evaluate(bag_classifier, randomSeed, "none");
			updateResult(validationResult, eval);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult(",smotebag", classifier_name, validationResult, times);

	}

	// using bagging classification method with under sampling
	public String getUnderBagClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		validationResult = new double[4];
		UnderBagging bag_classifier = new UnderBagging(); // set the classifier
															// // as bagging
		bag_classifier.setClassifier(classifier); // set the basic classifier of
		System.out.println("underbag"); // bagging
		startTime = System.currentTimeMillis();
		// double[] ratioes = new double[101];
		for (int randomSeed = 1; randomSeed <= 10; randomSeed++) {// //
			MyEvaluation eval = evaluate(bag_classifier, randomSeed, "none");
			// double[] oneceRatio = eval.getCostEffectiveness();
			// for (int i = 0; i < oneceRatio.length; i++) {
			// ratioes[i] += oneceRatio[i];
			// }
			updateResult(validationResult, eval);
			// System.out.println(randomSeed + " " + res);
		}
		// for (int i = 0; i < ratioes.length; i++) {
		// ratioes[i] = Double.parseDouble(String.format("%.2f",
		// ratioes[i] / 10));
		// }
		// PrintUtil.printArray(ratioes);
		// System.out.println("%20Pb:" + ratioes[20]);
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult(",underbag", classifier_name, validationResult, times);

	}

	private String getOverBagClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		validationResult = new double[4];
		OverBagging bag_classifier = new OverBagging(); // set the classifier as
														// bagging
		bag_classifier.setClassifier(classifier); // set the basic classifier of
		System.out.println("overbag"); // bagging
		startTime = System.currentTimeMillis();
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
			Evaluation eval = evaluate(bag_classifier, randomSeed, "none");
			updateResult(validationResult, eval);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult("overbag", classifier_name, validationResult, times);
	}
}
