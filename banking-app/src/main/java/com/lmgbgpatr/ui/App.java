package com.lmgbgpatr.ui;

import static dev.tamboui.toolkit.Toolkit.panel;

import com.lmgbgpatr.middleware.Bridge;
import com.lmgbgpatr.ui.states.AppState;
import com.lmgbgpatr.ui.states.NotifyState;
import com.lmgbgpatr.ui.states.UserState;
import com.lmgbgpatr.ui.states.ViewState;
import com.lmgbgpatr.ui.views.DashboardView;
import com.lmgbgpatr.ui.views.EmptyView;
import com.lmgbgpatr.ui.views.ErrorView;
import com.lmgbgpatr.ui.views.LoginView;
import com.lmgbgpatr.ui.views.NotifyView;
import com.lmgbgpatr.ui.views.View;
import com.lmgbgpatr.ui.widgets.Widget;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;

// 9. Create the Console Interface

// Create a clear console interface that allows:

//     Customers to register, log in, manage accounts, perform transactions, and view transaction history.
//     Employees to log in, look up customers and accounts, perform wire transfers, freeze or unfreeze accounts, create and view account notes, and view activity logs.

// The interface should provide understandable menus, prompts, results, and error messages, and should only show options the current user is permitted to use.

public class App extends ToolkitApp {
    
    public final AppState state;
    public final Bridge bridge;

    public App(String title, AppState state, Bridge bridge) {
        this.state = state;
        this.bridge = bridge;
        state.put("title", String.class, title);
        state.put("user", UserState.class, UserState.instance());

        View.setApp(this);
        Widget.setApp(this);
    }

    public App(AppState state, Bridge bridge) {
        this("Revature Banking App", state, bridge);
    }

    private void pollNotify() {
        ViewState viewState;
        NotifyState notify = state.get("notify", NotifyState.class);
        if (notify != null) {
            if (notify.isError()) {
                viewState = new ViewState.Error();
            } else {
                viewState = new ViewState.Notify();
            }
            state.put("view", ViewState.class, viewState);
        }
    }

    private void pollLoginState() {
        ViewState viewState;
        Boolean authorized = UserState.instance().authorized();
        if (authorized) {
            viewState = new ViewState.Dashboard();
        } else {
            viewState = new ViewState.Login();
        }
        state.put("view", ViewState.class, viewState);
    }

    private Element renderView() {
        ViewState viewState = state.get("view", ViewState.class);

        return switch(viewState) {
            case ViewState.Login login -> LoginView.render();
            case ViewState.Dashboard dashboard -> DashboardView.render();
            case ViewState.Notify notify -> NotifyView.render();
            case ViewState.Error error -> ErrorView.render();
            default -> EmptyView.render();
        };
    }

    public void setFocus(String elementId) {
        runner().focusManager().setFocus(elementId);
    }

    @Override
    protected Element render() {
        pollLoginState();
        pollNotify();

        return panel(state.get("title", String.class),
            renderView()
        )
        .titleCenter()
        .fill();
    }

    @Override
    public void quit() {
        super.quit();
    }
}