package com.insipro.common.animation.implement;

import com.insipro.common.animation.Animation;

public class SineOut extends Animation {

    @Override
    public double calculation(double value) {
        double x = value / ms;
        return Math.sin(x * Math.PI / 2);
    }
}










