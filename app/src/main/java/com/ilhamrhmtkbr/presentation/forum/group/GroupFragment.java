package com.ilhamrhmtkbr.presentation.forum.group;

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

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.databinding.FragmentForumGroupBinding;
import com.ilhamrhmtkbr.domain.model.forum.Group;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GroupFragment extends Fragment {
    private FragmentForumGroupBinding binding;
    private GroupViewModel viewModel;
    private GroupAdapter chatAdapter;
    private List<Group> chatData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        viewModel = new ViewModelProvider(this).get(GroupViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentForumGroupBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setupRecyclerView();
        observerViewModel();
    }

    private void setupRecyclerView() {
        binding.rvChatList.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatAdapter = new GroupAdapter(chatData, new GroupAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(Group group) {
                Bundle bundle = new Bundle();
                bundle.putString("course_id", group.getId());
                NavController navController = NavHostFragment.findNavController(GroupFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_chat_group, false)
                        .build();
                navController.navigate(R.id.nav_chat_message, bundle, navOptions);
            }
        });
        binding.rvChatList.setAdapter(chatAdapter);
    }

    private void observerViewModel() {
        viewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<Resource<List<Group>>>() {
            @Override
            public void onChanged(Resource<List<Group>> listResource) {
                boolean isLoading = listResource.isLoading();
                if (isLoading) {
                    binding.groupLoading.setVisibility(View.VISIBLE);
                } else {
                    binding.groupLoading.setVisibility(View.GONE);
                    chatData.clear();
                    chatData.addAll(listResource.getData());
                    chatAdapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.fetchGroups();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}