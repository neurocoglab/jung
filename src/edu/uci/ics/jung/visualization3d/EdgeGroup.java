/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 */
package edu.uci.ics.jung.visualization3d;

/**
 */
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4f;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

/**
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 */public class EdgeGroup<E> extends TransformGroup {

	 E edge;
	 Node shape;
	 
	 public EdgeGroup(E edge, Node shape) {
		 this.edge = edge;
		 this.shape = shape;
		 setCapability(TransformGroup.ENABLE_PICK_REPORTING);

//		 Cylinder cylinder = new Cylinder(radius, 1, 
//				 Cylinder.GENERATE_NORMALS |
//				 Cylinder.GENERATE_TEXTURE_COORDS |
//				 Cylinder.ENABLE_GEOMETRY_PICKING,
//				 26, 26, look);

		 Transform3D t = new Transform3D();
		 t.setTranslation(new Vector3f(0.f, .5f, 0.f));
		 TransformGroup group = new TransformGroup(t);
		 group.addChild(shape);
		 addChild(group);
	 }
	 
	 public String toString() { return edge.toString(); }
	 
	 public void setEndpoints(Point3f p0, Point3f p1) {
		 
		 // calculate length
		 float length = p0.distance(p1);
		 
		 // transform to accumulate values
		 Transform3D tx = new Transform3D();
		 
		 // translate so end is at p0
		 Transform3D p0tx = new Transform3D();
		 p0tx.setTranslation(new Vector3f(p0.getX(),p0.getY(),p0.getZ()));

		 // scale so length is dist p0,p1
		 Transform3D scaletx = new Transform3D();
		 scaletx.setScale(new Vector3d(1,length,1));

		 Vector3f yunit = new Vector3f(0,1,0);
		 
		 Vector3f v = new Vector3f(p1.getX()-p0.getX(), p1.getY()-p0.getY(), p1.getZ()-p0.getZ());
		 
		 Vector3f cross = new Vector3f();
		 cross.cross(yunit, v);
		 // cross is the vector to rotate about
		 float angle = yunit.angle(v);
		 
		 Transform3D rot = new Transform3D();
		 rot.setRotation(new AxisAngle4f(cross.getX(),cross.getY(),cross.getZ(),angle));
		 tx.mul(rot);

		 tx.mul(scaletx);
		 tx.setTranslation(new Vector3f(p0.getX(),p0.getY(),p0.getZ()));

		 try {
			 setTransform(tx);
		 } catch(Exception ex) {
			 System.err.println("tx = \n"+tx);
		 }
	 }
 }
