package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Earning;

import java.util.List;

public interface InstructorEarningsRepository {
    void fetch(String page, String sort);
    LiveData<Resource<List<Earning>>> getEarnings();
    LiveData<List<Page>> getEarningsPagination();
    void payout(FormCallback<String> callback);
}
