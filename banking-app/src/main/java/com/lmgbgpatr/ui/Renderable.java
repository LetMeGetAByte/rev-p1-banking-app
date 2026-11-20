package com.lmgbgpatr.ui;

import static dev.tamboui.toolkit.Toolkit.panel;

import dev.tamboui.toolkit.element.Element;

public abstract class Renderable {

    protected static AppContext ctx;

    protected Renderable() {}

    public static void setApp(AppContext appContext) {
        ctx = appContext;
    }

    public static Element render() {
        return panel();
    }
}
