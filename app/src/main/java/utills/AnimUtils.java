package utills;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimUtils
{

    public static void SlideAnimation(Context context, View view, int res) {
        Animation animation = AnimationUtils.loadAnimation(context, res);
        animation.setDuration(500);
        view.startAnimation(animation);
    }
}
