package xxy.com.gridleader.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xxy.com.gridleader.R;
import xxy.com.gridleader.adapter.GridViewAdapter;
import xxy.com.gridleader.model.CategoryListModel;
import xxy.com.gridleader.model.GridViewModel;
import xxy.com.gridleader.model.LineChartModel;
import xxy.com.gridleader.utils.MyConstants;
import xxy.com.gridleader.utils.NetWorkRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticFragment extends Fragment {
    private LineChart linechart_problem_trend;
    private GridView gridView;
    private long userId;
    private SharedPreferences loginSP;
    private int item_height;


    public StatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        initUi();

//        setGridView();

        LINECHART_CALL(userId);
        CATEALARM_CALL(userId);
    }

    private void initUi() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item, null);
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
//        view.getMeasuredWidth(); // 获取宽度
        item_height = view.getMeasuredHeight(); // 获取高度


        loginSP = getContext().getSharedPreferences("loginSP", Context.MODE_PRIVATE);
        userId = loginSP.getLong("userId",0);

        linechart_problem_trend = getView().findViewById(R.id.linechart_problem_trend);
        linechart_problem_trend.setNoDataText("暂无数据");
        linechart_problem_trend.setNoDataTextColor(Color.BLACK);


        gridView = getView().findViewById(R.id.grid_view);
        gridView.setFocusable(false);
    }

    private void setLinechartProblemTrend(final List<String> xVals, List<Entry> yVals) {
        linechart_problem_trend.setDescription(null);

        XAxis x1 = linechart_problem_trend.getXAxis();
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);
        x1.setTextSize(12);
        x1.setTextColor(Color.parseColor("#A19F9D"));
        x1.setSpaceMin(20);
        x1.setDrawGridLines(false);
        x1.setLabelRotationAngle(60);
        x1.setAxisMinimum(0f);

        YAxis leftAxis = linechart_problem_trend.getAxisLeft();
        leftAxis.setTextSize(12);
        leftAxis.setTextColor(Color.parseColor("#A19F9D"));
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(false);

        linechart_problem_trend.getAxisRight().setEnabled(false);
        linechart_problem_trend.setTouchEnabled(true); // 设置是否可以触摸
        // enable scaling and dragging
        linechart_problem_trend.setDragEnabled(true);// 是否可以拖拽
        linechart_problem_trend.setScaleEnabled(true);// 是否可以缩放
        linechart_problem_trend.setExtraBottomOffset(20f);
        linechart_problem_trend.animateX(1500);

//        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
//        mv.setChartView(linechart_problem_trend); // For bounds control
//        linechart_problem_trend.setMarker(mv); // Set the marker to the chart


        Legend l = linechart_problem_trend.getLegend();//图例
        l.setEnabled(false);
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//设置图例的位置
//        l.setTextSize(15f);//设置文字大小
//        l.setForm(Legend.LegendForm.LINE);//正方形，圆形或线
//        l.setFormSize(10f); // 设置Form的大小
//        l.setWordWrapEnabled(true);//是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
//        l.setFormSize(10f);//设置Form的宽度


//            x1.setLabelCount(6);
            x1.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf(xVals.get((int) value));
                }
            });





        LineDataSet lineDataSet = new LineDataSet(yVals, "");
        Typeface tf1 = Typeface.DEFAULT_BOLD;
        lineDataSet.setValueTypeface(tf1);

        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleHoleRadius(3f);
        lineDataSet.setCircleColorHole(Color.parseColor("#15AB92"));
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setColor(Color.parseColor("#15AB92"));
//            lineDataSet.enableDashedLine(20, 5, 5);
        lineDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.linechart_bg);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setValueTextColor(Color.parseColor("#15AB92"));
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet); // add the dataset
        LineData data = new LineData(dataSets);
        linechart_problem_trend.setData(data);
    }


    private void LINECHART_CALL(long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.LINECHART_CALL(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    LineChartModel lineChartModel = new Gson().fromJson(response.body(),LineChartModel.class);
                    List<String> xVals = new ArrayList<>();
                    List<Entry> yVals = new ArrayList<>();

                    for (int i = 0 ; i < lineChartModel.getLineChartList().size(); i++ ){
                        xVals.add(lineChartModel.getLineChartList().get(i).getLabel());
                        yVals.add(new Entry(i,(float) lineChartModel.getLineChartList().get(i).getValue()));
                    }
                    setLinechartProblemTrend(xVals,yVals);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void CATEALARM_CALL(long userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<String> call = request.CATEALARM_CALL(userId,1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    CategoryListModel categoryListModel = new Gson().fromJson(response.body(),CategoryListModel.class);
                    List<GridViewModel> gridViewModels = new ArrayList<>();
                    for (int i = 0; i < categoryListModel.getCategoryList().size(); i++){
                        GridViewModel model = new GridViewModel();
                        model.setPercent(String.valueOf(categoryListModel.getCategoryList().get(i).getPercent()));
                        model.setNum(String.valueOf(categoryListModel.getCategoryList().get(i).getValue()));
                        model.setName(String.valueOf(categoryListModel.getCategoryList().get(i).getName()));
                        gridViewModels.add(model);
                    }
                    GridViewAdapter adapter = new GridViewAdapter(gridViewModels,getContext());

                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) gridView.getLayoutParams();
//                    if (gridViewModels.size() % 4 == 0){
//                        params.height = item_height * (gridViewModels.size() / 4);
//                    }else {
//                        params.height = item_height * (gridViewModels.size() / 4)  + 1;
//                    }
                    if (gridViewModels.size() <= 4){
                        params.height = item_height + 20;
                    }else {
                        params.height = item_height * (gridViewModels.size()/4 + 1) + 20;
                    }
                    gridView.setLayoutParams(params);

                    gridView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
