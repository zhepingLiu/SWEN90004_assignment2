package utils;

public class RandomUtil {
	public static int getRandomInt(int range){
		return (int) (Math.random()*range);
	}
	
	public static double getRandomDouble(int range){
		return Math.random()*range;
	}
}
