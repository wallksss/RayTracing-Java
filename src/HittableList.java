import java.util.ArrayList;
import java.util.List;

public class HittableList extends Hittable{
    public final List<Hittable> objects;

    public HittableList(){
        this.objects = new ArrayList<>();
    }

    public HittableList(Hittable object) {
        this.objects = new ArrayList<>();
        objects.add(object);
    }

    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray r, Interval ray_t, HitRecord rec) {
        HitRecord temp_record = new HitRecord();
        boolean hit_anything = false;
        double closest_so_far = ray_t.max;

        for(Hittable object : objects){
            if(object.hit(r, new Interval(ray_t.min, closest_so_far), temp_record)){
                hit_anything = true;
                closest_so_far = temp_record.t;

                rec.p = temp_record.p;
                rec.normal = temp_record.normal;
                rec.material = temp_record.material;
                rec.t = temp_record.t;
                rec.front_face = temp_record.front_face;
            }
        }
        return hit_anything;
    }
}
