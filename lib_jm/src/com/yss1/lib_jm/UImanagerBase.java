/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.font.BitmapFont;
//import static com.jme3.font.BitmapFont.Align.Left;
//import static com.jme3.font.BitmapFont.Align.Right;
//import static com.jme3.font.BitmapFont.VAlign.Bottom;
//import static com.jme3.font.BitmapFont.VAlign.Center;
//import static com.jme3.font.BitmapFont.VAlign.Top;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ys
 */
public class UImanagerBase {
   protected List<Button3d> buttons;
   protected HashMap<Character,InfoPlane> planes;
   protected HashMap<Character,Wnd> popups;
   protected RessKeeper rKeeper;
   
   public UImanagerBase(RessKeeper a)
   {
       rKeeper=a;
       buttons = new ArrayList<Button3d>();
       planes=new HashMap<Character,InfoPlane>();
       popups=new HashMap<Character,Wnd>();
       init();
       makeButtons();
       makePlanes();
       makePopups();
       for (Character Ch: popups.keySet()) registerButtons(popups.get(Ch).getButtons());
       updateCaptions();
   }
   
   protected void init()
   {
       
   }
   
   protected void makeButtons(){
   
   }
   
    protected void makePlanes() {
        float X1 = rKeeper.getRess().getTextX(SettBase.CARD_W * 14);
        float X2 = rKeeper.getRess().getTextX(SettBase.CARD_W * 14 + 30 - 1);
        float Y2 = rKeeper.getRess().getTextY(0);
        float Y1 = rKeeper.getRess().getTextY(30 - 1);
        float pH = 0.3f;
        float HH = 0.15f;

        List<FaceState> fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X1, Y1, X2, Y1, X1, Y2, X2, Y2}));
        InfoPlaneBuilder builder = new InfoPlaneBuilder(rKeeper).setFontNo(0).setContainer(planes);
        builder.setFaces(fsList).
                setDimY(pH).
                setPlace(-SettBase.deskHw + pH + HH, -SettBase.deskHh + pH + HH, HH).
                build('M', "pln_menu");

        pH = 0.8f;
        fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("GP_Active", ToolsBase.singleTex));
        fsList.add(new FaceState(rKeeper).init("GP_NotActive", ToolsBase.singleTex));
        builder.setDimY(pH).
                setFaces(fsList).
                setPlace(-SettBase.deskHw + pH * 2.3f + HH, SettBase.deskHh - pH - HH, HH).
                build('P', "pln_google_play").getGe().setQueueBucket(Bucket.Translucent);
    }
   
   protected void makePopups(){
       Wnd popup = new Wnd(rKeeper,'A',"The caption window"," this is a message ant i may be too long...", 3f,2f,rKeeper.getRess().getTexture("Рамка0Центр").getImage().getHeight(),0);
       //Wnd popup = new Wnd(rKeeper,'A',"The caption window"," this is a message ant i may be too long...", 3f,2f,64,0);
       popup.setMyPlace(new Vector3f(0,0,1));
       popups.put('A', popup);
   }
   
   public void registerButtons(ArrayList<Button3d> B)
   {//добавляем кнопки, сделанные вне MakeButtons
       for (Button3d B1:B) if (!buttons.contains(B1)) 
       {
           buttons.add(B1);
          //System.out.println(B1.getName());
       }
   }
    
   public void detachMyButtons(ButtonListener bi)
   {
       for (Button3d b3: buttons) 
       {
           b3.detachMy(bi);
       }
   }
   
   public void flyOutButtons(ButtonListener bi,int section)
   {
    for (Button3d b3: buttons) 
    {
      b3.flySectionOut(bi,section);
    }
   }
   
   public void flyInButtons(ButtonListener bi,int section)
   {
    for (Button3d b3: buttons) 
    {
      b3.flySectionIn(bi,section);
    }
   }
   
   public void hideButtons(ButtonListener bi,int section)
   {
    for (Button3d b3: buttons) 
    {
      b3.hide(bi,section);
    }
   }
   
   public void unHideButtons(ButtonListener bi,int section)
   {
    for (Button3d b3: buttons) 
    {
      b3.unHide(bi,section);
    }
   }
   
   
   public void attachMyButtons(ButtonListener bi)
   {
       for (Button3d b3: buttons) 
       {
           b3.attachMy(bi);
       }
   }
   
   public Button3d getButton(String nm) {
        for (Button3d b3d : buttons) {
            if (b3d.getName().contains(nm)) {
                return b3d;
            }
        }
        return null;
    }

   public Button3d getAliasButton(String nm) {
        InfoPlane ip;
        for (Character pl : planes.keySet()) {
            ip=planes.get(pl);
            if (ip.getName().contains(nm)) {
               return ip.getTarget();
            }
        }
        return null;
    }
   
   public void setText1(String txt,Character c)
   {//ищет плоскость и меняет на ней верхний текст 
       InfoPlane ip=getPlane(c);
       if (ip!=null) ip.setText1(txt);
   }
   
    public void setText2(String txt,Character c)
   {//ищет плоскость и меняет на ней нижний текст 
       InfoPlane ip=getPlane(c);
       if (ip!=null) ip.setText2(txt);
   }
   
   public InfoPlane getPlane(Character c)
   {
       return planes.get(c);
   }
   
   public void initCheckButton(String nm, boolean ch) {
        for (Button3d b3d : buttons) {
            if (nm.contains(b3d.getName())) {
                b3d.initCheck(ch);
            }
        }
    }
   
   
   public void initRadioGroup(String nm, int rg) {
        for (Button3d b3d : buttons) {
            if (b3d.getRgroup()!=rg) continue;
                b3d.initCheck(nm.contains(b3d.getName()));
            }
    }
   
   public void updateCaptions() {
       
   }
   
   
       public Wnd getPopup(Character c)
    {
        return popups.get(c);
    }
   
    public Wnd getPopup(Spatial s)
    {
        if (s==null || s.getName().isEmpty()) return null;
        if (s.getName().equals("BitmapFont")) s=s.getParent();
        if (s.getName().contains("_pu_")) return popups.get(s.getName().charAt(0));
        return null;
    }
    
    public void switchPopup (Character ch,String capt,String bL,String bR,String bM, Wnd.PU_TYPE pt)
    {
        Wnd pu=getPopup(ch);
        if (pu==null) return;
        if (pu.isActive()) 
        {
            pu.close();
            return;
        }
        showPopup(ch,capt,null,bL,bR,bM,pt);
    }
    
   public void switchPopup (Character ch)
    {
        Wnd pu=getPopup(ch);
        if (pu==null) return;
        if (pu.isActive()) 
        {
            pu.close();
        }
        else
        {
            if (!rKeeper.getRootNode().hasChild(pu.getWndNode())) rKeeper.getRootNode().attachChild(pu.getWndNode());
        }
    }
   
   public void switchPopup (Character ch,boolean SHOW)
    {
        Wnd pu=getPopup(ch);
        if (pu==null) return;
        if (pu.isActive()) 
        {
            if (!SHOW) pu.close();
        }
        else 
        {
            if (!rKeeper.getRootNode().hasChild(pu.getWndNode()) && SHOW) rKeeper.getRootNode().attachChild(pu.getWndNode());
        }
    }  
    
    public void showPopup(Character ch, String capt, String msg,String bL,String bR,String bM, Wnd.PU_TYPE pt)
    {
        showPopup(ch,capt,msg,bL,bR,bM,pt,0);
    }
    
     public void showPopup(Character ch, String capt, String msg,String bL,String bR,String bM, Wnd.PU_TYPE pt,float fs)
    {
        Wnd pu=getPopup(ch);
        if (pu==null) return;
        pu.setPu_type(pt);
        pu.setCaption(capt);
        pu.setMessage(msg,fs);
        pu.setBtnText(bL,bR,bM);
        //System.out.println(" This="+pu);
        if (!rKeeper.getRootNode().hasChild(pu.getWndNode())) rKeeper.getRootNode().attachChild(pu.getWndNode());
    }
     
    public void showTablePopup(Character ch, String capt, String msg,String selected,String bR, float fs)
    {
        Wnd pu=getPopup(ch);
        if (pu==null) return;
        pu.setPu_type(Wnd.PU_TYPE.CANCEL_ONLY);
        pu.setBtnText("",bR,"");
        pu.setCaption(capt);
        pu.setTableMessage(msg,fs,selected);
        if (!rKeeper.getRootNode().hasChild(pu.getWndNode())) rKeeper.getRootNode().attachChild(pu.getWndNode());
    }
    
    public void closePopup(Character ch)
    {
        Wnd pu=getPopup(ch);
        if (pu==null) return;
        pu.close();
    }
    
    
    protected void distributeRadioGroupOnPlane(int RG, Character P)
    {
        InfoPlane IP=planes.get(P);
        if (IP==null) return;
        ArrayList<Button3d> ARG=new ArrayList<Button3d>();
        for (Button3d B: buttons) if (B.getRgroup()==RG) ARG.add(B);
        int cnt=ARG.size();
        if (cnt==0) return;
        float DX=IP.getDx();
        float DY=IP.getDy();
        float dx=ARG.get(0).getDx();
        float dy=ARG.get(0).getDy();
        float Dl=(DX*2-dx*2*cnt)/((float)cnt+1f);
        cnt=0;
        for (Button3d B: ARG) B.setMyPlace(-DX+(Dl+dx)+(Dl+dx*2)*(cnt++), -(DY-dy-dy/2), 0.3f);
    }
    
    protected void distributeOnPlane(BitmapFont.Align HA,BitmapFont.VAlign VA, Character P,String bname)
    {
        InfoPlane IP=planes.get(P);
        if (IP==null) 
        {
           // System.out.println("IP IS NULL "+P);
            return;
        }
        Button3d B=getButton(bname);
        if (B==null) 
        {
         //   System.out.println("B IS NULL "+bname);
            
            return;
        }
        float DX=IP.getDx();
        float DY=IP.getDy();
        float dx=B.getDx();
        float dy=B.getDy();
        float X=0,Y=0;
        float deltaX=dx;
        float deltaY=dy;

        if (HA==BitmapFont.Align.Right){
            X=DX-dx-deltaX;
        } else if (HA==BitmapFont.Align.Left){
            X=-DX+dx+deltaX;
        }


        if (VA==BitmapFont.VAlign.Bottom){
            Y=-DY+deltaY+dy;
        } else if (VA==BitmapFont.VAlign.Top){
            Y=DY-deltaY-dy;
        }


        B.setMyPlace(X,Y, 0.2f);
    }
    
    public void setGPmaterial(boolean active)
   {
       InfoPlane ip = getPlane('P');
                if (ip != null) {
                //    ip.setMaterial(active?"GP_Active":"GP_NotActive");
                    ip.applyTexture(active?0:1);
                }
   }
    
}
