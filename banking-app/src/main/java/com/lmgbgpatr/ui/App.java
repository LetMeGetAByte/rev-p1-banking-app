package com.lmgbgpatr.ui;

import com.lmgbgpatr.ui.views.LoginView;
import com.lmgbgpatr.ui.views.ViewState;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;

// 9. Create the Console Interface

// Create a clear console interface that allows:

//     Customers to register, log in, manage accounts, perform transactions, and view transaction history.
//     Employees to log in, look up customers and accounts, perform wire transfers, freeze or unfreeze accounts, create and view account notes, and view activity logs.

// The interface should provide understandable menus, prompts, results, and error messages, and should only show options the current user is permitted to use.

public class App extends ToolkitApp {
    
    private final AppState state = new AppState();

    public App() {
        state.put("app", App.class, this);
        state.put("title", String.class, "Revature Banking App");
        state.put("view", ViewState.class, new ViewState.Login());
        state.put("errorMessage", String.class, "");
        state.put("errorPending", Boolean.class, false);
    }

    private Element renderView() {
        ViewState viewState = state.get("view", ViewState.class);

        if (viewState instanceof ViewState.Login) {
            return LoginView.render(state, null);
        }

        return null;
    }

    public void setFocus(String elementId) {
        runner().focusManager().setFocus(elementId);
    }

    @Override
    protected Element render() {
        return renderView();
    }

    @Override
    public void quit() {
        super.quit();
    }
}