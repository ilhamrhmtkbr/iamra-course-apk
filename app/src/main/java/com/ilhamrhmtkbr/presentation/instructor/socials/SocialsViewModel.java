package com.ilhamrhmtkbr.presentation.instructor.socials;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.instructor.Social;
import com.ilhamrhmtkbr.domain.repository.InstructorSocialsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SocialsViewModel extends ViewModel {
    private final InstructorSocialsRepository socialsRepository;

    @Inject
    public SocialsViewModel(InstructorSocialsRepository socialsRepository) {
        this.socialsRepository = socialsRepository;
    }

    public void refreshSocials(){
        socialsRepository.fetch();
    }

    public LiveData<Resource<List<Social>>> getSocials() {
        return socialsRepository.getSocials();
    }
}