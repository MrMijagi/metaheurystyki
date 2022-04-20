package sa;

public class TemperatureMethods {

	public static double linear(double t_start, double t_end, int t, int iterations) {
		return t_start - ((t_start - t_end) * ((t * 2) / (double) iterations));
	}
	
	public static double square_root(double t_start, double t_end, int t, int iterations) {
		return t_start - ((t_start - t_end) * Math.sqrt((t * 2) / (double) iterations));
	}
	
}
