package com.koreait.boardapp;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//안드로이드는 메인쓰레드가 루프나, 대기상태, 웹 요청등을 수행하면 그 자체로
//에러 사항이다!! 왜 메인쓰레드는 UI, Event 등을 처리해야 하는 앱운영쓰레드 이므로..
//만일 위와 같은 금지된 작업을 수행하면 앱은 무한 대기 상태에 빠지게 된다.
public class HttpThread extends Thread{
    String TAG = this.getClass().getName();
    URL url;
    HttpURLConnection con;   //http통신을 위한 객체(헤더+바디를 구성하여 서버와 데이터를 주고받는 방식)
    //stateless 한 통신
    MainActivity mainActivity;
    String requestUrl;
    String data;

    //이 객체를 생성하는 자는 주소와 제이슨 데이터를 넘겨야 한다.
    public ConnectManager(MainActivity mainActivity, String requestUrl, String data){
        this.mainActivity=mainActivity;
        this.requestUrl=requestUrl;
        this.data=data;
    }

    public int requestByGet(){      //Get방식으로 요청을 시도하는 메서드
        BufferedReader buffr = null;
        int code = 0;

        try{
            url = new URL(requestUrl);   //요청 주소
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            //서버로 부터 응답 데이터 가져오기
            buffr = new BufferedReader(new InputStreamReader(con.getInputStream())); //바이트 기반 스트림

            String data = null;
            StringBuilder sb = new StringBuilder();      //문자열을 누적할 객체
            while(true){
                data = buffr.readLine();   //한줄 읽어들인다.
                if(data==null)break;   //읽어들일 데이터가 없다면 무한루프 종료
                sb.append(data);//읽어들인 문자열을 누적시키자
            }


            code = con.getResponseCode();      //서버로부터 받은 응답코드 반환   (이 시점에는 이미 서버에 요청을 완료 후 응답도 받은 상태)

            Log.d(TAG, "서버로 받은 응답코드는 "+code);

            //이 시점이 바로 통신이 완료된 시점이므로, 여기서 무언가를 해야한다..
            Log.d(TAG, "서버가 보낸 응답데이터는: "+sb.toString());
            mainActivity.printData(sb.toString()); //사용자 정의 쓰레드는 디자인 접근 불가
            //해결책? 디자인을 갱신해달라고, 요청하면 된다.. 이때 요청을 받는 전담객체를 핸들러 한다!!
            //핸들러에게 동작을 요청
            //handler의 handleMessage()를 호출하게 됨
            Message message = new Message(); //empty 메세지 객체 생성
            Bundle bundle = new Bundle();
            bundle.putString("data", sb.toString());
            message.setData(bundle);
            mainActivity.handler.sendMessage(message);

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(buffr!=null){
                try{
                    buffr.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return code;
    }

    //Post방식의 요청을 시도하되, JSON데이터를 전송하겠다!!
    public int requestByPost(){
        BufferedWriter buffw = null;   //버퍼처리된 문자기반 스트림
        int code=0;   //서버의 응답 코드

        try{
            url = new URL(requestUrl);   //요청 주소
            con = (HttpURLConnection)url.openConnection();
            //데이터 형식을 헤더에 첨가해줘야, 서버측에서 제이슨 데이터가 전소되어 온것임을 안다.. 이게 바로 .HTTP프로토콜간의 약속이다
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");

            con.setRequestMethod("POST");
            con.setDoOutput(true);   //서버에 데이터를 출력하기 위해 필요한 옵션!!
            //오청을 떠나기전에 준비할게 있다면 여기서 준비하자!!,json문자열을 준비하자
            //JSON 객체 자체가 아닌 문자열로 준비하는 이유는 ?   "{}"
         /*
         StringBuilder sb = new StringBuilder();
         sb.append("{");
         sb.append(" \"m_id\":\"batman\", ");
         sb.append(" \"m_pass\":\"1234\", ");
         sb.append(" \"m_name\":\"배트맨\" ");
         sb.append("}");
         */
            //실행중인 프로그램에서 서버로 데이터를 보내야 하므로, 출력 스트림으로 처리하자
            buffw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(),"UTF-8"));
            buffw.write(data);
            buffw.flush();

            code = con.getResponseCode();      //요청 + 응답이 발생
            Log.d(TAG, "서버로 부터 받은 응답 코드는 "+code);


        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(buffw!=null){
                try{
                    buffw.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        return code;   //응답코드 반환

    }

    @Override
    public void run() {
        Log.d(TAG,"B");
        int code = requestByGet();
        Log.d(TAG,"서버로 부터 받은 응답 코드는 "+code);
    }
}
