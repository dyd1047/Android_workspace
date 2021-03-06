package com.koreait.scheduleapp.animation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koreait.scheduleapp.R;

//이동 애니메이션을 실습하기 위한 페이지
public class TranslateFragment extends Fragment {
    ImageView imgView;
    Button bt_start;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        imgView = view.findViewById(R.id.imgView);
        bt_start = view.findViewById(R.id.bt_start);

        //버튼을 누르면, 이미지에 애니메이션을 적용하겠다.
        bt_start.setOnClickListener(e->{
            showAni();
        });

        return view;
    }

    public void showAni(){
        //애니메이션 객체 생성
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.ani_translate);

        //위의 애니메이션 객체를 누구에게?
        imgView.startAnimation(animation); //적용 및 시작!!
    }
}
