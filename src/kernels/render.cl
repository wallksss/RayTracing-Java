typedef struct {
    float e[3];
} Vec3;
typedef Vec3 Color;
typedef Vec3 Point;

typedef struct {
    Point origin;
    Vec3 direction;
} Ray;

typedef struct {
    Point center;
    float radius;
} Sphere;

typedef struct {
    Sphere objects[100];
    int total_objects;
} World;

typedef struct {
    Point p;
    Vec3 normal;
    float t;
    char front_face;
} HitRecord;

inline Vec3 vec3_create(float e1, float e2, float e3) {
    Vec3 v;
    v.e[0] = e1;
    v.e[1] = e2;
    v.e[2] = e3;
    return v;
}

inline float vec3_x(const Vec3* v) {return v->e[0];}
inline float vec3_y(const Vec3* v) {return v->e[1];}
inline float vec3_z(const Vec3* v) {return v->e[2];}

inline Vec3 vec3_negate(const Vec3* v) {return vec3_create(-v->e[0], -v->e[1], -v->e[2]);}

inline Vec3* vec3_add(Vec3* u, const Vec3* v) {
    u->e[0] += v->e[0];
    u->e[1] += v->e[1];
    u->e[2] += v->e[2];
    return u;
}

inline Vec3* vec3_multiply(Vec3* v, float t) {
    v->e[0] *= t;
    v->e[1] *= t;
    v->e[2] *= t;
    return v;
}

inline Vec3* vec3_divide(Vec3* v, float t) {
    return vec3_multiply(v, 1/t);
}

inline float vec3_length(const Vec3* v) {
    return sqrt(v->e[0]*v->e[0] + v->e[1]*v->e[1] + v->e[2]*v->e[2]);
}

inline float vec3_length_squared(const Vec3* v) {
    return v->e[0]*v->e[0] + v->e[1]*v->e[1] + v->e[2]*v->e[2];
}

inline Vec3 vec3_add_vec(const Vec3* u, const Vec3* v) {
    return vec3_create(u->e[0] + v->e[0], u->e[1] + v->e[1], u->e[2] + v->e[2]);
}

inline Vec3 vec3_subtract_vec(const Vec3* u, const Vec3* v) {
    return vec3_create(u->e[0] - v->e[0], u->e[1] - v->e[1], u->e[2] - v->e[2]);
}

inline Vec3 vec3_multiply_vec(const Vec3* u, const Vec3* v) {
    return vec3_create(u->e[0] * v->e[0], u->e[1] * v->e[1], u->e[2] * v->e[2]);
}

inline Vec3 vec3_multiply_scalar(const Vec3* v, float t) {
    return vec3_create(t * v->e[0], t * v->e[1], t * v->e[2]);
}

inline Vec3 vec3_divide_scalar(const Vec3* v, float t) {
    return vec3_multiply_scalar(v, 1/t);
}

inline float vec3_dot(const Vec3* u, const Vec3* v) {
    return u->e[0] * v->e[0] + u->e[1] * v->e[1] + u->e[2] * v->e[2];
}

inline Vec3 vec3_cross(const Vec3* u, const Vec3* v) {
    return vec3_create(
        u->e[1] * v->e[2] - u->e[2] * v->e[1],
        u->e[2] * v->e[0] - u->e[0] * v->e[2],
        u->e[0] * v->e[1] - u->e[1] * v->e[0]
    );
}

inline Vec3 vec3_unit_vector(const Vec3* v) {
    return vec3_divide_scalar(v, vec3_length(v));
}

inline void vec3_print(const Vec3* v) {
    printf("%f %f %f\n", v->e[0], v->e[1], v->e[2]);
}

inline Point ray_at(Ray* r,float t) {
    Vec3 multi = vec3_multiply_scalar(&r->direction, t);
    Vec3 result = vec3_add_vec(&r->origin, &multi);
    return result;
}

inline void set_face_normal(HitRecord* rec, Ray* r, Vec3* outward_normal) {
    float operation = vec3_dot(&r->direction, outward_normal);
    rec->front_face = operation < 0 ? 1 : 0;
    Vec3 normal_negate = vec3_negate(outward_normal);
    rec->normal = rec->front_face ? *outward_normal : normal_negate;
}

typedef struct __attribute__((packed)) {
    float aspect_ratio;
    int image_width;
    int image_height;
    float view_fov;
    Vec3 direction;
    Vec3 v_up;
    int samples_per_pixel;
    int max_depth;

    float pixel_samples_scale;
    Point camera_center;
    Point pixel00;
    Vec3 pixelDeltaU;
    Vec3 pixelDeltaV;
    Vec3 u, v, w;

    float pitch;
    float yaw;
} Camera;

void write_color(__global uint* output, Color pixel, int index) {
    float r = pixel.e[0];
    float g = pixel.e[1];
    float b = pixel.e[2];

    int rbyte = (int)(255.0f * r);
    int gbyte = (int)(255.0f * g);
    int bbyte = (int)(255.0f * b);

    int pixel_color = (rbyte << 16) | (gbyte << 8) | bbyte;

    output[index] = pixel_color;
}

char hit(Sphere* sphere, Ray* r, float ray_tmin, float ray_tmax, HitRecord* rec){
    Vec3 oc = vec3_subtract_vec(&sphere->center, &r->origin);
    float a = vec3_length_squared(&r->direction);
    float h = vec3_dot(&r->direction, &oc);
    float c = vec3_length_squared(&oc) - sphere->radius * sphere->radius;
    float discriminant = h * h - a * c;
    if(discriminant < 0){
        return 0;
    }

    float sqrtd = sqrt(discriminant);
    float root = (h - sqrtd) / a;

    if(root <= ray_tmin || ray_tmax <= root) {
        root = (h + sqrtd) / a;
        if(root <= ray_tmin || ray_tmax <= root) {
            return 0;
        }
    }
    rec->t = root;
    rec->p = ray_at(r, rec->t);
    Vec3 sub = vec3_subtract_vec(&rec->p, &sphere->center);
    Vec3 outward_normal = vec3_divide_scalar(&sub, sphere->radius);
    rec->normal = outward_normal;
    set_face_normal(rec, r, &outward_normal);

    return 1;
}

char world_hit(World* world, Ray* r, float ray_tmin, float ray_tmax, HitRecord* rec) {
    HitRecord temp_record;
    HitRecord* temp_rec;
    temp_rec = &temp_record;
    char hit_anything = 0;
    float closest_so_far = ray_tmax;

    for(int i = 0; i < world->total_objects; i++) {
        Sphere sphere = world->objects[i];
        if(hit(&sphere, r, ray_tmin, closest_so_far, temp_rec)) {
            hit_anything = 1;
            closest_so_far = temp_rec->t;
            *rec = *temp_rec;
        }
    }
    return hit_anything;
}

Color ray_color(Ray* r, World* world) {
    HitRecord record;
    HitRecord *rec;
    rec = &record;
    if(world_hit(world, r, 0.0001, INFINITY, rec)) {
        Color color1 = vec3_create(1, 1, 1);
        Vec3 add = vec3_add_vec(&rec->normal, &color1);
        Vec3 result = vec3_multiply_scalar(&add, 0.5);
        return result;
    }

    Vec3 unit_direction = vec3_unit_vector(&r->direction);
    float a = 0.5*(unit_direction.e[1] + 1);
    Color color1 = vec3_create(1.0, 1.0, 1.0);
    Color color2 = vec3_create(0.5, 0.7, 1.0);
    Vec3 vec1 = vec3_multiply_scalar(&color1, (1 - a));
    Vec3 vec2 = vec3_multiply_scalar(&color2, a);
    return vec3_add_vec(&vec1, &vec2);
}

__kernel void render_kernel(__global uint* output, int width, int height, __global Camera* camera) {
    unsigned int x = get_global_id(0);
    unsigned int y = get_global_id(1);
    int index = y * width + x;
    Camera local_camera = *camera; //shallow copy (be careful)

    Vec3 cameraX = vec3_multiply_scalar(&local_camera.pixelDeltaU, x);
    Vec3 cameraY = vec3_multiply_scalar(&local_camera.pixelDeltaV, y);

    Point pixel_center = vec3_add_vec(&local_camera.pixel00, &cameraX);
    pixel_center = vec3_add_vec(&pixel_center, &cameraY);

    Vec3 ray_direction = vec3_subtract_vec(&pixel_center, &local_camera.camera_center);
    Ray r;
    r.origin = local_camera.camera_center;
    r.direction = ray_direction;

    World world;
    Sphere sphere1;
    sphere1.center = vec3_create(0, 0, -1);
    sphere1.radius = 0.5;
    Sphere sphere2;
    sphere2.center = vec3_create(0, -100.5, -1);
    sphere2.radius = 100;
    world.objects[0] = sphere1;
    world.objects[1] = sphere2;
    world.total_objects = 2;

    Color pixel_color = ray_color(&r, &world);
    write_color(output, pixel_color, index);
}

