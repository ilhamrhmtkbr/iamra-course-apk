package com.ilhamrhmtkbr.presentation.student.questions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Question;
import com.ilhamrhmtkbr.domain.repository.StudentQuestionRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QuestionsViewModel extends ViewModel {
    private final StudentQuestionRepository questionRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>();

    @Inject
    public QuestionsViewModel(StudentQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public LiveData<Resource<List<Question>>> getQuestions() {
        return questionRepository.getAllQuestions();
    }

    public void refreshQuestions(String page, String sort) {
        questionRepository.fetch(page, sort);
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }


    public LiveData<List<Page>> getPaginationData() {
        return questionRepository.getPaginationData();
    }
}
