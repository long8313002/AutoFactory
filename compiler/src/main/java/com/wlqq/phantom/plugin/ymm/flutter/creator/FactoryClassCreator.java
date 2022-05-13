package com.wlqq.phantom.plugin.ymm.flutter.creator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.wlqq.phantom.plugin.ymm.flutter.definiton.ClassCreator;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class FactoryClassCreator implements ClassCreator {

    private final String factoryName;
    private final List<String> elementList;

    public FactoryClassCreator(String factoryName, List<String> elementList){

        this.factoryName = factoryName;
        this.elementList = elementList;
    }

    @Override
    public boolean create(Filer filer) {
        return createFactory(filer);
    }

    private boolean createFactory(Filer filer) {
        TypeSpec typeBuilder = TypeSpec.classBuilder(factoryName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(createMethod(elementList))
                .build();
        JavaFile javaFile = JavaFile.builder("com.mb.auto", typeBuilder)
                .addFileComment("自动生成的工厂类")
                .build();
        try {
            javaFile.writeTo(filer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private MethodSpec createMethod(List<String> elementList) {
        TypeVariableName typeVariableName= TypeVariableName.get("T", TypeName.get(Object.class));

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("createAll")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariable(typeVariableName)
                .returns(List.class);

        methodBuilder.addStatement("java.util.List list = new java.util.ArrayList()");

        int index = 0;
        for (String className : elementList) {
            String param = "v"+index;
            methodBuilder.addStatement("java.lang.Object "+param+" = new "+className+"()");
            methodBuilder.addStatement("list.add("+param+")");
            index++;
        }
        methodBuilder.addStatement("return list");
        return methodBuilder.build();
    }
}
