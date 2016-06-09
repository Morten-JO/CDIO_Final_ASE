package input;

import java.math.BigDecimal;

public class HelpFunctions {

	public static double roundDouble(double orig, int places){
		BigDecimal big = new BigDecimal(orig);
		big = big.setScale(places, BigDecimal.ROUND_HALF_UP);
		return big.doubleValue();
	}
	
}
