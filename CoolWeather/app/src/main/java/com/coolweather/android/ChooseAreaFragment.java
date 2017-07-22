package com.coolweather.android;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.Country;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by twx on 2017/4/16.
 */

public class ChooseAreaFragment extends Fragment {

    private List<String> dataList = new ArrayList<>();
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter adapter;

    private int currentLevel;//当前选中级别
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;
    private Province selectedProvince;
    private City selectedCity;
    private Country selectedCountry;
    private  ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TWX", "onCreateView: ");
        View view = inflater.inflate(R.layout.choose_area, container, false);

        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);

        listView = (ListView) view.findViewById(R.id.listView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        }
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TWX", "onActivityCreated: ");
        //列表
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCity();
                } else if (currentLevel==LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCountry();
                } else if (currentLevel == LEVEL_COUNTY) {
                    selectedCountry = countryList.get(position);
                    //查询天气
                }
            }
        });

        //返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCity();//查询县级列表
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();//查询省级列表
                }
            }
        });

        //查询省级数据
        queryProvinces();
    }


    /**
     * 查询省级数据
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);

        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            //添加数据
            for (Province pro : provinceList) {
                dataList.add(pro.getProvinceName());
            }
            //更新数据
            adapter.notifyDataSetChanged();
            //选择第一行数据
            listView.setSelection(0);
            //缓存当前级别
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            System.out.println("province "+address);
            queryFromServer(address,"province");
        }
    }

    /**
     * 查询市级数据
     */
    private void queryCity() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);

        cityList = DataSupport.where("provinceCode=?", String.valueOf(selectedProvince.getProvinceCode())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            listView.deferNotifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            //从服务器查询数据
            String address = "http://guolin.tech/api/china/"+selectedProvince.getProvinceCode();
            System.out.println("city "+address);
            queryFromServer(address,"city");
        }
    }

    /**
     * 查询县级数据
     */
    private void queryCountry() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        countryList = DataSupport.where("cityId=?", String.valueOf(selectedCity.getId())).find(Country.class);
        if (countryList.size() > 0) {
            dataList.clear();
            for (Country conutry : countryList) {
                dataList.add(conutry.getCountryName());
            }
            listView.deferNotifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            String address = "http://guolin.tech/api/china/" + selectedProvince.getProvinceCode() + "/" + selectedCity.getCityCode();
            System.out.println("country "+address);
            queryFromServer(address,"country");
        }
    }


    /**
     * 从服务器获取数据
     * @param address
     * @param type
     */
    private void queryFromServer(String address, final String type) {
    showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responeText = response.body().string();//json 格式的
                boolean result=false;
                if ("province".equals(type)) {
                    //处理json数据，入库
                     result = Utility.handleProvinceResponse(responeText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responeText, selectedProvince.getProvinceCode());
                } else if ("country".equals(type)) {
                    result = Utility.handleCountyResponse(responeText, selectedCity.getId());
                }

                if (result) {
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           closeProgressDialog();
                           if ("province".equals(type)) {
                               queryProvinces();
                           } else if ("city".equals(type)) {
                               queryCity();
                           } else if ("country".equals(type)) {
                               queryCountry();
                           }
                       }
                   });
                }
            }
        });
    }

    /**
     * 进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
