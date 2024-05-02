package com.example.project.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataStatistics;
import com.example.project.R;
import com.example.project.network.OnMessageReceivedListenner;
import com.example.project.network.WebSockerClientBarChart;
import com.example.project.ui.custom_adapter.StatisticsAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsDetail extends AppCompatActivity {

    WebSockerClientBarChart webSockerClientBarChart;

    EditText edtDayA,edtDayB,searchEditText;

    Button btnStatistic ,searchButton ;


    ArrayList<DataStatistics> dataStatistics = new ArrayList<>();;
    ListView listView;

    StatisticsAdapter statisticsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_detail);

        webSockerClientBarChart = new WebSockerClientBarChart();
        webSockerClientBarChart.connectWebSockert();
        setControl();
        setEvent();




    }

    private void setEvent() {

        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayA = edtDayA.getText().toString();
                String dayB = edtDayB.getText().toString();
                String event = "StatisticalDetail";
                webSockerClientBarChart.putDataEventStatisticsDetail(event,dayA,dayB);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchEditText.getText().toString();
                performSearch(keyword);
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String keyword = searchEditText.getText().toString();
                    performSearch(keyword);
                    return true;
                }
                return false;
            }
        });

    }

    private void setControl() {
        listView = findViewById(R.id.lsvStatisticsDetail);
        edtDayA = findViewById(R.id.editTextDateA);
        edtDayB=findViewById(R.id.editTextDateB);
        btnStatistic = findViewById(R.id.buttonStatistic);
        searchButton = findViewById(R.id.searchButton);
        searchEditText = findViewById(R.id.searchEditText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webSockerClientBarChart.setOnMessageReceivedListener(new OnMessageReceivedListenner() {
            @Override
            public void onMessageReceived(String message) {
                try {
                    JSONObject response = new JSONObject(message);
                    JSONArray dataStatisticArray = response.getJSONArray("dataBooks");
                    dataStatistics.clear();

                    for (int i = 0; i < dataStatisticArray.length(); i++) {
                        JSONObject bookObject = dataStatisticArray.getJSONObject(i);
                        String tensach = bookObject.getString("tensach");
                        int img = 0;
                        switch (tensach){
                            case "Chúng ta đã mỉm cười":
                                img = R.drawable.ctdmc;
                                break;
                            case "Khởi hành":
                                img = R.drawable.kh;
                                break;

                            case "Mọi thứ đều có thể thay đổi":
                                img = R.drawable.mtdtd;
                                break;
                            case "Làm chủ các mẫu thiết kế kinh điển trong lập trình":
                                img = R.drawable.tl;
                                break;
                            case "SÁCHE":
                                img = R.drawable.sache;
                                break;
                            case "SÁCHF":
                                img = R.drawable.sachf;
                                break;
                            case "SÁCHG":
                                img = R.drawable.sachg;
                                break;
                            case "SÁCHK":
                                img = R.drawable.sachh;
                                break;
                            default:
                                Log.e("Err", "onMessageReceived: ",null );

                        }

                        String tongluotmuon = bookObject.getString("tongluotmuon");
                        String tongluottra = bookObject.getString("tongluottra");
                        String doahthu = bookObject.getString("doanhthu");
                        String tonkho = bookObject.getString("tonkho");
                        dataStatistics.add(new DataStatistics(img,tensach,"Tổng lượt mượn: "+tongluotmuon,"Tổng lượt trả: "+tongluottra,"Doanh thu: "+doahthu,"Tồn kho: "+tonkho));


                        Log.i("NHAN222", "Tên sách: " + tensach + ", Số lượt mượn: " + tongluotmuon+"\n"+
                                        "so luot tra"+tongluottra+"doanh thu" +doahthu
                                );
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setDataStatistic(dataStatistics);
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDataStatistic(ArrayList<DataStatistics> data){
        statisticsAdapter = new StatisticsAdapter(StatisticsDetail.this,R.layout.layoutadapterstatistics,data);
        listView.setAdapter(statisticsAdapter);
        statisticsAdapter.notifyDataSetChanged();
    }

    private void performSearch(String keyword) {
        ArrayList<DataStatistics> searchResults = new ArrayList<>();
        for (DataStatistics data : dataStatistics) {
            if (data.getNameBook().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(data);
            }
        }

        // Cập nhật ListView để hiển thị kết quả tìm kiếm
        updateListView(searchResults);
    }

    private void updateListView(ArrayList<DataStatistics> searchResults) {
        // Cập nhật dữ liệu cho Adapter và ListView
        statisticsAdapter = new StatisticsAdapter(this, R.layout.layoutadapterstatistics, searchResults);
        listView.setAdapter(statisticsAdapter);
        statisticsAdapter.notifyDataSetChanged();
    }

    public void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final EditText selectedEditText = (EditText) view;
        DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsDetail.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Lấy ngày được chọn và hiển thị trên ô nhập ngày
                        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        selectedEditText.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

}