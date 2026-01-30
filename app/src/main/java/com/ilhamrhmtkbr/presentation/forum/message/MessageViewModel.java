package com.ilhamrhmtkbr.presentation.forum.message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.data.remote.dto.request.ForumMessageRequest;
import com.ilhamrhmtkbr.domain.model.forum.Chat;
import com.ilhamrhmtkbr.domain.repository.ForumRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MessageViewModel extends ViewModel implements ValidationFrontend<ForumMessageRequest> {
    private final ForumRepository forumRepository;
    private final MutableLiveData<Map<String, String>> validationFrontend = new MutableLiveData<>();

    // ✅ Tambahkan untuk track send message state
    private final MutableLiveData<Resource<String>> sendMessageState = new MutableLiveData<>();

    @Inject
    public MessageViewModel(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    public void sendMessage(String message, String courseId) {
        ForumMessageRequest request = new ForumMessageRequest(message, courseId);

        if (!isValidValue(request)) {
            return; // Validation errors sudah di-set di validationFrontend
        }

        sendMessageState.setValue(Resource.loading());

        forumRepository.sendMessage(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                if (state.isSuccess()) {
                    sendMessageState.setValue(Resource.success(state.getData()));
                } else if (state.isError()) {
                    sendMessageState.setValue(Resource.error(state.getMessage()));
                } else if (state.isLoading()) {
                    sendMessageState.setValue(Resource.loading());
                }
            }
        });
    }

    public void loadMessages(String courseId, String beforeTime) {
        forumRepository.loadMessages(courseId, beforeTime);
    }

    // ========== GETTERS ==========

    public MediatorLiveData<Resource<List<Chat>>> getChatMessage() {
        return forumRepository.getChatResult();
    }

    public LiveData<Resource<String>> getSendMessageState() {
        return sendMessageState;
    }

    public LiveData<Map<String, String>> getValidationFrontend() {
        return validationFrontend;
    }

    // ========== VALIDATION ==========

    @Override
    public boolean isValidValue(ForumMessageRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation message = new BaseValidation()
                .validation(request.message, "message")
                .required()
                .minMax(1, 500); // Naikin limit jadi 500 karakter

        if (message.hasError()) {
            errors.put(message.getFieldName(), message.getError());
        }

        if (errors.isEmpty()) {
            return true;
        } else {
            validationFrontend.setValue(errors);
            return false;
        }
    }
}