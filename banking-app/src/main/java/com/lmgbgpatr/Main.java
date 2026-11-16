package com.lmgbgpatr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lmgbgpatr.backend.DAO;
import com.lmgbgpatr.middleware.Bridge;
import com.lmgbgpatr.middleware.dto.Retrieval;
import com.lmgbgpatr.middleware.dto.RetrievalRequest;
import com.lmgbgpatr.middleware.dto.RetrievalResult;
import com.lmgbgpatr.middleware.dto.UserLevel;
import com.lmgbgpatr.ui.App;
import com.lmgbgpatr.ui.states.AppState;

public class Main {

    public static void main(String[] args) throws Exception {
        DAO dao = new DAO();
        AppState state = new AppState();
        Bridge bridge = new Bridge(state);
        bridge.setLoginHandler(dao::login);
        bridge.setRegisterHandler(dao::register);
        bridge.setRetrievalHandler(dao::request);
        new App(state, bridge).run();
    }
}