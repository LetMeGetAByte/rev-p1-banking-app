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

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;

public class App extends ToolkitApp implements AppContext {

    private final AppState state;
    private final Bridge bridge;

    public App(String title, AppState state, Bridge bridge) {
        this.state = state;
        this.bridge = bridge;
        state.put("title", String.class, title);
        state.put("user", UserState.class, UserState.instance());

        Renderable.setApp(this);
    }

    public App(AppState state, Bridge bridge) {
        this("Revature Banking App", state, bridge);
    }

    public AppState getState() {
        return state;
    }

    public Bridge getBridge() {
        return bridge;
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

    // this is unused so far, will remove soon if not used
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