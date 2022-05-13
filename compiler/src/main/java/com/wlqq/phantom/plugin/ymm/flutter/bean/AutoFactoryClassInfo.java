package com.wlqq.phantom.plugin.ymm.flutter.bean;


public class AutoFactoryClassInfo {


    private String className;
    private int order;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "AutoFactoryInfo{" +
                "className='" + className + '\'' +
                ", order=" + order +
                '}';
    }
}
