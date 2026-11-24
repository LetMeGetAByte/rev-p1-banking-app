package com.lmgbgpatr.ui;

import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.stack;

import java.util.Iterator;

import com.lmgbgpatr.middleware.Bridge;
import com.lmgbgpatr.ui.states.AppState;
import com.lmgbgpatr.ui.states.NotifyState;
import com.lmgbgpatr.ui.states.UserState;
import com.lmgbgpatr.ui.states.ViewState;
import com.lmgbgpatr.ui.views.AccountView;
import com.lmgbgpatr.ui.views.CreationView;
import com.lmgbgpatr.ui.views.DashboardView;
import com.lmgbgpatr.ui.views.EmptyView;
import com.lmgbgpatr.ui.views.ErrorView;
import com.lmgbgpatr.ui.views.LoginView;
import com.lmgbgpatr.ui.views.NotifyView;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;

public class App extends ToolkitApp implements AppContext {
    
    private ViewStack viewStack;
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

    public void registerID(ViewState state, String id) {
        viewStack.registerID(state, id);
    }

    public ViewState peekView() {
        return viewStack.peek();
    }

    public void pushView(ViewState state) {
        viewStack.push(state);
    }

    public void popView() {
        viewStack.pop();
    }

    public void clearView() {
        viewStack.clear();
    }

    private void pollNotify() {
        ViewState top = peekView();
        NotifyState notify = state.get("notify", NotifyState.class);

        if (notify != null) {
            ViewState target = notify.isError() ? ViewState.Error : ViewState.Notify;
            if (top != target && (top == ViewState.Notify || top == ViewState.Error)) {
                popView();
            }
            pushView(target);
        } else if (top == ViewState.Notify || top == ViewState.Error) {
            popView();
        }
    }

    private void pollLoginState() {
        boolean authorized = Boolean.TRUE.equals(UserState.instance().authorized());
        ViewState top = peekView();

        if (authorized) {
            if (top == null || top == ViewState.Login) {
                if (top == ViewState.Login) {
                    popView();
                }
                pushView(ViewState.Dashboard);
            }
        } else if (!viewStack.isRendered(ViewState.Login)) {
            viewStack.clear();
            pushView(ViewState.Login);
        }
    }

    @Override
    protected Element render() {
        if (viewStack == null) {
            viewStack = new ViewStack(runner());
        }

        pollLoginState();
        pollNotify();

        return panel(
            state.get("title", String.class),
            stack(
                viewStack.elementsToArray()
            ).fill()
        )
        .titleCenter()
        .fill();
    }

    @Override
    public void quit() {
        super.quit();
    }
}