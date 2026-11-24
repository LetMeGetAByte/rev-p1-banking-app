package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.list;
import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.table;
import static dev.tamboui.toolkit.Toolkit.tabs;
import static dev.tamboui.toolkit.Toolkit.text;

import com.lmgbgpatr.model.Account;
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

    private static Account account;

    private static final ListElement<?> options =
        list()
            .add("Deposit")
            .add("Withdraw")
            .add("Transfer")
            .add("Close Account")
            .add("Back")
            .highlightColor(Color.CYAN)
            .autoScroll();

    public static Element render() {
        account = ctx.getState().get("selectedAccount", Account.class);

        return dialog(
            "Account",
            table()
                .rows(
                    Row.from("No:   ", String.valueOf(account.id())),
                    Row.from("Balance:  ", String.valueOf(account.balance())),
                    Row.from("Standing:   ", "IMPL ME!")
                ),

            options
                .onKeyEvent(event -> {
                    if (!ctx.peekView().equals(ViewState.Account)) return EventResult.UNHANDLED;

                    if (event.isConfirm()) {
                        int sel = options.selected();
                        switch (sel) {
                            case 0: ctx.pushView(ViewState.Deposit); break;
                            case 1: ctx.pushView(ViewState.Withdraw); break;
                            case 2: ctx.pushView(ViewState.Transfer); break;
                            case 3: ctx.pushView(ViewState.Transfer); break;
                            default: ctx.popView();
                        }
                    } else {
                        return EventResult.UNHANDLED;
                    }

                    return EventResult.HANDLED;
                })
        )
        .id("account-view")
        .focusable()
        .onCancel(ctx::popView);
    }
}
