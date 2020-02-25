package com.devahmed.demo.animeapp.ui.Splash_Intro;



import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devahmed.demo.animeapp.R;

public class IntroFragment extends Fragment {

    TextView textView;
    ImageView imageView;
    int image;
    String title;

    public IntroFragment(int image, String title) {
        this.image = image;
        this.title = title;
    }


    public IntroFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro, container, false);
        imageView = view.findViewById(R.id.introImage);
        textView = view.findViewById(R.id.Introtitle);

        imageView.setImageResource(image);
        textView.setText(title);

        return view;
    }

}
