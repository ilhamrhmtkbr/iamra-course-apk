package com.ilhamrhmtkbr.presentation.forum.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.forum.Group;
import com.ilhamrhmtkbr.domain.repository.ForumRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GroupViewModel extends ViewModel {
    private ForumRepository repository;

    @Inject
    public GroupViewModel(ForumRepository repository) {
        this.repository = repository;
    }

    public void fetchGroups() {
        repository.fetchGroups();
    }

    public LiveData<Resource<List<Group>>> getGroups() {
        return repository.getGroups();
    }
}
