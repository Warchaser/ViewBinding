package com.warchaser.libcommonutils;

import java.util.Objects;

public class PackageUtil {

    private PackageUtil(){

    }

    public static String getSimpleClassName(Object object){
        if(object == null){
            return "";
        }
        Class clazz = object.getClass();
        String str1 = clazz.getName().replace("$", ".");
        String str2 = str1.replace(clazz.getPackage().getName(), "") + ".";

        return str2.substring(1);
    }

}
