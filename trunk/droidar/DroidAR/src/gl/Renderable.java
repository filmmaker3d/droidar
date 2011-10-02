package gl;

import javax.microedition.khronos.opengles.GL10;

import system.RenderStack;

/**
 * Use this interface for custom rendering
 * 
 * @author Spobo
 * 
 */
public interface Renderable {
	public void render(GL10 gl, Renderable parent, RenderStack stack);
}