package com.nxp.nhs31xx.demo.tlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import kr.co.mediex.myhealth.v1.MyHealth;
import kr.co.mediex.myhealth.v1.MyHealthUtils;
import kr.co.mediex.myhealth.v1.function.Error;
import kr.co.mediex.myhealth.v1.function.Success;
import kr.co.mediex.myhealth.v1.view.MyHealthActivity;
import kr.co.mediex.myhealth.v1.view.MyHealthWebActivity;
import retrofit2.HttpException;


import static android.content.ContentValues.TAG;

public class StartActivity extends AppCompatActivity {
    private MyHealth mMyHealthService = null;
    private Button mSaveButton = null;
    private EditText mWeightEditText = null;
    private EditText mHeightEditText = null;
    private TextView text_view = null;
    private List<Float> floatlist = null;
    int selector;
    ArrayList<String> arrayList = null;
    LinkedHashMap<String, ArrayList<String>> hash = new LinkedHashMap<>();
    LinkedHashMap<String, String> hashMAP = new LinkedHashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = getIntent();
        arrayList = intent.getExtras().getStringArrayList("list");
        selector = intent.getExtras().getInt("selector");
        Log.d("SELECTOR", String.valueOf(selector));

        if(selector == 0){
            Log.d("TAG_!", "NODE_A");
            this.mMyHealthService = MyHealthUtils.createService(getApplicationContext(),
                    SampleApplication.MY_SERVICE_NAME_IOP,
                    SampleApplication.MY_SERVICE_SECRET_IOP);
            hash.put("IOP", arrayList);
        }else if(selector == 1){
            Log.d("TAG_!", "NODE_B");
            this.mMyHealthService = MyHealthUtils.createService(getApplicationContext(),
                    SampleApplication.MY_SERVICE_NAME_GLUCOSE,
                    SampleApplication.MY_SERVICE_SECRET_GLUCOSE);
            hash.put("GLUCOSE", arrayList);
        }



        //hashMAP.putAll(hash);


        mSaveButton = findViewById(R.id.btn_export);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                finish();
            }
        });
        text_view = findViewById(R.id.text_view);
        for(int i =0; i<arrayList.size(); i++){
            text_view.append(arrayList.get(i));
            text_view.append("\n");
        }
        //text_view.setText(hashMAP);

    }
    private Error errorHandler = new Error() {
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            if (throwable instanceof SocketTimeoutException) {
                showToast("인터넷연결상태를 확인해주세요.");
            } else if (throwable instanceof HttpException) {
                showToast("서버 에러");
            } else {
                throwable.printStackTrace();
                showToast("에러");
            }
        }
    };



    private void saveData() {
        try {
            // TODO 데이터 유효성검사 필요!
            Log.d("SE!", String.valueOf(selector));
            // 서비스 사용자 정보
            MyUser myUser = new MyUser();
            // 샘플 유저 아이디를 지정
            myUser.setId((long) (Math.random() * 1000));
            myUser.setName("myUserName");
            if(selector == 0){
                Log.d("TAG_!", "NODE_I");
                mMyHealthService.insertResource(SampleApplication.MY_RESOURCE_NAME,
                        hash,
                        new Success<Object>() {
                            // 샘플 정보를 서버에서 저장하기 정공
                            @Override
                            public void onSuccess(Object o) {
                                Log.d("TAG_!", "DONE~~~~");
                                showToast("성공적으로 데이터가 저장되었습니다.");
                            }
                        }, errorHandler);
            }else if (selector == 1){
                Log.d("TAG_!", "NODE_G");
                mMyHealthService.insertResource(SampleApplication.MY_RESOURCE_NAME,
                        hash,
                        new Success<Object>() {
                            // 샘플 정보를 서버에서 저장하기 정공
                            @Override
                            public void onSuccess(Object o) {
                                Log.d("TAG_!", "DONE~~~~");
                                showToast("성공적으로 데이터가 저장되었습니다.");
                            }
                        }, errorHandler);
            }

        } catch (Throwable e) {
            e.printStackTrace();
            Log.d("TAG_!", "FAIL~~~~");
        }
        Log.d("TAG_!", "NODE_F");
    }

    private void showToast(final String message) {
        // 백그라운드 작업 후 호출시 ui thread를 사용
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }




}
