package fr.proneus.engine.input;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

import fr.proneus.engine.utils.Vector;

public class Controller {

	private int joy;

	public Controller(int joy) {
		this.joy = joy;
	}

	public boolean isConnected() {
		return glfwJoystickPresent(joy);
	}

	public String getName() {
		return glfwGetJoystickName(joy);
	}

	public boolean isButtonPressed(ControllerButton button) {
		return glfwGetJoystickButtons(joy).getInt(button.value) == 1;
	}

	public float getJoyStickValue(ControllerAxe axe, boolean deadzone) {
		if (!isConnected())
			return 0f;
		float value = glfwGetJoystickAxes(joy).get(axe.getValue());

		if (deadzone) {
			float precision = 0.25f;
			value = Math.abs(value) < precision ? 0 : value;
		}

		if (axe.equals(ControllerAxe.JOYSTICK_1_VERTICAL) || axe.equals(ControllerAxe.JOYSTICK_2_VERTICAL)) {
			value = -value;
		}

		return value;
	}

	public double getJoystickAngle(JoyStick joystick, boolean deadzone) {
		float h = joystick.equals(JoyStick.JOYSTICK_1) ? getJoyStickValue(ControllerAxe.JOYSTICK_1_HORIZONTAL, false)
				: getJoyStickValue(ControllerAxe.JOYSTICK_2_HORIZONTAL, false);
		float v = joystick.equals(JoyStick.JOYSTICK_1) ? getJoyStickValue(ControllerAxe.JOYSTICK_1_VERTICAL, false)
				: getJoyStickValue(ControllerAxe.JOYSTICK_2_VERTICAL, false);

		double angle = Math.toDegrees(Math.atan2(v, h));
		if (angle >= -180 && angle < 0)
			angle = 360 + angle;

		if (deadzone) {
			float precision = 0.25f;
			Vector vec = new Vector(v, h);
			angle = vec.length() < precision ? 0 : angle;
		}

		return angle;
	}

	public enum ControllerAxe {
		JOYSTICK_1_HORIZONTAL(0), JOYSTICK_1_VERTICAL(1), JOYSTICK_2_HORIZONTAL(2), JOYSTICK_2_VERTICAL(3), LT(4), RT(
				5);

		public int value;

		private ControllerAxe(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum JoyStick {
		JOYSTICK_1, JOYSTICK_2;
	}

	public enum ControllerButton {
		A(0), B(1), X(2), Y(3), LB(4), RB(5);

		public int value;

		private ControllerButton(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
