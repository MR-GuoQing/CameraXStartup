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
 * The source Renderscript file: /Users/i337911/AndroidStudioProjects/CameraXStartup/Startup/src/main/rs/rgb_rotate.rs.txt
 */

package com.zgq.camerax.startup.util.rs;

import androidx.renderscript.*;

/**
 * @hide
 */
public class ScriptC_rgb_rotate extends ScriptC {
    private static final String __rs_resource_name = "rgb_rotate";
    // Constructor
    public  ScriptC_rgb_rotate(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              rgb_rotateBitCode.getBitCode32(),
              rgb_rotateBitCode.getBitCode64());
        __ALLOCATION = Element.ALLOCATION(rs);
        __I32 = Element.I32(rs);
        mExportVar_rotation = 0.f;
        __F32 = Element.F32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __ALLOCATION;
    private Element __F32;
    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_F32;
    private FieldPacker __rs_fp_I32;
    private final static int mExportVarIdx_inImage = 0;
    private Allocation mExportVar_inImage;
    public synchronized void set_inImage(Allocation v) {
        setVar(mExportVarIdx_inImage, v);
        mExportVar_inImage = v;
    }

    public Allocation get_inImage() {
        return mExportVar_inImage;
    }

    public FieldID getFieldID_inImage() {
        return createFieldID(mExportVarIdx_inImage, null);
    }

    private final static int mExportVarIdx_inWidth = 1;
    private int mExportVar_inWidth;
    public synchronized void set_inWidth(int v) {
        setVar(mExportVarIdx_inWidth, v);
        mExportVar_inWidth = v;
    }

    public int get_inWidth() {
        return mExportVar_inWidth;
    }

    public FieldID getFieldID_inWidth() {
        return createFieldID(mExportVarIdx_inWidth, null);
    }

    private final static int mExportVarIdx_inHeight = 2;
    private int mExportVar_inHeight;
    public synchronized void set_inHeight(int v) {
        setVar(mExportVarIdx_inHeight, v);
        mExportVar_inHeight = v;
    }

    public int get_inHeight() {
        return mExportVar_inHeight;
    }

    public FieldID getFieldID_inHeight() {
        return createFieldID(mExportVarIdx_inHeight, null);
    }

    private final static int mExportVarIdx_rotation = 3;
    private float mExportVar_rotation;
    public synchronized void set_rotation(float v) {
        setVar(mExportVarIdx_rotation, v);
        mExportVar_rotation = v;
    }

    public float get_rotation() {
        return mExportVar_rotation;
    }

    public FieldID getFieldID_rotation() {
        return createFieldID(mExportVarIdx_rotation, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_rotate_270_clockwise_natural = 1;
    public KernelID getKernelID_rotate_270_clockwise_natural() {
        return createKernelID(mExportForEachIdx_rotate_270_clockwise_natural, 58, null, null);
    }

    public void forEach_rotate_270_clockwise_natural(Allocation aout) {
        forEach_rotate_270_clockwise_natural(aout, null);
    }

    public void forEach_rotate_270_clockwise_natural(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_rotate_270_clockwise_natural, (Allocation) null, aout, null, sc);
    }

    private final static int mExportForEachIdx_rotate_270_clockwise = 2;
    public KernelID getKernelID_rotate_270_clockwise() {
        return createKernelID(mExportForEachIdx_rotate_270_clockwise, 58, null, null);
    }

    public void forEach_rotate_270_clockwise(Allocation aout) {
        forEach_rotate_270_clockwise(aout, null);
    }

    public void forEach_rotate_270_clockwise(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_rotate_270_clockwise, (Allocation) null, aout, null, sc);
    }

    private final static int mExportForEachIdx_rotate_90_clockwise = 3;
    public KernelID getKernelID_rotate_90_clockwise() {
        return createKernelID(mExportForEachIdx_rotate_90_clockwise, 58, null, null);
    }

    public void forEach_rotate_90_clockwise(Allocation aout) {
        forEach_rotate_90_clockwise(aout, null);
    }

    public void forEach_rotate_90_clockwise(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_rotate_90_clockwise, (Allocation) null, aout, null, sc);
    }

    private final static int mExportForEachIdx_rotate_90_counterclockwise = 4;
    public KernelID getKernelID_rotate_90_counterclockwise() {
        return createKernelID(mExportForEachIdx_rotate_90_counterclockwise, 58, null, null);
    }

    public void forEach_rotate_90_counterclockwise(Allocation aout) {
        forEach_rotate_90_counterclockwise(aout, null);
    }

    public void forEach_rotate_90_counterclockwise(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_rotate_90_counterclockwise, (Allocation) null, aout, null, sc);
    }

    private final static int mExportForEachIdx_rotate_180 = 5;
    public KernelID getKernelID_rotate_180() {
        return createKernelID(mExportForEachIdx_rotate_180, 58, null, null);
    }

    public void forEach_rotate_180(Allocation aout) {
        forEach_rotate_180(aout, null);
    }

    public void forEach_rotate_180(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_rotate_180, (Allocation) null, aout, null, sc);
    }

    private final static int mExportForEachIdx_flip_vertical = 6;
    public KernelID getKernelID_flip_vertical() {
        return createKernelID(mExportForEachIdx_flip_vertical, 58, null, null);
    }

    public void forEach_flip_vertical(Allocation aout) {
        forEach_flip_vertical(aout, null);
    }

    public void forEach_flip_vertical(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_flip_vertical, (Allocation) null, aout, null, sc);
    }

    private final static int mExportForEachIdx_flip_horizontal = 7;
    public KernelID getKernelID_flip_horizontal() {
        return createKernelID(mExportForEachIdx_flip_horizontal, 58, null, null);
    }

    public void forEach_flip_horizontal(Allocation aout) {
        forEach_flip_horizontal(aout, null);
    }

    public void forEach_flip_horizontal(Allocation aout, LaunchOptions sc) {
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        forEach(mExportForEachIdx_flip_horizontal, (Allocation) null, aout, null, sc);
    }

}

