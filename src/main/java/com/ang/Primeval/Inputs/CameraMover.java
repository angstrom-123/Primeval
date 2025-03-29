package com.ang.Primeval.Inputs;

import com.ang.Primeval.Maths.Vec2;
import com.ang.Primeval.Camera;

import java.awt.event.KeyEvent;

public class CameraMover {
	private final double	MOVEMENT_STEP 	= 0.08;
	private final double	TURN_STEP 		= Math.PI / 40.0;
	private Camera 			camera;

	public CameraMover(Camera camera) {
		this.camera = camera;	
	}

	public void update(boolean[] inputs) {
		camera.changePosition(getMovementInput(inputs));
		camera.changeFacing(getTurnInput(inputs));
	}

	private double getTurnInput(boolean[] inputs) {
		double theta = 0.0;
		if (inputs[KeyEvent.VK_LEFT]) { 
			theta += TURN_STEP;
		}
		if (inputs[KeyEvent.VK_RIGHT]) { 
			theta -= TURN_STEP;
		}
		return theta;

	}

	private Vec2 getMovementInput(boolean[] inputs) {
		Vec2 out = new Vec2(0.0, 0.0);
		if (inputs[KeyEvent.VK_W]) {
			out = out.add(new Vec2(0.0, MOVEMENT_STEP));
		}
		if (inputs[KeyEvent.VK_A]) {
			out = out.add(new Vec2(MOVEMENT_STEP, 0.0));
		}
		if (inputs[KeyEvent.VK_S]) {
			out = out.add(new Vec2(0.0, -MOVEMENT_STEP));
		}
		if (inputs[KeyEvent.VK_D]) {
			out = out.add(new Vec2(-MOVEMENT_STEP, 0.0));
		}
		return out;

	}
}
