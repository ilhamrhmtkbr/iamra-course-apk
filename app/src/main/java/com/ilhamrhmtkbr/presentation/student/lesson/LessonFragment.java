package com.ilhamrhmtkbr.presentation.student.lesson;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ilhamrhmtkbr.databinding.FragmentStudentLessonBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LessonFragment extends Fragment {
    private FragmentStudentLessonBinding binding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentLessonBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupElements() {
        if (getArguments() != null) {
            binding.lessonTitle.setText(getArguments().getString("lesson_title"));
            binding.lessonDesc.setText(getArguments().getString("lesson_desc"));
            binding.lessonCode.setWebViewClient(new WebViewClient());
            binding.lessonCode.getSettings().setJavaScriptEnabled(true);

            byte[] decodedBytes = Base64.decode(getArguments().getString("lesson_code"), Base64.DEFAULT);
            String code = new String(decodedBytes);

            String html =
                    "<html>" +
                        "<head>" +
                            "<link rel='stylesheet' href='file:///android_asset/atom-one-dark.min.css'>" +
                            "<script src='file:///android_asset/highlight.min.js'></script>" +
                            "<script>hljs.highlightAll();</script>" +
                        "</head>" +
                        "<body style='padding: 0; background: #282c34; margin:0; '>" +
                            "<pre style='padding: 0; background: transparent; margin:0; '>" +
                                "<code class='" + getArguments().getString("lesson_editor") + "'>" + code.replace("<", "&lt;").replace(">", "&gt;") + "</code>" +
                            "</pre>" +
                        "</body>" +
                    "</html>";

            binding.lessonCode.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
        }
    }
}
