package carrot.ckl.helpers;

public class MathsHelper {
    public static int clamp(int value, int min, int max) {
        if (value > max) return max;
        if (value < min) return min;
        return value;
    }

    public static boolean isBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean isOutside(double value, double min, double max) {
        return value < min || value > max;
    }
}
