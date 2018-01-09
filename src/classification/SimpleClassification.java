package classification;

import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

public class SimpleClassification extends BasicClassification {

	public SimpleClassification(Instances data,
			Map<Instance, List<Integer>> ins_Loc) {
		super(data, ins_Loc);
	}

	// get the classification result without bagging
	public String getClassificationResult(Classifier classifier,
			String classifier_name, int times) throws Exception {
		// use different seed for 10-fold cross validation
		System.out.println("simple");
		startTime = System.currentTimeMillis();
		for (int randomSeed = 1; randomSeed <= times; randomSeed++) {
			Evaluation eval = evaluate(classifier, randomSeed, "none");
			updateResult(validationResult, eval);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time:" + (endTime - startTime));
		return getResult("simple", classifier_name, validationResult, times);
	}
}
