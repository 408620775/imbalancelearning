package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.InstanceUtil;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public abstract class MyEvaluation extends Evaluation {

	FastVector crs = null;
	Map<Instance, double[]> ins_actual_predict = new LinkedHashMap<Instance, double[]>();
	Map<Instance, List<Integer>> ins_loc;
	double[] costEffectiveness = new double[101];
	public static int INSTANCE_CHANGE_LINE_INDEX = 4;

	public MyEvaluation(Instances data, Map<Instance, List<Integer>> ins_loc)
			throws Exception {
		super(data);
		this.ins_loc = ins_loc;
	}

	public double areaUnderROC(int classIndex, FastVector predictions) {

		// Check if any predictions have been collected
		if (predictions == null) {
			return Instance.missingValue();
		} else {
			ThresholdCurve tc = new ThresholdCurve();
			Instances result = tc.getCurve(predictions, classIndex);
			return ThresholdCurve.getROCArea(result);
		}
	}

	public FastVector getCrossValidateResult() {
		return crs;
	}

	public double[] getCostEffectiveness() {
		int total_actual_bug_num = 0;
		double total_changedLine_num = 0;
		List<List<Double>> rankTable = new ArrayList<>();
		List<Instance> instancesList = new ArrayList<>();
		instancesList.addAll(ins_actual_predict.keySet());

		for (Instance ins : ins_loc.keySet()) {
			int matchIndex = -1;
			for (int i = 0; i < instancesList.size(); i++) {
				if (InstanceUtil.instanceEquel(ins, instancesList.get(i))) {
					matchIndex = i;
					break;
				}
			}
			if (matchIndex == -1) {
				System.out.println("Error! The mismatch between the "
						+ "instance in ten fold cross validation "
						+ "and the instance in changedLine");
				return null;
			}

			double[] actual_predict = ins_actual_predict.get(instancesList
					.get(matchIndex));
			instancesList.remove(matchIndex);
			if (actual_predict[0] == 1) {
				total_actual_bug_num++;
			}
			int changedLine = ins_loc.get(ins).get(INSTANCE_CHANGE_LINE_INDEX);
			total_changedLine_num += changedLine;
			List<Double> actual_predict_change = new ArrayList<Double>();
			actual_predict_change.add(actual_predict[0]);
			actual_predict_change.add(actual_predict[1]);
			actual_predict_change.add((double) changedLine);
			rankTable.add(actual_predict_change);
		}
		Collections.sort(rankTable, new Comparator<List<Double>>() {

			@Override
			public int compare(List<Double> o1, List<Double> o2) {
				if (o1.get(1).doubleValue() != o2.get(1).doubleValue()) {
					return (int) (o2.get(1) - o1.get(1));
				} else {
					return (int) (o1.get(2) - o2.get(2));
				}
			}
		});
		// PrintUtil.printListList(rankTable);//
		double alreadyFind = 0.0;
		double alreadyCheckLine = 0;
		// System.out.println("rankTable.size():" + rankTable.size());
		for (int i = 0; i < rankTable.size(); i++) {
			List<Double> actual_predict_change = rankTable.get(i);
			double findRatio = 0;
			double x = 0;
			double upper = 0;
			if (Math.abs(actual_predict_change.get(0).doubleValue() - 1) < 0.01) {
				alreadyFind += 1;
				alreadyCheckLine += actual_predict_change.get(2);
				findRatio = alreadyFind / total_actual_bug_num * 100;
				x = alreadyCheckLine / total_changedLine_num * 100;
				upper = Math.ceil(x);
				costEffectiveness[(int) upper] = findRatio;
			} else {
				alreadyCheckLine += actual_predict_change.get(2);
			}
		}
		// smooth
		for (int i = 1; i < costEffectiveness.length; i++) {
			if (costEffectiveness[i] == 0) {
				costEffectiveness[i] = costEffectiveness[i - 1];
			}
		}
		return costEffectiveness;
	}
}
