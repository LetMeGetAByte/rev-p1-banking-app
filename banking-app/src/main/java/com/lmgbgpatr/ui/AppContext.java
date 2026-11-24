package com.lmgbgpatr.ui;

import com.lmgbgpatr.service.Bridge;
import com.lmgbgpatr.ui.states.AppState;
import com.lmgbgpatr.ui.states.ViewState;

public interface AppContext {

    AppState getState();
    Bridge getBridge();
    void registerID(ViewState state, String id);
    ViewState peekView();
    void pushView(ViewState state);
    void popView();
    void clearView();
    void quit();
}
