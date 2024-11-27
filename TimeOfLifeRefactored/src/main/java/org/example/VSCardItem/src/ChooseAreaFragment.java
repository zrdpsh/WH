//package org.example.VSCardItem.src;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.xiaoguang.coolweather.db.City;
//import com.example.xiaoguang.coolweather.db.County;
//import com.example.xiaoguang.coolweather.db.Province;
//import com.example.xiaoguang.coolweather.util.HttpUtil;
//import com.example.xiaoguang.coolweather.util.Utility;
//
//import org.litepal.crud.DataSupport;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Response;
//
///*
//Про задание целиком - пока самое сложное из всех.
//Первая трудность - нет собственного подходящего кода (на несколько сотен строк) со сложной логикой.
//Из-за этого нужно было искать чужой - и отсюда очень много времени ушло не столько на написание, сколько на поиск и чтение.
//Не хватает опыта в работе с реальными проектами, поэтому контекст каждого отдельного файла восстанавливать дольше всего
//- что делает вся программа, и какую роль в ней выполняет выбранный код.
//Если бы была насмотренность (типовые структуры проектов, типовое разделение логики внутри, в Spring, в частности),
//получится гораздо быстрее.
//Второе соображение - писать чтобы то ни было с нуля, вероятно, всё равно гораздо проще, чем сделать уже готовый код несколько лучше.
//
// */
//
//
//
///* первая версия кода :
// - три однотипных класса - QueryProvinces - ..Cities - ..Counties
// - цепочки if else
// */
//
//public class ChooseAreaFragment extends Fragment {
//    public static final int LEVEL_PROVINCE = 0;
//    public static final int LEVEL_CITY = 1;
//    public static final int LEVEL_COUNTY = 2;
//    private static final String TAG = "ChooseAreaFragment";
//
//
//    private ProgressDialog progressDialog;
//
//    private TextView titleText;
//
//    private ImageView backButton;
//
//    private ListView listView;
//
//    private ArrayAdapter<String> adapter;
//
//    private List<String> dataList = new ArrayList<>();
//
//    private List<Province> provinceList;
//    private List<City> cityList;
//    private List<County> countyList;
//
//    private Province selectedProvince;
//
//    private City selectedCity;
//
//    private int currentLevel;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.choose_area, container, false);
//        titleText = (TextView) view.findViewById(R.id.title_text);
//        backButton = (ImageView) view.findViewById(R.id.back_button);
//        listView = (ListView) view.findViewById(R.id.list_view);
//        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
//        listView.setAdapter(adapter);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (currentLevel == LEVEL_PROVINCE) {
//                    selectedProvince = provinceList.get(position);
//                    queryCities();
//                } else if (currentLevel == LEVEL_CITY) {
//                    selectedCity = cityList.get(position);
//                    queryCounties();
//                }
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentLevel == LEVEL_COUNTY) {
//                    queryCities();
//                } else if (currentLevel == LEVEL_CITY) {
//                    queryProvinces();
//                }
//            }
//        });
//
//        queryProvinces();
//    }
//
//    private void queryFromServer(String address, final String type) {
//        showProgressDialog();
//        HttpUtil.sendOkHttpRequest(address, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        Toast.makeText(getContext(), "加載失敗", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseText = response.body().string();
//                boolean result = false;
//                if ("province".equals(type)) {
//                    result = Utility.handleProvinceResponse(responseText);
//                } else if ("city".equals(type)) {
//                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
//                } else if ("county".equals(type)) {
//                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
//                }
//
//                if (result) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            closeProgressDialog();
//                            if ("province".equals(type)) {
//                                queryProvinces();
//                            } else if ("city".equals(type)) {
//                                queryCities();
//                            } else if ("county".equals(type)) {
//                                queryCounties();
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void queryProvinces() {
//        titleText.setText("中国");
//        backButton.setVisibility(View.GONE);
//        provinceList = DataSupport.findAll(Province.class);
//        if (provinceList.size() > 0) {
//            dataList.clear();
//            for (Province province : provinceList) {
//                dataList.add(province.getProvinceName());
//            }
//            adapter.notifyDataSetChanged();
//            listView.setSelection(0);
//            currentLevel = LEVEL_PROVINCE;
//        } else {
//            String address = "http://guolin.tech/api/china";
//            queryFromServer(address, "province");
//        }
//    }
//
//    private void queryCities() {
//        titleText.setText(selectedProvince.getProvinceName());
//        backButton.setVisibility(View.VISIBLE);
//        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
//        if (cityList.size() > 0) {
//            dataList.clear();
//            for (City city : cityList) {
//                dataList.add(city.getCityName());
//            }
//            adapter.notifyDataSetChanged();
//            listView.setSelection(0);
//            currentLevel = LEVEL_CITY;
//        } else {
//            int provinceCode = selectedProvince.getProvinceCode();
//            String address = "http://guolin.tech/api/china/" + provinceCode;
//            queryFromServer(address, "city");
//        }
//    }
//
//    private void queryCounties() {
//        titleText.setText(selectedCity.getCityName());
//        backButton.setVisibility(View.VISIBLE);
//        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
//        if (countyList.size() > 0) {
//            dataList.clear();
//            for (County county : countyList) {
//                dataList.add(county.getCountyName());
//            }
//            adapter.notifyDataSetChanged();
//            listView.setSelection(0);
//            currentLevel = LEVEL_COUNTY;
//        } else {
//            int provinceCode = selectedProvince.getProvinceCode();
//            int cityCode = selectedCity.getCityCode();
//            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
//            Log.d(TAG, "address:_" + address);
//            queryFromServer(address, "county");
//        }
//    }
//
//    private void showProgressDialog() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("正在加载中。。。");
//            progressDialog.setCancelable(false);
//        }
//        progressDialog.show();
//    }
//
//    private void closeProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }
//}
//
///* Логика программы
//- загрузить список городов (объект, у которого может быть несколько состояний, одно из которых - дефолтное, Provinces в этом случае)
//- выловить нажатие по пункту меню
//- сделать запрос и обновить состояние объекта
//
//Состояний всего три (но можно представить, что их может быть больше) - им соответствуют три вида однотипных запросов к серверу.
//
//onCreateView и onActivityCreated - часть API Андроида, их менять нельзя.
//Логично явно объявить объект StateManager и перенести внутрь него логику работы с списком - запросы и обновление списка.
//Повторяющийся код из query<название уровня> разбить на функциональный интерфейс и добавить отдельные запросы для варьирующихся частей
// */
//
//package com.example.xiaoguang.coolweather;
//
//
//public class ChooseAreaFragment extends Fragment {
//    private static final String TAG = "ChooseAreaFragment";
//
//    private ProgressDialog progressDialog;
//    private TextView titleText;
//    private ImageView backButton;
//    private ListView listView;
//    private ArrayAdapter<String> adapter;
//    private List<String> dataList = new ArrayList<>();
//
//    private StateManager stateManager;
//
//    private final Map<String, Runnable> queryMethods = new HashMap<>();
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.choose_area, container, false);
//        titleText = view.findViewById(R.id.title_text);
//        backButton = view.findViewById(R.id.back_button);
//        listView = view.findViewById(R.id.list_view);
//        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
//        listView.setAdapter(adapter);
//
//        stateManager = new StateManager(this);
//
//        queryMethods.put("province", this::queryProvinces);
//        queryMethods.put("city", this::queryCities);
//        queryMethods.put("county", this::queryCounties);
//
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                stateManager.handleItemClick(position);
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stateManager.handleBackButtonClick();
//            }
//        });
//
//        stateManager.queryProvinces();
//    }
//
//    private void queryFromServer(String address, final String type) {
//        showProgressDialog();
//        HttpUtil.sendOkHttpRequest(address, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                getActivity().runOnUiThread(() -> {
//                    closeProgressDialog();
//                    Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseText = response.body().string();
//                boolean result = Utility.handleResponse(responseText, type, stateManager);
//
//                if (result) {
//                    getActivity().runOnUiThread(() -> {
//                        closeProgressDialog();
//                        stateManager.handleQueryResult(type);
//                    });
//                }
//            }
//        });
//    }
//
//    private void showProgressDialog() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("正在加载中...");
//            progressDialog.setCancelable(false);
//        }
//        progressDialog.show();
//    }
//
//    private void closeProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }
//
//    private void queryData(String title, boolean isBackButtonVisible, String addressPrefix, String type, DataProvider dataProvider) {
//        titleText.setText(title);
//        backButton.setVisibility(isBackButtonVisible ? View.VISIBLE : View.GONE);
//
//        List<?> dataList = dataProvider.getData();
//        if (dataList.size() > 0) {
//            updateDataList(dataList);
//            currentLevel = dataProvider.getLevel();
//            return;
//        }
//
//        String address = addressPrefix + type;
//        queryFromServer(address, type);
//    }
//
//    @FunctionalInterface
//    interface DataProvider {
//        List<?> getData();
//        default int getLevel() {
//            return -1;
//        }
//    }
//
//
//    private class StateManager {
//        private static final int LEVEL_PROVINCE = 0;
//        private static final int LEVEL_CITY = 1;
//        private static final int LEVEL_COUNTY = 2;
//
//        private int currentLevel;
//        private Province selectedProvince;
//        private City selectedCity;
//
//        StateManager(ChooseAreaFragment fragment) {
//            // TODO
//        }
//
//        void handleItemClick(int position) {
//            if (currentLevel == LEVEL_PROVINCE) {
//                selectedProvince = provinceList.get(position);
//                queryCities();
//            }
//            if (currentLevel == LEVEL_CITY) {
//                selectedCity = cityList.get(position);
//                queryCounties();
//            }
//        }
//
//        void handleBackButtonClick() {
//            if (currentLevel == LEVEL_COUNTY) {
//                queryCities();
//            }
//            if (currentLevel == LEVEL_CITY) {
//                queryProvinces();
//            }
//        }
//
//        void queryProvinces() {
//            queryData("中国", false, "http://guolin.tech/api/china/", "province",
//                    () -> DataSupport.findAll(Province.class));
//        }
//
//        void queryCities() {
//            queryData(selectedProvince.getProvinceName(), true, "http://guolin.tech/api/china/",
//                    String.valueOf(selectedProvince.getProvinceCode()),
//                    () -> DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class));
//        }
//
//        void queryCounties() {
//            queryData(selectedCity.getCityName(), true, "http://guolin.tech/api/china/",
//                    String.format("%d/%d", selectedProvince.getProvinceCode(), selectedCity.getCityCode()),
//                    () -> DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class));
//        }
//
//        void handleQueryResult(String type) {
//            Runnable queryMethod = queryMethods.get(type);
//            if (queryMethod != null) {
//                queryMethod.run();
//            }
//        }
//
//        void updateDataList(List<?> list) {
//            dataList.clear();
//            for (Object item : list) {
//                String name = null;
//                if (item instanceof Province) {
//                    name = ((Province) item).getProvinceName();
//                }
//                if (item instanceof City) {
//                    name = ((City) item).getCityName();
//                }
//                if (item instanceof County) {
//                    name = ((County) item).getCountyName();
//                }
//                if (name != null) {
//                    dataList.add(name);
//                }
//            }
//            adapter.notifyDataSetChanged();
//            listView.setSelection(0);
//        }
//
//        Province getSelectedProvince() {
//            return selectedProvince;
//        }
//
//        City getSelectedCity() {
//            return selectedCity;
//        }
//    }
//}
//
//
//
//
