typedef struct {
    float e[3];
} Vec3;
typedef Vec3 Color;
typedef Vec3 Point;

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


typedef struct {
    Point origin;
    Color direction;
} Ray;

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

int hit_sphere(Point* center, float radius, Ray* r){
    Vec3 oc = vec3_subtract_vec(center, &r->origin);
    float a = vec3_dot(&r->direction, &r->direction);
    float b = vec3_dot(&r->direction, &oc) * -2.0;
    float c = vec3_dot(&oc, &oc) - radius * radius;
    float discriminant = b*b - 4*a*c;
    if(discriminant >= 0) return 1;
    return 0;
}

Color ray_color(Ray* r) {
    Point esfera = vec3_create(0, 0, -1);
    if(hit_sphere(&esfera, 0.5, r))
        return vec3_create(1.0, 0, 0);

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
    Color pixel_color = ray_color(&r);
    write_color(output, pixel_color, index);
}

