import java.util.ArrayList;
import java.util.List;

public class HittableList extends Hittable{
    private List<Hittable> objects;

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
    public boolean hit(Ray r, double rayTMin, double rayTMax, HitRecord rec) {
        HitRecord temp_record = new HitRecord();
        boolean hit_anything = false;
        double closest_so_far = rayTMax;

        for(Hittable object : objects){
            if(object.hit(r, rayTMin, closest_so_far, temp_record)){
                hit_anything = true;
                closest_so_far = temp_record.t;
                rec.t = temp_record.t;
                rec.p = temp_record.p;
                rec.normal = temp_record.normal;
            }
        }
        return hit_anything;
    }
}
