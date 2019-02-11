/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.util.effect;

import feuille.util.effect.Basic.ASS.*;
import feuille.util.effect.Basic.SSA.*;
import feuille.util.effect.Extended.*;
import feuille.util.effect.VsFilterMod.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * https://web.archive.org/web/20090227113602/http://snippets.dzone.com:80/posts/show/4831
 * @author util2
 */
public class FxClassFinder {
    
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static List<AFx> getEffectsFromPackage(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        
        List<AFx> effects = new ArrayList<>();
        for(Class c : classes){
            String found = c.getTypeName().substring(c.getTypeName().lastIndexOf(".") + 1);
            switch(found){
                case "AlignmentInSSA":
                    effects.add((AlignmentInSSA)c.cast(new AlignmentInSSA()));
                    break;
                case "Bold":
                    effects.add((Bold)c.cast(new Bold()));
                    break;
                case "FontEncoding":
                    effects.add((FontEncoding)c.cast(new FontEncoding()));
                    break;
                case "FontName":
                    effects.add((FontName)c.cast(new FontName()));
                    break;
                case "FontSize":
                    effects.add((FontSize)c.cast(new FontSize()));
                    break;
                case "Italic":
                    effects.add((Italic)c.cast(new Italic()));
                    break;
                case "KaraokeSimple":
                    effects.add((KaraokeSimple)c.cast(new KaraokeSimple()));
                    break;
                case "Reset":
                    effects.add((Reset)c.cast(new Reset()));
                    break;
                case "AlignmentInASS":
                    effects.add((AlignmentInASS)c.cast(new AlignmentInASS()));
                    break;
                case "Animation":
                    effects.add((Animation)c.cast(new Animation()));
                    break;
                case "BlurEdge":
                    effects.add((BlurEdge)c.cast(new BlurEdge()));
                    break;
                case "Border":
                    effects.add((Border)c.cast(new Border()));
                    break;
                case "BorderAlpha":
                    effects.add((BorderAlpha)c.cast(new BorderAlpha()));
                    break;
                case "BorderColor":
                    effects.add((BorderColor)c.cast(new BorderColor()));
                    break;
                case "FadingComplex":
                    effects.add((FadingComplex)c.cast(new FadingComplex()));
                    break;
                case "FadingSimple":
                    effects.add((FadingSimple)c.cast(new FadingSimple()));
                    break;
                case "FontRotationX":
                    effects.add((FontRotationX)c.cast(new FontRotationX()));
                    break;
                case "FontRotationY":
                    effects.add((FontRotationY)c.cast(new FontRotationY()));
                    break;
                case "FontRotationZ":
                    effects.add((FontRotationZ)c.cast(new FontRotationZ()));
                    break;
                case "FontScaleX":
                    effects.add((FontScaleX)c.cast(new FontScaleX()));
                    break;
                case "FontScaleY":
                    effects.add((FontScaleY)c.cast(new FontScaleY()));
                    break;
                case "FontSpacing":
                    effects.add((FontSpacing)c.cast(new FontSpacing()));
                    break;
                case "GeneralAlpha":
                    effects.add((GeneralAlpha)c.cast(new GeneralAlpha()));
                    break;
                case "KaraokeAlpha":
                    effects.add((KaraokeAlpha)c.cast(new KaraokeAlpha()));
                    break;
                case "KaraokeColor":
                    effects.add((KaraokeColor)c.cast(new KaraokeColor()));
                    break;
                case "KaraokeFill":
                    effects.add((KaraokeFill)c.cast(new KaraokeFill()));
                    break;
                case "KaraokeOutline":
                    effects.add((KaraokeOutline)c.cast(new KaraokeOutline()));
                    break;
                case "Movement":
                    effects.add((Movement)c.cast(new Movement()));
                    break;
                case "NormalClip":
                    effects.add((NormalClip)c.cast(new NormalClip()));
                    break;
                case "Origin":
                    effects.add((Origin)c.cast(new Origin()));
                    break;
                case "Position":
                    effects.add((Position)c.cast(new Position()));
                    break;
                case "Shadow":
                    effects.add((Shadow)c.cast(new Shadow()));
                    break;
                case "ShadowAlpha":
                    effects.add((ShadowAlpha)c.cast(new ShadowAlpha()));
                    break;
                case "ShadowColor":
                    effects.add((ShadowColor)c.cast(new ShadowColor()));
                    break;
                case "StrikeOut":
                    effects.add((StrikeOut)c.cast(new StrikeOut()));
                    break;
                case "TextAlpha":
                    effects.add((TextAlpha)c.cast(new TextAlpha()));
                    break;
                case "TextColor":
                    effects.add((TextColor)c.cast(new TextColor()));
                    break;
                case "Underline":
                    effects.add((Underline)c.cast(new Underline()));
                    break;
                case "VectorClip":
                    effects.add((VectorClip)c.cast(new VectorClip()));
                    break;
                case "WrappingStyle":
                    effects.add((WrappingStyle)c.cast(new WrappingStyle()));
                    break;
                case "Blur":
                    effects.add((Blur)c.cast(new Blur()));
                    break;
                case "InvisibleNormalClip":
                    effects.add((InvisibleNormalClip)c.cast(new InvisibleNormalClip()));
                    break;
                case "InvisibleVectorClip":
                    effects.add((InvisibleVectorClip)c.cast(new InvisibleVectorClip()));
                    break;
                case "ShearX":
                    effects.add((ShearX)c.cast(new ShearX()));
                    break;
                case "ShearY":
                    effects.add((ShearY)c.cast(new ShearY()));
                    break;
                case "XBorder":
                    effects.add((XBorder)c.cast(new XBorder()));
                    break;
                case "XShadow":
                    effects.add((XShadow)c.cast(new XShadow()));
                    break;
                case "YBorder":
                    effects.add((YBorder)c.cast(new YBorder()));
                    break;
                case "YShadow":
                    effects.add((YShadow)c.cast(new YShadow()));
                    break;
                case "BaselineObliquity":
                    effects.add((BaselineObliquity)c.cast(new BaselineObliquity()));
                    break;
                case "BorderAlphaGradient":
                    effects.add((BorderAlphaGradient)c.cast(new BorderAlphaGradient()));
                    break;
                case "BorderColorGradient":
                    effects.add((BorderColorGradient)c.cast(new BorderColorGradient()));
                    break;
                case "BorderImagePaint":
                    effects.add((BorderImagePaint)c.cast(new BorderImagePaint()));
                    break;
                case "BoundariesDeforming":
                    effects.add((BoundariesDeforming)c.cast(new BoundariesDeforming()));
                    break;
                case "BoundariesDeformingX":
                    effects.add((BoundariesDeformingX)c.cast(new BoundariesDeformingX()));
                    break;
                case "BoundariesDeformingY":
                    effects.add((BoundariesDeformingY)c.cast(new BoundariesDeformingY()));
                    break;
                case "BoundariesDeformingZ":
                    effects.add((BoundariesDeformingZ)c.cast(new BoundariesDeformingZ()));
                    break;
                case "CubicMovement":
                    effects.add((CubicMovement)c.cast(new CubicMovement()));
                    break;
                case "Distortion":
                    effects.add((Distortion)c.cast(new Distortion()));
                    break;
                case "FontScale":
                    effects.add((FontScale)c.cast(new FontScale()));
                    break;
                case "KaraokeAlphaGradient":
                    effects.add((KaraokeAlphaGradient)c.cast(new KaraokeAlphaGradient()));
                    break;
                case "KaraokeColorGradient":
                    effects.add((KaraokeColorGradient)c.cast(new KaraokeColorGradient()));
                    break;
                case "KaraokeImagePaint":
                    effects.add((KaraokeImagePaint)c.cast(new KaraokeImagePaint()));
                    break;
                case "Leading":
                    effects.add((Leading)c.cast(new Leading()));
                    break;
                case "PolarMovement":
                    effects.add((PolarMovement)c.cast(new PolarMovement()));
                    break;
                case "QuadraticMovement":
                    effects.add((QuadraticMovement)c.cast(new QuadraticMovement()));
                    break;
                case "ShadowAlphaGradient":
                    effects.add((ShadowAlphaGradient)c.cast(new ShadowAlphaGradient()));
                    break;
                case "ShadowColorGradient":
                    effects.add((ShadowColorGradient)c.cast(new ShadowColorGradient()));
                    break;
                case "ShadowImagePaint":
                    effects.add((ShadowImagePaint)c.cast(new ShadowImagePaint()));
                    break;
                case "Shaking":
                    effects.add((Shaking)c.cast(new Shaking()));
                    break;
                case "TextAlphaGradient":
                    effects.add((TextAlphaGradient)c.cast(new TextAlphaGradient()));
                    break;
                case "TextColorGradient":
                    effects.add((TextColorGradient)c.cast(new TextColorGradient()));
                    break;
                case "TextImagePaint":
                    effects.add((TextImagePaint)c.cast(new TextImagePaint()));
                    break;
                case "VectorMovementComplex":
                    effects.add((VectorMovementComplex)c.cast(new VectorMovementComplex()));
                    break;
                case "VectorMovementSimple":
                    effects.add((VectorMovementSimple)c.cast(new VectorMovementSimple()));
                    break;
                case "ZCoordinate":
                    effects.add((ZCoordinate)c.cast(new ZCoordinate()));
                    break;
                default:
                    break;
            }
        }
        return effects;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
