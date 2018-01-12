package classloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class MyClassLoader extends SecureClassLoader {

    private ArrayList<File> path;

    public ArrayList<Class> loadingJar() {
        ArrayList<Class> listclass= new ArrayList<Class>();
        File file = new File("./" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "plugins_basique" + File.separator + "plugins.jar");
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<JarEntry> e =jarFile.entries();

        URL[] urls = new URL[1];
        try {
            urls[0]= new URL(" file:///"+file.getPath());
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        URLClassLoader cl = URLClassLoader.newInstance(urls);
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            //
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            Class cla = null;
            try {
                cla = findClass(className);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

        }

        return listclass;

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassData(name);
        if(b == null) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) throws ClassNotFoundException {
        for (File fichier : path) {
            if (fichier.isDirectory()) {
                // On vérifie si la classe n'a pas été déjà chargée
                // Class<?> classLoaded = findLoadedClass(name);
                // if(classLoaded == null)
                try {
                    String nomPackage = name.replace(".", "/");
                    String nomRepertoire = fichier.getPath();
                    String nomClasse = nomRepertoire + nomPackage;
                    Path cheminFichier = Paths.get(nomRepertoire + "/"
                            + nomPackage + ".class");
                    byte[] fileData = Files.readAllBytes(cheminFichier);
                    return fileData;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(fichier.getPath().endsWith(".zip") ||  fichier.getPath().endsWith(".jar")) {
                try {
                    ZipFile zFile = new ZipFile(fichier);
                    Enumeration<? extends ZipEntry> entries = zFile.entries();
                    while(entries.hasMoreElements()){
                        ZipEntry entry = entries.nextElement();
                        if(entry.toString().endsWith(".class")) {
                            String nomPackage = fichier.getPath();
                            nomPackage = nomPackage.replace(fichier.getName(), "");
                            String nomFichier = entry.toString();
                            String nomClasse = nomFichier.replace("/", ".");
                            nomClasse = nomClasse.substring(0, nomClasse.lastIndexOf('.'));
                            if(nomClasse.equals(name)) {
                                Path cheminFichier = Paths.get(nomPackage + nomFichier);
                                byte[] fileData = Files.readAllBytes(cheminFichier);
                                return fileData;
                            }
                        }
                    }
                } catch (ZipException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }else {
                try {
                    ZipFile zFile = new ZipFile(fichier);
                    Enumeration<? extends ZipEntry> entries = zFile.entries();
                    while(entries.hasMoreElements()){
                        ZipEntry entry = entries.nextElement();
                        if(entry.toString().endsWith(".class")) {
                            String nomPackage = fichier.getPath();
                            nomPackage = nomPackage.replace(fichier.getName(), "");
                            String nomFichier = entry.toString();
                            String nomClasse = nomFichier.replace("/", ".");
                            nomClasse = nomClasse.substring(0, nomClasse.lastIndexOf('.'));
                            if(nomClasse.equals(name)) {
                                Path cheminFichier = Paths.get(nomPackage + nomFichier);
                                byte[] fileData = Files.readAllBytes(cheminFichier);
                                return fileData;
                            }
                        }
                    }
                } catch (ZipException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }
}