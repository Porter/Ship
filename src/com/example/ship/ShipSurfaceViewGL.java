package com.example.ship;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class ShipSurfaceViewGL extends GLSurfaceView {

    public ShipSurfaceViewGL(Context context){
        super(context);

        setEGLContextClientVersion(2);
        
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(new MyRenderer());
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}