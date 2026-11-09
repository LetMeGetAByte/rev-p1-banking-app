package com.lmgbgpatr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lmgbgpatr.backend.Driver;
import com.lmgbgpatr.middleware.Bridge;
import com.lmgbgpatr.middleware.dto.Retrieval;
import com.lmgbgpatr.middleware.dto.RetrievalRequest;
import com.lmgbgpatr.middleware.dto.RetrievalResult;
import com.lmgbgpatr.middleware.dto.UserLevel;
import com.lmgbgpatr.ui.App;
import com.lmgbgpatr.ui.states.AppState;

public class Main {

    public static final HashMap<Integer, UserLevel> users = new HashMap<>();

    public static UserLevel loginUser(Integer userHash) {
        return users.getOrDefault(userHash, UserLevel.UNAUTHORIZED);
    }

    public static UserLevel registerUser(String username, Integer hash) {
        if (!users.containsKey(hash)) {
            users.put(hash, UserLevel.CUSTOMER);
            return UserLevel.CUSTOMER;
        }

        return UserLevel.UNAUTHORIZED;
    }

    public static RetrievalResult<String> retrieveData(Integer userHash, RetrievalRequest request) {
        if (RetrievalRequest.ACCOUNTS.equals(request)) {
            Retrieval<String> retrieval = new Retrieval<>(String.class, List.of("a", "b", "c"));
            return new RetrievalResult<>(Map.of(userHash, retrieval));
        }

        return new RetrievalResult<>(Map.of(userHash, new Retrieval<String>(String.class, null)));
    }

    public static void main(String[] args) throws Exception {
        Driver driver = new Driver();
        AppState state = new AppState();
        Bridge bridge = new Bridge(state);
        bridge.setLoginHandler(Main::loginUser);
        bridge.setRegisterHandler(Main::registerUser);
        bridge.setRetrievalHandler(Main::retrieveData);
        new App(state, bridge).run();
    }
}