package xxy.com.gridleader.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import xxy.com.gridleader.R;

public class ScreenActivity extends AppCompatActivity {

    private Button bt_location;
    private Button bt_start_time;
    private Button bt_end_time;
    private Button bt_submit;

    private double latitude;
    private double longitude;
    private String location = "";
    private String startTime = "";
    private String endTime = "";

    private static final int TAKE_LOCATION = 0x000002;

    private EditText tv_location;
    private EditText tv_end_time;
    private EditText tv_start_time;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        mContext = this;
        init();
    }

    private void init() {
        bt_start_time = findViewById(R.id.bt_start_time);
        bt_location = findViewById(R.id.bt_location);
        bt_end_time = findViewById(R.id.bt_end_time);
        bt_submit = findViewById(R.id.bt_submit);

        tv_location = findViewById(R.id.tv_location);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_start_time = findViewById(R.id.tv_start_time);
        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
//                    ActivityCompat.requestPermissions(ScreenActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 888);
//                } else {
//                    Intent intent = new Intent(ScreenActivity.this, MapViewActivity.class);
//                    startActivityForResult(intent, TAKE_LOCATION);
//                }
            }
        });
        final Calendar calender = Calendar.getInstance();

        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mouth1 = "";
                        String day1 = "";
                        if (month < 9) {
                            mouth1 = "0" + (month + 1);
                        } else {
                            mouth1 = String.valueOf(month + 1);
                        }
                        if (dayOfMonth <= 9) {
                            day1 = "0" + dayOfMonth;
                        } else {
                            day1 = String.valueOf(dayOfMonth);
                        }

                        endTime = year + "-" + mouth1 + "-" + day1;
                        tv_end_time.setText(endTime);

                    }
                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        bt_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mouth1 = "";
                        String day1 = "";
                        if (month < 9) {
                            mouth1 = "0" + (month + 1);
                        } else {
                            mouth1 = String.valueOf(month + 1);
                        }
                        if (dayOfMonth <= 9) {
                            day1 = "0" + dayOfMonth;
                        } else {
                            day1 = String.valueOf(dayOfMonth);
                        }

                        endTime = year + "-" + mouth1 + "-" + day1;
                        tv_end_time.setText(endTime);

                    }
                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mouth1 = "";
                        String day1 = "";
                        if (month < 9) {
                            mouth1 = "0" + (month + 1);
                        } else {
                            mouth1 = String.valueOf(month + 1);
                        }
                        if (dayOfMonth <= 9) {
                            day1 = "0" + dayOfMonth;
                        } else {
                            day1 = String.valueOf(dayOfMonth);
                        }

                        startTime = year + "-" + mouth1 + "-" + day1;
                        tv_start_time.setText(startTime);

                    }
                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        bt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mouth1 = "";
                        String day1 = "";
                        if (month < 9) {
                            mouth1 = "0" + (month + 1);
                        } else {
                            mouth1 = String.valueOf(month + 1);
                        }
                        if (dayOfMonth <= 9) {
                            day1 = "0" + dayOfMonth;
                        } else {
                            day1 = String.valueOf(dayOfMonth);
                        }

                        startTime = year + "-" + mouth1 + "-" + day1;
                        tv_start_time.setText(startTime);

                    }
                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                location = tv_location.getText().toString();
                intent.putExtra("location",location);
                intent.putExtra("latitude",latitude + "");
                intent.putExtra("longitude",longitude + "");
                if (TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(tv_start_time.getText().toString())){
                    startTime = tv_start_time.getText().toString();
                }
                intent.putExtra("startTime",startTime);
                if (TextUtils.isEmpty(endTime) && !TextUtils.isEmpty(tv_end_time.getText().toString())){
                    endTime = tv_end_time.getText().toString();
                }
                intent.putExtra("endTime",endTime);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_LOCATION:
                if (data != null && resultCode == RESULT_OK) {
                    if (data.getStringExtra("location") != null) {
                        location = data.getStringExtra("location");
                        tv_location.setText(location);
                    }
                    if (data.getStringExtra("latitude") != null && data.getStringExtra("longitude") != null) {
                        latitude = Double.valueOf(data.getStringExtra("latitude"));
                        longitude = Double.valueOf(data.getStringExtra("longitude"));
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
