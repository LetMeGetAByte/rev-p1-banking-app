package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.row;

import com.lmgbgpatr.service.UserState;
import com.lmgbgpatr.ui.Renderable;
import com.lmgbgpatr.ui.states.ViewState;
import com.lmgbgpatr.ui.widgets.dashboard.AccountManager;
import com.lmgbgpatr.ui.widgets.dashboard.ProfileManager;

import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.event.EventResult;

public class DashboardView extends Renderable {

    private static final UserState user = UserState.instance();

    public static Element render() {
        return panel(
            row(
                AccountManager.render(),
                ProfileManager.render()
            ).fill()
        )
        .fill()
        .borderless()
        .onKeyEvent(event -> {
            if (event.isCancel() && ctx.peekView().equals(ViewState.Dashboard)) {
                ctx.quit();
            }

            return EventResult.UNHANDLED;
        });
    }
}
