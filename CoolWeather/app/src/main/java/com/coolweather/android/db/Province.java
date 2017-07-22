package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by twx on 2017/4/16.
 */

public class Province extends DataSupport {
    private String provinceName;
    private int provinceCode;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
