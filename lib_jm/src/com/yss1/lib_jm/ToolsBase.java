/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ys
 */
public class ToolsBase{
  
    private static Vector3f scale = new Vector3f();
    private static Vector3f tr = new Vector3f();
    private static Quaternion rot = new Quaternion();
    public final static float[] singleTex = {0,0,1,0,0,1,1,1};
    public final static float[] zeroTex = {0,0,0,0,0,0,0,0};
    public final static float[] normals4 = {0,0,1,0,0,1,0,0,1,0,0,1};
    public final static float[] normals8 = {0,0,1,0,0,1,0,0,1,0,0,1,0,0,-1,0,0,-1,0,0,-1,0,0,-1};
    public final static short[] indexes1p = {2, 0, 1, 1, 3, 2};
    public final static short[] indexes2p = {2, 0, 1, 1, 3, 2, 7, 5, 4, 4, 6, 7};

    public static VectorsPool vPool= new VectorsPool();
    public static Waiters waiters=new Waiters();
    public static InputEvents inputEvents = new InputEvents();
    
    public static Transform getLocalTransformToPreserveWorldTransform(Transform PT, Transform CT) {
        //располагает child в парент без изменения положения      
        //PT parent world transform, CT child world transform     
        scale.set(CT.getScale().divide(PT.getScale()));
        rot.set(PT.getRotation().inverse().multLocal(CT.getRotation()));
        tr.set(PT.getRotation().inverse().multLocal(CT.getTranslation().subtract(PT.getTranslation())).divideLocal(PT.getScale()));
        return new Transform(tr, rot, scale);

    }

    public static Mesh make2planes(float w, float h, float t, Vector2f[] tx, Vector2f[] tx2) {//полуширина полувысота полутолщина текстурные координаты, лайтмэп текстура
        Vector3f[] vertices = new Vector3f[8];

        vertices[0] = vPool.getV3(-w, -h,  t);
        vertices[1] = vPool.getV3( w, -h,  t);
        vertices[2] = vPool.getV3(-w,  h,  t);
        vertices[3] = vPool.getV3( w,  h,  t);

        vertices[4] = vPool.getV3(-w, -h, -t);
        vertices[5] = vPool.getV3( w, -h, -t);
        vertices[6] = vPool.getV3(-w,  h, -t);
        vertices[7] = vPool.getV3( w,  h, -t);

//        Vector3f[] normals = new Vector3f[8];
//        for (int i = 0; i < 4; i++) {
//            normals[i] = vPool.getV3(0, 0, 1);
//        }
//        for (int i = 4; i < normals.length; i++) {
//            normals[i] = vPool.getV3(0, 0, -1);
//        }

        
        
                
        Mesh Msh = new Mesh();
        Msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        if (tx != null) {
            Msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tx));
        }
        if (tx2 != null) {
            Msh.setBuffer(VertexBuffer.Type.TexCoord2, 2, BufferUtils.createFloatBuffer(tx2));
        }
        Msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals8));
        Msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes2p));
        Msh.updateBound();
        for (int i = 0; i < vertices.length; i++) {
            vPool.freeV3(vertices[i]);
        }
//        for (int i = 0; i < normals.length; i++) {
//            vPool.freeV3(normals[i]);
//        }
        return Msh;
    }

    public static Geometry[] make_framed_geometries(float w, float h, float t, float texH, String name, float scW, float scH) {//полуширина полувысота полутолщина текстурные угол,край, середина
        Geometry[] gmt = new Geometry[9];
        Mesh msh;
        //ap.VPool.getV3(0,0,1);
        Vector3f[] vertices = new Vector3f[4];
        //float tH=texH;
        //float pxH=ap.getScrH();

        float pxOnUnitH = scH / SettBase.deskHh / 2f;//количество пикселов в 1 GL единице по высоте
        float pxOnUnitW = scW / SettBase.deskHw / 2f;//количество пикселов в 1 GL единице по ширине

        
        
        float H0 = texH / pxOnUnitH;//количество GL единиц в единичной текстуре
        float W0 = texH / pxOnUnitW;


        float xrpt = 2f * w / W0 - 2f;
        float yrpt = 2f * h / H0 - 2f;
        xrpt=Math.round(xrpt);
        yrpt=Math.round(yrpt);
        // System.out.println("h="+h+ " w="+w+ " H0="+H0+ " W0="+W0+ " yrpt="+yrpt+" xrpt="+xrpt);
        //левый верхний
        vertices[0] = vPool.getV3(-w, h - H0, t);
        vertices[1] = vPool.getV3(-w + W0, h - H0, t);
        vertices[2] = vPool.getV3(-w, h, t);
        vertices[3] = vPool.getV3(-w + W0, h, t);


//        Vector3f[] normals = new Vector3f[4];
//        for (int i = 0; i < 4; i++) {
//            normals[i] = vPool.getV3(0, 0, 1);
//        }
        //for (int i=4;i<normals.length;i++) normals[i]=vPool.getV3(0,0,-1);
        Vector2f[] tex = new Vector2f[4];
        tex[0] = vPool.getV2(0, 0);
        tex[1] = vPool.getV2(1, 0);
        tex[2] = vPool.getV2(0, 1);
        tex[3] = vPool.getV2(1, 1);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        gmt[0] = new Geometry(name + "_LU", msh);

        //верхняя полоса
        vertices[0].set(-w + W0, h - H0, t);
        vertices[1].set(w - W0, h - H0, t);
        vertices[2].set(-w + W0, h, t);
        vertices[3].set(w - W0, h, t);

        tex[0].set(0, 0);
        tex[1].set(1, 0);
        tex[2].set(0, 1);
        tex[3].set(1, 1);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        msh.scaleTextureCoordinates(new Vector2f(xrpt, 1));
        gmt[1] = new Geometry(name + "_UP", msh);

        //правый верхний угол
        vertices[0].set(w - W0, h - H0, t);
        vertices[1].set(w, h - H0, t);
        vertices[2].set(w - W0, h, t);
        vertices[3].set(w, h, t);

        tex[0].set(1, 0);
        tex[1].set(1, 1);
        tex[2].set(0, 0);
        tex[3].set(0, 1);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        gmt[2] = new Geometry(name + "_RU", msh);

        //левая полоса
        vertices[0].set(-w, -h + H0, t);
        vertices[1].set(-w + W0, -h + H0, t);
        vertices[2].set(-w, h - H0, t);
        vertices[3].set(-w + W0, h - H0, t);

        tex[0].set(0, 1);
        tex[1].set(0, 0);
        tex[2].set(1, 1);
        tex[3].set(1, 0);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        msh.scaleTextureCoordinates(new Vector2f(yrpt, 1));
        gmt[3] = new Geometry(name + "_LP", msh);

        //центр
        vertices[0].set(-w + W0, -h + H0, t);
        vertices[1].set(w - W0, -h + H0, t);
        vertices[2].set(-w + W0, h - H0, t);
        vertices[3].set(w - W0, h - H0, t);

        tex[0].set(0, 0);
        tex[1].set(1, 0);
        tex[2].set(0, 1);
        tex[3].set(1, 1);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        msh.scaleTextureCoordinates(new Vector2f(xrpt, yrpt));
        gmt[4] = new Geometry(name, msh);

        //правая полоса
        vertices[0].set(w - W0, -h + H0, t);
        vertices[1].set(w, -h + H0, t);
        vertices[2].set(w - W0, h - H0, t);
        vertices[3].set(w, h - H0, t);

        tex[0].set(1, 0);
        tex[1].set(1, 1);
        tex[2].set(0, 0);
        tex[3].set(0, 1);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        msh.scaleTextureCoordinates(new Vector2f(yrpt, 1));
        gmt[5] = new Geometry(name + "_RP", msh);

        //левый нижний угол
        vertices[0].set(-w, -h, t);
        vertices[1].set(-w + W0, -h, t);
        vertices[2].set(-w, -h + H0, t);
        vertices[3].set(-w + W0, -h + H0, t);

        tex[0].set(0, 1);
        tex[1].set(0, 0);
        tex[2].set(1, 1);
        tex[3].set(1, 0);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        gmt[6] = new Geometry(name + "_LD", msh);

        //нижняя полоса
        vertices[0].set(-w + W0, -h, t);
        vertices[1].set(w - W0, -h, t);
        vertices[2].set(-w + W0, -h + H0, t);
        vertices[3].set(w - W0, -h + H0, t);

        tex[0].set(1, 1);
        tex[1].set(0, 1);
        tex[2].set(1, 0);
        tex[3].set(0, 0);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        msh.scaleTextureCoordinates(new Vector2f(xrpt, 1));
        gmt[7] = new Geometry(name + "_DP", msh);

        //правый нижний угол
        vertices[0].set(w - W0, -h, t);
        vertices[1].set(w, -h, t);
        vertices[2].set(w - W0, -h + H0, t);
        vertices[3].set(w, -h + H0, t);

        tex[0].set(1, 1);
        tex[1].set(0, 1);
        tex[2].set(1, 0);
        tex[3].set(0, 0);

        msh = new Mesh();
        msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        msh.updateBound();
        gmt[8] = new Geometry(name + "_RD", msh);

        for (int i = 0; i < vertices.length; i++) {
            vPool.freeV3(vertices[i]);
        }
//        for (int i = 0; i < normals.length; i++) {
//            vPool.freeV3(normals[i]);
//        }
        for (int i = 0; i < tex.length; i++) {
            vPool.freeV2(tex[i]);
        }
        return gmt;
    }

    public static Mesh makePlane(float w, float h, Vector2f[] tx, Vector2f[] tx2) {//полуширина полувысота полутолщина текстурные координаты, лайтмэп текстура

        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = vPool.getV3(-w, -h, 0);
        vertices[1] = vPool.getV3(w, -h, 0);
        vertices[2] = vPool.getV3(-w, h, 0);
        vertices[3] = vPool.getV3(w, h, 0);

//        Vector3f[] normals = new Vector3f[4];
//        for (int i = 0; i < 4; i++) {
//            normals[i] = vPool.getV3(0, 0, 1);
//        }

        Mesh Msh = new Mesh();
        Msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        if (tx != null) {
            Msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tx));
        }
        if (tx2 != null) {
            Msh.setBuffer(VertexBuffer.Type.TexCoord2, 2, BufferUtils.createFloatBuffer(tx2));
        }
        Msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals4));
        Msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes1p));
        Msh.updateBound();

        for (int i = 0; i < vertices.length; i++) {
            vPool.freeV3(vertices[i]);
        }
//        for (int i = 0; i < normals.length; i++) {
//            vPool.freeV3(normals[i]);
//        }

        return Msh;
    }
    
    public static Mesh makeSimplePlane(float w, float h, float[] tx) {
        //полуширина полувысота полутолщина текстурные координаты
        Mesh Msh = new Mesh();
        Msh.setBuffer(VertexBuffer.Type.Position, 3,new float[]{ -w, -h, 0, w, -h, 0, -w, h, 0, w, h, 0});
        //Vector3d [] 
        Msh.setBuffer(VertexBuffer.Type.TexCoord, 2, tx);
        Msh.setBuffer(VertexBuffer.Type.Normal, 3, normals4);
        Msh.setBuffer(VertexBuffer.Type.Index, 3, indexes1p);
        Msh.updateBound();
        //Msh.setMode(Mesh.Mode.Triangles);
        Msh.setStatic();
        return Msh;
    }

    public static Mesh makeSimple2planes(float w, float h, float t, float[] tx) {
        Mesh Msh = new Mesh();
        Msh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(new float[]{
            -w, -h,  t,
             w, -h,  t,
            -w,  h,  t,
             w,  h,  t,
            -w, -h, -t,
             w, -h, -t,
            -w,  h, -t,
             w,  h, -t}));
        Msh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tx));
        Msh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals8));
        Msh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createShortBuffer(indexes2p));
        Msh.updateBound();
        Msh.setStatic();
        return Msh;
    }
     
    
    
    private static Random rnd = new Random((new Date()).getTime());

    public static float fluctuate(float DELTA) {//дает результат в диапазоне -DELTA..+DELTA
        return DELTA * (2f * rnd.nextFloat() - 1);
    }

    public static int rand(int max) {//дает результат в диапазоне [0..max]
        return rnd.nextInt(max + 1);
    }

    public static float rand(float max) {//дает результат в диапазоне [0..max]
        return max*rnd.nextFloat();
    }
    
    public static boolean rand() {
        return rnd.nextBoolean();
    }

    public static Transform getTRfromStr(String s) {
        Transform res = new Transform();
        ArrayList<Float> fa = new ArrayList<Float>();
        Pattern p = Pattern.compile("([E\\-\\d\\.]{2,})");
        Matcher m = p.matcher(s);
        try {
            while (m.find()) {
                fa.add(Float.parseFloat(m.group()));
            }
        } catch (Exception Exx) {
            return null;
        }
        if (fa.size() != 10) {
            return null;
        }
        res.setTranslation(fa.get(0), fa.get(1), fa.get(2));
        res.setRotation(new Quaternion().set(fa.get(3), fa.get(4), fa.get(5), fa.get(6)));
        res.setScale(fa.get(7), fa.get(8), fa.get(9));
        return res;
    }

    public static String getStrWithSpaces(String orig, int lgh) {
        //если длинее режим, короче дополняем пробелами
        StringBuilder sb = new StringBuilder(orig.trim());
        if (sb.length() > lgh) {
            return sb.substring(0, lgh - 1);
        }
        while (sb.length() < lgh) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String getStrWithSpacesLeft(String orig, int lgh) {
        //если длинее режим, короче дополняем пробелами
        StringBuilder sb = new StringBuilder(orig.trim());
        if (sb.length() > lgh) {
            return sb.substring(0, lgh - 1);
        }
        while (sb.length() < lgh) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }

    public static int getSuit(String nm) {
        if (nm == null || nm.length() > 3 || nm.length() < 2) {
            return 0;
        }
        switch (nm.toUpperCase().charAt(nm.length() - 1)) {
            case 'H':
                return 4;
            case 'D':
                return 3;
            case 'C':
                return 2;
            case 'S':
                return 1;
        }
        return 0;
    }

    public static int getRank(String nm) {
        if (nm == null || nm.length() > 3 || nm.length() < 2) {
            return 0;
        }
        String s1 = nm.toUpperCase().substring(0, nm.length() - 1);
        int i = 0;
        try {
            i = Integer.parseInt(s1);
            return i;
        } catch (NumberFormatException Ex) {
            switch (s1.charAt(0)) {
                case 'J':
                    return 11;
                case 'Q':
                    return 12;
                case 'K':
                    return 13;
                case 'A':
                    return 14;
                case 'R'://jokeR бывают мастей С(черный) и D(красный)
                    return 15;
            }
        }
        return 0;
    }

    public static String getTextBase(int id) {
        switch (id) {

            case 50://rematch title
                switch (SettBase.lang) {
                    case 1:
                        return "Поступило приглашение";
                    case 2:
                        return "Invitation received";
                }
                break;
            case 51://rematch answer
                switch (SettBase.lang) {
                    case 1:
                        return "%s предлагает сыграть в %s. Принять приглашение?";
                    case 2:
                        return "%s offers to play %s. Accept his invitation?";
                }
                break;
                 case 52://rematch title
                switch (SettBase.lang) {
                    case 1:
                        return "Ошибка подключения";
                    case 2:
                        return "Connection error";
                }
                break;
            case 53://rematch answer
                switch (SettBase.lang) {
                    case 1:
                        return "Нужно подключиться к Google Play.\nСмотри на джойстик ^";
                    case 2:
                        return "Connect to Google Play first. Click Joystick ^";
                }
                break;
                
            case 402:
                switch (SettBase.lang) {
                    case 1:
                        return "Два";
                    case 2:
                        return "Two";
                }
                break;
            case 403:
                switch (SettBase.lang) {
                    case 1:
                        return "Три";
                    case 2:
                        return "Three";
                }
                break;

            case 404:
                switch (SettBase.lang) {
                    case 1:
                        return "Четыре";
                    case 2:
                        return "Four";
                }
                break;
            case 405:
                switch (SettBase.lang) {
                    case 1:
                        return "Пять";
                    case 2:
                        return "Five";
                }
                break;
            case 406:
                switch (SettBase.lang) {
                    case 1:
                        return "Шесть";
                    case 2:
                        return "Six";
                }
                break;
            case 407:
                switch (SettBase.lang) {
                    case 1:
                        return "Семь";
                    case 2:
                        return "Seven";
                }
                break;
            case 408:
                switch (SettBase.lang) {
                    case 1:
                        return "Восемь";
                    case 2:
                        return "Eigth";
                }
                break;
            case 409:
                switch (SettBase.lang) {
                    case 1:
                        return "Девять";
                    case 2:
                        return "Nine";
                }
                break;
            case 410:
                switch (SettBase.lang) {
                    case 1:
                        return "Десять";
                    case 2:
                        return "Ten";
                }
                break;
            case 411:
                switch (SettBase.lang) {
                    case 1:
                        return "Валет";
                    case 2:
                        return "Jack";
                }
                break;
            case 412:
                switch (SettBase.lang) {
                    case 1:
                        return "Дама";
                    case 2:
                        return "Queen";
                }
                break;
            case 413:
                switch (SettBase.lang) {
                    case 1:
                        return "Король";
                    case 2:
                        return "King";
                }
                break;
            case 414:
                switch (SettBase.lang) {
                    case 1:
                        return "Туз";
                    case 2:
                        return "Асе";
                }
                break;
            case 415:
                switch (SettBase.lang) {
                    case 1:
                        return "Джокер";
                    case 2:
                        return "Joker";
                }
                break;

            case 451://масти в сочетании с номиналом (род. падеж)
                switch (SettBase.lang) {
                    case 1:
                        return "пик";
                    case 2:
                        return "of Spades";
                }
                break;
            case 452:
                switch (SettBase.lang) {
                    case 1:
                        return "крест";
                    case 2:
                        return "of Clubs";
                }
                break;
            case 453:
                switch (SettBase.lang) {
                    case 1:
                        return "бубей";
                    case 2:
                        return "of Diamonds";
                }
                break;
            case 454:
                switch (SettBase.lang) {
                    case 1:
                        return "червей";
                    case 2:
                        return "of Hearts";
                }
                break;
            case 461://масти просто
                switch (SettBase.lang) {
                    case 1:
                        return "Пики";
                    case 2:
                        return "Spades";
                }
                break;
            case 462:
                switch (SettBase.lang) {
                    case 1:
                        return "Крести";
                    case 2:
                        return "Clubs";
                }
                break;
            case 463:
                switch (SettBase.lang) {
                    case 1:
                        return "Буби";
                    case 2:
                        return "Diamonds";
                }
                break;


            case 601:
                switch (SettBase.lang) {
                    case 1:
                        return "Применить";
                    case 2:
                        return "Apply";
                }
                break;
            case 602:
                switch (SettBase.lang) {
                    case 1:
                        return "Отмена";
                    case 2:
                        return "Cancel";
                }
                break;
        case 603:
                switch (SettBase.lang) {
                    case 1:
                        return "Принять";
                    case 2:
                        return "Accept";
                }
                break;
            case 604:
                switch (SettBase.lang) {
                    case 1:
                        return "Отклонить";
                    case 2:
                        return "Dismiss";
                }
                break;
                
            case 610://диалог ввода
                switch (SettBase.lang) {
                    case 1:
                        return "Мое имя";
                    case 2:
                        return "My name is";
                }
                break;
            case 611://диалог ввода
                switch (SettBase.lang) {
                    case 1:
                        return "Номер игры";
                    case 2:
                        return "Game Number is";
                }

        }
        return "";
    }
    
}
