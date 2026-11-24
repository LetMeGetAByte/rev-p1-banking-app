package com.lmgbgpatr.ui.states;

import com.lmgbgpatr.ui.views.AccountView;
import com.lmgbgpatr.ui.views.CreationView;
import com.lmgbgpatr.ui.views.DashboardView;
import com.lmgbgpatr.ui.views.DepositView;
import com.lmgbgpatr.ui.views.EmptyView;
import com.lmgbgpatr.ui.views.ErrorView;
import com.lmgbgpatr.ui.views.LoginView;
import com.lmgbgpatr.ui.views.NotifyView;
import com.lmgbgpatr.ui.views.TransferView;
import com.lmgbgpatr.ui.views.WithdrawView;

import dev.tamboui.toolkit.element.Element;

public enum ViewState {
    Login,
    Dashboard,
    Account,
    Creation,
    Deposit,
    Withdraw,
    Transfer,
    Empty,
    Notify,
    Error;

    public static Element toElement(ViewState state) {
        return switch (state) {
            case ViewState.Login -> LoginView.render();
            case ViewState.Dashboard -> DashboardView.render();
            case ViewState.Account -> AccountView.render();
            case ViewState.Creation -> CreationView.render();
            case ViewState.Deposit -> DepositView.render();
            case ViewState.Withdraw -> WithdrawView.render();
            case ViewState.Transfer -> TransferView.render();
            case ViewState.Notify -> NotifyView.render();
            case ViewState.Error -> ErrorView.render();
            default -> EmptyView.render();
        };
    }
}
