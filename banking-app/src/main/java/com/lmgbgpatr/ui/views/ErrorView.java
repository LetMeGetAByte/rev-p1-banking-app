package com.lmgbgpatr.ui.views;

import dev.tamboui.style.Color;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.widgets.block.BorderType;

public class ErrorView extends NotifyView {

    private static final Style STYLE = new Style(BorderType.DOUBLE, Color.RED, Color.RED);

    public static Element render() {
        return NotifyView.render(STYLE);
    }
}
