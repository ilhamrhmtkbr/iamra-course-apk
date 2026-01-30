package com.ilhamrhmtkbr.data.repository;

import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberRegisterAsStudentRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAdditionalInfoRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAuthenticationRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateEmailRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.api.UserApi;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberRepositoryImpl implements MemberRepository {
    private final UserApi api;
    private final AuthStateManager authStateManager;

    @Inject
    public MemberRepositoryImpl(UserApi api, AuthStateManager authStateManager) {
        this.api = api;
        this.authStateManager = authStateManager;
    }

    @Override
    public void logout() {
        authStateManager.logout();
        api.userLogout().enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {

            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {

            }
        });
    }

    @Override
    public void getMe(FormCallback<UserResponse> callback) {
        callback.onResult(FormState.loading());
        api.userGetData().enqueue(new Callback<BaseResponseApi<UserResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<UserResponse>> call, Response<BaseResponseApi<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    authStateManager.saveUserSession(response.body().data);
                    callback.onResult(FormState.success(response.body().message, response.body().data));
                } else if (response.errorBody() != null) {
                    try {
                        callback.onResult(FormState.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<UserResponse>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void updateAdditionalInfo(MemberUpdateAdditionalInfoRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.memberUpdateAdditionalInfo(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void updateAuthentication(MemberUpdateAuthenticationRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.memberUpdateAuthentication(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void updateEmail(MemberUpdateEmailRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.memberUpdateEmail(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void registerAsStudent(MemberRegisterAsStudentRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.registerAsStudent(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void registerAsInstructor(RequestBody role, RequestBody summary, MultipartBody.Part resume, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.registerAsInstructor(role, summary, resume).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void storeLike(String courseId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.memberUpSertCourseLike(courseId).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<Void>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }
}
