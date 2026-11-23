package com.lmgbgpatr.ui.widgets.dashboard;

import static dev.tamboui.toolkit.Toolkit.list;
import static dev.tamboui.toolkit.Toolkit.panel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.lmgbgpatr.middleware.dto.Account;
import com.lmgbgpatr.middleware.dto.RetrievalRequest;
import com.lmgbgpatr.middleware.dto.RetrievalResult;
import com.lmgbgpatr.ui.Renderable;
import com.lmgbgpatr.ui.states.ViewState;

import dev.tamboui.style.Color;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import dev.tamboui.toolkit.event.EventResult;

/**
 * 
 * Customer facing element for viewing accounts
 */
public class AccountManager extends Renderable {

    private static List<Account> accounts = retrieveAccounts();
    private static List<String> accountsAsStrings =
        accounts
            .stream()
            .map(Object::toString)
            .collect(Collectors.toList());

    private static ListElement<?> accountsList = 
        list(accountsAsStrings)
            .add("[+] Create Account")
            .highlightColor(Color.CYAN)
            .autoScroll();

    // RetrievalRequest.ACCOUNTS will always return RetrievalResult<Account>
    @SuppressWarnings("unchecked")
    private static List<Account> retrieveAccounts() {
        RetrievalResult<?> results = ctx.getBridge().handleRetrieval(RetrievalRequest.ACCOUNTS);

        return (List<Account>) results.getAll();
    }

    private static void storeSelectedAccount() {
        ctx.getState().put("selectedAccount", Account.class, accounts.get(accountsList.selected()));
    }

    public static Element render() {
        return panel("Account Manager",
            accountsList.onKeyEvent(event -> {
                if (event.isUp()) {
                    accountsList.selectLast(0);
                } else if (event.isDown()) {
                    accountsList.selectNext(1);
                } else if (event.isConfirm()) {
                    int sel = accountsList.selected();
                    if (sel == accounts.size()) {
                        ctx.setView(new ViewState.CreationView());
                    } else {
                        storeSelectedAccount();
                        ctx.setView(new ViewState.AccountView());
                    }
                    return EventResult.HANDLED;
                }

                return EventResult.UNHANDLED;
            })
        )
        .fill();
    }
}
