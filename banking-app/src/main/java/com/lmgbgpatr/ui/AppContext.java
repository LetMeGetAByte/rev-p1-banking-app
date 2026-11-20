package com.lmgbgpatr.ui;

import com.lmgbgpatr.middleware.Bridge;
import com.lmgbgpatr.ui.states.AppState;

public interface AppContext {

    AppState getState();
    Bridge getBridge();
    void quit();
}
