package com.ilhamrhmtkbr.domain.repository;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginWithGoogleRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthRegisterRequest;

public interface AuthRepository {
    void login(UserAuthLoginRequest request, FormCallback<String> callback);
    void register(UserAuthRegisterRequest request, FormCallback<String> callback);
    void loginWithGoogle(UserAuthLoginWithGoogleRequest request, FormCallback<String> callback);
}
