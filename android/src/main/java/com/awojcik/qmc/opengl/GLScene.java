package com.awojcik.qmc.opengl;

import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class GLScene
{
    private final List<GLObject> sceneObjects = new LinkedList<GLObject>();
    private final List<GLSceneRedrawListener> listeners = new LinkedList<GLSceneRedrawListener>();

    public void addSceneObject(GLObject obj)
    {
        this.sceneObjects.add(obj);
    }

    public void addSceneRedrawListener(GLSceneRedrawListener listener)
    {
        this.listeners.add(listener);
    }

    public void redraw()
    {
        for (GLSceneRedrawListener listener : this.listeners)
        {
            listener.redraw();
        }
    }

    public void render(GL10 gl)
    {
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        for (GLObject obj : this.sceneObjects)
        {
            gl.glPushMatrix();
            gl.glLoadMatrixf(obj.getTransformationMatrix(), 0);
            obj.render(gl);
            gl.glPopMatrix();
        }
    }
}
