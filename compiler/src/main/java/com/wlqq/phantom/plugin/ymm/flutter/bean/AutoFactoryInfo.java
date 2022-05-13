package com.wlqq.phantom.plugin.ymm.flutter.bean;

import java.util.ArrayList;
import java.util.List;

public class AutoFactoryInfo {

    private String factoryName;
    private List<AutoFactoryClassInfo> classInfoList;

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public List<AutoFactoryClassInfo> getClassInfoList() {
        return classInfoList;
    }

    public void setClassInfoList(List<AutoFactoryClassInfo> classInfoList) {
        this.classInfoList = classInfoList;
    }

    public List<String> getClassNameList(){
        List<String> classNameList = new ArrayList<>();
        if(classInfoList==null){
            return classNameList;
        }
        for (AutoFactoryClassInfo info:classInfoList){
            classNameList.add(info.getClassName());
        }
        return classNameList;
    }

    @Override
    public String toString() {
        return "AutoFactoryInfo{" +
                "factoryName='" + factoryName + '\'' +
                ", classInfoList=" + classInfoList +
                '}';
    }
}
