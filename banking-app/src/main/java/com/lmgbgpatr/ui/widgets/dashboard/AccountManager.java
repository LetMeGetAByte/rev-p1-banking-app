package com.lmgbgpatr.ui.widgets.dashboard;

import static dev.tamboui.toolkit.Toolkit.list;
import static dev.tamboui.toolkit.Toolkit.panel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.lmgbgpatr.ui.Bridge.Retrieval;
import com.lmgbgpatr.ui.Bridge.RetrievalRequest;
import com.lmgbgpatr.ui.Bridge.RetrievalResult;
import com.lmgbgpatr.ui.widgets.Widget;

import dev.tamboui.style.Color;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import dev.tamboui.toolkit.event.EventResult;

/**
 * 
 * Customer facing element for viewing accounts
 */
public class AccountManager extends Widget {

    private static List<String> accounts = retrieveAccounts();
    private static ListElement<?> accountsList = 
        list(accounts)
            .highlightColor(Color.CYAN)
            .autoScroll();

    private static List<String> retrieveAccounts() {
        RetrievalResult<?> result = app.bridge.handleRetrieval(RetrievalRequest.ACCOUNTS);
        Collection<? extends Retrieval<?>> retrievals = result.results().values();
        List<String> data = 
            retrievals
                .stream()
                .map(Retrieval::data)
                .flatMap(Collection::stream)
                .map(Object::toString)
                .collect(Collectors.toList());

        return data;
    }

    public static Element render() {
        return panel("Account Manager",
            accountsList.onKeyEvent(event -> {
                if (event.isUp()) {
                    accountsList.selectPrevious();
                    return EventResult.HANDLED;
                } else if (event.isDown()) {
                    accountsList.selectNext(1);
                }
                return EventResult.UNHANDLED;
            })
        )
        .fill();
    }
}
