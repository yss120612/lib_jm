/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.Stack;

/**
 *
 * @author ys
 */
public class VectorsPool {

    private final Stack<Vector3f> v3fs = new Stack<>();
    private final Stack<Vector2f> v2fs = new Stack<>();
    private final Stack<Ray> rays = new Stack<>();
    ;
    private final Stack<Quaternion> qts = new Stack<>();
    private final Stack<CollisionResults> colls = new Stack<>();
    ;
    private final Stack<InputEvent> events = new Stack<>();
    private final Stack<Plane> planes = new Stack<>();
    private final Stack<NetPacket> npkts = new Stack<>();
    private final Stack<WaiterElement> welts = new Stack<>();
    public int log3fget, log2fget, logqtget, logcollget, lograyget, logeventget, logplanesget, lognpget, logweget;

    public VectorsPool() {
        init();
    }

    private void init() {
        log3fget = 0;
        log2fget = 0;
        lograyget = 0;
        logqtget = 0;
        logcollget = 0;
        logeventget = 0;
        logplanesget = 0;
        lognpget = 0;
        logweget = 0;
    }

    public Vector2f getV2(Vector2f v) {
        return getV2().set(v);
    }

    public Vector2f getV2(float x, float y) {
        return getV2().set(x, y);
    }

    public Vector2f getV2() {
        log2fget++;
        if (v2fs.empty()) {
            return new Vector2f(0f, 0f);
        }
        return v2fs.pop();
    }

    public void freeV2(Vector2f v) {
        if (v == null) {
            throw new RuntimeException("Attempt to free null Vector2f in VectorPool class");
        }
        v2fs.push(v);
    }

    public InputEvent getEvent(InputEvent.EVENTTYPE et) {
        logeventget++;
        if (events.empty()) {
            return new InputEvent(et);
        }
        return events.pop().init(et);
    }

    public void freeEvent(InputEvent v) {
        if (v == null) {
            throw new RuntimeException("Attempt to free null Event in VectorPool class");
        }
        events.push(v);
    }

    public Plane getPlane(Vector3f v0, Vector3f v1, Vector3f v2) {
        logplanesget++;
        Plane P;
        if (planes.isEmpty()) {
            P = new Plane();
        } else {
            P = planes.pop();
        }
        P.setPlanePoints(v0, v1, v2);
        return P;
    }

    public void freePlane(Plane p) {
        planes.push(p);
    }

    public Vector3f getV3(Vector3f v) {
        return getV3().set(v);
    }

    public Vector3f getV3(float x, float y, float z) {

        return getV3().set(x, y, z);

    }

    public Vector3f getV3() {
        log3fget++;

        if (v3fs.empty()) {
            return new Vector3f(0f, 0f, 0f);
        }
        return v3fs.pop();
    }

    public void freeV3(Vector3f v) {
        if (v == null) {
            throw new RuntimeException("Attempt to free null Vector3f in VectorPool class");
        }
        v3fs.push(v);
    }

    public Quaternion getQt(Quaternion q) {
        return getQt().set(q);
    }

    public Quaternion getQt(float x, float y, float z) {

        return getQt().fromAngles(x, y, z);
    }

    public Quaternion getQt() {
        logqtget++;
        if (qts.empty()) {
            return new Quaternion();
        }
        return qts.pop();
    }

    public void freeQt(Quaternion q) {
        if (q == null) {
            throw new RuntimeException("Attempt to free null Quaternion in VectorPool class");
        }
        qts.push(q);
    }

    public CollisionResults getColl() {
        logcollget++;
        if (colls.empty()) {
            return new CollisionResults();
        }
        return colls.pop();
    }

    public void freeColl(CollisionResults c) {
        if (c == null) {
            throw new RuntimeException("Attempt to free null CollisionResults in VectorPool class");
        }
        c.clear();
        colls.push(c);
    }

    public Ray getRay(Vector3f o, Vector3f d) {
        Ray R = getRay();
        R.setOrigin(o);
        //d.normalize();
        float length = d.length();
        if (( length <= 0.99f  || length >= 1.01f) && length != 0f) {
            length = 1.0f / length;
            d.setX(d.getX() * length);
            d.setY(d.getY() * length);
            d.setZ(d.getZ() * length);
        }
        R.setDirection(d);
        return R;
    }

    public Ray getRay() {
        lograyget++;

        if (rays.empty()) {
            return new Ray();
        }
        return rays.pop();
    }

    public void freeRay(Ray r) {
        if (r == null) {
            throw new RuntimeException("Attempt to free null Ray in VectorPool class");
        }
        rays.push(r);
    }

    public NetPacket getNPacket() {
        lognpget++;
        if (npkts.empty()) {
            return new NetPacket();
        }
        return npkts.pop();
    }

    public void freeNPacket(NetPacket np) {
        if (np == null) {
            throw new RuntimeException("Attempt to free null NetPacket in VectorPool class");
        }
        npkts.push(np);
    }

    public WaiterElement getWElement() {
        logweget++;
        if (welts.empty()) {
            return new WaiterElement();
        }
        return welts.pop();
    }

    public void freeWElement(WaiterElement we) {
        if (we == null) {
            throw new RuntimeException("Attempt to free null WaiterElement in VectorPool class");
        }
        welts.push(we);
    }

    public String showDiag() {
        StringBuilder SB = new StringBuilder();
        SB.append("Vector2f: used:");
        SB.append(log2fget);
        SB.append(" pool size:");
        SB.append(v2fs.size());
        SB.append("\n");

        SB.append("Vector3f: used:");
        SB.append(log3fget);
        SB.append(" pool size:");
        SB.append(v3fs.size());
        SB.append("\n");

        SB.append("Quaternion: used:");
        SB.append(logqtget);
        SB.append(" pool size:");
        SB.append(qts.size());
        SB.append("\n");

        SB.append("Ray: used:");
        SB.append(lograyget);
        SB.append(" pool size:");
        SB.append(rays.size());
        SB.append("\n");

        SB.append("Collisions: used:");
        SB.append(logcollget);
        SB.append(" pool size:");
        SB.append(colls.size());
        SB.append("\n");

        SB.append("Events: used:");
        SB.append(logeventget);
        SB.append(" pool size:");
        SB.append(events.size());
        SB.append("\n");

        SB.append("Planes: used:");
        SB.append(logplanesget);
        SB.append(" pool size:");
        SB.append(planes.size());
        SB.append("\n");

        SB.append("NetPackets: used:");
        SB.append(lognpget);
        SB.append(" pool size:");
        SB.append(npkts.size());
        SB.append("\n");

        SB.append("WaiterElements: used:");
        SB.append(logweget);
        SB.append(" pool size:");
        SB.append(welts.size());
        SB.append("\n");

        return SB.toString();
    }
}
