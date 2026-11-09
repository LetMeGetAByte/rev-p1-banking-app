package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.column;
import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.row;
import static dev.tamboui.toolkit.Toolkit.spacer;
import static dev.tamboui.toolkit.Toolkit.tabs;
import static dev.tamboui.toolkit.Toolkit.text;
import static dev.tamboui.toolkit.Toolkit.textInput;

import com.lmgbgpatr.middleware.dto.Credentials;
import com.lmgbgpatr.ui.states.UserState;

import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.TextInputElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.widgets.input.TextInputState;
import dev.tamboui.widgets.tabs.TabsState;

public final class LoginView extends View {

    private static TextInputState usernameState = new TextInputState();
    private static TextInputState passwordState = new TextInputState();
    private static TabsState tabsState = new TabsState();

    private static TextInputElement generateField(String label) {
        return textInput(label.equals("username") ? usernameState : passwordState)
            .placeholder(label)
            .id(label + "-input");
    }

    private static void callCredSubmissionHandler() {
        Credentials credentials = new Credentials(usernameState.text(), passwordState.text());
        UserState user = UserState.instance();
        user.setCredentials(credentials);
        if (tabsState.selected() == 0) {
            app.bridge.handleLogin();
        } else {
            app.bridge.handleRegistration();
        }
    }

    public static Element render() {
        final String bottomText = "[enter] submit / [esc] exit";
        final Integer bottomTextWidth = (int) (CharWidth.of(bottomText) * 1.25);

        final String[] tabTitles = {"Login", "Register"};
        final Integer tabTitlesWidth = (int) ((tabTitles[0].length() + tabTitles[1].length()) * 2);

        final Element tabBar = tabs(tabTitles)
            .state(tabsState)
            .highlightStyle(Style.EMPTY.bold().fg(Color.CYAN).underlined())
            .divider(" | ");

        if (tabsState.selected() == null) {
            tabsState.select(0);
        }

        return column(
                dialog(
                    row(
                        spacer(),
                        tabBar,
                        spacer()
                    ),
                    column(
                        generateField("username"),
                        generateField("password"),
                        text(bottomText)
                            .centered()
                            .dim()
                    )
                )
                .width(Math.max(bottomTextWidth, tabTitlesWidth))
                .rounded()
                .onConfirm(LoginView::callCredSubmissionHandler)
                .onCancel(app::quit)
                .onKeyEvent(event -> {
                    if (event.code() == KeyCode.F1) {
                        tabsState.select(0);
                        return EventResult.HANDLED;
                    } else if (event.code() == KeyCode.F2) {
                        tabsState.select(1);
                        return EventResult.HANDLED;
                    }
                    return EventResult.UNHANDLED;
                })
            )
            .fill();
    }
}