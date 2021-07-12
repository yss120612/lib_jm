/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.LoopMode;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;

/**
 *
 * @author ys
 */
public class Card implements Comparable {

    public static enum State {

        FACE, BACK
    };
    private int suit;
    private int rank;
    private int weight;
    public int[] rating;
    private State state;
    private Geometry ge;
    private String name;
    private VectorsPool vPool;
    private RessKeeper rKeep;
    private boolean valid;
    private char owner;
    private Vector3f endpointM;//Конечнное положение трансформации
    private Vector3f endpointRv;
    private Quaternion endpointRq;
    private float endpointS;
    private AnimControl control;
    private AnimComposer acomposer;
    AnimComposer control1;
    private int stockNo;
    
    public Card(RessKeeper stk, String n) {
        stockNo=0;
        rKeep = stk;
        suit = ToolsBase.getSuit(n);
        rank = ToolsBase.getRank(n);
        if (rank < 2 || rank > 15 || suit < 1 || suit > 4 || (rank == 15 && (suit == 1 || suit == 4))) {
            throw new Error("Недопустимое имя карты " + n);
        }
        name = n.toUpperCase();
        init();
    }

    public Card(RessKeeper stk,int k, int r, int s) {//Joker (R) must be a C od D suit (i.e. RC or RD name)
        rKeep = stk;
        stockNo=k;
        switch (r) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                name = String.valueOf(r);
                break;
            case 11:
                name = "J";
                break;
            case 12:
                name = "Q";
                break;
            case 13:
                name = "K";
                break;
            case 14:
                name = "A";
                break;
            case 15:
                name = "R";
                break;
            default:
                throw new Error("Недопустимый rank карты " + r);
        }
        switch (s) {
            case 1:
                if (name.equals("R")) {
                    throw new Error("Недопустимый suit Джокера " + s);
                }
                name += "S";
                break;
            case 2:
                name += "C";
                break;
            case 3:
                name += "D";
                break;
            case 4:
                if (name.equals("R")) {
                    throw new Error("Недопустимый suit Джокера " + s);
                }
                name += "H";
                break;
            default:
                throw new Error("Недопустимый suit карты " + s);
        }
        rank = r;
        suit = s;
        init();

    }

    public int getStockNo() {
        return stockNo;
    }

    public float getDistance()//расстояние между текущим положением и ендпоинт
    {
        if (endpointM != null) {
            return ge.getLocalTranslation().subtract(endpointM).length();
        }

        return 0;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {

        setState(state, false);
    }

    public void setState(State state, boolean noRot) {

        if (state != this.state) {
            if (noRot) {
                invertState();
            } else {
                rotate();
            }
        }
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void incWeight(int weight) {
        this.weight += weight;
    }

    public void saveTransform() {
        setEndpointLike(this);
//        setEndpointM(ge.getLocalTranslation());
//        setEndpointRq(ge.getLocalRotation());
//        setEndpointS(ge.getLocalScale().x);
    }

    public String getStateStr(char cs) {
        String S = "C:" + cs + "_O:" + owner + "_S:" + state + "_T:" + ge.getLocalTranslation() + ge.getLocalRotation() + ge.getLocalScale() + "_W:" + getWeight() + "_Y";
        return S;
    }

    public String getNetStr(char cs) {
        String S = "N:" + getName() + "_C:" + cs + "_O:" + owner + "_S:" + state + "_Y";
        return S;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Card)) {
            return false;
        }
        Card other = (Card) obj;
        return getSuit() == other.getSuit() && getRank() == other.getRank() && getStockNo() == other.getStockNo();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.suit;
        hash = 97 * hash + this.rank;
        hash = 97 * hash + this.stockNo;
        return hash;
    }

    public void setStateStr(String st1, String st2, String st3, String st4) {
        setIdleAnim();
        try {
            setOwner(st1.charAt(0));
            if (st2.equals("FACE")) {
                setState(State.FACE, true);
            } else {
                setState(State.BACK, true);
            }
            ge.setLocalTransform(ToolsBase.getTRfromStr(st3));
            try {
                setWeight(Integer.parseInt(st4));
            } catch (NumberFormatException NFE) {
                setWeight(0);
            }
        } catch (Exception Ex) {
        }
    }

    public Vector3f getEndpointM() {
        return endpointM;
    }

    public void setEndpointM(Vector3f endpointM) {
        this.endpointM.set(endpointM);
    }

    public void setEndpointM(float x, float y, float z) {
        this.endpointM.set(x, y, z);
    }
    

    public Vector3f getEndpointRv() {
        return endpointRv;
    }

    public void setEndpointRv(Vector3f epRv) {
        endpointRv.set(epRv);
        endpointRq.fromAngles(endpointRv.x, endpointRv.y, endpointRv.z);
    }

    public void setEndpointRv(float x, float y, float z) {
        endpointRv.set(x, y, z);
        endpointRq.fromAngles(endpointRv.x, endpointRv.y, endpointRv.z);
    }

    public Quaternion getEndpointRq() {
        return endpointRq;
    }

    public void setEndpointRq(Quaternion endpointRq) {
        this.endpointRq.set(endpointRq);
        float[] a = endpointRq.toAngles(null);
        endpointRv.set(a[0], a[1], a[2]);
    }

    public void setEndpointLike(Card C) {
        if (C == null || C.getGe() == null) {
            return;
        }
        setEndpointM(C.getGe().getLocalTranslation());
        setEndpointRq(C.getGe().getLocalRotation());
        setEndpointS(C.getGe().getLocalScale().x);
    }

     public void setTransformLike(Card C) {
        if (C == null || C.getGe() == null) {
            return;
        }
        getGe().setLocalTransform(C.getGe().getLocalTransform());
    }
     
    
    public void setEndpointLikeNotZ(Card C) {
        if (C == null || C.getGe() == null) {
            return;
        }
        endpointM.x=C.getGe().getLocalTranslation().x;
        endpointM.y=C.getGe().getLocalTranslation().y;
        endpointM.z=getGe().getLocalTranslation().z;//своя
        setEndpointRq(C.getGe().getLocalRotation());
        setEndpointS(C.getGe().getLocalScale().x);
    }
    
    
    public void setEndpointLikeEndpoint(Card C) {
        if (C == null || C.getGe() == null) {
            return;
        }
        setEndpointM(C.getEndpointM());
        setEndpointRq(C.getEndpointRq());
        setEndpointS(C.getEndpointS());
    }


    public float getEndpointS() {
        return endpointS;
    }

    public void setEndpointS(float endpointS) {
        this.endpointS = endpointS;
    }

    public void applyEndpoint() {
        ge.setLocalRotation(endpointRq.clone());
        ge.setLocalTranslation(endpointM.x,endpointM.y,endpointM.z);
        ge.setLocalScale(endpointS);
    }

    public void setHi(boolean lifhtOn) {
        setHi(lifhtOn, true);
    }

    public void setHi(boolean lightOn, boolean lightPositive) //ColorRGBA color)
    {
        Material mat = ge.getMaterial();
        if (mat != null) {
            if (!(mat.equals(rKeep.getRess().getMaterial()) ^ lightOn)) {
                ge.setMaterial(lightOn ? rKeep.getRess().getMaterial(lightPositive) : rKeep.getRess().getMaterial());
            }
        }
        if (!lightOn) {
        } else {
            valid = lightPositive;
        }
    }

    public boolean isValid() {
        return valid;
    }

    public void setIdleAnim() {
        channel.setAnim("Idle", 0);
    }

    public String getAnim() {
        return channel.getAnimationName();
    }

    public boolean isIdle() {
        return channel == null || channel.getAnimationName() == null || channel.getAnimationName().equals("Idle");
    }

    public char getOwner() {
        return owner;
    }

    public void setOwner(char ow) {
        if (ow != 'U' && ow != 'L' && ow != 'D' && ow != 'R' && ow != 'B') {
            owner = 'N';
        } else {
            owner = ow;
        }
    }

    public void setBackground(int bk) {
        if (ge == null || bk < 1 || bk > 4) {
            return;
        }
        float Lx = SettBase.CARD_W / (float) rKeep.getRess().getTX_W();
        FloatBuffer fbuff = ge.getMesh().getFloatBuffer(VertexBuffer.Type.TexCoord);
        fbuff.put(8, (bk) * Lx);
        fbuff.put(10, (bk - 1) * Lx);
        fbuff.put(12, (bk) * Lx);
        fbuff.put(14, (bk - 1) * Lx);
        ge.getMesh().scaleTextureCoordinates(Vector2f.UNIT_XY);
    }

    @Override
    public String toString() {
        String res = ToolsBase.getTextBase(400 + getRank());
        if (getRank() != 15) {
            res = res + " " + ToolsBase.getTextBase(450 + getSuit());
        }
        return res;
    }

    public String getRankST() {
        String res = ToolsBase.getTextBase(400 + getRank());
        return res;
    }

    public String getSuitST() {
        if (getRank() != 15) {
            return ToolsBase.getTextBase(460 + getRank());
        }
        return "";
    }

    public int getSuit() {
        return suit;
    }

    public Geometry getGe() {

        return ge;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return stockNo==0?name:name+"*"+stockNo;
    }

    
    
    private void init() {
        vPool = ToolsBase.vPool;
        owner = 'N';
        state = State.FACE;
        rating = new int[]{0, 0, 0, 0, 0, 0, 0};
        //int i = rKeep.getRess().getTX_H();
    }
    private Vector3f vdiff;

    public void setDiff(Geometry plane, Vector3f v0, Vector3f vCam, boolean flyUp) {//v0 точка, кде коснулись карты в координатах RootNode
        float dx = v0.x; 
        float dy = v0.y;
        if (flyUp) {
            ge.move(0, 0, 0.8f);
        }

        Ray ray = vPool.getRay(vCam, v0.subtract(vCam));
        ge.getParent().attachChild(plane);
        plane.setLocalTranslation(ge.getLocalTranslation());
        CollisionResults CRes = vPool.getColl();
        plane.collideWith(ray, CRes);
        if (CRes.size() > 0) {
            v0 = CRes.getClosestCollision().getContactPoint();
        }
        ge.move(v0.x - dx, v0.y - dy, 0);
        Transform TR = ge.getParent().getLocalTransform();
        Vector3f tmp = vPool.getV3(0, 0, 0);
        //Переводим в координаты ноды CardSet
        TR.transformInverseVector(v0, tmp);//обратная TR трансформация вектора V0 записывается в tmp
        vdiff = tmp.subtract(ge.getLocalTranslation());//вектор между центром карты и куда ткнули
        //ge.getLocalTranslation()-центр карты в координатах родителя
        //tmp-точка коллизии в координатах родителя
        //vdiff вектор из центра карты в точку коллизии
        vdiff.z = 0;

        vPool.freeV3(tmp);
        vPool.freeColl(CRes);
        vPool.freeRay(ray);
    }

    public void shift4Show(Geometry plane, Vector3f v0, Vector3f vCam,float up) {//v0 точка, кде коснулись карты в координатах RootNode
        float dx = v0.x; 
        float dy = v0.y;
        ge.move(0, 0, up);
        
        Ray ray = vPool.getRay(vCam, v0.subtract(vCam));
        ge.getParent().attachChild(plane);
        plane.setLocalTranslation(ge.getLocalTranslation());
        CollisionResults CRes = vPool.getColl();
        plane.collideWith(ray, CRes);
        if (CRes.size() > 0) {
            v0 = CRes.getClosestCollision().getContactPoint();
        }
        ge.move(v0.x - dx, v0.y - dy, 0);
        ge.getParent().detachChild(plane);
        vPool.freeColl(CRes);
        vPool.freeRay(ray);
    }
    
    
    public Vector3f getDiff() {
        return vdiff;
    }

    public int getPictSuit() {//как нарисованы крест-буб-черв-пик
        int su = 0;
        switch (suit) {
            case 1:
                su = 3;
                break;
            case 2:
                su = 0;
                break;
            case 3:
                su = 1;
                break;
            case 4:
                su = 2;
                break;
        }
        return su;
    }

    public void initGeometry(AnimEventListener AEL, int cBG) {
        endpointM = new Vector3f();
        endpointRv = new Vector3f();
        endpointRq = new Quaternion();

        float cH = SettBase.cardH / 2f;
        float cW = SettBase.cardW() / 2f;
        float cTH = SettBase.th / 2f;

        int bk = cBG;
        float Lx = SettBase.CARD_W / (float) rKeep.getRess().getTX_W();
        float Ly = SettBase.CARD_H / (float) rKeep.getRess().getTX_H();

        int No = 4 * (rank - 2) + getPictSuit();
        float No14 = (float) (No % 14);
        float Nc14 = (float) (No / 14);
        No14 *= Lx;
        Nc14 = 1f - Nc14 * Ly;

        float tx[] = new float[]{No14, Nc14 - Ly,
            No14 + Lx, Nc14 - Ly,
            No14, Nc14,
            No14 + Lx, Nc14,
            (bk) * Lx, 1f - Ly * 5,
            (bk - 1) * Lx, 1f - Ly * 5,
            (bk) * Lx, 1f - Ly * 4,
            (bk - 1) * Lx, 1f - Ly * 4};

        Mesh ME = ToolsBase.makeSimple2planes(cW, cH, cTH, tx);
        ge = new Geometry(getName(), ME);
        ge.setQueueBucket(RenderQueue.Bucket.Opaque);
        acomposer=new AnimComposer();
        ge.addControl(acomposer);
        acomposer.

        control = new AnimControl();
        control.addListener(AEL);
        ge.addControl(control);
        control.addAnim(new Animation("Idle", 0));
        channel = control.createChannel();
    }
    

    public AnimControl getControl() {
        return control;
    }

    public AnimChannel getChannel() {
        return channel;
    }
    private AnimChannel channel;

    public void setRotatedEndpoint(boolean XorY) {
        //resetEndpoint();
        System.out.println("rotate \n"+ge.getLocalTranslation());
        
        //System.out.println(ge.getLocalTranslation()+"------------\n");
        //setIdleAnim();
        Transform TR = ge.getLocalTransform();
        setEndpointM(TR.getTranslation());
        setEndpointS(TR.getScale().x);
        Vector3f RV = vPool.getV3();
        if (XorY) {
            RV.set(1, 0, 0);
        } else {
            RV.set(0, 1, 0);
        }
        Quaternion Q1 = vPool.getQt().fromAngleNormalAxis(FastMath.PI, RV);
        setEndpointRq(TR.getRotation().mult(Q1));
        vPool.freeV3(RV);
        vPool.freeQt(Q1);
       // Q1.nlerp(Q1, endpointS);
    }

    public void invertState() {
        if (state == State.BACK) {
            state = State.FACE;
        } else if (state == State.FACE) {
            state = State.BACK;
        }

    }

    @Override
    public int compareTo(Object o) {
        Card cr = (Card) o;
        Integer s = suit;
        Integer r = rank;
        Integer s1 = cr.suit;
        Integer r1 = cr.rank;
        if (r == 15) {
            s += 4;
        }
        if (r1 == 15) {
            s1 += 4;
        }

        if (!s.equals(s1)) {
            return s.compareTo(s1);
        } else {
            return r.compareTo(r1);
        }
    }

    public void reset() {
        channel.setAnim("Idle");
        channel.setLoopMode(LoopMode.DontLoop);
        ge.setLocalTransform(Transform.IDENTITY);
        state = State.FACE;
    }

    public void resetEndpoint() {
        endpointM.set(0f,0f,0f);
        endpointS=1f;
        endpointRv.set(0f,0f,0f);
        endpointRq.fromAngles(0f,0f,0f);
    }
    
    public void rotate() {
        invertState();
        float A;
        if (state == State.FACE) {
            A = 0;
        } else {
            A = FastMath.PI;
        }
        Quaternion Q = vPool.getQt(A, 0, 0);
        ge.setLocalRotation(Q);
        vPool.freeQt(Q);
    }
}
