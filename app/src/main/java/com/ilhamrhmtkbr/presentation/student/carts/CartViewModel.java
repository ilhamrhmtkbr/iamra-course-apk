package com.ilhamrhmtkbr.presentation.student.carts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Cart;
import com.ilhamrhmtkbr.domain.repository.StudentCartRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CartViewModel extends ViewModel {
    private final StudentCartRepository cartRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>("desc");
    private final SingleLiveEvent<FormState<String>> deleteResult = new SingleLiveEvent<>();

    @Inject
    public CartViewModel(StudentCartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public LiveData<Resource<List<Cart>>> getCarts() {
        return cartRepository.getAllCarts();
    }

    public LiveData<List<Page>> getPaginationData() {
        return cartRepository.getPaginationData();
    }

    public void refreshCarts(String page, String sort) {
        cartRepository.fetch(page, sort);
    }

    public void deleteCart(String cartId) {
        cartRepository.delete(cartId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                deleteResult.postValue(state);
            }
        });
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }

    public LiveData<FormState<String>> getDeleteResult() {
        return deleteResult;
    }
}
