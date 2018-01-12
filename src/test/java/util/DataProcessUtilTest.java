package util;

import classification.Classification;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataProcessUtilTest {

    public void covertAllDetailFileToSK_ESDFile() throws Exception {
        DataProcessUtil.covertAllDetailFileToSK_ESDFile("testCovert", "SK_ESD",
                Classification.DETAIL_NUM, Classification.METHOD_NAMES, Classification.EVALUATION_NAMES);
    }

    public void covertDetailFileToSK_ESDFile() throws Exception {
        DataProcessUtil.covertDetailFileToSK_ESDFile("OriginDetailFiles/j48_MyAnt_DETAIL", "J_SK_ESD",
                Classification.DETAIL_NUM, Classification.METHOD_NAMES, Classification.EVALUATION_NAMES);
    }

    @Test
    public void testDoubleArrayAdd() {
        double[] array = new double[2];
        array[0] = 0.3;
        array[1] = 1.3;
        Assert.assertEquals(++array[0], 1.3, 0.001);
        Assert.assertEquals(array[0], 1.3, 0.001);
    }

}