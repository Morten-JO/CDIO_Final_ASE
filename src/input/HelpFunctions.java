package input;

import java.math.BigDecimal;

public class HelpFunctions {

	public static double roundDouble(double orig, int places){
		BigDecimal big = new BigDecimal(orig);
		big = big.setScale(places, BigDecimal.ROUND_HALF_UP);
		return big.doubleValue();
	}
	
	//used to cut strings down to values that can be in a RM20 question
	public static String cutStringRM20(String orig){
		return orig.substring(0, 22);
	}
	
}
