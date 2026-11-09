package com.lmgbgpatr.middleware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.lmgbgpatr.middleware.dto.Retrieval;
import com.lmgbgpatr.middleware.dto.RetrievalRequest;
import com.lmgbgpatr.middleware.dto.RetrievalResult;
import com.lmgbgpatr.middleware.dto.UserLevel;
import com.lmgbgpatr.ui.states.AppState;
import com.lmgbgpatr.ui.states.NotifyState;
import com.lmgbgpatr.ui.states.UserState;

public class Bridge {

    @FunctionalInterface
    public interface LoginHandler extends Function<Integer, UserLevel> {}

    @FunctionalInterface
    public interface RegistrationHandler extends BiFunction<String, Integer, UserLevel> {}

    @FunctionalInterface
    public interface RetrievalHandler extends BiFunction<Integer, RetrievalRequest, RetrievalResult<?>> {}

    private final UserState user = UserState.instance();

    private AppState state;
    private LoginHandler loginHandler;
    private RegistrationHandler registrationHandler;
    private RetrievalHandler retrievalHandler;

    public Bridge() {}

    public Bridge(AppState state) {
        this.state = state;
    }

    public void setState(AppState state) {
        this.state = state;
    }

    public void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    public void setRegisterHandler(RegistrationHandler registrationHandler) {
        this.registrationHandler = registrationHandler;
    }

    public void setRetrievalHandler(RetrievalHandler retrievalHandler) {
        this.retrievalHandler = retrievalHandler;
    }

    public void notify(String title, String message, Boolean isError) {
        state.put("notify", NotifyState.class, new NotifyState(title, message, isError));
    }

    public void handleLogin() {
        UserState user = UserState.instance();
        UserLevel userLevel = loginHandler.apply(user.hash());

        if (userLevel.compareTo(UserLevel.CUSTOMER) < 0) {
            notify("Login Failed", "Incorrect username or password.", false);
        } else {
            user.setLevel(userLevel);
        }
    }

    public void handleRegistration() {
        UserLevel userLevel = registrationHandler.apply(user.username(), user.hash());

        if (userLevel.compareTo(UserLevel.CUSTOMER) < 0) {
            notify("Registration Failed", "An unknown error occured.", false);
        } else {
            notify("Registration Successful", "Please login to access your account.", false);
        }
    }

    public RetrievalResult<?> handleRetrieval(RetrievalRequest request) {
        return retrievalHandler.apply(user.hash(), request);
    }
}
