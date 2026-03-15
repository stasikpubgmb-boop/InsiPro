package com.insipro.common.animation.implement;

import com.insipro.common.animation.Animation;

public class CubicOut extends Animation {

    @Override
    public double calculation(double value) {
        double x = value / ms;
        return 1 - Math.pow(1 - x, 3);
    }
}










