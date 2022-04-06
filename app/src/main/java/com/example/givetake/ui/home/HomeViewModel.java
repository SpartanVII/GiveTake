package com.example.givetake.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.givetake.model.Product;
import com.example.givetake.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Product>> mList;
    private Presenter presenter;

    public HomeViewModel() {
        presenter = new Presenter();
        mList = new MutableLiveData<>();
        waitAndSetValue();
    }

    public void  waitAndSetValue(){
        mList.setValue(presenter.getProductsByTag(null));
    }
    public LiveData<List<Product>> getList() {
        return mList;
    }
}