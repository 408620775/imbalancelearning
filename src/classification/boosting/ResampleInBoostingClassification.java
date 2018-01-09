package classification.boosting;

import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import Classifier.OverBoosting;
import Classifier.SmoteBoosting;
import Classifier.UnderBoosting;
import classification.BasicClassification;

public class ResampleInBoostingClassification extends BasicClassification {

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

	// int maxseed shi shen me?
	private String getSmoteBoostClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		SmoteBoosting boost_classifier = new SmoteBoosting();
		boost_classifier.setClassifier(classifier);
		boost_classifier.setUseResampling(true);
		System.out.println("smoteboost");
		startTime = System.currentTimeMillis();
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
			Evaluation eval = evaluate(boost_classifier, randomSeed, "none");
			updateResult(validationResult, eval);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult(",smoteinboost", classifier_name, validationResult,
				times);
	}

	// using bagging classification method with under sampling
	public String getUnderBoostClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		UnderBoosting boost_classifier = new UnderBoosting(); // set the
		System.out.println("underboost"); // classifier as
		// bagging
		boost_classifier.setClassifier(classifier); // set the basic classifier
													// of bagging
		boost_classifier.setUseResampling(true);
		startTime = System.currentTimeMillis();
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
			Evaluation eval = evaluate(boost_classifier, randomSeed, "none");
			updateResult(validationResult, eval);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult(",underinboost", classifier_name, validationResult,
				times);
	}

	private String getOverBoostClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		OverBoosting boost_classifier = new OverBoosting(); // set the
		System.out.println("overboost");
		boost_classifier.setClassifier(classifier); // set the basic classifier
													// of bagging
		boost_classifier.setUseResampling(true);
		startTime = System.currentTimeMillis();
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
			Evaluation eval = evaluate(boost_classifier, randomSeed, "none");
			updateResult(validationResult, eval);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult("overinboost", classifier_name, validationResult,
				times);
	}

}
