package util;

import classification.Classification;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataProcessUtilTest {
    @Test
    public void covertDetailFileToSK_ESDFile() throws Exception {
        DataProcessUtil.covertDetailFileToSK_ESDFile("OriginDetailFiles/j48_MyAnt_DETAIL", "J_SK_ESD",
                Classification.DETAIL_NUM, Classification.METHOD_NAMES, Classification.EVALUATION_NAMES);
    }

}