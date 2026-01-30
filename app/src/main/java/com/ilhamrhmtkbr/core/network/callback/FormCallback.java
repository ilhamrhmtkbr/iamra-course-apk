package com.ilhamrhmtkbr.core.network.callback;

import com.ilhamrhmtkbr.core.state.FormState;

public interface FormCallback<T> {
    void onResult(FormState<T> state);
}
