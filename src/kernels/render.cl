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
    int material;
} Sphere;

#define LAMBERTIAN 1
#define METAL 2
#define DIELETRIC 3


typedef struct {
    Sphere objects[100];
    int total_objects;
} World;

typedef struct {
    float min;
    float max;
} Interval;

typedef struct {
    Point p;
    Vec3 normal;
    float t;
    char front_face;
} HitRecord;

Interval make_empty_interval() {
    Interval result;
    result.min = +FLT_MAX;
    result.max = -FLT_MAX;
    return result;
}

Interval make_interval(float min, float max) {
    Interval result;
    result.min = min;
    result.max = max;
    return result;
}

float interval_size(Interval* i) {
    return i->max - i->min;
}

int interval_contains(Interval* i, float x) {
    return (i->min <= x) && (x <= i->max);
}

int interval_surrounds(Interval* i, float x) {
    return (i->min < x) && (x < i->max);
}

float interval_clamp(Interval* i, float x) {
    if(x < i->min) return i->min;
    if(x > i->max) return i->max;
    return x;
}

float random_float(mrg31k3p_state* state) {
    float random = mrg31k3p_float(*state);
    return random;
}

float random_float_interval(mrg31k3p_state* state,float min, float max) {
    float random = min + (max - min) * random_float(state);
    return random;
}

__constant Interval interval_empty = { +FLT_MAX, -FLT_MAX };
__constant Interval interval_universe = { -FLT_MAX, +FLT_MAX };

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

Vec3 vec3_random(mrg31k3p_state* state) {
    Vec3 random_vec = vec3_create(random_float(state), random_float(state), random_float(state));
    return random_vec;
}

Vec3 vec3_random_interval(mrg31k3p_state* state, float min, float max) {
    Vec3 random_vec = vec3_create(random_float_interval(state, min, max), random_float_interval(state, min, max), random_float_interval(state, min, max));
    return random_vec;
}

inline Vec3 random_unit_vector(mrg31k3p_state* state) {
    while(true) {
        Vec3 p = vec3_random_interval(state, -1, 1);
        float lensq = vec3_length_squared(&p);
        if(1e-38f < lensq && lensq <= 1)
            return vec3_divide_scalar(&p, sqrt(lensq));
    }
}

inline Vec3 random_on_hemisphere(mrg31k3p_state* state, Vec3* normal) {
    Vec3 on_unit_sphere = random_unit_vector(state);
    if(vec3_dot(&on_unit_sphere, normal) > 0.0)
        return on_unit_sphere;
    else
        return vec3_negate(&on_unit_sphere);
}

Vec3 vec3_sample_square(mrg31k3p_state* state) {
    float a = random_float(state);
    float b = random_float(state);
    return vec3_create(a - 0.5, b - 0.5, 0);
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

inline float degrees_to_radian(float degrees) {
    return degrees * M_PI_F / 180;
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

inline float linear_to_gamma(float linear_component) {
    if(linear_component > 0) {
        return sqrt(linear_component);
    }
    return 0;
}

void write_color(__global uint* output, Color pixel, int index) {
    float r = pixel.e[0];
    float g = pixel.e[1];
    float b = pixel.e[2];

    r = linear_to_gamma(r);
    g = linear_to_gamma(g);
    b = linear_to_gamma(b);

    Interval intensity = make_interval(0.000, 0.9999);
    int rbyte = (int)(256 * interval_clamp(&intensity, r));
    int gbyte = (int)(256 * interval_clamp(&intensity, g));
    int bbyte = (int)(256 * interval_clamp(&intensity, b));

    int pixel_color = (rbyte << 16) | (gbyte << 8) | bbyte;

    output[index] = pixel_color;
}

char hit(Sphere* sphere, Ray* r, Interval* ray_t, HitRecord* rec){
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

    if(!interval_surrounds(ray_t, root)){
            root = (h + sqrtd) / a;
            if(!interval_surrounds(ray_t, root))
                return 0;
    }

    rec->t = root;
    rec->p = ray_at(r, rec->t);
    Vec3 sub = vec3_subtract_vec(&rec->p, &sphere->center);
    Vec3 outward_normal = vec3_divide_scalar(&sub, sphere->radius);
    rec->normal = outward_normal;
    set_face_normal(rec, r, &outward_normal);

    return 1;
}

char world_hit(World* world, Ray* r, Interval* ray_t, HitRecord* rec) {
    HitRecord temp_record;
    HitRecord* temp_rec;
    temp_rec = &temp_record;
    char hit_anything = 0;
    float closest_so_far = ray_t->max;

    for(int i = 0; i < world->total_objects; i++) {
        Sphere sphere = world->objects[i];
        Interval interval = make_interval(ray_t->min, closest_so_far);
        if(hit(&sphere, r, &interval, temp_rec)) {
            hit_anything = 1;
            closest_so_far = temp_rec->t;
            *rec = *temp_rec;
        }
    }
    return hit_anything;
}

Color ray_color(Ray* r, World* world, mrg31k3p_state* state, int max_depth) {
    HitRecord record;
    HitRecord *rec;
    rec = &record;
    Ray current_ray = *r;
    Interval interval = make_interval(0.001, INFINITY);
    Color accumulated_color = vec3_create(1, 1 ,1);
    int depth = 0;

    while(depth < max_depth) {
        if(world_hit(world, &current_ray, &interval, rec)) {
            Vec3 random = random_unit_vector(state);
            Vec3 direction = vec3_add_vec(&random, &rec->normal);
            current_ray.origin = rec->p;
            current_ray.direction = direction;
            accumulated_color = vec3_multiply_scalar(&accumulated_color, 0.5);
        } else {
            Vec3 unit_direction = vec3_unit_vector(&current_ray.direction);
            float a = 0.5*(unit_direction.e[1] + 1);
            Color color1 = vec3_create(1.0, 1.0, 1.0);
            Color color2 = vec3_create(0.5, 0.7, 1.0);
            Vec3 vec1 = vec3_multiply_scalar(&color1, (1 - a));
            Vec3 vec2 = vec3_multiply_scalar(&color2, a);
            Vec3 background_color = vec3_add_vec(&vec1, &vec2);
            return vec3_multiply_vec(&accumulated_color, &background_color);
        }
        depth++;
    }
    return vec3_create(0, 0, 0);
}

Ray get_ray(Camera* camera, mrg31k3p_state* state, int i, int j) {
    Vec3 offset = vec3_sample_square(state);

    Vec3 offsetX = vec3_multiply_scalar(&camera->pixelDeltaU, i + offset.e[0]);
    Vec3 offsetY = vec3_multiply_scalar(&camera->pixelDeltaV, j + offset.e[1]);

    Vec3 offset_add = vec3_add_vec(&offsetX, &offsetY);
    Point pixel_sample = vec3_add_vec(&camera->pixel00, &offset_add);

    Point ray_origin = camera->camera_center;
    Vec3 ray_direction = vec3_subtract_vec(&pixel_sample, &ray_origin);

    Ray ray;
    ray.origin = ray_origin;
    ray.direction = ray_direction;  
    return ray;
}


__kernel void render_kernel(__global uint* output, int width, int height, __global Camera* camera, __global int* seeds) {
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

    mrg31k3p_state state;
    mrg31k3p_seed(&state, seeds[index]);

    Color pixel_color = vec3_create(0, 0, 0);
    for(int sample = 0; sample < local_camera.samples_per_pixel; sample++) {
        Ray r = get_ray(&local_camera, &state, x, y);
        Color new_color = ray_color(&r, &world, &state, local_camera.max_depth);
        pixel_color = vec3_add_vec(&pixel_color, &new_color);
    }
    Color final_color = vec3_multiply_scalar(&pixel_color, local_camera.pixel_samples_scale);
    write_color(output, final_color, index);
}