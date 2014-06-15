package com.awojcik.qmc.opengl;

import android.opengl.Matrix;
import com.awojcik.qmc.math.MatrixEx;

import javax.microedition.khronos.opengles.GL10;

public abstract class GLObject
{
    private float[] translateM = new float[16];
    private float[] rotationM = new float[16];
    private float[] transformationM = new float[16];

    public GLObject()
    {
        Matrix.setIdentityM(translateM, 0);
        Matrix.setIdentityM(rotationM, 0);
    }

    public void setRotationEuler(float x, float y, float z)
    {
        MatrixEx.setRotateEulerMA(this.rotationM, 0, x, y, z);
    }

    public void setPosition(float x, float y, float z)
    {
        MatrixEx.setTranslation(this.translateM, 0, x, y, z);
    }

    public float[] getTransformationMatrix()
    {
        Matrix.multiplyMM(this.transformationM, 0, this.translateM, 0, this.rotationM, 0);
        return this.transformationM;
    }

    public abstract void render(GL10 gl);
}
