package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.text;

import com.lmgbgpatr.ui.AppState;

import dev.tamboui.style.Color;
import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.widgets.block.BorderType;

public class ErrorView {

    private ErrorView() {}

    private static void dismiss(AppState state) {
        state.put("errorPending", Boolean.class, false);
    }

    public static Element render(AppState state) {
        String error = state.get("errorMessage", String.class);
        Integer errorWidth = (int) (CharWidth.of(error) * 1.1);

        return dialog("Error",
            text(error).centered()
        )
        .fg(Color.RED)
        .borderType(BorderType.DOUBLE)
        .borderColor(Color.RED)
        .rounded()
        .width(errorWidth)
        .onConfirm(() -> dismiss(state))
        .onCancel(() -> dismiss(state));
    }
}
