package com.solucionestpvpos.periodicooficial.ui.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.solucionestpvpos.periodicooficial.R;
import com.solucionestpvpos.periodicooficial.databinding.FragmentHomeBinding;
import com.solucionestpvpos.periodicooficial.ui.SingupActivity;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button button_register_home;
    ImageView image1ImageView, image2ImageView,image3ImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        image1ImageView = root.findViewById(R.id.image1ImageView);
        Glide.with(getActivity()).
                load("https://poax.b32.mx/app_imgs/public-add.png").
                into(image1ImageView);

        image2ImageView = root.findViewById(R.id.image2ImageView);

        Glide.with(getActivity()).
                load("https://poax.b32.mx/app_imgs/online-card.jpg").
                into(image2ImageView);

        image3ImageView = root.findViewById(R.id.image3ImageView);

        Glide.with(getActivity()).
                load("https://poax.b32.mx/app_imgs/note-digital.jpg").
                into(image3ImageView);

        button_register_home = root.findViewById(R.id.button_register_home);
        button_register_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SingupActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}