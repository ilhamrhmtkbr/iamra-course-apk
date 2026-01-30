package com.ilhamrhmtkbr.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.student.CertificateMapper;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.CertificateDetail;
import com.ilhamrhmtkbr.domain.repository.StudentCertificateRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCertificateStoreRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCertificateResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCertificatesResponse;
import com.ilhamrhmtkbr.data.local.dao.StudentCertificatesDao;
import com.ilhamrhmtkbr.data.local.dao.StudentCertificatesPagination;
import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.StudentApi;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentCertificateRepositoryImpl implements StudentCertificateRepository {
    private final MediatorLiveData<Resource<List<Certificate>>> certificatesResult = new MediatorLiveData<>();
    private final StudentApi api;
    private final StudentCertificatesDao studentCertificatesDao;
    private final StudentCertificatesPagination studentCertificatesPagination;
    private final SingleLiveEvent<Resource<CertificateDetail>> certificateDetailResult = new SingleLiveEvent<>();

    @Inject
    public StudentCertificateRepositoryImpl(StudentApi api, StudentCertificatesDao studentCertificatesDao, StudentCertificatesPagination studentCertificatesPagination) {
        this.api = api;
        this.studentCertificatesDao = studentCertificatesDao;
        this.studentCertificatesPagination = studentCertificatesPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        certificatesResult.postValue(Resource.loading());

        api.certificatesFetch(page, sort).enqueue(new Callback<BaseResponseApi<StudentCertificatesResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentCertificatesResponse>> call, Response<BaseResponseApi<StudentCertificatesResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            studentCertificatesDao.refresh(CertificateMapper.fromResponseToEntities(response.body().data));
                            studentCertificatesPagination.refresh(CertificateMapper.fromResponseToPaginationEntities(response.body().data));
                        }
                    });
                    certificatesResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        certificatesResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentCertificatesResponse>> call, Throwable t) {
                certificatesResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<List<Page>> getPaginationData() {
        return Transformations.map(studentCertificatesPagination.getAll(), entities -> {
            List<Page> pages = new ArrayList<>();
            for (StudentCertificatesPaginationEntity item : entities) {
                pages.add(new Page(item.url, item.label, item.isActive));
            }
            return pages;
        });
    }

    @Override
    public LiveData<Resource<List<Certificate>>> getAllCertificates() {
        LiveData<List<StudentCertificatesEntity>> dbSource = studentCertificatesDao.getAll();
        certificatesResult.addSource(dbSource, new Observer<List<StudentCertificatesEntity>>() {
            @Override
            public void onChanged(List<StudentCertificatesEntity> studentCertificatesEntities) {
                if (studentCertificatesEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Certificate> certificates = CertificateMapper.fromEntitiesToList(studentCertificatesEntities);
                    if (certificatesResult.getValue() == null || !certificatesResult.getValue().isLoading()) {
                        certificatesResult.setValue(Resource.success(certificates));
                    }
                }
            }
        });

        return certificatesResult;
    }

    @Override
    public void show(String certificateId) {
        certificateDetailResult.postValue(Resource.loading());
        api.certificateFetch(certificateId).enqueue(new Callback<StudentCertificateResponse>() {
            @Override
            public void onResponse(Call<StudentCertificateResponse> call, Response<StudentCertificateResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    certificateDetailResult.postValue(Resource.success(CertificateMapper.fromResponseToCertificateDetail(response.body())));
                }
            }

            @Override
            public void onFailure(Call<StudentCertificateResponse> call, Throwable t) {
                certificateDetailResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<CertificateDetail>> getCertificateDetailResult() {
        return certificateDetailResult;
    }

    @Override
    public void store(StudentCertificateStoreRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.certificateStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<String>();
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

    @Override
    public void download(String certificateId, FormCallback<String> callback, Context context) {
        callback.onResult(FormState.loading());

        api.certificateDownload(certificateId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        try {
                            String filePath = saveFileToDownloads(response.body(), certificateId, context);
                            callback.onResult(FormState.success("Download berhasil", filePath));
                        } catch (Exception e) {
                            callback.onResult(FormState.error("Download gagal: " + e.getMessage()));
                        }
                    }).start();
                } else {
                    try {
                        callback.onResult(FormState.error(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onResult(FormState.error("Error: " + e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    private String saveFileToDownloads(ResponseBody body, String certificateId, Context context) throws Exception {
        String fileName = certificateId + "_certificate.pdf";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ (Scoped Storage)
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            ContentResolver resolver = context.getContentResolver();
            Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

            if (uri == null) {
                throw new Exception("Gagal membuat file");
            }

            try (InputStream inputStream = body.byteStream();
                 OutputStream outputStream = resolver.openOutputStream(uri)) {

                if (outputStream == null) {
                    throw new Exception("Gagal membuka output stream");
                }

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
            }

            return uri.toString(); // Return URI untuk buka file nanti

        } else {
            // Android 9 and below
            File downloadsDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, fileName);

            try (InputStream inputStream = body.byteStream();
                 FileOutputStream outputStream = new FileOutputStream(file)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
            }

            return file.getAbsolutePath();
        }
    }
}
