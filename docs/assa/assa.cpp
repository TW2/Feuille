#include <iostream>
#include "ass/ass.h"
#include "ass/ass_types.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <png.h>

//==============================================================
//== STRUCTS AND INSTANCES                                    ==
//==============================================================

typedef struct image_s {
    int width, height, stride;
    unsigned char *buffer;      // RGBA32
} image_t;

ASS_Library *ass_library;
ASS_Renderer *ass_renderer;


//==============================================================
//== FUNCTIONS                                                ==
//==============================================================

// void say_hello(){
//     std::cout << "Hello, from assa!\n";
// }

bool init(int frame_w, int frame_h){
    // Initialize ASS library
    ass_library = ass_library_init();

    // Check if library works otherwise returns a false value
    if (!ass_library) {
        printf("An error has ocurred -> ass_library failed to initialize.\n");
        return false;
    }

    // Set up fonts
    ass_set_extract_fonts(ass_library, 1);

    // Initialize ASS renderer
    ass_renderer = ass_renderer_init(ass_library);

    // Check if renderer works otherwise returns a false value
    if (!ass_renderer) {
        printf("An error has ocurred -> ass_renderer failed to initialize.\n");
        return false;
    }

    ass_set_storage_size(ass_renderer, frame_w, frame_h);
    ass_set_frame_size(ass_renderer, frame_w, frame_h);
    ass_set_fonts(ass_renderer, NULL, "sans-serif",
                  ASS_FONTPROVIDER_AUTODETECT, NULL, 1);

    return true;
}

void write_png(char *fname, image_t *img){

    png_structp png_ptr = NULL;
    png_infop info_ptr = NULL;
    png_byte **volatile row_pointers = NULL;

    FILE *fp = fopen(fname, "wb");
    if (fp == NULL) {
        printf("An error has ocurred -> PNG Error opening %s for writing!\n", fname);
        goto fail;
    }

    png_ptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
    if (!png_ptr) {
        printf("An error has ocurred -> PNG Error creating write struct!\n");
        goto fail;
    }

    info_ptr = png_create_info_struct(png_ptr);
    if (!info_ptr) {
        printf("An error has ocurred -> PNG Error creating info struct!\n");
        goto fail;
    }

    row_pointers = (png_byte **)malloc(img->height * sizeof(png_byte *));
    if (!row_pointers) {
        printf("An error has ocurred -> PNG Failed to allocate row pointers!\n");
        goto fail;
    }
    for (int k = 0; k < img->height; k++)
        row_pointers[k] = img->buffer + img->stride * k;


    if (setjmp(png_jmpbuf(png_ptr))) {
        printf("An error has ocurred -> PNG unknown error!\n");
        goto fail;
    }

    png_init_io(png_ptr, fp);
    png_set_compression_level(png_ptr, 9);

    png_set_IHDR(png_ptr, info_ptr, img->width, img->height,
                 8, PNG_COLOR_TYPE_RGBA, PNG_INTERLACE_NONE,
                 PNG_COMPRESSION_TYPE_DEFAULT, PNG_FILTER_TYPE_DEFAULT);

    png_write_info(png_ptr, info_ptr);

    png_write_image(png_ptr, row_pointers);
    png_write_end(png_ptr, info_ptr);

fail:
    free(row_pointers);

    if (png_ptr && !info_ptr)
        png_destroy_write_struct(&png_ptr, NULL);
    else if (png_ptr && info_ptr)
        png_destroy_write_struct(&png_ptr, &info_ptr);

    if (fp)
        fclose(fp);
}

image_t *gen_image(int width, int height)
{
    image_t *img = static_cast<image_t *>(malloc(sizeof(image_t)));
    img->width = width;
    img->height = height;
    img->stride = width * 4;
    img->buffer = static_cast<unsigned char *>(calloc(1, height * width * 4));
    return img;
}

void blend_single(image_t * frame, ASS_Image *img)
{
    unsigned char r = img->color >> 24;
    unsigned char g = (img->color >> 16) & 0xFF;
    unsigned char b = (img->color >> 8) & 0xFF;
    unsigned char a = 255 - (img->color & 0xFF);

    unsigned char *src = img->bitmap;
    unsigned char *dst = frame->buffer + img->dst_y * frame->stride + img->dst_x * 4;

    for (int y = 0; y < img->h; ++y) {
        for (int x = 0; x < img->w; ++x) {
            unsigned k = ((unsigned) src[x]) * a;
            // For high-quality output consider using dithering instead;
            // this static offset results in biased rounding but is faster
            unsigned rounding_offset = 255 * 255 / 2;
            // If the original frame is not in premultiplied alpha, convert it beforehand or adjust
            // the blending code. For fully-opaque output frames there's no difference either way.
            dst[x * 4 + 0] = (k *   r + (255 * 255 - k) * dst[x * 4 + 0] + rounding_offset) / (255 * 255);
            dst[x * 4 + 1] = (k *   g + (255 * 255 - k) * dst[x * 4 + 1] + rounding_offset) / (255 * 255);
            dst[x * 4 + 2] = (k *   b + (255 * 255 - k) * dst[x * 4 + 2] + rounding_offset) / (255 * 255);
            dst[x * 4 + 3] = (k * 255 + (255 * 255 - k) * dst[x * 4 + 3] + rounding_offset) / (255 * 255);
        }
        src += img->stride;
        dst += frame->stride;
    }
}

void blend(image_t * frame, ASS_Image *img)
{
    int cnt = 0;
    while (img) {
        blend_single(frame, img);
        ++cnt;
        img = img->next;
    }
    printf("%d images blended\n", cnt);

    // Convert from pre-multiplied to straight alpha
    // (not needed for fully-opaque output)
    for (int y = 0; y < frame->height; y++) {
        unsigned char *row = frame->buffer + y * frame->stride;
        for (int x = 0; x < frame->width; x++) {
            const unsigned char alpha = row[4 * x + 3];
            if (alpha) {
                // For each color channel c:
                //   c = c / (255.0 / alpha)
                // but only using integers and a biased rounding offset
                const uint32_t offs = (uint32_t) 1 << 15;
                uint32_t inv = ((uint32_t) 255 << 16) / alpha + 1;
                row[x * 4 + 0] = (row[x * 4 + 0] * inv + offs) >> 16;
                row[x * 4 + 1] = (row[x * 4 + 1] * inv + offs) >> 16;
                row[x * 4 + 2] = (row[x * 4 + 2] * inv + offs) >> 16;
            }
        }
    }
}

///// INPUTS ///////////////////////////////////////////////////

bool set_and_get(char* png_file, char* sub_file, double seconds, int width, int height){

    // Check resolution, returns false if there is an unauthorized state
    if(width <= 0 || height <= 0){
        return false;
    }

    // Initialize state
    bool res = init(width, height);
    if(res == false){
        return false;
    }

    // Initialize a new track, if there is no track return false
    ASS_Track *track = ass_read_file(ass_library, sub_file, NULL);
    if (!track) {
        printf("An error has ocurred -> track init failed!\n");
        return false;
    }

    // Search moment and render an image
    ASS_Image *img = ass_render_frame(ass_renderer, track, (int) (seconds * 1000), NULL);
    image_t *frame = gen_image(width, height);
    blend(frame, img);

    // Free resources
    ass_free_track(track);
    ass_renderer_done(ass_renderer);
    ass_library_done(ass_library);

    // Write date and free resources
    write_png(png_file, frame);
    free(frame->buffer);
    free(frame);

    // All it done! Good job!
    return true;
}