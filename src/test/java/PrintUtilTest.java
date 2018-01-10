import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.PrintUtil;

public class PrintUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testFormatDouble() {
		String outPutString = PrintUtil.formatDouble(3, 5.467231);
		Assert.assertTrue(outPutString.equals("5.467"));
		outPutString = PrintUtil.formatDouble(2, 5.467);
		Assert.assertTrue(outPutString.equals("5.47"));
	}

}
