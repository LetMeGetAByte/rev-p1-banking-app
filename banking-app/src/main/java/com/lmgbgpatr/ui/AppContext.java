package com.lmgbgpatr.ui;

import com.lmgbgpatr.middleware.Bridge;
import com.lmgbgpatr.ui.states.AppState;
import com.lmgbgpatr.ui.states.ViewState;

public interface AppContext {

    AppState getState();
    Bridge getBridge();
    void setView(ViewState vs);
    ViewState getView();
    void prevView();
    int cnt();
    void quit();
}
