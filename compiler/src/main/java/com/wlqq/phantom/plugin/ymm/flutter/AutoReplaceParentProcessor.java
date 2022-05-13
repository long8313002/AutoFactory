package com.wlqq.phantom.plugin.ymm.flutter;

import com.google.auto.service.AutoService;
import com.mb.flutter.annotated.AutoReplaceClassParent;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import javax.tools.JavaFileObject;


@AutoService(Process.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AutoReplaceParentProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>" + "222222:" + new File(".").getAbsolutePath());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.size() == 0) {
            return false;
        }

        try {
            invoke(roundEnvironment);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    private void invoke(RoundEnvironment roundEnvironment) throws Exception {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AutoReplaceClassParent.class);
        for (Element element : elements) {
            // 获取注解元素的值
            AutoReplaceClassParent annotation = element.getAnnotation(AutoReplaceClassParent.class);

            String moduleName = annotation.moduleName();
            String className = annotation.replaceClass();
            String replaceParent = annotation.replaceParent();
            String[] split = className.split("\\.");
            String singleClassName = split[split.length-1];
            String classNamePath = className.replaceAll("\\.", "/");
            File file ;
            if(new File("").getAbsolutePath().contains("plugin_flutter_apk")){
                file = new File("../",moduleName + "/src/main/java/" + classNamePath + ".java");
            }else{
                file = new File(moduleName + "/src/main/java/" + classNamePath + ".java");
            }
            messager.printMessage(Diagnostic.Kind.NOTE, ">>>" + "xxxxx:" + file.getAbsolutePath());
            messager.printMessage(Diagnostic.Kind.NOTE, ">>>" + "xxxxx:" + file.exists());

            JavaFileObject source = filer.createSourceFile("com.mb.auto."+singleClassName+"AutoProxy");

            Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()));

            StringBuilder codeContent = new StringBuilder();

            lines.forEach(line -> {
                if(line.startsWith("package")){
                    line = "package com.mb.auto;";
                }
                if(line.contains("class ")&&line.contains("extends ")){
                    line =  line.replace(singleClassName,singleClassName+"AutoProxy");
                    String parentClass = findParent(line);
                    line = line.replace(parentClass,replaceParent);
                }
                codeContent.append(line).append(System.lineSeparator());
            });

            String content = codeContent.toString();


            messager.printMessage(Diagnostic.Kind.NOTE, ">>>" + "xxxxx:" + content);
            // File to List
            //List<String> list = lines.collect(Collectors.toList());

            Writer writer = source.openWriter();
            writer.write(content);
            writer.flush();
            writer.close();
        }
    }

    private String findParent(String codeLine){
        String[] anExtends = codeLine.split("extends");
        String[] splits = anExtends[1].split(" ");
        for (String name:splits){
            if(!name.trim().equals("")){
                return name;
            }
        }
        return "";
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(AutoReplaceClassParent.class.getCanonicalName());

        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
