package xxy.com.gridleader.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.model.CertificateTypeModel;
import xxy.com.gridleader.model.CommunityModel;
import xxy.com.gridleader.model.LoginModel;
import xxy.com.gridleader.model.ToAddModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

public class RegisterActivity extends AppCompatActivity {
    private ImageView img_back;
    private long userId;
    private Button btn_register;
    private Spinner gender,nation,marriage_status,cultural_level,political_status,military_service,disability_category,city_subsistence_allowances,volunteer,certificate_type,community,humanType;
    private TextView name,native_place,household_registration,current_address,job_status,working_place,car_number,contact_information,remarks,certificate_number,birthday,relationship,roomNumber,gardenName,building,unit,houseNumber;
    private List<Integer> certificate_typeList = new ArrayList<>();
    private List<Long> communityidList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        ToaddCall(userId);
    }

    private void init(){
        final Intent intent = getIntent();
        userId = Long.valueOf(intent.getStringExtra("userId"));

        btn_register = findViewById(R.id.btn_register);
        img_back = findViewById(R.id.img_back);
        gender = findViewById(R.id.gender);
        nation = findViewById(R.id.nation);
        marriage_status = findViewById(R.id.marriage_status);
        cultural_level = findViewById(R.id.cultural_level);
        political_status = findViewById(R.id.political_status);
        military_service = findViewById(R.id.military_service);
        disability_category = findViewById(R.id.disability_category);
        city_subsistence_allowances = findViewById(R.id.city_subsistence_allowances);
        volunteer = findViewById(R.id.volunteer);
        certificate_type = findViewById(R.id.certificate_type);
        community = findViewById(R.id.community);

        roomNumber = findViewById(R.id.roomNumber);

        gardenName = findViewById(R.id.gardenName);

        name = findViewById(R.id.name);
        native_place = findViewById(R.id.native_place);
        household_registration = findViewById(R.id.household_registration);
        current_address = findViewById(R.id.current_address);
        job_status = findViewById(R.id.job_status);
        working_place = findViewById(R.id.working_place);
        car_number = findViewById(R.id.car_number);
        contact_information = findViewById(R.id.contact_information);
        remarks = findViewById(R.id.remarks);
        certificate_number = findViewById(R.id.certificate_number);
        birthday = findViewById(R.id.birthday);
        relationship = findViewById(R.id.relationship);
        humanType = findViewById(R.id.humanType);
        building = findViewById(R.id.building);
        unit = findViewById(R.id.unit);
        houseNumber = findViewById(R.id.houseNumber);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        certificate_number.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                } else {
                    if(certificate_type.getSelectedItemId() == 0) {
                        String c1 = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
                        String c2 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$";
                        if (!certificate_number.getText().toString().matches(c1) && !certificate_number.getText().toString().matches(c2)) {
                            Toast.makeText(RegisterActivity.this, "身份证信息不规范", Toast.LENGTH_SHORT).show();
                        }else{
                            String birthdayStr = certificate_number.getText().toString().substring(6,13);
                            birthday.setText(birthdayStr);
                            int cLength = certificate_number.getText().toString().trim().length();
                            String sexStr = certificate_number.getText().toString().trim().substring(cLength-1,cLength);
                            int sexNum = Integer.parseInt(sexStr)%2;
                            if(sexNum == 1) {
                                gender.setSelection(0);
                            }else{
                                gender.setSelection(1);
                            }

                        }
                    }
                }
            }
        });

        building.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                } else {
                    if(unit.getText().toString() != null && !unit.getText().toString().trim().equals("") &&
                            building.getText().toString() != null && !building.getText().toString().trim().equals("") &&
                            houseNumber.getText().toString() != null && !houseNumber.getText().toString().trim().equals("")) {
                        roomNumber.setText(building.getText().toString() + "-"+unit.getText().toString()+"-"+houseNumber.getText().toString());
                    }
                }
            }
        });

        unit.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                } else {
                    if(unit.getText().toString() != null && !unit.getText().toString().trim().equals("") &&
                            building.getText().toString() != null && !building.getText().toString().trim().equals("") &&
                            houseNumber.getText().toString() != null && !houseNumber.getText().toString().trim().equals("")) {
                        roomNumber.setText(building.getText().toString() + "-"+unit.getText().toString()+"-"+houseNumber.getText().toString());
                    }
                }
            }
        });

        houseNumber.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                } else {
                    if(unit.getText().toString() != null && !unit.getText().toString().trim().equals("") &&
                            building.getText().toString() != null && !building.getText().toString().trim().equals("") &&
                            houseNumber.getText().toString() != null && !houseNumber.getText().toString().trim().equals("")) {
                        roomNumber.setText(building.getText().toString() + "-"+unit.getText().toString()+"-"+houseNumber.getText().toString());
                    }
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(RegisterActivity.this,certificate_typeList.get(certificate_type.getSelectedItemPosition()) + "",Toast.LENGTH_SHORT).show();

                String p = "^[0-9a-zA-Z]+$";
                if (roomNumber.getText().toString() == null || roomNumber.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入户号", Toast.LENGTH_SHORT).show();
                } else if (gardenName.getText().toString() == null || gardenName.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入小区名",Toast.LENGTH_SHORT).show();
                }else if (building.getText().toString() == null || building.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入楼栋号",Toast.LENGTH_SHORT).show();
                }else if (!building.getText().toString().trim().matches(p)){
                    Toast.makeText(RegisterActivity.this,"楼栋号只能为英文或数字",Toast.LENGTH_SHORT).show();
                }else if (unit.getText().toString() == null || unit.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入单元号",Toast.LENGTH_SHORT).show();
                }else if (!unit.getText().toString().trim().matches(p)){
                    Toast.makeText(RegisterActivity.this,"单元号只能为英文或数字",Toast.LENGTH_SHORT).show();
                }else if (houseNumber.getText().toString() == null || houseNumber.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入房号",Toast.LENGTH_SHORT).show();
                }else if (!houseNumber.getText().toString().trim().matches(p)){
                    Toast.makeText(RegisterActivity.this,"房号只能为英文或数字",Toast.LENGTH_SHORT).show();
                }else if (relationship.getText().toString() == null || relationship.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入与户主关系",Toast.LENGTH_SHORT).show();
                }else if (name.getText().toString() == null || name.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入姓名",Toast.LENGTH_SHORT).show();
                }else if (certificate_number.getText().toString() == null || certificate_number.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入证件号码",Toast.LENGTH_SHORT).show();
                }else if (birthday.getText().toString() == null || birthday.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入生日",Toast.LENGTH_SHORT).show();
                }else {
                    int sex = 1;
                    if (gender.getSelectedItemId() == 0){
                        sex = 1;
                    }else if (gender.getSelectedItemId() == 1){
                        sex = 0;
                    }

                    Log.d("TAG","qwer");

                    AddResidentCall(userId,name.getText().toString(),
                            sex,nation.getSelectedItem()+"",native_place.getText().toString(),
                            cultural_level.getSelectedItem()+"",political_status.getSelectedItem()+"",
                            marriage_status.getSelectedItem()+"",military_service.getSelectedItem()+"",disability_category.getSelectedItem()+"",
                            city_subsistence_allowances.getSelectedItem()+"",
                            volunteer.getSelectedItem()+"",household_registration.getText().toString(),
                            current_address.getText().toString(), job_status.getText().toString(),
                            working_place.getText().toString(),car_number.getText().toString(),
                            contact_information.getText().toString(),remarks.getText().toString(),
                            certificate_typeList.get(certificate_type.getSelectedItemPosition()),certificate_number.getText().toString(),communityidList.get(community.getSelectedItemPosition()),Integer.parseInt(birthday.getText().toString()),
                            relationship.getText().toString(),roomNumber.getText().toString(),gardenName.getText().toString(),building.getText().toString(),unit.getText().toString(),houseNumber.getText().toString(),humanType.getSelectedItem()+"");
                }
            }
        });
    }


    private void AddResidentCall(long userId,String name, int gender, String nation, String native_place, String cultural_level, String political_status, String marriage_status, String military_service, String disability_category, String city_subsistence_allowances, String volunteer, String household_registration, String current_address, String job_status, String working_place, String car_number, String contact_information, String remarks, int certificate_type, String certificate_number, long communityid, @Nullable int birthday, String relationship,String roomNumber,String gardenName,String building,String unit,String houseNumber,String humanType){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.ADD_RESIDENT_CALL(userId,name, gender, nation, native_place, cultural_level, political_status, marriage_status, military_service, disability_category, city_subsistence_allowances, volunteer, household_registration, current_address, job_status, working_place, car_number, contact_information, remarks, certificate_type, certificate_number, communityid, birthday,relationship,roomNumber,gardenName,building,unit,houseNumber,humanType);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = new Gson().fromJson(response.body(),LoginModel.class);
                    if (loginModel.getResultValue()){
                        Toast.makeText(RegisterActivity.this,"登记成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(RegisterActivity.this,loginModel.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void ToaddCall(long userId){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);

        Call<String> call = request.TOADD_CALL(userId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    ToAddModel toAddModel = new Gson().fromJson(response.body(),ToAddModel.class);

                    String[] sexStr = {"男","女"};
                    ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,sexStr);
                    sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    gender.setAdapter(sexAdapter);

                    ArrayAdapter<String> nationAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getNationList());
                    nationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    nation.setAdapter(nationAdapter);

                    ArrayAdapter<String> marriageAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getMarriage_statusList());
                    marriageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marriage_status.setAdapter(marriageAdapter);

                    ArrayAdapter<String> cultural_levelAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getCultural_levelList());
                    cultural_levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cultural_level.setAdapter(cultural_levelAdapter);

                    ArrayAdapter<String> political_statusAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getPolitical_statusList());
                    political_statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    political_status.setAdapter(political_statusAdapter);

                    ArrayAdapter<String> military_serviceAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getMilitary_serviceList());
                    military_serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    military_service.setAdapter(military_serviceAdapter);

                    ArrayAdapter<String> disability_categoryAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getDisability_categoryList());
                    disability_categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    disability_category.setAdapter(disability_categoryAdapter);

                    ArrayAdapter<String> city_subsistence_allowancesAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getCity_subsistence_allowancesList());
                    city_subsistence_allowancesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    city_subsistence_allowances.setAdapter(city_subsistence_allowancesAdapter);

                    ArrayAdapter<String> volunteerAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getVolunteerList());
                    volunteerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    volunteer.setAdapter(volunteerAdapter);

                    ArrayAdapter<String> humanTypeAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,toAddModel.getHumanTypeList());
                    humanTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    humanType.setAdapter(humanTypeAdapter);

                    List<String> certificateStr = new ArrayList<>();
                    for (CertificateTypeModel certificateTypeModel : toAddModel.getId_typeList()){
                        certificateStr.add(certificateTypeModel.getName());
                        certificate_typeList.add(certificateTypeModel.getType());
                    }

                    ArrayAdapter<String> certificate_typeAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,certificateStr);
                    certificate_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    certificate_type.setAdapter(certificate_typeAdapter);

                    List<String> communityStr = new ArrayList<>();
                    for (CommunityModel communityModel : toAddModel.getCommunityidList()){
                        communityStr.add(communityModel.getName());
                        communityidList.add(communityModel.getId());
                    }
                    ArrayAdapter<String> communityAdapter = new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_spinner_item,communityStr);
                    communityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    community.setAdapter(communityAdapter);


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
