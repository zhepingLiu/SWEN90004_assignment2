
import java.util.Random;

/**
 *  Generate random value
 */
public class RandomUtil {
	static Random random = new Random(System.currentTimeMillis());

	public static int getRandomInt(int range) {
		return (int) (random.nextDouble() * range);
	}

	public static double getRandomDouble(double range) {
		return random.nextDouble() * range;
	}
}
