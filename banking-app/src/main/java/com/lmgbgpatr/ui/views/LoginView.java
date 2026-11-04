package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.column;
import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.row;
import static dev.tamboui.toolkit.Toolkit.spacer;
import static dev.tamboui.toolkit.Toolkit.tabs;
import static dev.tamboui.toolkit.Toolkit.text;
import static dev.tamboui.toolkit.Toolkit.textInput;

import java.util.function.Consumer;

import com.lmgbgpatr.ui.App;
import com.lmgbgpatr.ui.AppState;

import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.TextInputElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.widgets.input.TextInputState;
import dev.tamboui.widgets.tabs.TabsState;

public final class LoginView {

    private static TextInputState usernameState = new TextInputState();
    private static TextInputState passwordState = new TextInputState();
    private static TabsState tabState = new TabsState(0);    

    public record Credentials(String username, String password, Integer intent) {
        public Credentials() {
            this(usernameState.text(), passwordState.text(), tabState.selected());
        }
    }

    private LoginView() {}

    private static TextInputElement generateField(String label) {
        return textInput(label.equals("username") ? usernameState : passwordState)
            .placeholder(label)
            .id(label + "-input");
    }

    private static Runnable safeOnSubmitWrapper(AppState state, Consumer<Credentials> onSubmit) {
        return () -> {
            try {
                onSubmit.accept(new Credentials());
            } catch (Exception e) {
                state.put("errorMessage", String.class, e.toString());
                state.put("errorPending", Boolean.class, true);
            }
        };
    }

    public static Element render(AppState state, Consumer<Credentials> onSubmit) {
        final App app = state.get("app", App.class);

        final String bottomText = "[enter] submit / [esc] exit";
        final Integer bottomTextWidth = (int) (CharWidth.of(bottomText) * 1.25);

        final String[] tabTitles = {"Login", "Register"};
        final Integer tabTitlesWidth = (int) ((tabTitles[0].length() + tabTitles[1].length()) * 2);

        final Element tabBar = tabs(tabTitles)
            .state(tabState)
            .highlightStyle(Style.EMPTY.bold().fg(Color.CYAN).underlined())
            .divider(" | ");

        if (tabState.selected() == null) {
            tabState.select(0);
        }

        if (state.get("errorPending", Boolean.class)) {
            return ErrorView.render(state);
        }

        return panel(state.get("title", String.class),
            column(
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
                .onConfirm(safeOnSubmitWrapper(state, onSubmit))
                .onCancel(app::quit)
                .onKeyEvent(event -> {
                    if (event.code() == KeyCode.F1) {
                        tabState.select(0);
                        return EventResult.HANDLED;
                    } else if (event.code() == KeyCode.F2) {
                        tabState.select(1);
                        return EventResult.HANDLED;
                    }
                    return EventResult.UNHANDLED;
                })
            ).fill()
        )
        .titleCenter()
        .fill()
        .rounded();
    }
}