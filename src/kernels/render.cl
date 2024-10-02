__kernel void render(
    __global float4* image,
    float3 pixel00,
    float3 pixelDeltaU, float3 pixelDeltaV,
    float3 cameraCenter,
    int image_width, int image_height,
    int samples_per_pixel, double pixel_samples_scale,
    int max_depth
    )
{
    int i = get_global_id(0);
    int j = get_global_id(1);
    if (i >= image_width || j >= image_height) return;

    float3 pixelColor = (float3)(1.0f, 0.0f, 0.0f);
    for(int sample = 0; sample < samples_per_pixel; sample++) {
        //Ray r = getRay(i, j, pixel00, pixelDeltaU, pixelDeltaV, cameraCenter);
        //pixelColor += rayColor(r, max_depth, world);
    }

    pixelColor *= (float)pixel_samples_scale;
    image[j * image_width + i] = (float4)(pixelColor, 1.0f);
}