package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSectionUpSertRequest;
import com.ilhamrhmtkbr.domain.model.common.Section;

import java.util.List;

public interface InstructorSectionsRepository {
    void fetch(String courseId);
    LiveData<Resource<List<Section>>> getSections(String courseId);
    void store(InstructorSectionUpSertRequest request, FormCallback<String> callback);
    void modify(InstructorSectionUpSertRequest request, FormCallback<String> callback);
    void delete(String sectionId, FormCallback<String> callback);
}
