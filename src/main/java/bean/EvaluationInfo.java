package bean;

public class EvaluationInfo {
		private String project;
		private String method;
		private String classifier;
		private double accuracy;
		private double[]recall;
		private double[]precision;
		private double[]fMeasure;
		private double gmean;
		private double auc;
		private int classNum;
		
		public EvaluationInfo(int classNum) {
			recall = new double[classNum];
			precision = new double[classNum];
			fMeasure = new double[classNum];
			this.classNum = classNum;
		}

		public String getProject() {
			return project;
		}

		public void setProject(String project) {
			this.project = project;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getClassifier() {
			return classifier;
		}

		public void setClassifier(String classifier) {
			this.classifier = classifier;
		}

		public double getAccuracy() {
			return accuracy;
		}

		public void setAccuracy(double accuracy) {
			this.accuracy = accuracy;
		}

		public double[] getRecall() {
			return recall;
		}

		public void setRecall(double[] recall) {
			this.recall = recall;
		}

		public double[] getPrecision() {
			return precision;
		}

		public void setPrecision(double[] precision) {
			this.precision = precision;
		}

		public double[] getfMeasure() {
			return fMeasure;
		}

		public void setfMeasure(double[] fMeasure) {
			this.fMeasure = fMeasure;
		}

		public double getGmean() {
			return gmean;
		}

		public void setGmean(double gmean) {
			this.gmean = gmean;
		}

		public double getAuc() {
			return auc;
		}

		public void setAuc(double auc) {
			this.auc = auc;
		}

		public int getClassNum() {
			return classNum;
		}

		public void setClassNum(int classNum) {
			this.classNum = classNum;
		}
		
}
