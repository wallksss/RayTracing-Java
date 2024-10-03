typedef struct {
    float x;
    float y;
    float z;
} Vec3;
typedef Vec3 Color;
typedef Vec3 Point;

inline Vec3 vec3_create(float e1, float e2, float e3) {
    Vec3 v;
    v.x = e1;
    v.y = e2;
    v.z = e3;
    return v;
}

inline float vec3_x(const Vec3* v) {return v->x;}
inline float vec3_y(const Vec3* v) {return v->y;}
inline float vec3_z(const Vec3* v) {return v->z;}

inline Vec3 vec3_negate(const Vec3* v) {return vec3_create(-v->x, -v->y, -v->z);}

inline Vec3* vec3_add(Vec3* u, const Vec3* v) {
    u->x += v->x;
    u->y += v->y;
    u->z += v->z;
    return u;
}

inline Vec3* vec3_multiply(Vec3* v, float t) {
    v->x *= t;
    v->y *= t;
    v->z *= t;
    return v;
}

inline Vec3* vec3_divide(Vec3* v, float t) {
    return vec3_multiply(v, 1/t);
}

inline float vec3_length(const Vec3* v) {
    return sqrt(v->x*v->x + v->y*v->y + v->z*v->z);
}

inline float vec3_length_squared(const Vec3* v) {
    return v->x*v->x + v->y*v->y + v->z*v->z;
}

inline Vec3 vec3_add_vec(const Vec3* u, const Vec3* v) {
    return vec3_create(u->x + v->x, u->y + v->y, u->z + v->z);
}

inline Vec3 vec3_subtract_vec(const Vec3* u, const Vec3* v) {
    return vec3_create(u->x - v->x, u->y - v->y, u->z - v->z);
}

inline Vec3 vec3_multiply_vec(const Vec3* u, const Vec3* v) {
    return vec3_create(u->x * v->x, u->y * v->y, u->z * v->z);
}

inline Vec3 vec3_multiply_scalar(const Vec3* v, float t) {
    return vec3_create(t * v->x, t * v->y, t * v->z);
}

inline Vec3 vec3_divide_scalar(const Vec3* v, float t) {
    return vec3_multiply_scalar(v, 1/t);
}

inline float vec3_dot(const Vec3* u, const Vec3* v) {
    return u->x * v->x + u->y * v->y + u->z * v->z;
}

inline Vec3 vec3_cross(const Vec3* u, const Vec3* v) {
    return vec3_create(
        u->y * v->z - u->z * v->y,
        u->z * v->x - u->x * v->z,
        u->x * v->y - u->y * v->x
    );
}

inline Vec3 vec3_unit_vector(const Vec3* v) {
    return vec3_divide_scalar(v, vec3_length(v));
}

inline void vec3_print(const Vec3* v) {
    printf("%f %f %f\n", v->x, v->y, v->z);
}


typedef struct {
    Point origin;
    Color direction;
} Ray;

typedef struct {
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
    float r = pixel.x;
    float g = pixel.y;
    float b = pixel.z;

    int rbyte = (int)(255.0f * r);
    int gbyte = (int)(255.0f * g);
    int bbyte = (int)(255.0f * b);

    int pixel_color = (rbyte << 16) | (gbyte << 8) | bbyte;

    output[index] = pixel_color;
}

Color ray_color(Ray r) {
    Vec3 unit_direction = vec3_unit_vector(&r.direction);
    float a = 0.5*(unit_direction.y + 1);
    Color color1 = vec3_create(1.0, 1.0, 1.0);
    Color color2 = vec3_create(0.5, 0.7, 1.0);
    Vec3 vec1 = vec3_multiply_scalar(&color1, (1 - a));
    Vec3 vec2 = vec3_multiply_scalar(&color2, a);
    return vec3_add_vec(&vec1, &vec2); 
}

__kernel void render_kernel(__global uint* output, int width, int height, __global float* aspect_ratio) {
    unsigned int x = get_global_id(0);
    unsigned int y = get_global_id(1);

    int index = y * width + x;
    Camera local_camera;
    //printf("%d\n", camera->image_width);
    // local_camera.pixelDeltaU = camera->pixelDeltaU;
    // local_camera.pixelDeltaV = camera->pixelDeltaV;
    // local_camera.camera_center = camera->camera_center;
    local_camera.pixel00 = vec3_create(-1.7764, 0.9986, -1.0);
    local_camera.pixelDeltaU = vec3_create(0.0028, 0.0, 0.0);
    local_camera.pixelDeltaV = vec3_create(0.0, -0.0028, 0.0);
    local_camera.camera_center = vec3_create(0, 0, 0);

    Vec3 cameraX = vec3_multiply_scalar(&local_camera.pixelDeltaU, x);
    Vec3 cameraY = vec3_multiply_scalar(&local_camera.pixelDeltaV, y);

    Point pixel_center = vec3_add_vec(&local_camera.pixel00, &cameraX);
    pixel_center = vec3_add_vec(&pixel_center, &cameraY);
    
    Vec3 ray_direction = vec3_subtract_vec(&pixel_center, &local_camera.camera_center);
    Ray r;
    r.origin = local_camera.camera_center;
    r.direction = ray_direction;
    Color pixel_color = ray_color(r);
    write_color(output, pixel_color, index);
}

