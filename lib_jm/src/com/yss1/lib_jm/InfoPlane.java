/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ys
 */
public class InfoPlane {
    private RessKeeper rKeeper;
    private Node nodePlane;
    private Geometry ge;
    private Geometry ge_pict;
    private boolean side;
    private Vector3f myPlace;
    BitmapText txt1, txt2;
    float Dx, Dy;
    final int MAX_TEX = 2;//столько массивов см выше;
    private Button3d target;
    List<FaceState> faces = new ArrayList<>();
    FaceState moreFace;
    String name;

    int texNo;

    public InfoPlane(RessKeeper a, String nm, float h, List<FaceState> fsList) {
        Dy = h;
        Dx = fsList.get(0).calcWidthForHeight(h);
        faces = fsList;
        rKeeper = a;
        name = nm;
        init();
    }

    public InfoPlane(RessKeeper a, String nm, float h, float w, List<FaceState> fsList) {//для подложек
        Dy = h;
        Dx = w;
        faces = fsList;
        rKeeper = a;
        name = nm;
        init();
    }

    public List<FaceState> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceState> faces) {
        this.faces = faces;
    }

    public void copyFaces(List<FaceState> faces) {
        if (this.faces == null) {
            this.faces = new ArrayList<>();
        }
        this.faces.clear();
        for (FaceState fS : faces) {
            this.faces.add(new FaceState(rKeeper).init(fS.matKey, fS.texCoord));
        }
    }

    private void init() {
        nodePlane = new Node(name + "_node");
        target = null;
        side = true;
        texNo = 0;
        ge = new Geometry(name, ToolsBase.makeSimplePlane(Dx, Dy, faces.get(texNo).texCoord));
        faces.get(texNo).applyMatTo(ge);
        nodePlane.attachChild(ge);
    }

    public void addTexture(String mKey, float[] texCoord) {
        FaceState fs = new FaceState(rKeeper);
        fs.init(mKey, texCoord);
        faces.add(fs);
    }

    public void applyTexture(int no) {
        if (no == texNo) {
            return;
        }
        if (no < 0 || no >= faces.size()) {
            throw new RuntimeException("InfoPlane.applyTexture Texture " + no + " not exists!");
        }
        texNo = no;
        faces.get(texNo).applyTo(ge);
    }


    public void setTarget(Button3d tgt) {
        target = tgt;
    }

    public Button3d getTarget() {
        return target;
    }

    public void setPictFromALL(float planeW, float planeH, float X, float Y, float L, float H, String mat) {

        if (ge_pict == null) {
            ge_pict = new Geometry(name + "_pict", ToolsBase.makeSimplePlane(planeW, planeH, ToolsBase.singleTex));
            ge_pict.move(0, 0, 0.005f);
        }

        FaceState fs = new FaceState(rKeeper).
                init(mat, new float[]{
                        X, Y,
                        X + L, Y,
                        X, Y + H,
                        X + L, Y + H});
        fs.applyTo(ge_pict);
        nodePlane.attachChild(ge_pict);

    }

    //передаем ТКСТУРНЫЕ  координаты и размеры
    public void setPictFromALL(float X, float Y, float L, float H, boolean selected, boolean transparent) {
        float pw = Dx / 2;
        float ph = Dy / 2;
        if (ge_pict == null) {
            ge_pict = new Geometry(name + "_pict", ToolsBase.makeSimplePlane(pw, ph, ToolsBase.singleTex));
            ge_pict.move(0, 0, 0.005f);
        }

        FaceState fs = new FaceState(rKeeper).
                init(selected ? "ОбщийВыбран" : transparent ? "ОбщийПрозрачный" : "Общий", new float[]{
                        X, Y,
                        X + L, Y,
                        X, Y + H,
                        X + L, Y + H});

        fs.applyTo(ge_pict);
        nodePlane.attachChild(ge_pict);
    }


    public void setPictCard(Card Cr) {
        //размер карты в текстурных координатах
        float Lx = SettBase.CARD_W / (float) rKeeper.getRess().getTX_W();
        float Ly = SettBase.CARD_H / (float) rKeeper.getRess().getTX_H();
        float pix2show = 36f;
        int No = 4 * (Cr.getRank() - 2) + Cr.getPictSuit();
        float NoCOL = (float) (No % 14);//столбец карты
        float NoROW = (float) (No / 14);//строка карты

        float showPixelsX = pix2show / (float) rKeeper.getRess().getTX_W();//ширина фрагмента в текстурных  координатах
        float showPixelsY = pix2show / (float) rKeeper.getRess().getTX_H();//высота фрагмента в текстурных  координатах

        // начальные координаты текстуры карты
        Lx *= NoCOL;
        Ly = 1f - NoROW * Ly;

        setPictFromALL(Lx, Ly - showPixelsY, showPixelsX, showPixelsY, true, false);
        if (txt1 != null) {
            if (nodePlane.hasChild(txt1)) nodePlane.detachChild(txt1);
        }
    }

    public String getName() {
        return ge.getName();
    }

    public float getDx() {
        return Dx;
    }

    public float getDy() {
        return Dy;
    }

    public void setMyPlace(float x, float y, float z) {
        //this.myPlace = myPlace;
        if (myPlace == null) myPlace = new Vector3f(x, y, z);
        else myPlace.set(x, y, z);
        nodePlane.setLocalTranslation(myPlace);
        nodePlane.updateGeometricState();
    }

    public Geometry getGe() {
        return ge;
    }

    public Node getNodePlane() {
        return nodePlane;
    }

    public Vector3f getMyPlace() {
        return myPlace;
    }


    public BitmapText reinitTxt(int txtNo, int fontNo, Rectangle rect, ColorRGBA color, BitmapFont.Align alignX, float size, float dx, float dy) {
        BitmapText BT = new BitmapText(fontNo == 1 ? rKeeper.getRess().getFont("Большой") :
                fontNo == 2 ? rKeeper.getRess().getFont("Веселый") :
                        rKeeper.getRess().getFont("Моно"));
        nodePlane.attachChild(BT);
        if (txtNo == 1) {
            if (txt1 != null) nodePlane.detachChild(txt1);
        } else {
            if (txt2 != null) nodePlane.detachChild(txt2);
        }
        BT.setName(ge.getName() + "_txt_" + txtNo);
        BT.setColor(color);
        if (rect == null) rect = new Rectangle(-Dx, Dy, Dx * 2, Dy * 2);
        BT.setBox(rect);
        BT.setAlignment(alignX);
        BT.setVerticalAlignment(BitmapFont.VAlign.Center);
        BT.setLineWrapMode(LineWrapMode.NoWrap);
        BT.setSize(size);
        if (txtNo == 1) txt1 = BT;
        else txt2 = BT;
        BT.setLocalTranslation(dx, dy, ge.getLocalTranslation().z + 0.02f);
        BT.setQueueBucket(RenderQueue.Bucket.Translucent);
        return BT;
    }

    private BitmapText initTxt(int txtN, int fontN, ColorRGBA cl, float s, float dy) {
        return reinitTxt(txtN, fontN, null, cl, BitmapFont.Align.Center, s, 0f, dy);
    }

    public void initText1(int NoFont, ColorRGBA cl, float s, float dy, float dx, Align AL) {
        txt1 = reinitTxt(1, NoFont, null, cl, AL, s, dx, dy);
    }

    public void initText1(int NoFont, ColorRGBA cl, float s, float dy) {
        txt1 = initTxt(1, NoFont, cl, s, dy);
    }

    public void initText2(int NoFont, ColorRGBA cl, float s, float dy) {
        txt2 = initTxt(2, NoFont, cl, s, dy);
    }

    public void setText1Color(ColorRGBA c) {
        if (txt1 != null) txt1.setColor(c);
    }

    public void setText1ColorA(ColorRGBA c, String RX) {
        if (txt1 != null) txt1.setColor(RX, c);
    }

    public void setText2Color(ColorRGBA c) {
        if (txt2 != null) txt2.setColor(c);
    }

    public void setText1(String text) {
        if (txt1 == null) txt1 = initTxt(1, 1, ColorRGBA.Blue, 0.7f * Dy, Dy / 3f);

        if (ge_pict != null) {
            if (nodePlane.hasChild(ge_pict)) nodePlane.detachChild(ge_pict);
        }
        txt1.setText(text);
        if (!nodePlane.hasChild(txt1)) nodePlane.attachChild(txt1);
        //txt1.updateModelBound();
    }

    public void setText2(String text) {
        if (txt2 == null) txt2 = initTxt(2, 2, ColorRGBA.Red, 0.7f * Dy, -Dy / 1.6f);
        txt2.setText(text);
        //txt2.updateModelBound();
    }

    public void setTextAndColor(String text, ColorRGBA cl) {
        if (txt1 == null) txt1 = initTxt(1, 1, cl, 0.7f * Dy, Dy / 3);

        if (ge_pict != null) {
            if (nodePlane.hasChild(ge_pict)) nodePlane.detachChild(ge_pict);
        }
        if (!txt1.getText().equals(text)) txt1.setText(text);
        if (!txt1.getColor().equals(cl)) txt1.setColor(cl);
        if (!nodePlane.hasChild(txt1)) nodePlane.attachChild(txt1);
    }


}
