package rcs.feyn.two.geo;

import rcs.feyn.math.linalg.Matrix33;
import rcs.feyn.math.linalg.Vector2d;

// todo
public class Ray2d implements Movable2d, Transformable2d {
    
    private Vector2d origin;
    private Vector2d normal;
    
    public static Ray2d xAxis() {
      return new Ray2d(Vector2d.ZERO, Vector2d.X_AXIS);
    }  

    public static Ray2d yAxis() {
      return new Ray2d(Vector2d.ZERO, Vector2d.Y_AXIS);
    }  

    public Ray2d(Vector2d origin, Vector2d normal) {
      this.origin = new Vector2d(origin);
      this.normal = new Vector2d(normal).normalizeLocal();
    }
    
    @Override
    public void transform(Matrix33 transform) {
        origin.affineTransformLocal(transform);
        normal.affineTransformLocal(transform.extractRotation());
    }

    @Override
    public void translate(Vector2d delta) {
        origin.addLocal(delta);
    }

    @Override
    public int hashCode() {
      int result = 17;
      result = 31 * result + origin.hashCode();
      result = 31 * result + normal.hashCode();
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof Ray2d)) {
        return false;
      }
      
      Ray2d that = (Ray2d) obj;
      
      return this.origin.equals(that.origin) && 
             this.normal.equals(that.normal);
    }

    @Override
    public String toString() {
      return String.format("%s:[p=%s,n=%s]", this.getClass().getName(), origin, normal);
    } 
}
