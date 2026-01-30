package com.ilhamrhmtkbr.presentation.instructor.sections;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.repository.InstructorSectionsRepositoryImpl;
import com.ilhamrhmtkbr.data.local.entity.InstructorSectionsEntity;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.domain.repository.InstructorSectionsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SectionsViewModel extends ViewModel {
    private final InstructorSectionsRepository sectionsRepository;

    @Inject
    public SectionsViewModel(InstructorSectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    public void refresh(String courseId){
        sectionsRepository.fetch(courseId);
    }

    public LiveData<Resource<List<Section>>> getSections(String courseId) {
        return sectionsRepository.getSections(courseId);
    }
}