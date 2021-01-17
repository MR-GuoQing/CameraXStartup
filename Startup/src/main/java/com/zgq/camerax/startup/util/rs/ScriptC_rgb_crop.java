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
 * The source Renderscript file: /Users/i337911/AndroidStudioProjects/CameraXStartup/Startup/src/main/rs/rgb_crop.rs.txt
 */

package com.zgq.camerax.startup.util.rs;

import androidx.renderscript.*;

/**
 * @hide
 */
public class ScriptC_rgb_crop extends ScriptC {
    private static final String __rs_resource_name = "rgb_crop";
    // Constructor
    public  ScriptC_rgb_crop(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              rgb_cropBitCode.getBitCode32(),
              rgb_cropBitCode.getBitCode64());
        __ALLOCATION = Element.ALLOCATION(rs);
        __U32 = Element.U32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __ALLOCATION;
    private Element __U32;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_U32;
    private final static int mExportVarIdx_input = 0;
    private Allocation mExportVar_input;
    public synchronized void set_input(Allocation v) {
        setVar(mExportVarIdx_input, v);
        mExportVar_input = v;
    }

    public Allocation get_input() {
        return mExportVar_input;
    }

    public FieldID getFieldID_input() {
        return createFieldID(mExportVarIdx_input, null);
    }

    private final static int mExportVarIdx_xStart = 1;
    private long mExportVar_xStart;
    public synchronized void set_xStart(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_xStart, __rs_fp_U32);
        mExportVar_xStart = v;
    }

    public long get_xStart() {
        return mExportVar_xStart;
    }

    public FieldID getFieldID_xStart() {
        return createFieldID(mExportVarIdx_xStart, null);
    }

    private final static int mExportVarIdx_yStart = 2;
    private long mExportVar_yStart;
    public synchronized void set_yStart(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_yStart, __rs_fp_U32);
        mExportVar_yStart = v;
    }

    public long get_yStart() {
        return mExportVar_yStart;
    }

    public FieldID getFieldID_yStart() {
        return createFieldID(mExportVarIdx_yStart, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_crop = 1;
    public KernelID getKernelID_crop() {
        return createKernelID(mExportForEachIdx_crop, 58, null, null);
    }

    public void forEach_crop(Allocation aout) {
        forEach_crop(aout, null);
    }

    public void forEach_crop(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_crop, (Allocation) null, aout, null, sc);
    }

}

