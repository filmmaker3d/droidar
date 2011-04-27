package de.rwth.setups;

import geo.GeoObj;
import gl.Color;
import gl.CustomGLSurfaceView;
import gl.GLCamera;
import gl.GLFactory;
import gl.GLRenderer;
import gl.MeshComponent;
import gui.GuiSetup;
import system.ErrorHandler;
import system.EventManager;
import system.Setup;
import util.Vec;
import worldData.MoveObjComp;
import worldData.Obj;
import worldData.SystemUpdater;
import worldData.World;
import actions.ActionBufferedCameraAR;
import actions.ActionCalcRelativePos;
import actions.ActionMoveCameraBuffered;
import actions.ActionRotateCameraBuffered;
import android.app.Activity;

import commands.Command;

public class PlaceObjectsSetupTwo extends Setup {

	private GLCamera camera;
	private World world;
	private ViewPosCalcerComp viewPosCalcer;
	private Obj selectedObj;
	private MoveObjComp moveComp;

	@Override
	public void _a_initFieldsIfNecessary() {

		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");
		camera = new GLCamera(new Vec(0, 0, 15));
		world = new World(camera);
		viewPosCalcer = new ViewPosCalcerComp(camera, 50, 0.1f);
		moveComp = new MoveObjComp(4);
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		world.add(newObject());
		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView) {
		arView.addOnTouchMoveAction(new ActionBufferedCameraAR(camera));
		eventManager
				.addOnOrientationChangedAction(new ActionRotateCameraBuffered(
						camera));

		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		worldUpdater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity context) {

		guiSetup.addButtonToTopView(new Command() {

			@Override
			public boolean execute() {
				world.add(newObject());
				return true;
			}

		}, "Place next");

		guiSetup.setTopViewCentered();
	}

	private Obj newObject() {
		final Obj obj = new Obj();
		Color c = Color.getRandomRGBColor();
		c.alpha = 0.5f;
		MeshComponent diamond = GLFactory.getInstance().newDiamond(c);
		obj.setComp(diamond);
		setComps(obj);
		diamond.setOnClickCommand(new Command() {
			@Override
			public boolean execute() {
				setComps(obj);
				return true;
			}

		});
		return obj;
	}

	private void setComps(Obj obj) {
		if (selectedObj != null) {
			selectedObj.remove(viewPosCalcer);
			selectedObj.remove(moveComp);
		}
		obj.setComp(viewPosCalcer);
		obj.setComp(moveComp);
		selectedObj = obj;
	}
}
