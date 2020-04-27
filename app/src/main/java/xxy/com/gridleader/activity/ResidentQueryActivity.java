package xxy.com.gridleader.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import xxy.com.gridleader.R;
import xxy.com.gridleader.model.ResidentQueryModel;
import xxy.com.gridleader.model.ResultModel;

public class ResidentQueryActivity extends AppCompatActivity {
    private TextView name,genderStr,nation,native_place,cultural_level,political_status,marriage_status,military_service,disability_category,city_subsistence_allowances,volunteer,household_registration,current_address,job_status,working_place,car_number,contact_information,remarks,certificate_type,certificate_number,communityName,relationship,humanType;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_query);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        ResultModel resultModel = new Gson().fromJson(response,ResultModel.class);
        ResidentQueryModel residentQueryModel = resultModel.getData();

//        Toast.makeText(ResidentQueryActivity.this,response,Toast.LENGTH_SHORT).show();
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name = findViewById(R.id.name);
        name.setText(residentQueryModel.getName());
        genderStr = findViewById(R.id.genderStr);
        genderStr.setText(residentQueryModel.getGenderStr());
        nation = findViewById(R.id.nation);
        nation.setText(residentQueryModel.getNation());
        native_place = findViewById(R.id.native_place);
        native_place.setText(residentQueryModel.getNative_place());
        cultural_level = findViewById(R.id.cultural_level);
        cultural_level.setText(residentQueryModel.getCultural_level());
        political_status = findViewById(R.id.political_status);
        political_status.setText(residentQueryModel.getPolitical_status());
        marriage_status = findViewById(R.id.marriage_status);
        marriage_status.setText(residentQueryModel.getMarriage_status());
        military_service = findViewById(R.id.military_service);
        military_service.setText(residentQueryModel.getMilitary_service());
        disability_category = findViewById(R.id.disability_category);
        disability_category.setText(residentQueryModel.getDisability_category());
        city_subsistence_allowances = findViewById(R.id.city_subsistence_allowances);
        city_subsistence_allowances.setText(residentQueryModel.getCity_subsistence_allowances());
        volunteer = findViewById(R.id.volunteer);
        volunteer.setText(residentQueryModel.getVolunteer());
        household_registration = findViewById(R.id.household_registration);
        household_registration.setText(residentQueryModel.getHousehold_registration());
        current_address = findViewById(R.id.current_address);
        current_address.setText(residentQueryModel.getCurrent_address());
        job_status = findViewById(R.id.job_status);
        job_status.setText(residentQueryModel.getJob_status());
        working_place = findViewById(R.id.working_place);
        working_place.setText(residentQueryModel.getWorking_place());
        car_number = findViewById(R.id.car_number);
        car_number.setText(residentQueryModel.getCar_number());
        contact_information = findViewById(R.id.contact_information);
        contact_information.setText(residentQueryModel.getContact_information());
        remarks = findViewById(R.id.remarks);
        remarks.setText(residentQueryModel.getRemarks());
        certificate_type = findViewById(R.id.certificate_type);
        certificate_type.setText(residentQueryModel.getId_type());
        certificate_number = findViewById(R.id.certificate_number);
        certificate_number.setText(residentQueryModel.getId_number());
        communityName.setText(residentQueryModel.getCommunityName());
        relationship = findViewById(R.id.relationship);
        relationship.setText(residentQueryModel.getRelationship());
        humanType = findViewById(R.id.humanType);
        humanType.setText(residentQueryModel.getHumanType());

//        Toast.makeText(ResidentQueryActivity.this,residentQueryModel.getCareer(),Toast.LENGTH_SHORT).show();
    }
}
