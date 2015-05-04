package com.AppWithColor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public class JarCreator {

    @SuppressWarnings("resource")
    public JarCreator(CtClass classToAdd, String outputPath, String outputFileName) 
            throws NotFoundException, CannotCompileException, IOException {
        // Write .class file to disk
        String className = AppWithColorMain.class.getSimpleName();
        classToAdd.writeFile(outputPath + File.separator + className);
        
        // Prepare JAR Manifest
        String packageName = this.getClass().getPackage().getName();
        File manifestFile = new File(outputPath + File.separator + className + File.separator + "META-INF" + File.separator + "MANIFEST.MF");
        manifestFile.getParentFile().mkdirs();
        manifestFile.createNewFile();
        FileWriter manifestFileWriter = new FileWriter(manifestFile.getAbsolutePath());
        BufferedWriter manifestBufferedWriter = new BufferedWriter(manifestFileWriter);
        manifestBufferedWriter.write("Manifest-Version: 1.0\nClass-Path: .\nMain-Class: " + packageName + "." + className + "\n\n");
        manifestBufferedWriter.close();
        
        /*Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, ".");
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, packageName + "." + className);*/
        
        // Write JAR to disk
        String packagePath = packageName.replace(".", File.separator) + File.separator;
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath + File.separator + outputFileName + ".jar");
        JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream);//, manifest);
        
        jarOutputStream.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        jarOutputStream.write(Files.readAllBytes(Paths.get(outputPath + File.separator
                + AppWithColorMain.class.getSimpleName() + File.separator + "META-INF" + File.separator + "MANIFEST.MF")));
        jarOutputStream.closeEntry();
        
        jarOutputStream.putNextEntry(new ZipEntry(packagePath + className + ".class"));
        jarOutputStream.write(Files.readAllBytes(Paths.get(outputPath + File.separator
                + AppWithColorMain.class.getSimpleName() + File.separator + packagePath + className + ".class")));
        jarOutputStream.closeEntry();
        
        jarOutputStream.flush();
        jarOutputStream.close();
        fileOutputStream.close();
        
        // Delete .class file because now we have the JAR
        deleteDirectory(new File(outputPath + File.separator + className));
    }

    private boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }

}
