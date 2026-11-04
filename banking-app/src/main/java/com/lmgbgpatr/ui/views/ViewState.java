package com.lmgbgpatr.ui.views;

/**
 * Used to store views as a single type for easier switching
 * ViewState
 */
public sealed interface ViewState {
    record Login() implements ViewState {}
    record Dashboard() implements ViewState {}
}
