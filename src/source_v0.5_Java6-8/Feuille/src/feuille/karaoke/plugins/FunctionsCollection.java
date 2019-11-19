/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.plugins;

import feuille.karaoke.plugins.AssFunctionPlugin;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

/**
 *
 * @author Unknown User
 */
public class FunctionsCollection {
    
    private List<AssFunctionPlugin> lplug = new ArrayList<AssFunctionPlugin>();
    private File javaPluginsDir = null;
    private File rubyPluginsDir = null;

    /** Create a new empty collection of functions */
    public FunctionsCollection(){
        //Nothing - just a simple call
    }

    /** Try to load inner functions, Java plugins and Ruby plugins */
    public void loadAll(){

    }
    
    /** Set up the directory path for javaPluginsDir variable */
    public boolean setJavaPluginsDirPath(String path){
        File f = new File(path);
        if(f.isDirectory()){
            javaPluginsDir = f;
            return true;
        }
        return false;
    }

    /** Add a function element. */
    public void addMember(AssFunctionPlugin name)
    throws UnsupportedOperationException, NullPointerException,
            IllegalArgumentException{
        if(name.equals(null)){
            //name = "";
        }
        if(lplug.contains(name)==false){
            lplug.add(name);
        }
    }

    /** Delete a function element. */
    public void deleteMember(AssFunctionPlugin afp)
    throws UnsupportedOperationException, NullPointerException{
        lplug.remove(afp);
    }

    /** Modify a function element. */
    public void modifyMember(AssFunctionPlugin oldAfp, AssFunctionPlugin newAfp)
    throws UnsupportedOperationException, NullPointerException,
            IllegalArgumentException{
        lplug.remove(oldAfp);
        lplug.add(newAfp);
    }

    /** Return a table of function elements */
    public AssFunctionPlugin[] getMembers(){
        AssFunctionPlugin[] s = lplug.toArray(new AssFunctionPlugin [0]);
        return s;
    }

    /** Get the length of the collection */
    public int getSize(){
        return lplug.size();
    }
    
    // Modified from code of page :
    // http://vincentlaine.developpez.com/tutoriel/java/plugins/
    public void initializeLoader() throws Exception{
		//On vérifie que la liste des plugins à charger à été initialisé
		if(this.javaPluginsDir == null 
                || this.javaPluginsDir.listFiles().length == 0 ){
			throw new Exception("No java files found !");
		}
		
//		//Pour eviter le double chargement des plugins
//		if(this.classIntPlugins.size() != 0 || this.classStringPlugins.size() != 0 ){
//			return ;
//		}
		
        //Pour charger le .jar en memoire
		URLClassLoader loader;
		//Pour la comparaison de chaines
		String tmp = "";
		//Pour le contenu de l'archive jar
		Enumeration enumeration;
		//Pour déterminé quels sont les interfaces implémentées
		Class tmpClass = null;
        
        for(File nf : javaPluginsDir.listFiles()){
            try{
                if(nf.exists()){

                    //Get the URL path of this file.
                    URL u = nf.toURI().toURL();

                    //Loader for a jar outside a CLASSPATH.
                    loader = new URLClassLoader(new URL[] {u});

                    //Load the jar in memory.
                    JarFile jar = new JarFile(nf.getAbsolutePath());

                    //Get the Jarfile content.
                    enumeration = jar.entries();

                    while(enumeration.hasMoreElements()){

                        tmp = enumeration.nextElement().toString();

                        //On vérifie que le fichier courant est un .class (et pas un fichier d'informations du jar )
                        if(tmp.length() > 6 && tmp.substring(tmp.length()-6).compareTo(".class") == 0) {

                            tmp = tmp.substring(0,tmp.length()-6);
                            tmp = tmp.replaceAll("/",".");

                            tmpClass = Class.forName(tmp ,true,loader);

                            for(int i = 0 ; i < tmpClass.getInterfaces().length; i ++ ){

                                //Une classe ne doit pas appartenir à deux catégories de plugins différents.
                                //Si tel est le cas on ne la place que dans la catégorie de la première interface correct
                                // trouvée
                                if(tmpClass.getInterfaces()[i].getName().toString().equals("assfxmaker.lib.plugins.AssFunctionPlugin") ) {
                                    addMember((AssFunctionPlugin)tmpClass.newInstance());
                                }
        //						else {
        //							if( tmpClass.getInterfaces()[i].getName().toString().equals("tutoPlugins.plugins.IntPlugins") ) {
        //								this.classIntPlugins.add(tmpClass);
        //							}
        //						}
                            }

                        }
                    }

                }
            }catch(Exception exc){
                System.out.println(nf.getName()+" is not a valid file.");
            }
            
        }		
		
    }

}
