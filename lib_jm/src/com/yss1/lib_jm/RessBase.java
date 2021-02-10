/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author ys
 */
public class RessBase {

    protected AssetManager ap;
    protected HashMap<String, BitmapFont> fnt = new HashMap<>();
    ;
    protected HashMap<String, Texture> tex = new HashMap<>();
    protected HashMap<String, Material> mat = new HashMap<>();
    private int TX_H, TX_W;
    public ColorRGBA LightBlue = new ColorRGBA(0.5f, 0.5f, 1f, 1f);

    //protected boolean alredyLoaded;
    
    public RessBase(AssetManager a) {
        ap = a;
        initTextures();
        initMaterials();
        SettBase.deskHw = SettBase.deskHh * SettBase.getSCRatio();
     //   alredyLoaded=false;
    }

    public void initFonts(HashMap<String, String> dict) {
        fnt.clear();
        for (Entry<String, String> ENT : dict.entrySet()) {
            fnt.put(ENT.getKey(), ap.loadFont("Interface/Fonts/" + ENT.getValue() + ".fnt"));
        }
    }

    public BitmapFont getFont(String key) {
        if (fnt.containsKey(key)) {
            return fnt.get(key);
        }
        return null;
    }

    private void initTextures() {//Сначала добавить свои дополнителные а потом вызвать этот родительский класс
        Texture TX;
        tex.put("Стол", ap.loadTexture("Textures/DESK.png"));
        TX = tex.get("Стол");
        TX.setWrap(Texture.WrapMode.Repeat);

        tex.put("Общая", ap.loadTexture("Textures/ALL.png"));
        TX = tex.get("Общая");
        TX_W = TX.getImage().getWidth();
        TX_H = TX.getImage().getHeight();

        tex.put("Spark", ap.loadTexture("Textures/SPARK.png"));

        tex.put("GP_Active", ap.loadTexture("Textures/gpA.png"));

        tex.put("GP_NotActive", ap.loadTexture("Textures/gpNA.png"));

        tex.put("Рамка0Центр", ap.loadTexture("Textures/RamkaC.png"));
        TX = tex.get("Рамка0Центр");
        TX.setWrap(Texture.WrapMode.Repeat);

        tex.put("Рамка0Край", ap.loadTexture("Textures/RamkaL.png"));
        TX = tex.get("Рамка0Край");
        TX.setWrap(Texture.WrapMode.Repeat);

        tex.put("Рамка0Угол", ap.loadTexture("Textures/RamkaU.png"));
        //TX=tex.get("Рамка0Угол");
        //TX.setWrap(Texture.WrapMode.Repeat);

        tex.put("Рамка1Центр", ap.loadTexture("Textures/RamkaC1.png"));
        TX = tex.get("Рамка1Центр");
        TX.setWrap(Texture.WrapMode.Repeat);

        tex.put("Рамка1Край", ap.loadTexture("Textures/RamkaL1.png"));
        TX = tex.get("Рамка1Край");
        TX.setWrap(Texture.WrapMode.Repeat);

        tex.put("Рамка1Угол", ap.loadTexture("Textures/RamkaU1.png"));
        //TX=tex.get("Рамка1Угол");
        //TX.setWrap(Texture.WrapMode.Repeat);

        initMoreTextures();

        for (Entry<String, Texture> ent : tex.entrySet()) {//это для всех
            ent.getValue().setMagFilter(Texture.MagFilter.Bilinear);
            ent.getValue().setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
        }

    }

    public Vector2f getTexDim4Material(String matKey) {// размеры текстуры в пикселях
        Vector2f res = new Vector2f(-1f, -1f);
        Material ma = mat.get(matKey);
        if (ma == null) {
            return res;
        }
        MatParam mp = ma.getParam("ColorMap");
        if (mp == null || mp.getValue() == null) {
            return res;
        }
        if (mp.getValue().getClass() != Texture2D.class) {
            return res;
        }
        Texture2D tX = (Texture2D) mp.getValue();
        res.x = tX.getImage().getWidth();
        res.y = tX.getImage().getHeight();
        return res;
    }

    public float getTexW2HMaterial(String matKey) {//отношение ширины текстуры к высоте
        Vector2f res = getTexDim4Material(matKey);
        return res.x / res.y;
    }

    protected void initMoreTextures() {
    }

    public Texture getTexture(String key) {
        if (tex.containsKey(key)) {
            return tex.get(key);
        }
        return null;
    }

    private void initMaterials() {
        mat.put("Общий", MakeMaterial(ColorRGBA.White, tex.get("Общая"), false));
        mat.put("ОбщийВыбран", MakeMaterial(SettBase.selClr, tex.get("Общая"), false));
        
        mat.put("ОбщийПлохо", MakeMaterial(SettBase.selClrBad, tex.get("Общая"), false));
        mat.put("ОбщийПрозрачный", MakeMaterial(ColorRGBA.White, tex.get("Общая"), true));
        mat.put("Стол", MakeMaterial(ColorRGBA.White, tex.get("Стол"), false));
        mat.put("СтолТемный", MakeMaterial(ColorRGBA.Gray, tex.get("Стол"), false));
        mat.put("Рамка0Центр", MakeMaterial(ColorRGBA.White, tex.get("Рамка0Центр"), false));
        mat.put("Рамка0Край", MakeMaterial(ColorRGBA.White, tex.get("Рамка0Край"), true));
        mat.put("Рамка0Угол", MakeMaterial(ColorRGBA.White, tex.get("Рамка0Угол"), true));
        mat.put("Spark", new Material(ap, "Common/MatDefs/Misc/Particle.j3md"));
        mat.get("Spark").setTexture("Texture", tex.get("Spark"));
        mat.put("Рамка1Центр", MakeMaterial(ColorRGBA.White, tex.get("Рамка1Центр"), false));
        mat.put("Рамка1Край", MakeMaterial(ColorRGBA.White, tex.get("Рамка1Край"), false));
        mat.put("Рамка1Угол", MakeMaterial(ColorRGBA.White, tex.get("Рамка1Угол"), true));
        mat.put("GP_NotActive", MakeTranslucentMaterial(ColorRGBA.White, tex.get("GP_NotActive")));
        mat.put("GP_Active", MakeTranslucentMaterial(ColorRGBA.White, tex.get("GP_Active")));
        initMoreMaterials();
    }

    protected void initMoreMaterials() {
    }

    public Material getMaterial(String key) {
        if (mat.containsKey(key)) {
            return mat.get(key);
        }
        throw new RuntimeException("Material " + key + " not exists!");
    }

    public Material getMaterial() {
        if (mat.containsKey("Общий")) {
            return mat.get("Общий");
        }
        throw new RuntimeException("Material Общий not exists!");
    }

    public Material getMaterial(boolean good) {
        if (good) {
            if (mat.containsKey("ОбщийВыбран")) {
                return mat.get("ОбщийВыбран");
            }
            throw new RuntimeException("Material ОбщийВыбран not exists!");
        } else {
            if (mat.containsKey("ОбщийПлохо")) {
                return mat.get("ОбщийПлохо");
            }
            throw new RuntimeException("Material ОбщийПлохо not exists!");
        }
    }

    protected Material MakeMaterial(ColorRGBA col, Texture T, boolean transp) {
        Material Mat = new Material(ap, "Common/MatDefs/Misc/Unshaded.j3md");
        Mat.setColor("Color", col);
        Mat.setTexture("ColorMap", T);
        if (transp) {
            Mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            Mat.setFloat("AlphaDiscardThreshold", 0.5f);
            Mat.getAdditionalRenderState().setAlphaFallOff(0.5f);
        }
        return Mat;
    }

    protected Material MakeTranslucentMaterial(ColorRGBA col, Texture T) {
        Material Mat = new Material(ap, "Common/MatDefs/Misc/Unshaded.j3md");
        Mat.setColor("Color", col);
        Mat.setTexture("ColorMap", T);
        Mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        return Mat;
    }

    /*
    ширина главной текстуры в пикселях
    */
    public int getTX_W() {
        return TX_W;
    }

    
    /*
    высота главной текстуры в пикселях
    */
    public int getTX_H() {
        return TX_H;
    }
    
    
    /*
    высота текстуры  S в пикселях
    */
    public int getTexH(String S) {
        if (tex.containsKey(S))
        {
        return tex.get(S).getImage().getHeight();
        }                
        return 0;        
    }

     /*
    ширина текстуры  S в пикселях
    */
    public int getTexW(String S) {
        if (tex.containsKey(S))
        {
        return tex.get(S).getImage().getWidth();
        }                
        return 0;        
    }
    
    
    
    /*
    текстурная координата Х для главной текстуры в при смещении в Х пикселов
    */
    public float getTextX(float X) {
        if (TX_W > 0) {
            return X / TX_W;
        }
        return 0;
    }
    
/*
    текстурная координата Y для главной текстуры в при смещении в Y пикселов сверху
    */
    public float getTextY(float Y) {
        if (TX_H > 0) {
            return (TX_H - Y) / TX_H;
        }
        return 0;
    }
    
    /*
    текстурная высота для главной текстуры при высоте H пикселов
    */
    public float getTextH(float H) {
        if (TX_H > 0) {
            return H/ TX_H;
        }
        return 0;
    }
}
