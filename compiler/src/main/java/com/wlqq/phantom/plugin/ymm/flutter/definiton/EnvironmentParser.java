package com.wlqq.phantom.plugin.ymm.flutter.definiton;

import javax.annotation.processing.RoundEnvironment;

/**
 * 环境变量解析器，解析注解的类
 */
public interface EnvironmentParser<T> {

    T parse(RoundEnvironment roundEnvironment);
}
