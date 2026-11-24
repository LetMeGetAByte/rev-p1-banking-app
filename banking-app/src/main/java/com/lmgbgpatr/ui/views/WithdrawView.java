package com.lmgbgpatr.ui.views;

import static dev.tamboui.toolkit.Toolkit.row;
import static dev.tamboui.toolkit.Toolkit.spacer;
import static dev.tamboui.toolkit.Toolkit.column;
import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.text;
import static dev.tamboui.toolkit.Toolkit.textInput;

import com.lmgbgpatr.ui.Renderable;
import com.lmgbgpatr.ui.states.ViewState;

import dev.tamboui.text.CharWidth;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.widgets.input.TextInputState;

public class WithdrawView extends Renderable {

    private static TextInputState amountState = new TextInputState();
    private static String amountInputID = "amount-input";

    private static final String bottomText = "[enter] withdraw / [esc] cancel";
    private static final Integer bottomTextWidth = (int) (CharWidth.of(bottomText) * 1.25);

    public static Element render() {
        ctx.registerID(ViewState.Withdraw, amountInputID);

        return dialog(
            "Withdraw",

            column(
                textInput(amountState)
                    .placeholder("Amount")
                    .id(amountInputID),
                text(bottomText).centered()
            )
        )
        .width(bottomTextWidth)
        .onConfirm(null)
        .onCancel(() -> {amountState.clear(); ctx.popView();});
    }
}
