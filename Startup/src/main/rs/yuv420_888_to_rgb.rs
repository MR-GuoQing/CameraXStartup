  #pragma version(1)
  #pragma rs java_package_name(com.zgq.camerax.startup.util);
  #pragma rs_fp_relaxed

  uint uvPixelStride, uvRowStride ;
  rs_allocation yAllocation,uAllocation,vAllocation;

 uchar4 __attribute__((kernel)) yuvToRGB(uint32_t x, uint32_t y) {

    uint uvIndex=  uvPixelStride * (x/2) + uvRowStride*(y/2);
    uchar ay= rsGetElementAt_uchar(yAllocation, x, y);
    uchar au= rsGetElementAt_uchar(uAllocation, uvIndex);
    uchar av= rsGetElementAt_uchar(vAllocation, uvIndex);

    int4 argb;
    argb.r = ay + av * 1436 / 1024 - 179;
    argb.g =  ay -au * 46549 / 131072 + 44 -av * 93604 / 131072 + 91;
    argb.b = ay +au * 1814 / 1024 - 227;
    argb.a = 255;

    uchar4 out = convert_uchar4(clamp(argb, 0, 255));
    return out;
}