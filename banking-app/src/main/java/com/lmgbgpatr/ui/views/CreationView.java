package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.row;
import static dev.tamboui.toolkit.Toolkit.spacer;
import static dev.tamboui.toolkit.Toolkit.tabs;
import static dev.tamboui.toolkit.Toolkit.text;

import com.lmgbgpatr.ui.Renderable;
import com.lmgbgpatr.ui.states.ViewState;

import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.widgets.tabs.TabsState;

public class CreationView extends Renderable {

    private static TabsState tabsState = new TabsState();

    private static final String bottomText = "[enter] create / [esc] cancel";
    private static final Integer bottomTextWidth = (int) (CharWidth.of(bottomText) * 1.25);

    private static final String[] tabTitles = {"Checking", "Saving"};
    private static final Integer tabTitlesWidth = (int) ((tabTitles[0].length() + tabTitles[1].length()) * 2);

    private static final Element tabBar = tabs(tabTitles)
        .state(tabsState)
        .highlightStyle(Style.EMPTY.bold().fg(Color.CYAN).underlined())
        .divider(" | ");

    public static Element render() {

        if (tabsState.selected() == null) {
            tabsState.select(0);
        }

        return dialog(
                "Create Account",
                row(
                    spacer(),
                    tabBar,
                    spacer()
                ),
                text(bottomText)
                    .centered()
                    .dim()
            )
            .width(Math.max(bottomTextWidth, tabTitlesWidth))
            .rounded()
            .onConfirm(null)
            .onCancel(() -> ctx.prevView())
            .onKeyEvent(event -> {
                if (event.code() == KeyCode.F1) {
                    tabsState.select(0);
                    return EventResult.HANDLED;
                } else if (event.code() == KeyCode.F2) {
                    tabsState.select(1);
                    return EventResult.HANDLED;
                }
                return EventResult.UNHANDLED;
            });
    }
}
