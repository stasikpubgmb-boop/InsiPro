package com.insipro.common.animation.implement;

import com.insipro.common.animation.Animation;

public class SineInOut extends Animation {

    @Override
    public double calculation(double value) {
        double x = value / ms;
        return -(Math.cos(Math.PI * x) - 1) / 2;
    }
}










