package com.example.haozai.jen.buy;


import java.io.Serializable;

/**
 * Created by haozai on 2018-08-15.
 */

public class Bean implements Serializable {

    private String Title; //每条item的数据
    private boolean isChecked; //每条item的状态

    public Bean(String title) {
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
