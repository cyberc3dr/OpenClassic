package org.spacehq.openclassic.client.input;

import java.util.ArrayDeque;
import java.util.Deque;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {

	private static final Deque<KeyboardEvent> keyEvents = new ArrayDeque<KeyboardEvent>();
	private static final Deque<MouseEvent> mouseEvents = new ArrayDeque<MouseEvent>();
	
	private static final boolean keysDown[] = new boolean[Keyboard.KEYBOARD_SIZE];
	private static final boolean buttonsDown[] = new boolean[256];
	
	private static boolean setRepeat = false;
	private static boolean newRepeat = false;
	
	private static boolean setCursor = false;
	private static int newMouseX = 0;
	private static int newMouseY = 0;
	private static int mouseX = 0;
	private static int mouseY = 0;
	private static int mouseDX = 0;
	private static int mouseDY = 0;
	
	private static boolean setGrabbed = false;
	private static boolean newGrabbed = false;
	private static boolean grabbed = false;
	
	public static void update() {
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		mouseDX = Mouse.getDX();
		mouseDY = Mouse.getDY();
		grabbed = Mouse.isGrabbed();
		while(Keyboard.next()) {
			keyEvents.push(new KeyboardEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState(), Keyboard.isRepeatEvent()));
			keysDown[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
		}
		
		while(Mouse.next()) {
			mouseEvents.push(new MouseEvent(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventDWheel(), Mouse.getEventButton(), Mouse.getEventButtonState()));
			if(Mouse.getEventButton() != -1) {
				buttonsDown[Mouse.getEventButton()] = Mouse.getEventButtonState();
			}
		}
		
		if(setRepeat) {
			Keyboard.enableRepeatEvents(newRepeat);
			setRepeat = false;
		}
		
		if(setCursor) {
			Mouse.setCursorPosition(newMouseX, newMouseY);
			mouseX = newMouseX;
			mouseY = newMouseY;
			setCursor = false;
		}
		
		if(setGrabbed) {
			Mouse.setGrabbed(newGrabbed);
			grabbed = newGrabbed;
			setGrabbed = false;
		}
	}
	
	public static KeyboardEvent nextKeyEvent() {
		return keyEvents.pollLast();
	}
	
	public static MouseEvent nextMouseEvent() {
		return mouseEvents.pollLast();
	}
	
	public static String getKeyName(int key) {
		return Keyboard.getKeyName(key);
	}
	
	public static boolean isKeyDown(int key) {
		return key < keysDown.length && keysDown[key];
	}
	
	public static void enableRepeatEvents(boolean repeat) {
		newRepeat = repeat;
		setRepeat = true;
	}
	
	public static boolean isButtonDown(int button) {
		return button < buttonsDown.length && buttonsDown[button];
	}
	
	public static int getMouseX() {
		return mouseX;
	}
	
	public static int getMouseY() {
		return mouseY;
	}
	
	public static int getMouseDX() {
		return mouseDX;
	}
	
	public static int getMouseDY() {
		return mouseDY;
	}
	
	public static boolean isMouseGrabbed() {
		return grabbed;
	}
	
	public static void setMouseGrabbed(boolean grabbed) {
		newGrabbed = grabbed;
		setGrabbed = true;
	}

	public static void setCursorPosition(int x, int y) {
		newMouseX = x;
		newMouseY = y;
		setCursor = true;
	}
	
}
