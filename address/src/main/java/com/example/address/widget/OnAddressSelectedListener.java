package com.example.address.widget;


import com.example.address.bean.City;
import com.example.address.bean.County;
import com.example.address.bean.Province;
import com.example.address.bean.Street;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county, Street street);
}
