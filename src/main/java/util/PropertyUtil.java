package util;

import java.util.Arrays;
import java.util.List;

public class PropertyUtil {
    public static int PENCENTAGE_OF_CONCERN = 20;
    public static boolean CALCULATION_COST = true;
    public static String FOR_TWO_RANK = "For_Two_Rank";
    public static String DETAIL_FOLDER_PATH = "DetailFiles";
    public static String COST_FOLDER_PATH = "CostFiles";
    public static String RESULT_FOLDER_PATH = "ResultFiles";
    public static String DEFAULT_DETAIL_FOLDER = "DetailFiles";
    public static String DEFAULT_SKESD_FOLDER = "SK_ESD";
    public static String CUR_DETAIL_FILENAME = "";
    public static String CUR_COST_EFFECTIVE_RECORD = "";
    public static String CUR_COST_20PB_SK_ONE = "";
    public static String LOC_FILE_PATH = "LOCFiles";
    public static String ARFF_PATH = "Arffs_old_paper";
    public static String AVG_NAME = "Avg";
    public static String FILE_NAME_DELIMITER = "_";
    public static String FILE_PATH_DELIMITER = "/";
    public static String[] indicators = {"recall-1", "precision-1", "fMeasure-1", "auc"};
    public static String[] BASE_LEARNERS = {"j48", "RF", "naivebayes", "smo"};
    //public static String[] BASE_LEARNERS = {"naivebayes", "smo"};
    public static String[] PROJECTS = {"MyAnt", "MyBuck", "MyFlink", "MyHadoop", "MyItextpdf", "MyJedit", "MyLucene",
            "MySynapse", "MyTomcat", "MyVoldemort"};

    //Be careful to change!
    public static String[] METHOD_NAMES = {"Simple", "ROS", "RUS", "Smote", "Bag", "ROSBag", "RUSBag", "SmoteBag",
            "Boost", "ROSBoost", "RUSBoost", "SmoteBoost"};

    //2,3,6,7,10,11 is no use.
    public static boolean[] METHOD_USE_MAP = {true, true, true, true, true, true, true, true, true, true, true, true};
}
