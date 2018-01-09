package main;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.supervised.instance.SMOTE;

public class SmoteTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ArffLoader arffLoader = new ArffLoader();
		arffLoader.setFile(new File("weather.arff"));
		Instances instances = arffLoader.getDataSet();
		System.out.println(instances);
		instances.setClassIndex(instances.numAttributes() - 1);
		SMOTE smote = new SMOTE();
		smote.setInputFormat(instances);
		Instances af = weka.filters.Filter.useFilter(instances, smote);
		System.out.println(af);
	}

}
