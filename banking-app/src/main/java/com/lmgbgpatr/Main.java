package com.lmgbgpatr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lmgbgpatr.dao.DAO;
import com.lmgbgpatr.dto.RetrievalRequest;
import com.lmgbgpatr.dto.RetrievalResult;
import com.lmgbgpatr.dto.UserLevel;
import com.lmgbgpatr.service.Bridge;
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