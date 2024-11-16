//package com.example.hihi;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class API {
//    private static final String BASE_URL = "http://10.0.2.2:3000";  // Đảm bảo dùng địa chỉ đúng
//    private Retrofit retrofit;
//
//    public ApiClient() {
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
//
//    public interface ApiCallback {
//        void onSuccess(List<Fruits> fruits);
//        void onFailure(Throwable t);
//    }
//
//    public void getFruits(ApiCallback callback) {
//        ApiService apiService = retrofit.create(ApiService.class);
//        Call<List<Fruits>> call = apiService.getFruits();
//
//        call.enqueue(new Callback<List<Fruit>>() {
//            @Override
//            public void onResponse(Call<List<Fruit>> call, Response<List<Fruit>> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.body());
//                } else {
//                    callback.onFailure(new Exception("Error: " + response.message()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Fruit>> call, Throwable t) {
//                callback.onFailure(t);
//            }
//        });
//    }
//}