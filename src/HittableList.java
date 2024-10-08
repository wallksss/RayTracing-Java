import java.util.ArrayList;
import java.util.List;

public class HittableList{
    public final List<Sphere> objects;

    public HittableList(){
        this.objects = new ArrayList<>();
    }

    public HittableList(Sphere object) {
        this.objects = new ArrayList<>();
        objects.add(object);
    }

    public void clear() {
        objects.clear();
    }

    public void add(Sphere object) {
        objects.add(object);
    }
}
