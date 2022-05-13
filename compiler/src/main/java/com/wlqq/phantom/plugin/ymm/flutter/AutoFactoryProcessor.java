package com.wlqq.phantom.plugin.ymm.flutter;

import com.google.auto.service.AutoService;
import com.mb.flutter.annotated.AutoFactory;
import com.wlqq.phantom.plugin.ymm.flutter.bean.AutoFactoryClassInfo;
import com.wlqq.phantom.plugin.ymm.flutter.bean.AutoFactoryInfo;
import com.wlqq.phantom.plugin.ymm.flutter.creator.FactoryClassCreator;
import com.wlqq.phantom.plugin.ymm.flutter.creator.ProxyClassCreator;
import com.wlqq.phantom.plugin.ymm.flutter.definiton.ClassCreator;
import com.wlqq.phantom.plugin.ymm.flutter.definiton.EnvironmentParser;
import com.wlqq.phantom.plugin.ymm.flutter.parser.AutoFactoryEnvironmentParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Process.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AutoFactoryProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>" + "111");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.size() == 0) {
            return false;
        }

        List<AutoFactoryInfo> autoFactoryInfoList = createEnvironmentParser().parse(roundEnvironment);

        messager.printMessage(Diagnostic.Kind.NOTE, "autoFactoryInfoList>>>" + autoFactoryInfoList);

        createClassCreator(autoFactoryInfoList).create(filer);

        return false;
    }


    protected EnvironmentParser<List<AutoFactoryInfo>> createEnvironmentParser(){
        return new AutoFactoryEnvironmentParser();
    }

    protected ClassCreator createClassCreator(List<AutoFactoryInfo> autoFactoryInfoList) {

        ProxyClassCreator  classCreator = new ProxyClassCreator(messager);
        for (AutoFactoryInfo autoFactoryInfo:autoFactoryInfoList){
            classCreator.add(new FactoryClassCreator(autoFactoryInfo.getFactoryName()
                    + "AutoFactory", autoFactoryInfo.getClassNameList()));
        }
        return classCreator;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(AutoFactory.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
