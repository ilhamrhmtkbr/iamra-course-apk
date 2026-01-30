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
import com.ilhamrhmtkbr.databinding.FragmentPublicHomeViewPagerStudentBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerStudent extends Fragment {
    FragmentPublicHomeViewPagerStudentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentPublicHomeViewPagerStudentBinding.inflate(layoutInflater, viewGroup, false);
        setupView();
        return binding.getRoot();
    }

    public void setupView() {
        List<AccordionAdapter.AccordionItem> items = new ArrayList<>();
        items.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_student_question_1),
                getString(R.string.fragment_public_home_student_answer_1)));
        items.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_student_question_2),
                getString(R.string.fragment_public_home_student_answer_2)));
        items.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_student_question_3),
                getString(R.string.fragment_public_home_student_answer_3)));
        items.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_student_question_4),
                getString(R.string.fragment_public_home_student_answer_4)));
        items.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_student_question_5),
                getString(R.string.fragment_public_home_student_answer_5)));
        items.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_student_question_6),
                getString(R.string.fragment_public_home_student_answer_6)));
        items.add(new AccordionAdapter.AccordionItem(
                getString(R.string.fragment_public_home_student_question_7),
                getString(R.string.fragment_public_home_student_answer_7)));

        binding.rvInfoStudent.setLayoutManager(new LinearLayoutManager(requireContext()));
        AccordionAdapter accordionAdapter = new AccordionAdapter(items);
        binding.rvInfoStudent.setAdapter(accordionAdapter);
    }
}