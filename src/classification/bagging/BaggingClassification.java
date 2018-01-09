package classification.bagging;

import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.core.Instance;
import weka.core.Instances;
import classification.BasicClassification;

public class BaggingClassification extends BasicClassification {

	public BaggingClassification(Instances data,
			Map<Instance, List<Integer>> ins_Loc) {
		super(data, ins_Loc);
	}

	// using bagging classification method
	public String getClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		Bagging bag_classifier = new Bagging(); // set the classifier as
		// bagging
		bag_classifier.setClassifier(classifier);
		System.out.println("bagging");
		startTime = System.currentTimeMillis();
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) { // classifier
																		// of
																		// bagging
			Evaluation eval = evaluate(bag_classifier, randomSeed, "none");
			updateResult(validationResult, eval);
			// System.out.println(randomSeed + " " + res);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult("bag", classifier_name, validationResult, times);
	}

}
