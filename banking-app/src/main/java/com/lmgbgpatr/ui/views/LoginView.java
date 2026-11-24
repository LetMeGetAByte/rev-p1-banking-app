package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.column;
import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.row;
import static dev.tamboui.toolkit.Toolkit.spacer;
import static dev.tamboui.toolkit.Toolkit.tabs;
import static dev.tamboui.toolkit.Toolkit.text;
import static dev.tamboui.toolkit.Toolkit.textInput;

import com.lmgbgpatr.middleware.dto.Credentials;
import com.lmgbgpatr.ui.Renderable;
import com.lmgbgpatr.ui.states.UserState;
import com.lmgbgpatr.ui.states.ViewState;

import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.TextInputElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.widgets.input.TextInputState;
import dev.tamboui.widgets.tabs.TabsState;

public final class LoginView extends Renderable {

    private static TextInputState usernameState = new TextInputState();
    private static TextInputState passwordState = new TextInputState();
    private static TextInputState fnameState = new TextInputState();
    private static TextInputState lnameState = new TextInputState();
    private static TabsState tabsState = new TabsState();

    private static final String bottomText = "[enter] submit / [esc] exit";
    private static final Integer bottomTextWidth = (int) (CharWidth.of(bottomText) * 1.25);

    private static final String[] tabTitles = {"Login", "Register"};
    private static final Integer tabTitlesWidth = (int) ((tabTitles[0].length() + tabTitles[1].length()) * 2);

    private static TextInputElement generateField(String label) {
        if (label == null || label.strip().isEmpty()) {
            label = "blank";
        }
        String labelID = label.strip().replaceAll("\\s+", "-") + "-input";
        
        TextInputState state;
        if (label.equals("username")) {
            state = usernameState;
            ctx.registerID(ViewState.Login, labelID);
        } else if (label.equals("password")) {
            state = passwordState;
        } else if (label.equals("first name")) {
            state = fnameState;
        } else {
            state = lnameState;
        }

        return textInput(state).placeholder(label).id(labelID);
    }

    private static void callCredSubmissionHandler() {
        Credentials credentials = new Credentials(
            usernameState.text(),
            passwordState.text(),
            fnameState.text(),
            lnameState.text()
        );
        UserState.instance().setCredentials(credentials);
        if (tabsState.selected() == 0) {
            ctx.getBridge().handleLogin();
        } else {
            ctx.getBridge().handleRegistration();
        }
    }

    public static Element render() {

        if (tabsState.selected() == null) {
            tabsState.select(0);
        }

        return dialog(
                row(
                    spacer(),
                    tabs(tabTitles)
                        .state(tabsState)
                        .highlightStyle(Style.EMPTY.bold().fg(Color.CYAN).underlined())
                        .divider(" | "),
                    spacer()
                ),
                switch (tabsState.selected()) {
                    case 1 -> column(
                        generateField("username"),
                        generateField("password"),
                        generateField("first name"),
                        generateField("last name"),
                        text(bottomText)
                            .centered()
                            .dim()
                        );
                    default -> column(
                        generateField("username"),
                        generateField("password"),
                        text(bottomText)
                            .centered()
                            .dim()
                        );
                }
            )
            .width(Math.max(bottomTextWidth, tabTitlesWidth))
            .rounded()
            .onKeyEvent(event -> {
                if (!ctx.peekView().equals(ViewState.Login)) return EventResult.UNHANDLED;
                
                if (event.code() == KeyCode.F1) {
                    tabsState.select(0);
                } else if (event.code() == KeyCode.F2) {
                    tabsState.select(1);
                } else if (event.isConfirm()) {
                    callCredSubmissionHandler();
                } else if (event.isCancel()) {
                    ctx.quit();
                } else {
                    return EventResult.UNHANDLED;
                }

                return EventResult.HANDLED;
            });
    }
}