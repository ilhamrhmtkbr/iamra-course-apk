package com.ilhamrhmtkbr.data.repository;

import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginWithGoogleRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthRegisterRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.api.UserApi;
import com.ilhamrhmtkbr.domain.repository.AuthRepository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {
    private final UserApi api;

    @Inject
    public AuthRepositoryImpl(UserApi api) {
        this.api = api;
    }

    public void login(UserAuthLoginRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.userLogin(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    callback.onResult(FormState.success(response.body().message, null));
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    public void register(UserAuthRegisterRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.userRegister(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    callback.onResult(FormState.success(response.body().message, null));
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    public void loginWithGoogle(UserAuthLoginWithGoogleRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.userLoginWithGoggle(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    callback.onResult(FormState.success(response.body().message, null));
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }
}
