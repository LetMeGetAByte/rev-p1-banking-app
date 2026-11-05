package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.text;

import com.lmgbgpatr.ui.states.NotifyState;

import dev.tamboui.style.Color;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.widgets.block.BorderType;

public class NotifyView extends View {

    protected record Style(BorderType borderType, Color fgColor, Color borderColor) {}

    private static final Style STYLE = new Style(BorderType.PLAIN, Color.RESET, Color.RESET);

    protected static void dismiss() {
        app.state.remove("notify", NotifyState.class);
    }

    public static Element render() {
        return render(STYLE);
    }

    public static Element render(Style style) {
        NotifyState notify = app.state.get("notify", NotifyState.class);
        Integer messageWidth = (int) (CharWidth.of(notify.message()) * 1.1);

        return dialog(notify.title(),
            text(notify.message()).centered()
        )
        .fg(style.fgColor())
        .borderType(style.borderType())
        .borderColor(style.borderColor())
        .rounded()
        .width(messageWidth)
        .onConfirm(NotifyView::dismiss)
        .onCancel(NotifyView::dismiss);
    }
}
