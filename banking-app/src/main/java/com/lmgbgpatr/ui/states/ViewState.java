package com.lmgbgpatr.ui.states;

/**
 * Used to store views as a single type for easier switching
 * ViewState
 */
public sealed interface ViewState {
    record Login() implements ViewState {}
    record Dashboard() implements ViewState {}
    record Empty() implements ViewState {}
    record Notify() implements ViewState {}
    record Error() implements ViewState {}
}
