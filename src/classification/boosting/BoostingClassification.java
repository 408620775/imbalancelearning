package classification.boosting;

import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AdaBoostM1;
import weka.core.Instance;
import weka.core.Instances;
import classification.BasicClassification;

public class BoostingClassification extends BasicClassification {

	public BoostingClassification(Instances data,
			Map<Instance, List<Integer>> ins_Loc) {
		super(data, ins_Loc);
	}

	// using bagging classification method
	public String getClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		AdaBoostM1 boost_classifier = new AdaBoostM1(); // set the classifier as
														// // bagging
		boost_classifier.setClassifier(classifier); // set the basic classifier
		System.out.println("boosting");
		startTime = System.currentTimeMillis(); // of bagging
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
			Evaluation eval = evaluate(boost_classifier, randomSeed, "none");
			updateResult(validationResult, eval);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult("boost", classifier_name, validationResult, times);
	}

}
