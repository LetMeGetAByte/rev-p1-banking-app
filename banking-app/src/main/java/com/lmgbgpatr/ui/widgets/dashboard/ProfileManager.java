package com.lmgbgpatr.ui.widgets.dashboard;

import static dev.tamboui.toolkit.Toolkit.column;
import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.row;

import com.lmgbgpatr.ui.Renderable;

import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.event.EventResult;

public class ProfileManager extends Renderable {

    public static Element render() {
        return panel(
            "Profile",
            column(
                row(

                )
            )
        )
        .fill();
    }
}
