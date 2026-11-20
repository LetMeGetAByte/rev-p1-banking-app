package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.text;

import com.lmgbgpatr.ui.Renderable;

import dev.tamboui.toolkit.element.Element;

public class EmptyView extends Renderable {

    public static Element render() {
        return panel(
            text("So much empty...")
        );
    }
}
