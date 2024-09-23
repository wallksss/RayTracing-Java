public class Interval {
    public double min;
    public double max;

    public Interval() {
        min = Util.INFINITY;
        max = Util.NEGATIVE_INFINITY;
    }

    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    double size() {
        return max - min;
    }

    boolean contains(double x) {
        return x >= min && x <= max;
    }

    boolean surrounds(double x) {
        return x > min && x < max;
    }

    public static final Interval EMPTY = new Interval(Util.INFINITY, Util.NEGATIVE_INFINITY);
    public static final Interval UNIVERSE = new Interval(Util.NEGATIVE_INFINITY, Util.INFINITY);
}
