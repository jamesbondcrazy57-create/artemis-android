package com.limelight;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class KeyboardAccessibilityService extends AccessibilityService {

    //不屏蔽的按键列表
    private final static List BLACKLIST_KEYS = Arrays.asList(
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_POWER
    );

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        if (Game.instance != null && Game.instance.connected && !BLACKLIST_KEYS.contains(keyCode)) {
            // External physical ESC is already reported correctly by Android as
            // keyCode=ESCAPE(111), scanCode=1. Accessibility filtering happens
            // before Activity.dispatchKeyEvent(), so handle it HERE and bypass
            // the normal Activity/key translation/back-navigation path entirely.
            if (event.getScanCode() == 1 &&
                    (keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_BACK)) {
                if (action == KeyEvent.ACTION_DOWN) {
                    Game.instance.forwardPhysicalEscape(
                            new KeyEvent(event.getDownTime(), event.getEventTime(), action,
                                    KeyEvent.KEYCODE_ESCAPE, event.getRepeatCount(), event.getMetaState(),
                                    event.getDeviceId(), 1, event.getFlags(), event.getSource()), true);
                } else if (action == KeyEvent.ACTION_UP) {
                    Game.instance.forwardPhysicalEscape(
                            new KeyEvent(event.getDownTime(), event.getEventTime(), action,
                                    KeyEvent.KEYCODE_ESCAPE, event.getRepeatCount(), event.getMetaState(),
                                    event.getDeviceId(), 1, event.getFlags(), event.getSource()), false);
                }
                // Always consume physical ESC so Android cannot turn it into Back.
                return true;
            }

            if (action == KeyEvent.ACTION_DOWN) {
                Game.instance.handleKeyDown(event);
                return true;
            } else if (action == KeyEvent.ACTION_UP) {
                Game.instance.handleKeyUp(event);
                return true;
            }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
//        LimeLog.info("onAccessibilityEvent:"+accessibilityEvent.toString());
    }
    @Override
    public void onInterrupt() {

    }

}