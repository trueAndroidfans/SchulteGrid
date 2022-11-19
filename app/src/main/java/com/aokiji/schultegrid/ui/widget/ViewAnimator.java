package com.aokiji.schultegrid.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;

public class ViewAnimator {

    public static void wobble(@NonNull final View view)
    {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator watermelon = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 8f);
        ObjectAnimator apple = ObjectAnimator.ofFloat(view, View.ROTATION, 8f, -8f);
        ObjectAnimator banana = ObjectAnimator.ofFloat(view, View.ROTATION, -8f, 8f);
        ObjectAnimator pear = ObjectAnimator.ofFloat(view, View.ROTATION, 8f, 0);
        animatorSet.playSequentially(watermelon, apple, banana, pear);
        animatorSet.setDuration(150);
        animatorSet.start();
    }

}
