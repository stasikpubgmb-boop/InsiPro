package com.insipro.common.animation.implement;

import com.insipro.common.animation.Animation;

public class CircOut extends Animation {

    @Override
    public double calculation(double value) {
        double x = value / ms;
        return Math.sqrt(1 - Math.pow(x - 1, 2));
    }
}










