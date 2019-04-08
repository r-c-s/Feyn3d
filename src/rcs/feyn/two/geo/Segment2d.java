package rcs.feyn.two.geo;

import rcs.feyn.math.linalg.Matrix33;
import rcs.feyn.math.linalg.Vector2d;

//todo
public class Segment2d implements Movable2d, Transformable2d {

    private Vector2d a;
    private Vector2d b;

    public Segment2d(Vector2d a, Vector2d b) {
      this.a = new Vector2d(a);
      this.b = new Vector2d(b);
    }

    public Vector2d getA() {
      return new Vector2d(a);
    }

    public void setA(Vector2d a) {
      this.a.set(a);
    }

    public Vector2d getB() {
      return new Vector2d(b);
    }

    public void setB(Vector2d b) {
      this.b.set(b);
    }

    public Vector2d midPoint() {
      return a.midPoint(b);
    }

    public double length() {
      return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
      return a.distanceSquared(b);
    }

    public Vector2d getOrigin() {
      return midPoint();
    }

    public void setOrigin(Vector2d position) {
      translate(position.sub(midPoint()));
    }

    @Override
    public void translate(Vector2d v3d) {
      a.addLocal(v3d);
      b.addLocal(v3d);
    } 
    
    @Override
    public void transform(Matrix33 transform) {
      a.affineTransformLocal(transform);
      b.affineTransformLocal(transform);
    }

    public Vector2d closestPoint(Vector2d q) {
      Vector2d w = q.sub(a);
      Vector2d diff = b.sub(a);
      double dot = w.dotProd(diff);
      if (dot <= 0) {
        return a;
      }
      else {
        double dotMax = diff.dotProd(diff);
        if (dot >= dotMax) {
          return b;
        }
        else {
          return a.add(diff.mul(dot/dotMax));
        }
      }
    }

    public double distance(Vector2d q) {
      return Math.sqrt(distanceSquared(q));
    }

    public double distanceSquared(Vector2d q) {
      return q.sub(closestPoint(q)).normSquared();
    }

    @Override
    public int hashCode() {
      int result = 17;
      result = 31 * result + a.hashCode();
      result = 31 * result + b.hashCode();
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof Segment2d)) {
        return false;
      }
      
      Segment2d that = (Segment2d) obj;
      
      return this.a.equals(that.a) && 
             this.b.equals(that.b);
    }

    @Override
    public String toString() {
      return String.format("%s:[a=%s,b=%s]", this.getClass().getName(), a, b);
    }
}
