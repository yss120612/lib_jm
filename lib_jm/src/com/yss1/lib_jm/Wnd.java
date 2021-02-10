/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.yss1.lib_jm.WaiterElement.WAITERTYPE;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ys
 */
public class Wnd implements 
        ButtonListener, 
        AnimEventListener,
        IExecutor{

    

    
   public enum PU_TYPE {EMPTY,OK_CANCEL,YES_ONLY,CANCEL_ONLY,PREV_NEXT,NEXT_ONLY,PREV_NEXT_CLS,NEXT_ONLY_CLS,PREV_ONLY_CLS}
   //OK_CANCEL - обе видны и вызывают закрытие
   //YES_ONLY - видна правая и вызывает закрытие
   //PREV_NEXT - видны обе и не вызывают закрытие
   //NEXT_ONLY - видна правая и не вызывает закрытие
   //PREV_ONLY - видна левая и не вызывает закрытие
   //*_*_CLS - с кнопочкой Close посередине
   
   public enum PU_KIND{MESSAGE,TABLE,MENU,CUSTOM}
   protected float Dx,Dy;
   private BitmapText caption;
   private BitmapText mainMessage;
   protected RessKeeper rKeeper;
   protected IPUowner myOwner;
   protected ArrayList<Button3d> buttons=new ArrayList<>();
   protected Node nodeWnd;
   protected PU_TYPE puType;
   protected PU_KIND kind;
   private final Geometry geo[];
   //protected Vector3f myPlace;
   protected String name;
   
   
    public Wnd(RessKeeper a, Character nm, String cap, String msg, float dx, float dy, float th, int menuBtns) {
        rKeeper = a;
        Dx = dx;
        Dy = dy;
        name=""+nm;
        geo = ToolsBase.make_framed_geometries(Dx, Dy, 0f, th, nm + "_pu_ge", SettBase.scrW, SettBase.scrH);

        nodeWnd = new Node(nm + "_pu_node");
        for (int i = 0; i < geo.length; i++) {
            nodeWnd.attachChild(geo[i]);
        }

        setCaption(cap);

       //close_menu=true;
        //is_menu=menuBtns!=0;
        if (menuBtns == 0) {
            kind = PU_KIND.MESSAGE;
        } else if (menuBtns > 0) {
            kind = PU_KIND.MENU;
        } else {
            kind = PU_KIND.CUSTOM;
        }
        buttons.clear();
        
        if (kind == PU_KIND.MENU) {
            makeMenuButtons(menuBtns);
            makeGeo("Рамка1");
        } else if (kind == PU_KIND.MESSAGE) {
            makeButtons();
            setMessage(msg);
            makeGeo("Рамка0");
        } else {
            makeButtons();
            makeMoreButtons();
            setMessage(msg);
            makeGeo("Рамка0");
        }

        puType = PU_TYPE.EMPTY;
        //System.out.println(" kind="+kind+ " type="+puType+" name="+nodeWnd.getName()+" this="+this+" button="+buttons.get(buttons.size()-1).getBI());
    }
   
   private void makeGeo(String prefix)
   {
       geo[0].setMaterial(rKeeper.getRess().getMaterial(prefix+"Угол"));
       geo[1].setMaterial(rKeeper.getRess().getMaterial(prefix+"Край"));
       geo[2].setMaterial(rKeeper.getRess().getMaterial(prefix+"Угол"));
       geo[3].setMaterial(rKeeper.getRess().getMaterial(prefix+"Край"));
       geo[4].setMaterial(rKeeper.getRess().getMaterial(prefix+"Центр"));
       geo[5].setMaterial(rKeeper.getRess().getMaterial(prefix+"Край"));
       geo[6].setMaterial(rKeeper.getRess().getMaterial(prefix+"Угол"));
       geo[7].setMaterial(rKeeper.getRess().getMaterial(prefix+"Край"));
       geo[8].setMaterial(rKeeper.getRess().getMaterial(prefix+"Угол")); 
   }
   
    public PU_TYPE getPu_type() {
        return puType;
    }

    public void setPu_type(PU_TYPE pu_type) {
       // if (this.puType == puType) return;
       this.puType = pu_type;
       for (Button3d B0:buttons)
       {
           if (B0.getName().contains("pubtn_left"))
           {
               if (pu_type!=PU_TYPE.NEXT_ONLY && pu_type!=PU_TYPE.CANCEL_ONLY && pu_type!=PU_TYPE.YES_ONLY && pu_type!=PU_TYPE.NEXT_ONLY_CLS)
               {
                   if (!nodeWnd.hasChild(B0.getGe())) nodeWnd.attachChild(B0.getGe());    
               }
               else
               {
                   if (nodeWnd.hasChild(B0.getGe())) nodeWnd.detachChild(B0.getGe());
               }
           }                               
           else if (B0.getName().contains("pubtn_middle")) 
           {
               
               if (pu_type==PU_TYPE.NEXT_ONLY_CLS || pu_type==PU_TYPE.PREV_NEXT_CLS || pu_type==PU_TYPE.PREV_ONLY_CLS)
               {
                   if (!nodeWnd.hasChild(B0.getGe())) nodeWnd.attachChild(B0.getGe());
               }
               else
               {
                   if (nodeWnd.hasChild(B0.getGe())) nodeWnd.detachChild(B0.getGe());
               }
           }
           else if (B0.getName().contains("pubtn_right"))
           {
               if (pu_type==PU_TYPE.PREV_ONLY_CLS)
               {
                   if (nodeWnd.hasChild(B0.getGe())) nodeWnd.detachChild(B0.getGe());
               }
               else
               {
                   if (!nodeWnd.hasChild(B0.getGe())) nodeWnd.attachChild(B0.getGe());
               }
           }
       }
       
    }
    
   @Override
   public Node getParentNode(String n)
    {
      return nodeWnd;
    }
   
    public Node getWndNode() {
        return nodeWnd;
    }

    public void setBtnText(String left, String right, String middle) {
        for (Button3d B0 : buttons) {
            if (B0.getName().contains("pubtn_left")) {
                B0.setText(left);
            }
            if (B0.getName().contains("pubtn_right")) {
                B0.setText(right);
            }
            if (B0.getName().contains("pubtn_middle")) {
                B0.setText(middle);
            }
        }
    }

   public void setMyPlace(Vector3f myPlace) {
        //this.myPlace = myPlace;
        nodeWnd.setLocalTranslation(myPlace);
    }
   
   public boolean isActive()
   {
       return rKeeper.getRootNode().hasChild(nodeWnd);
   }
   
   public boolean close()
   {
       boolean res=nodeWnd.removeFromParent();
       //nodeWnd.setLocalTranslation(myPlace);
       return res;
   }
   
     
    public String getCaption() {
        return caption!=null?caption.getText():"";
    }
    
    public String getName() {
        try
        {
        return geo[4].getName();
        }
        catch (Exception E)
        {
            return "";
        }
    }
    
    public void setCaption(String cpt) {
        if (caption==null)
        {
            caption= new BitmapText(rKeeper.getRess().getFont("Веселый"));
            caption.setBox(new Rectangle(0, 0, Dx*2-textPad()*8, Dy/3));
            caption.setName(getName()+"_capt");
            caption.setAlignment(BitmapFont.Align.Left);
            caption.setVerticalAlignment(BitmapFont.VAlign.Top);
            caption.setLineWrapMode(LineWrapMode.Clip);
            caption.setSize(0.55f);
            caption.setColor(ColorRGBA.Orange);
            caption.setQueueBucket(RenderQueue.Bucket.Translucent);
            nodeWnd.attachChild(caption);
            caption.setLocalTranslation(-Dx+textPad()*4, Dy, geo[4].getLocalTranslation().z+0.03f);
        }
        caption.setText(cpt);
    }
    
    private float textPad()
            {
                return Dx/15f;
            }
    
    public String getMessage() {
        return mainMessage!=null?mainMessage.getText():"";
    }
    

   public void setMessage(String cpt) {
       setMessage(cpt,0);
   }

    public void setMessage(String cpt, float s) {

        if (cpt == null || cpt.isEmpty()) {
            if (mainMessage != null) {
                nodeWnd.detachChild(mainMessage);
            }
            return;
        }

        if (mainMessage == null || !mainMessage.getFont().equals(rKeeper.getRess().getFont("Колибри"))) {
            if (mainMessage != null) {
                nodeWnd.detachChild(mainMessage);
               // mainMessage = null;
            }
            mainMessage = new BitmapText(rKeeper.getRess().getFont("Колибри"));
            mainMessage.setBox(new Rectangle(0, 0, Dx * 2 - textPad() * 2, Dy));
            mainMessage.setName(getName() + "_msg");
            if (kind!=PU_KIND.CUSTOM)
            {
            mainMessage.setAlignment(BitmapFont.Align.Left);
            }
            else
            {
                mainMessage.setAlignment(BitmapFont.Align.Center);
            }
            mainMessage.setVerticalAlignment(BitmapFont.VAlign.Center);
            mainMessage.setLineWrapMode(LineWrapMode.Word);
            mainMessage.setQueueBucket(RenderQueue.Bucket.Translucent);
            nodeWnd.attachChild(mainMessage);
            mainMessage.setLocalTranslation(-Dx + textPad(), Dy / 3f * 2f, geo[4].getLocalTranslation().z + 0.05f);
        }

        mainMessage.setSize(s == 0 ? Dy * 0.25f : s);
        mainMessage.setText(cpt);
        mainMessage.setColor(ColorRGBA.White);
    }

    public void setTableMessage(String cpt, float s, String rx) {
        if (cpt == null || cpt.isEmpty()) {
            if (mainMessage != null) {
                nodeWnd.detachChild(mainMessage);
            }
            return;
        }

        if (mainMessage == null || mainMessage.getFont() != rKeeper.getRess().getFont("Моно")) {
            if (mainMessage != null) {
                nodeWnd.detachChild(mainMessage);
            }
            mainMessage = new BitmapText(rKeeper.getRess().getFont("Моно"));
            mainMessage.setName(getName() + "_msg");
            mainMessage.setBox(new Rectangle(0, 0, Dx * 2 - textPad() * 2, Dy));
            mainMessage.setAlignment(BitmapFont.Align.Left);
            mainMessage.setVerticalAlignment(BitmapFont.VAlign.Center);
            mainMessage.setQueueBucket(RenderQueue.Bucket.Translucent);
            mainMessage.setLineWrapMode(LineWrapMode.Word);
            nodeWnd.attachChild(mainMessage);
            mainMessage.setLocalTranslation(-Dx + textPad(), Dy / 3f * 2f, geo[4].getLocalTranslation().z + 0.05f);
            
            mainMessage.setText(cpt);    
            mainMessage.setSize(s == 0 ? Dy * 0.25f : s);
            if (rx != null && !rx.isEmpty()) {
            mainMessage.setColor(rx, ColorRGBA.Yellow);
        }
            
        }
    }

   
//   private boolean close_menu;
//
//    public boolean isClose_menu() {
//        return close_menu;
//    }
//
//    public void setClose_menu(boolean close_menu) {
//        this.close_menu = close_menu;
//    }
    


    public IPUowner getMyOwner() {
        return myOwner;
    }

    public void setMyOwner(IPUowner myOwner) {
        this.myOwner = myOwner;
    }
  
    
    
    private void makeButtons() {
        //WOOD buttons
        float X1 = rKeeper.getRess().getTextX(SettBase.CARD_W * 4 + 90 + 64);
        float X2 = rKeeper.getRess().getTextX(SettBase.CARD_W * 4 + 90 + 64 + 124);
        float Y2 = rKeeper.getRess().getTextY(SettBase.CARD_H * 4);
        float Y1 = rKeeper.getRess().getTextY(SettBase.CARD_H * 4 + 76);

        Button3dBuilder builder = new Button3dBuilder(rKeeper).setIf(this);
        List<FaceState> fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X1, Y1, X2, Y1, X1, Y2, X2, Y2}));

        Button3d b3d = builder.setFaces(fsList).
                setPlace(-Dx / 1.8f, -Dy + SettBase.BTN_H * 1.2f, 0.3f).
                setSize(Button3d.BSIZE.SMALL).
                build("pubtn_left"+name);
        buttons.add(b3d);
        nodeWnd.attachChild(b3d.getGe());

        b3d = builder.setPlace(Dx / 1.8f, -Dy + SettBase.BTN_H * 1.2f, 0.3f).build("pubtn_right"+name);
        buttons.add(b3d);
        nodeWnd.attachChild(b3d.getGe());

        b3d = builder.setPlace(0, -Dy + SettBase.BTN_H * 1.2f, 0.3f).build("pubtn_middle"+name);
        buttons.add(b3d);
        nodeWnd.attachChild(b3d.getGe());
    }
  
    protected void makeMoreButtons() {
        
    }
     
  
    private void makeMenuButtons(int N) {
        //WOOD buttons
        float X1 = rKeeper.getRess().getTextX(SettBase.CARD_W * 4 + 90 + 64);
        float X2 = rKeeper.getRess().getTextX(SettBase.CARD_W * 4 + 90 + 64 + 124);
        float Y2 = rKeeper.getRess().getTextY(SettBase.CARD_H * 4);
        float Y1 = rKeeper.getRess().getTextY(SettBase.CARD_H * 4 + 76);

        float deltaY = (Dy * 2f) / (N + 2f);
        Button3dBuilder builder = new Button3dBuilder(rKeeper).setIf(this);
        List<FaceState> fsList = new ArrayList<>();
        fsList.add(new FaceState(rKeeper).init("ОбщийПрозрачный", new float[]{X1, Y1, X2, Y1, X1, Y2, X2, Y2}));

        builder.setFaces(fsList).setSize(Button3d.BSIZE.LONG_SMALL);

        Button3d b3d;
        for (int i = 0; i < N; i++) {
            b3d = builder.setPlace(0, Dy - deltaY * (i) - 1.3f, 0.11f).build(String.format("pubtn_menu%02d", i));
            buttons.add(b3d);
            nodeWnd.attachChild(b3d.getGe());
        }
    }
  
  
   private boolean isNoneBehaviour(String bName) {
        for (Button3d B : buttons) {
            if (B.getName().contains(bName)) {
                return B.getBehav() == Button3d.BEHAVIOR.NONE;
            }
        }
        return true;
    }

    public ArrayList<Button3d> getButtons() {
        return buttons;
    }
  
    public Button3d getButton(String s) {
        for (Button3d b3 : buttons) {
            if (b3.getName().equals(s)) {
                return b3;
            }
        }
        return null;
    }

  
   @Override
  public void clickButton(String name) {
//         if (name.contains("pubtn_left")) 
//         {
//             
//         }
//         else if (name.contains("pubtn_right")) 
//         {
//             
//         }
//         else if (name.contains("pubtn_middle")) 
//         {
//             
//         }
         
//      System.out.println("Click="+name+" kind="+kind+ " type="+puType+" Node="+nodeWnd.getName()+" buttons="+buttons.size());
      
         if (myOwner!=null) myOwner.popUpClosing(this,name);
         
         if (kind==PU_KIND.CUSTOM && !name.contains("pubtn_")){
             return;
         }
         
         if (kind==PU_KIND.MENU)
         {
          if (isNoneBehaviour(name)) 
             {
                 close();
                 myOwner.popUpClosed(this,name);
             }
             else{
                 ToolsBase.waiters.initWaiter(this,WAITERTYPE.CLOSE_WAIT,name);
             }
         }
         else
         {
         if (puType!=PU_TYPE.PREV_ONLY_CLS && puType!=PU_TYPE.NEXT_ONLY_CLS && puType!=PU_TYPE.NEXT_ONLY && puType!=PU_TYPE.PREV_NEXT && puType!=PU_TYPE.PREV_NEXT_CLS || name.contains("pubtn_middle")) 
             
             if (isNoneBehaviour(name)) 
             {
                 close();
                 myOwner.popUpClosed(this,name);
             }
             else{
                 ToolsBase.waiters.initWaiter(this,WAITERTYPE.CLOSE_WAIT,name);
             }
         }
    }
    
  
  
  @Override
  public void selectRadio(String name) {
    }

  @Override
  public void changeCheckbox(String name, boolean state) {
    }
    
  @Override
  public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        channel.setAnim("Idle");
        channel.setLoopMode(LoopMode.DontLoop);
        ToolsBase.waiters.checkActions(this);
   }
   
   
@Override
  public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }   
  
   private Vector3f vdiff;
    
    public void setDiff(Geometry plane,Vector3f v0)
    {//v0 точка, кде коснулись карты в координатах rootNode
        vdiff=v0.subtract(nodeWnd.getLocalTranslation());//вектор между центром ge и куда ткнули
        vdiff.z=0;
        nodeWnd.getParent().attachChild(plane);
        plane.setLocalTranslation(nodeWnd.getLocalTranslation());
    }
    
    public Vector3f getDiff()
    {
        return vdiff;
    }
    
    @Override
    public void execute(WaiterElement par) {
        close();
        if (myOwner!=null) myOwner.popUpClosed(this,par.getInfo());
    }
    
    
    @Override
    public AnimEventListener getAEL() {
        return this;
    }
    
}

