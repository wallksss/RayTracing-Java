__kernel void render_kernel(__global uint* output, int width, int height) {
    unsigned int x = get_global_id(0);
    unsigned int y = get_global_id(1);

    int index = y * width + x;

    float fx = (float)x / (float)width;
    float fy = (float)y / (float)height;
    unsigned int r = (unsigned int)(fx * 255.0f);
    unsigned int g = (unsigned int)(fy * 255.0f);
    unsigned int b = 0;

    unsigned int rgb = (r << 16) | (g << 8) | b;
    output[index] = rgb;
}