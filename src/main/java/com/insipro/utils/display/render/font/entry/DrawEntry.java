package com.insipro.utils.display.render.font.entry;

import com.insipro.utils.display.render.font.glyph.Glyph;

public record DrawEntry(float atX, float atY, int color, Glyph toDraw) {
}
