package com.awojcik.qmc.opengl;

import android.content.Context;

public class GLSurfaceView extends android.opengl.GLSurfaceView
{
    public GLSurfaceView(Context context, GLScene scene)
    {
        super(context);
        this.setRenderer(new GLRenderer(scene));
        this.setRenderMode(RENDERMODE_WHEN_DIRTY);
        this.attachToRedrawEvent(scene);
    }

    private void attachToRedrawEvent(GLScene scene)
    {
        scene.addSceneRedrawListener(new RedrawListener());
    }

    private void redrawScene()
    {
        this.requestRender();
    }

    class RedrawListener implements GLSceneRedrawListener
    {
        @Override
        public void redraw()
        {
            GLSurfaceView.this.redrawScene();
        }
    }
}
