package com.lmgbgpatr.ui.widgets;

import static dev.tamboui.toolkit.Toolkit.panel;

import com.lmgbgpatr.ui.App;

import dev.tamboui.toolkit.element.Element;

public class Widget {

    protected static App app;

    protected Widget() {}

    public static void setApp(App appInstance) {
        app = appInstance;
    }

    public static Element render() {
        return panel();
    }
}
