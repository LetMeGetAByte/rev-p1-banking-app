package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.panel;

import com.lmgbgpatr.ui.App;

import dev.tamboui.toolkit.element.Element;

/**
 * 
 * Abstract super class for views.
 */
public abstract class View {

    protected static App app;

    protected View() {}

    public static void setApp(App appInstance) {
        app = appInstance;
    }

    public static Element render() {
        return panel();
    }
}
