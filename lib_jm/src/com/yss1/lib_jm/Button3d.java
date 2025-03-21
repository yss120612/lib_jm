/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimFactory;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.List;

/**button
 *
 * @author ys
 */
public class Button3d {
    public Button3d(RessKeeper a, String nm, ButtonListener bi, BTYPE bt, BSIZE bs, List<FaceState> faces) {
        this.faces = faces;
        rKeeper = a;
        btype = bt;
        BI = bi;
        bsize = bs;
        snd_up = true;
        snd_down = true;
        currTex = 0;
        currImage = 0;
        caption = "";
        name = nm;
        enabled=true;

        switch (bsize) {
            case LARGE:
                Dy = SettBase.BTN_H * 1.4f;
                break;
            case SMALL:
            case LONG_SMALL:
                Dy = SettBase.BTN_H * 0.7f;
                break;
            default:
                Dy = SettBase.BTN_H;
        }

        FaceState fs = faces.get(currTex);

        Dx = fs.calcWidthForHeight(Dy);
        if (bsize == BSIZE.LONG_SMALL) {
            Dx *= 1.3f;
        }

        switch (bt) {
            case PRESS:
                behav = BEHAVIOR.UPDOWN;
                break;
            case CHECK:
                behav = BEHAVIOR.ROTATE_PI;
                break;
            case RADIO:
                behav = BEHAVIOR.UPDOWN;
                break;
            default:
                behav = BEHAVIOR.NONE;
        }

        if (fs.texCoord.length == 16) {
            //двухсторонняя плоскость
            ge = new Geometry(name, ToolsBase.makeSimple2planes(Dx, Dy, SettBase.BTN_T, fs.texCoord));
        } else {
            //односторонняя плоскость
            ge = new Geometry(name, ToolsBase.makeSimplePlane(Dx, Dy, fs.texCoord));
        }

        fs.applyMatTo(ge);

        nodeBtn = new Node(name + "_node");
        nodeBtn.attachChild(ge);

        animComposerYss=new AnimComposerYss();
        animComposerYss.setAnimClipListener(BI.getAnimClipListener());
        nodeBtn.addControl(animComposerYss);

        rotate = ROT.Y;
    }

    //библиотека ресурсов
    private RessKeeper rKeeper;

    //обработчик событий кнопки;
    private ButtonListener BI;

    public static enum BTYPE {
        PRESS, CHECK, RADIO
    };

    //поведение кнопки при нажатии
    public static enum BEHAVIOR {
        NONE, ROTATE_PI, ROTATE_2PI, UPDOWN
    };

    //если вращаем при нажатии то вокруг какой оси
    public static enum ROT {
        X, Y
    };

    public static enum BSIZE {

        LARGE, MEDIUM, SMALL, LONG_SMALL
    };

    private BEHAVIOR behav;
    public BEHAVIOR getBehav() {
        return behav;
    }
    public void setBehav(BEHAVIOR behav) {
        this.behav = behav;
    }

    private BTYPE btype;
    private ROT rotate;
    private BSIZE bsize;

    //размер
    private float Dx, Dy;
    public float getDx() {
        return Dx;
    }
    public float getDy() {
        return Dy;
    }

    private Node nodeBtn;

    private boolean isDown;

    private Vector3f myPlace;
    public Vector3f getMyPlace() {
        return myPlace;
    }
    public void setMyPlace(float x, float y, float z) {
        if (myPlace == null) {
            myPlace = new Vector3f(x, y, z);
        } else {
            myPlace.set(x, y, z);
        }
        resetAnim();
        nodeBtn.setLocalTranslation(myPlace);
    }

    //список других кнопок если это радио
    private List<Button3d> radio_list;

    private Geometry ge;
    private Geometry gePict;
    private String caption;
    private boolean checked;
    private int rgroup;
    private boolean snd_up;
    private boolean snd_down;
    private float dPict;
    private int section;

    protected AnimComposerYss animComposerYss;
    BitmapText txt;
    private int currTex;
    private int currImage;
    List<FaceState> faces;
    List<FaceState> images;
    private String name;
    private boolean enabled;

    public void initRadio(List<Button3d> al, int grp, boolean chk) {
        radio_list = al;
        rgroup = grp;
        btype = BTYPE.RADIO;
        checked = chk;
        if (chk) {
            initCheck(true);
        }
    }

    public boolean attachMy(ButtonListener bi) {
        Node N;
        if (BI != null && BI.equals(bi)) {
            N = BI.getParentNode(getName());
            if (N != null) {
                N.attachChild(nodeBtn);
                return true;
            }
        }
        return false;
    }

    public boolean detachMy(ButtonListener bi) {
        Node N;
        if (BI != null && BI.equals(bi)) {
            N = BI.getParentNode(getName());
            if (N != null && N.hasChild(nodeBtn)) {
                N.detachChild(nodeBtn);
                return true;
            }
        }
        return false;
    }

    public void flySectionOut(ButtonListener bi, int sec) {
        if (BI != null && BI.equals(bi) && section == sec) {
            flyOut();
        }
    }

    public void flySectionIn(ButtonListener bi, int sec) {
        if (BI != null && BI.equals(bi) && section == sec) {
            flyIn();
        }
    }

    public void hide(ButtonListener bi, int sec) {
        if (BI != null && BI.equals(bi) && section == sec) {
            resetAnim();
            nodeBtn.setLocalTranslation(SettBase.b3dX, SettBase.b3dY, SettBase.b3dZ);
        }
    }

    public void unHide(ButtonListener bi, int sec) {
        if (BI != null && BI.equals(bi) && section == sec) {
            resetAnim();
            nodeBtn.setLocalTranslation(myPlace);
        }
    }

    private void selectRadio() {
        if (radio_list == null || radio_list.isEmpty() || checked) {
            return;
        }
        for (Button3d b3 : radio_list) {
            if (b3.getRgroup() != rgroup) {
                continue;
            }
            if (b3.equals(this)) {
                setChecked(true);
            } else {
                if (b3.isChecked()) {
                    b3.setChecked(false);
                }
            }
        }
    }

    public ROT getRotate() {
        return rotate;
    }

    public void setRotate(ROT rotate) {
        this.rotate = rotate;
    }

    public Spatial getGe() {
        return nodeBtn;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public BTYPE getBtype() {
        return btype;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getRgroup() {
        return rgroup;
    }

    public void setRgroup(int rgroup) {
        this.rgroup = rgroup;
    }

    public boolean invertChecked() {
        setChecked(!checked);
        return checked;
    }

    public void initCheck(boolean chk) {//WO animation
        checked = chk;
        Quaternion currR = ToolsBase.vPool.getQt(0, 0, 0);
        switch (behav) {
            case UPDOWN:
                if (checked) {
                    nodeBtn.setLocalTranslation(myPlace.x, myPlace.y, myPlace.z - SettBase.DELTA_PRESS);
                } else {
                    nodeBtn.setLocalTranslation(myPlace.x, myPlace.y, myPlace.z);
                }
                isDown = checked;
                break;
            case ROTATE_PI:
                if (!checked) {
                    //currR.fromAngles(FastMath.PI,0,0);
                } else {
                    if (rotate == ROT.X) {
                        currR.fromAngles(FastMath.PI, 0, 0);
                    } else {
                        currR.fromAngles(0, FastMath.PI, 0);
                    }
                }

                nodeBtn.setLocalRotation(currR);
                break;
        }
        if (btype == BTYPE.RADIO) {
            setMaterial();
        }
        ToolsBase.vPool.freeQt(currR);
    }

    private void setMaterial() {
        if (checked && currTex == 1 || !checked && currTex == 0) {
            return;
        }

        if (checked) {
            currTex = 1;
        } else {
            currTex = 0;
        }
        faces.get(currTex).applyMatTo(ge);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        if (btype == BTYPE.RADIO) {
            if (checked) {
                if (snd_up) {
                    rKeeper.getSound().playSound("Клик");
                }
                if (BI != null) {
                    BI.selectRadio(getName());
                }


            }
            setMaterial();
        } else if (btype == BTYPE.CHECK) {
            if (BI != null) {
                BI.changeCheckbox(getName(), this.checked);
            }
            if (this.checked && snd_up || !this.checked && snd_down) {
                rKeeper.getSound().playSound("Клик");
            }
        }

        switch (behav) {
            case UPDOWN:
                pressBtn(this.checked);
                break;
            case ROTATE_PI:
                rotateBtn(this.checked);
                break;
        }
    }

    public void setImage(List<FaceState> images, float h) {
        if (gePict != null && nodeBtn.hasChild(gePict)) {
            nodeBtn.detachChild(gePict);
        }
        if (h > Dy) {
            dPict = Dy;
        } else {
            dPict = h;
        }
        this.images = images;
        currImage = 0;
        if (images == null) {
            dPict = 0;
        } else {
            FaceState fs = images.get(currImage);
            gePict = new Geometry(name + "_img", ToolsBase.makeSimplePlane(dPict, dPict, fs.texCoord));
            fs.applyMatTo(gePict);
            gePict.setLocalTranslation(-Dx + dPict, 0, ge.getLocalTranslation().z + 0.03f);
            nodeBtn.attachChild(gePict);
        }
        if (txt != null) {
            if (nodeBtn.hasChild(txt)) {
                nodeBtn.detachChild(txt);
            }
            txt = null;
        }
        if (!caption.isEmpty()) {
            setText(caption);
        }
    }

    public void applyImage(int No) {
        if (images == null || gePict == null) {
            return;
        }
        currImage = No;
        FaceState fs = images.get(currImage);
        fs.applyTo(gePict);
    }

    public void applyTex(int No) {
        if (No == currTex) {
            return;
        }
        currTex = No;
        FaceState fs = faces.get(currTex);
        fs.applyTo(ge);
    }
    
    public ButtonListener getBI() {
        return BI;
    }

    public void touchDown() {
        if (!enabled) {
            return;
        }
        if (btype == BTYPE.CHECK) {
            return;
        }
        if (btype == BTYPE.RADIO) {
            if (!checked) {
                selectRadio();
            }
            return;
        }
        
        switch (behav) {
            case UPDOWN:
                pressBtn(true);
                break;
            case ROTATE_PI:
                rotateBtn(true);
                break;
            case NONE:
                if (BI != null) {
                    BI.clickButton(getName());
                }
                break;
        }
        if (snd_down) {
            rKeeper.getSound().playSound("КнопкаВниз");
        }
    }

    public void touchUp(boolean success) {
        if (!enabled) {
            return;
        }

        if (btype == BTYPE.CHECK) {
            if (success) {
                invertChecked();
            }
            return;
        }

        if (btype == BTYPE.RADIO) {
            return;
        }
        switch (behav) {
            case UPDOWN:
                pressBtn(false);
                if (snd_up) {
                    rKeeper.getSound().playSound("КнопкаВверх");
                }
                break;
            case ROTATE_PI:
                rotateBtn(false);
                if (snd_up) {
                    rKeeper.getSound().playSound("КнопкаВверх");
                }
                break;
            case ROTATE_2PI:
                if (success) {
                    rotateBtn(false);
                }
                if (snd_up) {
                    rKeeper.getSound().playSound("Клик");
                }
                break;
            case NONE:
                if (snd_up) {
                    //rKeeper.getSound().playSound("КнопкаВверх");
                }
                return;

        }
        if (btype == BTYPE.PRESS && BI != null && success) {
            BI.clickButton(getName());
        }
    }

    public void setText(String text) {
        
        caption = text;
        
        if (txt == null) {
            txt = new BitmapText(rKeeper.getRess().getFont("Большой"));
            txt.setName(name + "_txt");
            //txt.setBox(new Rectangle(-Dx + dPict * 2, Dy, (Dx - dPict) * 2, Dy * 2));
            txt.setBox(new Rectangle(-Dx  , Dy, (Dx) * 2, Dy * 2));
            txt.setAlignment(BitmapFont.Align.Center);
            txt.setLineWrapMode(LineWrapMode.NoWrap);
            txt.setVerticalAlignment(BitmapFont.VAlign.Center);
            txt.setQueueBucket(RenderQueue.Bucket.Translucent);

            if (bsize == BSIZE.SMALL || bsize == BSIZE.LONG_SMALL) {
                txt.setSize(Dy * 0.7f);
            } else {
                txt.setSize(Dy * 0.5f);
            }
            nodeBtn.attachChild(txt);
        }
        float dy = 0;//Dy / 9;
        if (text.contains("\n")) {
            dy = Dy / 7f;
        
        }
        txt.setLocalTranslation(0, dy, ge.getLocalTranslation().z + 0.03f);
        txt.setText(text);
//        System.out.println(txt.getName()+" TEXT="+ text+" TR="+txt.getLocalTranslation());
    }

    private void pressBtn(boolean down) {
        if (isDown == down) {
            return;
        }
        String AName;
        if (down) {
            AName = "PressBtnDown";
        } else {
            AName = "PressBtnUp";
        }
        //if (!animComposerYss.hasAction(AName)) {
            makePressAnim(down, AName);
        //}
        animComposerYss.setCurrentAction(AName, AnimComposer.DEFAULT_LAYER,false);
        isDown = down;
    }

    private void rotateBtn(boolean down) {
        //  if (achannel.getAnimationName()!=null && achannel.getAnimationName().contains("RotateBtn")) return;
        String aName = "Idle";
        switch (behav) {
            case ROTATE_2PI:
                if (rotate == ROT.X) {
                    aName = "RotateBtnX2PI";
                    if (!animComposerYss.hasAction(aName)) {
                        makeRotateAnimX2PI();
                    }
                } else {
                    aName = "RotateBtnY2PI";
                    if (!animComposerYss.hasAction(aName)) {
                        makeRotateAnimY2PI();
                    }
                }
                break;

            case ROTATE_PI:
                if (rotate == ROT.X) {
                    if (down) {
                        aName = "RotateBtnX0_PI";
                        if (!animComposerYss.hasAction(aName)) {
                            makeRotateAnimX0_PI();
                        }
                    } else {
                        aName = "RotateBtnXPI_2PI";
                        if (!animComposerYss.hasAction(aName)) {
                            makeRotateAnimXPI_2PI();
                        }
                    }
                } else {
                    if (down) {
                        aName = "RotateBtnY0_PI";
                        if (!animComposerYss.hasAction(aName)) {
                            makeRotateAnimY0_PI();
                        }
                    } else {
                        aName = "RotateBtnYPI_2PI";
                        if (!animComposerYss.hasAction(aName)) {
                            makeRotateAnimYPI_2PI();
                        }
                    }
                }
                break;
        }
        animComposerYss.setCurrentAction(aName, AnimComposer.DEFAULT_LAYER,false);

    }

    public void resetAnim() {
        if (animComposerYss!=null) animComposerYss.reset();
        if (btype == BTYPE.PRESS) {
            nodeBtn.setLocalRotation(Quaternion.IDENTITY);
            nodeBtn.setLocalTranslation(myPlace);
        }
    }

    public void flyIn() {
        if (animComposerYss==null){
            throw new NullPointerException("Cannot find Anim composer for flyOut");
        }
        AnimClip clip = animComposerYss.getAnimClip("flyIn");
        if (clip==null) {
            Vector3f currM = ToolsBase.vPool.getV3(SettBase.b3dX, section % 2 > 0 ? -SettBase.b3dY : SettBase.b3dY, SettBase.b3dZ);
            AnimFactory animFactory = new AnimFactory(SettBase.amimBtnTime, "flyIn", 25f);
            animFactory.addTimeTransform(0, nodeBtn.getLocalTransform());
            animFactory.addTimeTransform(SettBase.amimBtnTime * 1.5f, new Transform(myPlace, nodeBtn.getLocalRotation(), nodeBtn.getLocalScale()));
            clip=animFactory.buildAnimation(nodeBtn);
            animComposerYss.addAnimClip(clip);
            ToolsBase.vPool.freeV3(currM);
        }
        resetAnim();
        animComposerYss.setCurrentAction("flyIn");
    }

    public void flyOut() {

        if (animComposerYss==null){
               throw new NullPointerException("Cannot find Anim composer for flyOut");
        }
        AnimClip clip = animComposerYss.getAnimClip("flyOut");
        if (clip==null) {
            //animComposerYss.removeAction("flyOut");
            Vector3f currM = ToolsBase.vPool.getV3(SettBase.b3dX, section % 2 > 0 ? -SettBase.b3dY : SettBase.b3dY, SettBase.b3dZ);
            AnimFactory animFactory = new AnimFactory(SettBase.amimBtnTime, "flyOut", 25f);
            animFactory.addTimeTransform(0, nodeBtn.getLocalTransform());
            animFactory.addTimeTransform(SettBase.amimBtnTime * 1.5f, new Transform(currM, nodeBtn.getLocalRotation(), nodeBtn.getLocalScale()));
            clip=animFactory.buildAnimation(nodeBtn);
            animComposerYss.addAnimClip(clip);
            ToolsBase.vPool.freeV3(currM);
        }
        resetAnim();
        animComposerYss.setCurrentAction("flyOut");
    }

    private void makeRotateAnimY0_PI()
    {
        makeRotateAnim("RotateBtnY0_PI", false, 0, 180);
    }

    private void makeRotateAnimYPI_2PI()
    {
        makeRotateAnim("RotateBtnYPI_2PI", false, 1, 180);
    }

    private void makeRotateAnimY2PI()
    {
        makeRotateAnim("RotateBtnY2PI", false, 0, 360);
    }

    private void makeRotateAnimX0_PI()
    {
        makeRotateAnim("RotateBtnX0_PI", true, 0, 180);
    }

    private void makeRotateAnimXPI_2PI()
    {
        makeRotateAnim("RotateBtnXPI_2PI", true, 1, 180);
    }

    private void makeRotateAnimX2PI()
    {
        makeRotateAnim("RotateBtnX2PI", true, 0, 360);
    }

    private void makeRotateAnim(String aname, boolean XorY, int part, int deg) {

        if (animComposerYss==null){
            throw new NullPointerException("Cannot find Anim composer for " + aname);
        }
        Vector3f currM = ToolsBase.vPool.getV3(myPlace);
        Vector3f currS = ToolsBase.vPool.getV3(nodeBtn.getLocalScale());
        Vector3f RV = ToolsBase.vPool.getV3(XorY?1:0,XorY?0:1,0);
        Vector3f tmpM = ToolsBase.vPool.getV3(nodeBtn.getLocalTranslation());
        Quaternion currR=null;

        //if (animComposerYss.getAction(aname)!=null) animComposerYss.removeAction(aname);
        AnimClip clip=animComposerYss.getAnimClip(aname);
        if (clip==null) {//animComposerYss.removeAnimClip(clip);

            AnimFactory animFactory = new AnimFactory(SettBase.amimBtnTime, aname, 25f);
            currR = ToolsBase.vPool.getQt().fromAngleNormalAxis(deg * part * FastMath.DEG_TO_RAD, RV);
            //from
            animFactory.addTimeTransform(0, new Transform(currM, currR, currS));
            //middle
            currR.fromAngleNormalAxis(((deg * part) + deg / 2) * FastMath.DEG_TO_RAD, RV);
            animFactory.addTimeTransform(SettBase.amimBtnTime / 2, new Transform(tmpM, currR, currS));
            //to
            currR.fromAngleNormalAxis(deg * (part + 1) * FastMath.DEG_TO_RAD, RV);
            animFactory.addTimeTransform(SettBase.amimBtnTime, new Transform(currM, currR, currS));

            clip = animFactory.buildAnimation(nodeBtn);
        }
        animComposerYss.addAnimClip(clip);

        ToolsBase.vPool.freeV3(RV);
        ToolsBase.vPool.freeV3(currM);
        ToolsBase.vPool.freeV3(currS);
        if (currR!=null) ToolsBase.vPool.freeQt(currR);


    }

    private void makePressAnim(boolean down, String aname) {
        if (animComposerYss==null){
            throw new NullPointerException("Cannot find Anim composer for " + aname);
        }

        Vector3f currM = ToolsBase.vPool.getV3(nodeBtn.getLocalTranslation());
        Vector3f currS = ToolsBase.vPool.getV3(nodeBtn.getLocalScale());
        Quaternion currR = ToolsBase.vPool.getQt(nodeBtn.getLocalRotation());

        //if (animComposerYss.getAction(aname)!=null) animComposerYss.removeAction(aname);
        AnimClip clip=animComposerYss.getAnimClip(aname);
        //if (clip!=null) animComposerYss.removeAnimClip(clip);
        if (clip==null) {
            AnimFactory animFactory = new AnimFactory(SettBase.amimBtnTime, aname, 25f);
            animFactory.addTimeTransform(0, nodeBtn.getLocalTransform());
            if (down) {
                currM.set(myPlace.x, myPlace.y, myPlace.z - SettBase.DELTA_PRESS);
            } else {
                currM.set(myPlace);
            }
            animFactory.addTimeTransform(SettBase.amimBtnTime, new Transform(currM, currR, currS));
            clip = animFactory.buildAnimation(nodeBtn);
        }
        animComposerYss.addAnimClip(clip);

        ToolsBase.vPool.freeV3(currM);
        ToolsBase.vPool.freeV3(currS);
        ToolsBase.vPool.freeQt(currR);
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
}
