package com.lmgbgpatr.ui;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.lmgbgpatr.ui.states.ViewState;
import com.lmgbgpatr.ui.views.AccountView;
import com.lmgbgpatr.ui.views.CreationView;
import com.lmgbgpatr.ui.views.DashboardView;
import com.lmgbgpatr.ui.views.EmptyView;
import com.lmgbgpatr.ui.views.ErrorView;
import com.lmgbgpatr.ui.views.LoginView;
import com.lmgbgpatr.ui.views.NotifyView;

import dev.tamboui.toolkit.app.ToolkitRunner;
import dev.tamboui.toolkit.element.ContainerElement;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.focus.FocusManager;

public class ViewStack {

    private final ToolkitRunner runner;
    private final FocusManager focusManager;
    private final Map<ViewState, String> registeredID = new HashMap<>();
    private final Set<ViewState> rendered = new HashSet<>();
    private final Deque<ViewState> views = new ArrayDeque<>();

    public ViewStack(ToolkitRunner runner) {
        this.runner = runner;
        this.focusManager = runner.focusManager();
    }

    /**
     * Peeks at the top view and attempts to retrieve its ID.
     * If the top view is not focusable, it will instead check if the view has a registered ID.
     * @return ID or null if no ID was found.
     */
    private String peekID() {
        if (views.isEmpty()) {
            return null;
        }

        Element topElement = peekElement();

        if (topElement.isFocusable() && topElement.id() != null) {
            return topElement.id();
        }

        return registeredID.getOrDefault(peek(), null);
    }

    private void focusTop() {
        String targetId = peekID();
        if (targetId == null) {
            return;
        }

        runner.schedule(() -> focusManager.setFocus(targetId), Duration.ZERO);
    }

    public void registerID(ViewState state, String id) {
        registeredID.put(state, id);
    }

    public ViewState peek() {
        return views.peek();
    }

    public Element peekElement() {
        if (views.isEmpty()) {
            return null;
        }

        return ViewState.toElement(peek());
    }

    public boolean push(ViewState state) {
        if (state == null || state.equals(peek())) {
            return false;
        }

        boolean bubbleUp = false;
        if (rendered.contains(state)) {
            views.remove(state);
            rendered.remove(state);
            bubbleUp = true;
        }

        Element e = ViewState.toElement(state);
        views.push(state);
        rendered.add(state);
        focusTop();
        return bubbleUp;
    }

    public ViewState pop() {
        if (views.isEmpty()) {
            return null;
        }

        ViewState top = views.pop();
        rendered.remove(top);
        focusTop();
        return top;
    }

    public void clear() {
        views.clear();
        rendered.clear();
    }

    public boolean isRendered(ViewState state) {
        return rendered.contains(state);
    }

    public Stream<ViewState> stream() {
        return views.reversed().stream();
    }

    public Stream<Element> streamElements() {
        return stream().map(state -> ViewState.toElement(state));
    }

    public Iterator<ViewState> iterator() {
        return views.descendingIterator();
    }

    public ViewState[] toArray() {
        return views.reversed().toArray(ViewState[]::new);
    }

    public Element[] elementsToArray() {
        return streamElements().toArray(Element[]::new);
    }
}