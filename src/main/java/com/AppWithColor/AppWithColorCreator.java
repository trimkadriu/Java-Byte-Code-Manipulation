package com.AppWithColor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class AppWithColorCreator {

    private static String color = "KUQ";
    private static String outputPath = "c:\\temp";
    
    @SuppressWarnings("null")
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the COLOR you want to generate your application with:");
            //String color = br.readLine();
            System.out.print("\nEnter the path to save your application:");
            //String outputPath = br.readLine();
            if(color == null || color.isEmpty() || outputPath == null || outputPath.isEmpty()) {
                System.out.println("\n\nERROR:\n----------------------------------------\nYou entered incorrect data!\n----------------------------------------");
                return;
            }
            CtClass appWithColorClass = generateAppWithColor(color);
            new JarCreator(appWithColorClass, outputPath, "AppWith-" + color + "-Color");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static CtClass generateAppWithColor(String color) throws Exception {
        CtClass appWithColor = ClassPool.getDefault().get("com.AppWithColor.AppWithColorMain");
        
        CtField fieldColor = CtField.make("private static java.lang.String color =\""+ color + "\";", appWithColor);
        appWithColor.addField(fieldColor);
        appWithColor.rebuildClassFile();
        
        CtMethod methodGetColor = CtNewMethod.make("public static java.lang.String getColor(){return color;}", appWithColor);
        appWithColor.addMethod(methodGetColor);
        appWithColor.rebuildClassFile();
        
        CtMethod methodMain = CtNewMethod.make("public static void main(java.lang.String[] args) { System.out.println(getColor()); }", appWithColor);
        appWithColor.addMethod(methodMain);
        appWithColor.toClass();
        
        return appWithColor;
    }

}