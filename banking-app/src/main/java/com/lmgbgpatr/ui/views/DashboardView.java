package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.panel;

import com.lmgbgpatr.ui.states.UserState;
import com.lmgbgpatr.ui.widgets.dashboard.AccountManager;

import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.event.EventResult;

public class DashboardView extends View {

    private static final UserState user = UserState.instance();

    public static Element render() {
        return panel(
            AccountManager.render()
        )
        .fill()
        .borderless()
        .onKeyEvent(event -> {
            if (event.isCancel()) {
                app.quit();
                return EventResult.HANDLED;
            }

            return EventResult.UNHANDLED;
        });
    }
}
