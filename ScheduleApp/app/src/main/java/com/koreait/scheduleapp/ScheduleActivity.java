package com.koreait.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.koreait.scheduleapp.common.FormatManager;

import org.w3c.dom.Text;

import java.util.Calendar;

public class ScheduleActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();
    LayoutInflater layoutInflater;
    TableLayout tableLayout;
    TextView t_current;
    //현재 사용자가 보고있는 날짜(앱 가동 시점엔 현재 날짜로 채우자, 버튼 누를때는 그 날짜에 맞는 데이터로 조작하자)
    int year;
    int month;
    Calendar cal; //최종적으로 출력시 사용할 날짜 객체, 이 객체를 사용하면 더이상 제목에
                    //출력되는 데이터가 순수한 정수가 아닌, 날짜 정보로 출력될 수 있다

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //view 받아놓기
        tableLayout = this.findViewById(R.id.tableLayout);
        t_current = this.findViewById(R.id.t_current);

        //이전으로 이동(back) 버튼 추가
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutInflater = this.getLayoutInflater();

        createCell(); //테이블 만들기 시작
        getCurrentDate();
        printDate();
    }

    //back 버튼에 이벤트 구현
    @Override
    public boolean onSupportNavigateUp() {
        //현재 액티비티는 스택에서 제거!!
        this.finish();

        return true;
    }

    //달력의 틀 만들기
    public void createCell(){
        for (int j = 0; j < 7; j++){ //7층짜리 테이블의 row
            //true : 지정한 부모뷰에 부착하여, 그 부모뷰를 반환
            //false : 지정한 부모뷰에 부착하지 않으므로, xml의 최상위 뷰를 반환
            TableRow tableRow = (TableRow) layoutInflater.inflate(R.layout.item_row, tableLayout, false);

            //7개의 셀(날짜 박스) 만들기
            for(int i = 0; i < 7; i++){
                //row에 셀을 부착
                ViewGroup cell = (ViewGroup) layoutInflater.inflate(R.layout.item_cell, tableRow, false);

                //셀에 들어갈 텍스트뷰 생성 및 부착하기
                TextView t_title = (TextView) layoutInflater.inflate(R.layout.item_text, cell, false);
                cell.addView(t_title); //td안에 컨텐츠 부착하는 느낌..
                getCurrentDate();
                tableRow.addView(cell); //tr에 td 부착하는 느낌..
            }
            tableLayout.addView(tableRow); //각층을 테이블에 부착!!
        }
    }

    //현재 날짜 구해오기
    public void getCurrentDate(){
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
    }

    //각 월의 시작 요일
    public int getFirstDayOfMonth(int yy, int mm){
        Calendar cal = Calendar.getInstance(); //무조건 현재 날짜 정보가 들어있음
        cal.set(yy, mm, 1); //넘겨받은 매개변수로, 날짜 객체 조작!!
        int day = cal.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "2월 1일은 "+day+"요일입니다.");

        return day;
    }

    //각 월의 총 일 수
    public int getTotalDate(int yy, int mm){
        Calendar cal = Calendar.getInstance();
        //조작
        cal.set(yy, mm+1, 0); //주의할 점 : 0일로 조작할 때 월도 자동으로 변경됨..

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    //이미 존재하는 셀들의 각 칸에 알맞는 날짜 등을 출력
    public void printDate(){
        int firstDay = getFirstDayOfMonth(year, month); //각 월의 시작 요일
        int totalDay = getTotalDate(year, month); //각 월의 총 일 수

        //행의 갯수만큼 반복
        int rowCount = tableLayout.getChildCount();
        int pos = 0; //각 셀의 순번
        int num = 0; //날짜 변수
        String value = null;

        printCurrent(); //현재 날짜 제목 출력

        for(int i = 0; i < rowCount; i++){
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            int columnCount = tableRow.getChildCount(); //한 행이 보유한 셀의 갯수
            for (int j = 0; j < columnCount; j++){
                //각 셀에 접근하자!! 단 제일 첫번째 row의 경우에는 요일을 출력(일, 월, 화, 수, 목, ...)
                ViewGroup cell = (ViewGroup) tableRow.getChildAt(j);
                TextView t_title = (TextView) cell.getChildAt(0); //날짜 혹은 요일이 출력되는 텍스트뷰

                //row 반복문이 0일 때
                if (i == 0){
                    value = Days.values()[j].name();
                }else{
                    pos++; //요일을 제외한 셀에 대해서만 순번을 매긴다...

                    //pos의 순번이 시작 요일 이상일 경우부터는 num을 증가시키자!!
                    if (pos < firstDay) {
                        value = "";
                    }else{
                        num++;
                        value = Integer.toString(num);
                        if(num > totalDay)value = "";
                    }
                }
                t_title.setText(value);

                //셀에 리스너 연결
                cell.setOnClickListener(e->{
                    Log.d(TAG, "선택한 날짜는 "+year+"년 "+(month+1)+"월 "+t_title.getText()+"일");

                    showDialog();
                });
            }
        }
    }

    //현재 달력의 날짜 출력
    public void printCurrent(){
        t_current.setText(cal.get(Calendar.YEAR)+"-"+ FormatManager.getNumberString(cal.get(Calendar.MONTH)+1));
    }

    //이전 월
    public void prev(View view){
        month--;
        cal.set(Calendar.YEAR, year); //정수에 불과했던 데이터를 대상으로 실제 날짜로 지정
        cal.set(Calendar.MONTH, month);

        printDate();
    }

    //다음 월
    public void next(View view){
        month++;
        cal.set(Calendar.YEAR, year); //정수에 불과했던 데이터를 대상으로 실제 날짜로 지정
        cal.set(Calendar.MONTH, month);

        printDate();
        
        Log.d(TAG, "현재 날짜는 "+cal.get(Calendar.YEAR)+"년, "+cal.get(Calendar.MONTH)+"월");
    }

    //다이얼로그 창 띄우기(모달)
    public void showDialog(){
        /*
        ProgressDialog dialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로딩 중입니다..");
        dialog.show();
        */

        RegistDialog registDialog = new RegistDialog(this);
        registDialog.setCancelable(false);
        registDialog.show();
    }
}