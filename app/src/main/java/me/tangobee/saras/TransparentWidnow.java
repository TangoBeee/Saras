package me.tangobee.saras;

import android.graphics.Color;
import android.view.Window;

import androidx.core.view.WindowCompat;

public class TransparentWidnow {

    public static void transparentWindow(Window window) {
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.setStatusBarColor(Color.TRANSPARENT);
        WindowCompat.setDecorFitsSystemWindows(window, false);
    }

}
