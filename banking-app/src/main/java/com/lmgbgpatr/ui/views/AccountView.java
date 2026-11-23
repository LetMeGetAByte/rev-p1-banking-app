package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.list;
import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.table;
import static dev.tamboui.toolkit.Toolkit.tabs;
import static dev.tamboui.toolkit.Toolkit.text;

import com.lmgbgpatr.middleware.dto.Account;
import com.lmgbgpatr.ui.Renderable;
import com.lmgbgpatr.ui.states.ViewState;

import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.widgets.table.Row;
import dev.tamboui.widgets.tabs.TabsState;

public class AccountView extends Renderable {

    private static Account account = ctx.getState().get("selectedAccount", Account.class);

    private static final ListElement<?> options =
        list()
            .add("Transfer")
            .add("Back")
            .highlightColor(Color.CYAN)
            .autoScroll();

    public static Element render() {
        return dialog(
            "Account Viewer",
            table()
                .rows(
                    Row.from("No:", String.valueOf(account.id())),
                    Row.from("Balance:", String.valueOf(account.balance()))
                ),

            options
                .onKeyEvent(event -> {
                    if (event.isUp()) {
                        options.selectLast(0);
                    } else if (event.isUp()) {
                        options.selectNext(1);
                    } else if (event.isConfirm()) {
                        int sel = options.selected();
                        switch (sel) {
                            case 0: ctx.setView(new ViewState.Empty()); break;
                            default: ctx.prevView();
                        }

                        return EventResult.HANDLED;
                    }

                    return EventResult.UNHANDLED;
                })
        )
        .onKeyEvent(event -> {
            if (event.isCancel()) {
                ctx.setView(new ViewState.Dashboard());
            }

            return EventResult.UNHANDLED;
        });
    }
}
