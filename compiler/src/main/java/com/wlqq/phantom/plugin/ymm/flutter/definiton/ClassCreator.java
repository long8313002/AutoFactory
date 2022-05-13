package com.wlqq.phantom.plugin.ymm.flutter.definiton;

import javax.annotation.processing.Filer;

/**
 * 类构造者吗，负责生成class文件
 */
public interface ClassCreator {

    boolean create(Filer filer);
}
