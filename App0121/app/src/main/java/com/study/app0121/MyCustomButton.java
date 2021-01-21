package com.study.app0121;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.jar.Attributes;

//안드로이드의 네이티브 버튼을 상속받아 나만의 버튼으로 재정의 해보자
public class MyCustomButton extends Button {

    //버튼을 포함한 모든 뷰는 단독으로 존재할 수 없기 때문에 반드시 하나의
    //액티비티에서 관리되어야 한다.. 따라서 버튼의 생성자는 이버튼을 관리할 액티비티 클래스를
    //명시해 주어야 한다.
    public MyCustomButton(Context context) {
        super(context);
    }

    //xml에서 이 버튼을 사용하고자 할 때, xml에 지정된 버튼 속성이 AttributeSet
    //으로 넘어오게 된다..
    public MyCustomButton(@NonNull Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
}
