public class Interval {
    public float min;
    public float max;

    public Interval() {
        min = Util.INFINITY;
        max = Util.NEGATIVE_INFINITY;
    }

    public Interval(float min, float max) {
        this.min = min;
        this.max = max;
    }

    float size() {
        return max - min;
    }

    boolean contains(float x) {
        return x >= min && x <= max;
    }

    boolean surrounds(float x) {
        return x > min && x < max;
    }

    float clamp(float x) {
        if(x < min) return min;
        if(x > max) return max;
        return x;
    }

    public static final Interval EMPTY = new Interval(Util.INFINITY, Util.NEGATIVE_INFINITY);
    public static final Interval UNIVERSE = new Interval(Util.NEGATIVE_INFINITY, Util.INFINITY);
}
