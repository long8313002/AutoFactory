package com.wlqq.phantom.plugin.ymm.flutter.creator;

import com.wlqq.phantom.plugin.ymm.flutter.definiton.ClassCreator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class ProxyClassCreator implements ClassCreator {

    private final List<ClassCreator> classCreatorList = new ArrayList<>();
    private final Messager messager;

    public ProxyClassCreator(Messager messager){

        this.messager = messager;
    }

    public void add(ClassCreator creator) {
        classCreatorList.add(creator);
    }

    @Override
    public boolean create(Filer filer) {
        boolean isSuccess = false;
        for (ClassCreator creator : classCreatorList) {
            try{
                isSuccess = creator.create(filer) || isSuccess;
            }catch (Exception e){
                messager.printMessage(Diagnostic.Kind.NOTE, "createClass异常："+e);
                throw e;
            }

        }
        return isSuccess;
    }
}
