/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author ys
 */
public class SettBase {
   //время анимации сек.
   public static float animTime = 0.6f; 
   public final static float helpSize = 0.37f;
   
   public final static int CARD_H=96;
   public final static int CARD_W=71;  
   public static float CARD_RATIO=CARD_H/(float)CARD_W;  
   
   public static String netSeparator="&";
   
   public final static float cardW(){return scrW>900?1f:0.9f;}//ширина карты GL
   public static float cardH=1f;//ширина карты GL (высота вычисляется пропорционально текстуре)
   
   public static float scrH=0;
   public static float scrW;
   public static float getSCRatio() 
   {
       if (scrH>0) return scrW/scrH;
       return 1f;
   }
   
   public final static float th=0.005f;//толщина карты
   
   
   
   public static float deskHw=7.6f;   //половинные размеры стола
   public final static  float deskHh=4.15f;//половинные размеры стола
   public final static  float deskT=0.02f;//толщина стола
   public static boolean sound_on=false;//звук
   public static boolean screen_on=false;//гасить экран
   public static String youName1="Ты";
   public static String youName2="You";
   public static String youName=youName1;
   //public static String forInputDialog="";
   public static int lang=1;
   //подсветка карты если все НЕ ОК
   public static final ColorRGBA selClrBad=new ColorRGBA(1.0f,0.6f,0.6f,1);
   //подсветка карты если все ОК
   public static final ColorRGBA selClr=new ColorRGBA(0.6f,1,0.6f,1);
   
   public static float amimBtnTime = 0.2f;
   public final static float BTN_H=0.4f;
   public final static float BTN_W=0.7f;
   public final static float BTN_T=0.01f;
   public final static float DELTA_PRESS=0.1f;
    
   //размер текстуры монеты
//   public final static int COIN_H=128;
//   public final static int COIN_W=210;
//    //размер текстуры сукна
//   public final static int DTOP_W=128;
//   public final static int DTOP_H=128;
    
   public final static float b3dX=0f;
   public final static float b3dY=5f;
   public final static float b3dZ=2f;
   
   public static boolean is_net=false;
   
   
}
