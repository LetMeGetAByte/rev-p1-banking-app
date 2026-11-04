package com.lmgbgpatr.ui;

import java.util.Map;

import java.util.HashMap;

/**
 * 
 * Object that stores variably typed app states. Each state requires a unique key.
 */
public class AppState {
    
    public record State<T>(String name, Class<T> type) {}

    private final Map<State<?>, Object> states = new HashMap<>();
    
    /**
     * Maps a state's key to its value.
     * @param <T> - generic type of state's object
     * @param key - State<T> key for state
     * @param value - state object
     * @return value previously mapped to state or null if unmapped
     */
    public <T> T put(State<T> key, T value) {
        Object state = states.put(key, value);
        return key.type().cast(state);
    }

    /**
     * Maps a state name with its value.
     * Automatically generates the state's key using the name and type.
     * @param <T> - generic type of state object
     * @param name - name of state
     * @param type - type of state object
     * @param value - state object
     * @return insantiated State<T> key for state
     */
    public <T> State<T> put(String name, Class<T> type, T value) {
        State<T> state = new State<T>(name, type);
        this.states.put(state, value);
        return state;
    }

    /**
     * Returns the value mapped to the specified state or null if unmapped.
     * @param <T> - generic type of state's object
     * @param key - State<T> key for state
     * @return value mapped to state
     */
    public <T> T get(State<T> key) {
        Object state = states.get(key);
        return key.type().cast(state);
    }

    /**
     * Returns the value mapped to the specified state or null if unmapped.
     * @param <T> - generic type of state's object
     * @param key - State<T> key for state
     * @return value mapped to state
     */
    public <T> T get(String name, Class<T> type) {
        return get(new State<>(name, type));
    }

    /**
     * Removes the mapping for a state if it is present. Returns the removed value or null if unmapped.
     * @param <T> - generic type for state's object
     * @param key - State<T> key for state
     * @return value mapped to state or null if unmapped
     */
    public <T> T remove(State<T> key) {
        Object state = states.remove(key);
        return key.type().cast(state);
    }
}
