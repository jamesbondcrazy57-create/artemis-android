package com.limelight;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.limelight.LimeLog;

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
            // Use the same classifier and proven Send Keys path as Game.
            // This also marks the short Back-suppression window.
            if (Game.instance.sendPhysicalEscape(event)) {
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