package com.stereoviewer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;

import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.GLUT;

import javax.swing.JFrame;

/**
 * Class for the window the scene is seen in
 * @author Jesse Fish
 *
 */
public class SceneViewer extends JFrame{
	private static final long serialVersionUID = 1L;

	private Scene scene;

	private final String scenePath="data/Scene1.xml";

	private FPSAnimator animator;

	/**
	 * 
	 */
	public SceneViewer()
	{
		super();
		scene=SceneLoader.loadScene(scenePath);
		//		scene.loadScene(path);
		this.setTitle(scene.getTitle());
		this.setSize(600,600);
		init();
	}

	/**
	 * 
	 * @param width
	 * @param height
	 */
	public SceneViewer( int width, int height)
	{
		super();
		scene=SceneLoader.loadScene(scenePath);
		this.setTitle(scene.getTitle());
		this.setSize(width,height);
		init();
	}

	/**
	 * initializes the window and gl settings
	 */
	public void init()
	{
		GLCapabilities capabilities=new GLCapabilities();
		capabilities.setStereo(true);
		GLCanvas drawArea=new GLCanvas(capabilities);

		animator=new FPSAnimator(drawArea,60);
		drawArea.addGLEventListener(new Refresher());
		this.add(drawArea);
		animator.start();

		this.setLocationRelativeTo(null); // Center the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public Scene getScene() {
		return scene;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SceneViewer viewer= new SceneViewer();
	}

	/**
	 * class that responds when an GLEvent is fired
	 * @author Jesse Fish
	 *
	 */
	class Refresher implements GLEventListener
	{
		private GLU glu;
		private GLUT glut;

		/**
		 * draws every object into the scene
		 */
		public void display(GLAutoDrawable gLDrawable) {
			// TODO Auto-generated method stub
			final GL gl = gLDrawable.getGL();
			scene.draw(gl, glu, glut);	
		}

		public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
			// TODO Auto-generated method stub

		}

		public void init(GLAutoDrawable gLDrawable) {
			GL gl = gLDrawable.getGL();
			gl.glShadeModel(GL.GL_SMOOTH);              // Enable Smooth Shading
			float[] temp=new float[3];
			scene.getClearColor().getRGBColorComponents(temp);
			gl.glClearColor(temp[0], temp[1],temp[2], (float) 1.0);
			glu = new GLU();
			glut = new GLUT();

			
			//lighting stuff
			gl.glEnable(GL.GL_LIGHTING);
			gl.glEnable(GL.GL_COLOR_MATERIAL);
			float light_ambient[] = { 0.2f, 0.2f, 0.2f, 1.0f};
			
			
			gl.glLightModelfv( GL.GL_LIGHT_MODEL_AMBIENT,light_ambient, 0 );


			gl.glLightModeli( GL.GL_LIGHT_MODEL_LOCAL_VIEWER, 
					//GL.GL_TRUE );
					GL.GL_FALSE );

			gl.glLightModeli( GL.GL_LIGHT_MODEL_TWO_SIDE, 
					//GL.GL_TRUE );
					GL.GL_FALSE );

			scene.initModels(gl, glu);
		}

		public void reshape(GLAutoDrawable gLDrawable,int x, int y, int width, int height) {
			final GL gl = gLDrawable.getGL();
			if (height <= 0) // avoid a divide by zero error
			{height = 1;}
			final float h = (float) width / (float) height;
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(scene.getCamera().getField_of_view(), h, scene.getCamera().getZnear(), scene.getCamera().getZfar());
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glLoadIdentity();
		}
	}
}


