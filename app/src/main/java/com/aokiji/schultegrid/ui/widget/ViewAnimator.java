package com.aokiji.schultegrid.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;

public class ViewAnimator {

    public static void wobble(@NonNull final View view)
    {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator watermelon = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 3f);
        ObjectAnimator apple = ObjectAnimator.ofFloat(view, View.ROTATION, 3f, -3f);
        ObjectAnimator banana = ObjectAnimator.ofFloat(view, View.ROTATION, -3f, 3f);
        ObjectAnimator pear = ObjectAnimator.ofFloat(view, View.ROTATION, 3f, -2f);
        ObjectAnimator cherry = ObjectAnimator.ofFloat(view, View.ROTATION, -2f, 2f);
        ObjectAnimator plum = ObjectAnimator.ofFloat(view, View.ROTATION, 2f, 0);
        animatorSet.playSequentially(watermelon, apple, banana, pear, cherry, plum);
        animatorSet.setDuration(100);
        animatorSet.start();
    }

}
