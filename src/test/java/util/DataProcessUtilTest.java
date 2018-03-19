package util;

import classification.Classification;
import org.junit.Assert;
import org.junit.Test;
import resample.OverSubsample;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.supervised.instance.SpreadSubsample;

import java.io.*;
import java.util.Map;

public class DataProcessUtilTest {

    public void covertAllDetailFileToSK_ESDFile() throws Exception {
        DataProcessUtil.covertAllDetailFileToSK_ESDFile("testCovert", "SK_ESD",
                Classification.DETAIL_NUM, PropertyUtil.METHOD_NAMES, Classification.EVALUATION_NAMES);
    }

    public void covertDetailFileToSK_ESDFile() throws Exception {
        DataProcessUtil.covertDetailFileToSK_ESDFile("OriginDetailFiles/j48_MyAnt_DETAIL", "J_SK_ESD",
                Classification.DETAIL_NUM, PropertyUtil.METHOD_NAMES, Classification.EVALUATION_NAMES);
    }

    @Test
    public void testDoubleArrayAdd() {
        double[] array = new double[2];
        array[0] = 0.3;
        array[1] = 1.3;
        Assert.assertEquals(++array[0], 1.3, 0.001);
        Assert.assertEquals(array[0], 1.3, 0.001);
    }

    @Test
    public void testResample() throws Exception {
        BufferedReader bReader = new BufferedReader(new FileReader(new File("TestFolder/MyVoldemort.arff")));
        Instances data = new Instances(bReader);
        bReader.close();
        data.setClassIndex(data.numAttributes() - 1);
        System.out.println("Total number of instances in Arff file : " + data.numInstances());
        AttributeStats as = data.attributeStats(data.numAttributes() - 1);
        int count[] = as.nominalCounts;
        int bugNum = count[1];
        int cleanNum = count[0];
        System.out.println("Number of buggy instances: " + count[1]);

        System.out.println("Test OverSubsample");
        OverSubsample oversample = new OverSubsample();
        oversample.setInputFormat(data);
        oversample.setDistributionSpread(1);
        Instances tmpData = Filter.useFilter(data, oversample);
        AttributeStats asTmp = tmpData.attributeStats(data.numAttributes() - 1);
        Assert.assertEquals(asTmp.nominalCounts[1], cleanNum);
        Assert.assertEquals(asTmp.nominalCounts[0], cleanNum);

        System.out.println("Test SMOTE");
        SMOTE smotesample = new SMOTE();
        smotesample.setInputFormat(data);
        double percent = ((double) 1 / 2 * count[0] - count[1]) / count[1] * 100;
        smotesample.setPercentage(percent);
        tmpData = Filter.useFilter(data, smotesample);
        asTmp = tmpData.attributeStats(data.numAttributes() - 1);
        Assert.assertEquals(asTmp.nominalCounts[1], cleanNum / 2);
        Assert.assertEquals(asTmp.nominalCounts[0], cleanNum);

        SpreadSubsample undersample = new SpreadSubsample();
        undersample.setInputFormat(data);
        undersample.setDistributionSpread(2);
        tmpData = Filter.useFilter(data, undersample);
        asTmp = tmpData.attributeStats(data.numAttributes() - 1);
        System.out.println(asTmp.nominalCounts[0] + "," + asTmp.nominalCounts[1]);
        Assert.assertEquals(asTmp.nominalCounts[1], bugNum);
        Assert.assertEquals(asTmp.nominalCounts[0], bugNum*2);
    }

}