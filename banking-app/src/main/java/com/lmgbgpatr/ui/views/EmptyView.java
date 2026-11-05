package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.text;

import dev.tamboui.toolkit.element.Element;

public class EmptyView extends View {

    public static Element render() {
        return panel(
            text("So much empty...")
        );
    }
}
