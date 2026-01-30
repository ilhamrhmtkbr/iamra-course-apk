package com.ilhamrhmtkbr.presentation.student.certificates;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentStudentCertificatesBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CertificatesFragment extends Fragment {
    private FragmentStudentCertificatesBinding binding;
    private CertificatesViewModel viewModel;
    private CertificatesAdapter certificatesAdapter;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(CertificatesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentCertificatesBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupCertificates();
        setupPagination();
        setupSwipeRefresh();
        setupListeners();
        setupViewModel();
    }

    private void setupCertificates() {
        certificatesAdapter = new CertificatesAdapter(new CertificatesAdapter.onClickListener() {
            @Override
            public void detail(String certificateId) {
                Bundle bundle = new Bundle();
                bundle.putString("certificate_id", certificateId);

                NavController navController = NavHostFragment.findNavController(CertificatesFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_certificates, false)
                        .build();
                navController.navigate(R.id.nav_certificate_detail, bundle, navOptions);
            }
        });
        binding.rv.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(33));
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(certificatesAdapter);
    }

    private void setupPagination() {
        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshCertificates(page, viewModel.getSort().getValue());
            }
        });
        binding.pagination.setLayoutManager(new LinearLayoutManager(requireContext(), HORIZONTAL, false));
        binding.pagination.setAdapter(paginationAdapter);
        binding.scrollIndicator.attachToRecyclerView(binding.pagination);
        viewModel.getPaginationData().observe(getViewLifecycleOwner(), new Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> pages) {
                paginationAdapter.updateLinks(pages);
            }
        });
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshCertificates("1", "desc");
            }
        });
    }

    private void setupListeners() {
        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshCertificates("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));
        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshCertificates("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));
        });
    }

    private void setupViewModel() {
        viewModel.getCertificates().observe(getViewLifecycleOwner(), new Observer<Resource<List<Certificate>>>() {
            @Override
            public void onChanged(Resource<List<Certificate>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);
                        certificatesAdapter.setData(listResource.getData());
                    }
                } else if (listResource.isError()) {
                    binding.noData.setVisibility(GONE);
                    binding.data.setVisibility(VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        certificatesAdapter = null;
        paginationAdapter = null;
    }
}
