package com.wlqq.phantom.plugin.ymm.flutter.parser;

import com.mb.flutter.annotated.AutoFactory;
import com.wlqq.phantom.plugin.ymm.flutter.bean.AutoFactoryClassInfo;
import com.wlqq.phantom.plugin.ymm.flutter.bean.AutoFactoryInfo;
import com.wlqq.phantom.plugin.ymm.flutter.definiton.EnvironmentParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

public class AutoFactoryEnvironmentParser implements EnvironmentParser<List<AutoFactoryInfo>> {

    @Override
    public List<AutoFactoryInfo> parse(RoundEnvironment roundEnvironment) {

        Map<String,List<AutoFactoryClassInfo>> autoFactoryInfoMap = new HashMap<>();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoFactory.class);
        for (Element element : elements) {
            String fullClassName = element.asType().toString();
            AutoFactory autoFactory = element.getAnnotation(AutoFactory.class);
            String[] factoryNames = autoFactory.value();
            int order = autoFactory.order();

            for (String factoryName : factoryNames) {
                List<AutoFactoryClassInfo> autoFactoryClassInfoList = autoFactoryInfoMap.computeIfAbsent(
                        factoryName, k -> new ArrayList<>());
                autoFactoryClassInfoList.add(createAutoFactoryInfo(fullClassName,order));
            }
        }

        return sort(parse(autoFactoryInfoMap));
    }

    private AutoFactoryClassInfo createAutoFactoryInfo(String className, int order){
        AutoFactoryClassInfo autoFactoryClassInfo = new AutoFactoryClassInfo();
        autoFactoryClassInfo.setClassName(className);
        autoFactoryClassInfo.setOrder(order);
        return autoFactoryClassInfo;
    }

    private List<AutoFactoryInfo> parse(Map<String,List<AutoFactoryClassInfo>> autoFactoryInfoMap){
        List<AutoFactoryInfo> autoFactoryInfoList = new ArrayList<>();
        Set<String> keys = autoFactoryInfoMap.keySet();
        for (String key:keys){
            AutoFactoryInfo autoFactoryInfo = new AutoFactoryInfo();
            autoFactoryInfo.setFactoryName(key);
            autoFactoryInfo.setClassInfoList(autoFactoryInfoMap.get(key));
            autoFactoryInfoList.add(autoFactoryInfo);
        }
        return autoFactoryInfoList;
    }

    private List<AutoFactoryInfo> sort(List<AutoFactoryInfo> autoFactoryInfoList){
        for (AutoFactoryInfo autoFactoryInfo:autoFactoryInfoList){
            List<AutoFactoryClassInfo> classInfoList = autoFactoryInfo.getClassInfoList();
            classInfoList.sort((t0, t1) -> t1.getOrder()-t0.getOrder());
        }
        return autoFactoryInfoList;
    }
}
