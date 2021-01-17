/*
 * Copyright (C) 2011-2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: /Users/i337911/AndroidStudioProjects/CameraXStartup/Startup/src/main/rs/yuv420_888_to_rgb.rs.txt
 */

package com.zgq.camerax.startup.util.rs;

import androidx.renderscript.*;

/**
 * @hide
 */
public class ScriptC_yuv420_888_to_rgb extends ScriptC {
    private static final String __rs_resource_name = "yuv420_888_to_rgb";
    // Constructor
    public  ScriptC_yuv420_888_to_rgb(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              yuv420_888_to_rgbBitCode.getBitCode32(),
              yuv420_888_to_rgbBitCode.getBitCode64());
        __U32 = Element.U32(rs);
        __ALLOCATION = Element.ALLOCATION(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __ALLOCATION;
    private Element __U32;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_U32;
    private final static int mExportVarIdx_uvPixelStride = 0;
    private long mExportVar_uvPixelStride;
    public synchronized void set_uvPixelStride(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_uvPixelStride, __rs_fp_U32);
        mExportVar_uvPixelStride = v;
    }

    public long get_uvPixelStride() {
        return mExportVar_uvPixelStride;
    }

    public FieldID getFieldID_uvPixelStride() {
        return createFieldID(mExportVarIdx_uvPixelStride, null);
    }

    private final static int mExportVarIdx_uvRowStride = 1;
    private long mExportVar_uvRowStride;
    public synchronized void set_uvRowStride(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_uvRowStride, __rs_fp_U32);
        mExportVar_uvRowStride = v;
    }

    public long get_uvRowStride() {
        return mExportVar_uvRowStride;
    }

    public FieldID getFieldID_uvRowStride() {
        return createFieldID(mExportVarIdx_uvRowStride, null);
    }

    private final static int mExportVarIdx_yAllocation = 2;
    private Allocation mExportVar_yAllocation;
    public synchronized void set_yAllocation(Allocation v) {
        setVar(mExportVarIdx_yAllocation, v);
        mExportVar_yAllocation = v;
    }

    public Allocation get_yAllocation() {
        return mExportVar_yAllocation;
    }

    public FieldID getFieldID_yAllocation() {
        return createFieldID(mExportVarIdx_yAllocation, null);
    }

    private final static int mExportVarIdx_uAllocation = 3;
    private Allocation mExportVar_uAllocation;
    public synchronized void set_uAllocation(Allocation v) {
        setVar(mExportVarIdx_uAllocation, v);
        mExportVar_uAllocation = v;
    }

    public Allocation get_uAllocation() {
        return mExportVar_uAllocation;
    }

    public FieldID getFieldID_uAllocation() {
        return createFieldID(mExportVarIdx_uAllocation, null);
    }

    private final static int mExportVarIdx_vAllocation = 4;
    private Allocation mExportVar_vAllocation;
    public synchronized void set_vAllocation(Allocation v) {
        setVar(mExportVarIdx_vAllocation, v);
        mExportVar_vAllocation = v;
    }

    public Allocation get_vAllocation() {
        return mExportVar_vAllocation;
    }

    public FieldID getFieldID_vAllocation() {
        return createFieldID(mExportVarIdx_vAllocation, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_yuvToRGB = 1;
    public KernelID getKernelID_yuvToRGB() {
        return createKernelID(mExportForEachIdx_yuvToRGB, 58, null, null);
    }

    public void forEach_yuvToRGB(Allocation aout) {
        forEach_yuvToRGB(aout, null);
    }

    public void forEach_yuvToRGB(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_yuvToRGB, (Allocation) null, aout, null, sc);
    }

}

