package utils;

/**
 * @author Zewen Xu
 *
 * Generate random value
 */
public class RandomUtil {
	public static int getRandomInt(int range){
		return (int) (Math.random()*range);
	}
	
	public static double getRandomDouble(double range){
		return Math.random()*range;
	}
}
