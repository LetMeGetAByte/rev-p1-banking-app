package com.lmgbgpatr.ui.widgets.dashboard;

import static dev.tamboui.toolkit.Toolkit.list;
import static dev.tamboui.toolkit.Toolkit.panel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.lmgbgpatr.dto.RetrievalRequest;
import com.lmgbgpatr.dto.RetrievalResult;
import com.lmgbgpatr.model.Account;
import com.lmgbgpatr.ui.Renderable;
import com.lmgbgpatr.ui.states.ViewState;

import dev.tamboui.style.Color;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import dev.tamboui.toolkit.event.EventResult;

public class AccountManager extends Renderable {

    private static List<Account> accounts;
    private static List<String> accountsAsStrings;
    private static ListElement<?> accountsList;
    
    private static boolean needsRefresh = true;

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

        if (needsRefresh) {
            accounts = retrieveAccounts();

            accountsAsStrings =
                accounts
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());

            accountsList =
                list(accountsAsStrings)
                    .add("[+] Create Account")
                    .highlightColor(Color.CYAN)
                    .autoScroll();
            
            needsRefresh = false;
        }

        return panel("Account Manager",
            accountsList.onKeyEvent(event -> {
                if (!ctx.peekView().equals(ViewState.Dashboard)) return EventResult.UNHANDLED;

                if (event.isConfirm()) {
                    int sel = accountsList.selected();
                    if (sel == accounts.size()) {
                        ctx.pushView(ViewState.Creation);
                    } else {
                        storeSelectedAccount();
                        ctx.pushView(ViewState.Account);
                    }
                } else {
                    return EventResult.UNHANDLED;
                }

                return EventResult.HANDLED;
            })
        )
        .fill();
    }
}
