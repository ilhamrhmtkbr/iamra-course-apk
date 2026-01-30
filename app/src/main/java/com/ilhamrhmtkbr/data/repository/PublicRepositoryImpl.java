package com.ilhamrhmtkbr.data.repository;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.guest.CourseMapper;
import com.ilhamrhmtkbr.data.mapper.guest.SectionMapper;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.domain.repository.PublicRepository;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCertificateVerifyResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCourseDetailResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCourseSectionResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCoursesResponse;
import com.ilhamrhmtkbr.data.remote.api.PublicApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicRepositoryImpl implements PublicRepository {
    private final PublicApi api;
    private final MediatorLiveData<Resource<List<Course>>> coursesResult = new MediatorLiveData<>();
    private final MediatorLiveData<Resource<List<Page>>> coursesPagination = new MediatorLiveData<>();
    private final MediatorLiveData<Resource<Course>> courseResult = new MediatorLiveData<>();
    private final MediatorLiveData<Resource<List<Section>>> sectionsResult = new MediatorLiveData<>();

    @Inject
    public PublicRepositoryImpl(PublicApi api) {
        this.api = api;
    }

    @Override
    public void courses(String keyword, String page, String orderBy, String level, String status) {
        coursesResult.postValue(Resource.loading());

        api.getCourses(keyword, page, orderBy, level, status).enqueue(new Callback<PublicCoursesResponse>() {
            @Override
            public void onResponse(Call<PublicCoursesResponse> call, Response<PublicCoursesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    coursesResult.postValue(Resource.success(CourseMapper.fromResponseToCourseList(response.body())));
                    if (response.body().meta != null && response.body().meta.links != null) {
                        List<Page> pages = new ArrayList<>();
                        for (PublicCoursesResponse.Page item : response.body().meta.links) {
                            pages.add(new Page(item.url, item.label, item.active));
                        }
                        coursesPagination.postValue(Resource.success(pages));
                    } else {
                        coursesPagination.postValue(Resource.success(null));
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        coursesResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<PublicCoursesResponse> call, Throwable t) {
                coursesResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public MediatorLiveData<Resource<List<Course>>> getCoursesResult() {
        return coursesResult;
    }

    @Override
    public MediatorLiveData<Resource<List<Page>>> getCoursesPagination() {
        return coursesPagination;
    }

    @Override
    public void course(String courseId) {
        courseResult.postValue(Resource.loading());

        api.getCourse(courseId).enqueue(new Callback<BaseResponseApi<PublicCourseDetailResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<PublicCourseDetailResponse>> call, Response<BaseResponseApi<PublicCourseDetailResponse>> response) {
                if (response.isSuccessful()) {
                    Log.d("Repo", "success");
                    if (response.body() != null) {
                        Log.d("Repo", response.body().toString());
                        if (response.body().data != null) {
                            Log.d("Repo", "response data : ");
                        }
                    }
                } else {
                    Log.d("Repo", "ga success");
                }

                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    courseResult.postValue(Resource.success(CourseMapper.fromResponseToCourseDetail(response.body().data)));
                } else if (response.errorBody() != null) {
                    try {
                        courseResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        courseResult.postValue(Resource.error(e.getMessage()));
                        throw new RuntimeException(e);
                    }
                } else {
                    courseResult.postValue(Resource.error("error"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<PublicCourseDetailResponse>> call, Throwable t) {
                courseResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    public MediatorLiveData<Resource<Course>> getCourseResult() {
        return courseResult;
    }

    @Override
    public void courseSection(String courseId) {
        sectionsResult.postValue(Resource.loading());

        api.getCourseSection(courseId).enqueue(new Callback<BaseResponseApi<List<PublicCourseSectionResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<List<PublicCourseSectionResponse>>> call, Response<BaseResponseApi<List<PublicCourseSectionResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    sectionsResult.postValue(Resource.success(SectionMapper.fromResponseToList(response.body().data)));
                } else if (response.errorBody() != null) {
                    try {
                        sectionsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        sectionsResult.postValue(Resource.error(e.getMessage()));
                        throw new RuntimeException(e);
                    }
                } else {
                    sectionsResult.postValue(Resource.error("error"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<List<PublicCourseSectionResponse>>> call, Throwable t) {
                sectionsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public MediatorLiveData<Resource<List<Section>>> getSectionResult() {
        return sectionsResult;
    }

    @Override
    public void certificateVerify(String certId, FormCallback<PublicCertificateVerifyResponse> callback) {
        callback.onResult(FormState.loading());

        api.getCertificateVerify(certId).enqueue(new Callback<BaseResponseApi<PublicCertificateVerifyResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<PublicCertificateVerifyResponse>> call, Response<BaseResponseApi<PublicCertificateVerifyResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, response.body().data));
                } else if (response.errorBody() != null) {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<PublicCertificateVerifyResponse> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error("Failed to parse error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<PublicCertificateVerifyResponse>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }
}
