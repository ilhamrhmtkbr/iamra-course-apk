package com.ilhamrhmtkbr.presentation.instructor.answers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Answer;
import com.ilhamrhmtkbr.domain.repository.InstructorAnswersRepository;
import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AnswersViewModel extends ViewModel {
    private final MutableLiveData<String> sort = new MutableLiveData<>();
    private final InstructorAnswersRepository answersRepository;

    @Inject
    public AnswersViewModel(InstructorAnswersRepository answersRepository) {
        this.answersRepository = answersRepository;
    }

    public void refreshAnswers(String page, String sort) {
        answersRepository.fetch(page, sort);
    }

    public LiveData<Resource<List<Answer>>> getAnswers() {
        return answersRepository.getAnswers();
    }

    public LiveData<List<Page>> getPaginationData() {
        return answersRepository.getAnswersPagination();
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }
}
