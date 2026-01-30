package com.ilhamrhmtkbr.presentation.guest.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.utils.adapter.AccordionAdapter;
import com.ilhamrhmtkbr.databinding.FragmentPublicHomeViewPagerInstructorBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerInstructor extends Fragment {
    FragmentPublicHomeViewPagerInstructorBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentPublicHomeViewPagerInstructorBinding.inflate(layoutInflater, viewGroup, false);
        setupView();
        return binding.getRoot();
    }

    public void setupView() {
        List<AccordionAdapter.AccordionItem> accordionItemList = new ArrayList<>();
        accordionItemList.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_instructor_question_1),
                getString(R.string.fragment_public_home_instructor_answer_1)
        ));
        accordionItemList.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_instructor_question_2),
                getString(R.string.fragment_public_home_instructor_answer_2)
        ));
        accordionItemList.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_instructor_question_3),
                getString(R.string.fragment_public_home_instructor_answer_3)
        ));
        accordionItemList.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_instructor_question_4),
                getString(R.string.fragment_public_home_instructor_answer_4)
        ));
        accordionItemList.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_instructor_question_5),
                getString(R.string.fragment_public_home_instructor_answer_5)
        ));
        accordionItemList.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_instructor_question_6),
                getString(R.string.fragment_public_home_instructor_answer_6)
        ));
        accordionItemList.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_instructor_question_7),
                getString(R.string.fragment_public_home_instructor_answer_7)
        ));

        binding.rvInfoInstructor.setLayoutManager(new LinearLayoutManager(requireContext()));
        AccordionAdapter accordionAdapter = new AccordionAdapter(accordionItemList);
        binding.rvInfoInstructor.setAdapter(accordionAdapter);
    }
}
