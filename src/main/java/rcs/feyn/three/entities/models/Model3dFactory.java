package rcs.feyn.three.entities.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rcs.feyn.math.TrigLookUp;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.geo.GeoUtils3d;
import rcs.feyn.math.MathConsts;

public class Model3dFactory { 

  public static Model3dBuilder hexahedron(double radius) {
    return cube(radius * MathConsts.SQURT_3 * TrigLookUp.sin(45));
  }

  public static Model3dBuilder dodecahedron(double radius) {
    Model3dBuilder dodecahedron = new Model3dBuilder();
    
    double phi = MathConsts.PHI;
    
    dodecahedron.addVertex(signPermutations(new Vector3d(    1,    1,      1)));
    dodecahedron.addVertex(signPermutations(new Vector3d(    0, 1/phi,   phi)));
    dodecahedron.addVertex(signPermutations(new Vector3d(1/phi,   phi,     0)));
    dodecahedron.addVertex(signPermutations(new Vector3d(  phi,     0, 1/phi))); 
    
    for (Vector3d vertex : dodecahedron.vertices) {
      vertex.normalizeLocal();
      dodecahedron.addNormal(new Vector3d(vertex));
      vertex.mulLocal(radius);
    }

    dodecahedron.addFace( 8,  0, 12, 13,  1);
    dodecahedron.addFace(12,  3, 10,  6, 13);
    dodecahedron.addFace( 0, 16, 18,  3, 12);
    dodecahedron.addFace(13,  6, 19, 17,  1);
    dodecahedron.addFace( 1, 17,  4,  9,  8);
    dodecahedron.addFace( 8,  9,  2, 16,  0);
    dodecahedron.addFace( 9,  4, 15, 14,  2);
    dodecahedron.addFace(14, 15,  5, 11,  7);
    dodecahedron.addFace(10, 11,  5, 19,  6);
    dodecahedron.addFace( 3, 18,  7, 11, 10);
    dodecahedron.addFace(19,  5, 15,  4, 17);
    dodecahedron.addFace(16,  2, 14,  7, 18);
    
    return dodecahedron;
  }

  public static Model3dBuilder icosahedron(double radius) {
    return icosphere(radius, 0);
  }

  public static Model3dBuilder icosphere(double radius, double recursionLevel) {
    double t = (1.0 + Math.sqrt(5.0)) / 2.0;
    
    ArrayList<Vector3d> vertices = new ArrayList<>();
    vertices.add(new Vector3d(-1,  t,  0)); 
    vertices.add(new Vector3d( 1,  t,  0)); 
    vertices.add(new Vector3d(-1, -t,  0)); 
    vertices.add(new Vector3d( 1, -t,  0)); 
    vertices.add(new Vector3d( 0, -1,  t)); 
    vertices.add(new Vector3d( 0,  1,  t)); 
    vertices.add(new Vector3d( 0, -1, -t)); 
    vertices.add(new Vector3d( 0,  1, -t)); 
    vertices.add(new Vector3d( t,  0, -1));
    vertices.add(new Vector3d( t,  0,  1));
    vertices.add(new Vector3d(-t,  0, -1));
    vertices.add(new Vector3d(-t,  0,  1));
    
    ArrayList<Integer[]> patches = new ArrayList<>();
    patches.add(new Integer[]{ 0, 11,  5}); 
    patches.add(new Integer[]{ 0,  5,  1}); 
    patches.add(new Integer[]{ 0,  1,  7}); 
    patches.add(new Integer[]{ 0,  7, 10});
    patches.add(new Integer[]{ 0, 10, 11});
    patches.add(new Integer[]{ 1,  5,  9}); 
    patches.add(new Integer[]{ 5, 11,  4}); 
    patches.add(new Integer[]{11, 10,  2});
    patches.add(new Integer[]{10,  7,  6}); 
    patches.add(new Integer[]{ 7,  1,  8}); 
    patches.add(new Integer[]{ 3,  9,  4}); 
    patches.add(new Integer[]{ 3,  4,  2}); 
    patches.add(new Integer[]{ 3,  2,  6}); 
    patches.add(new Integer[]{ 3,  6,  8}); 
    patches.add(new Integer[]{ 3,  8,  9}); 
    patches.add(new Integer[]{ 4,  9,  5}); 
    patches.add(new Integer[]{ 2,  4, 11});
    patches.add(new Integer[]{ 6,  2, 10});
    patches.add(new Integer[]{ 8,  6,  7}); 
    patches.add(new Integer[]{ 9,  8,  1});  
    
    HashMap<Long, Integer> cache = new HashMap<>();
    
    for (int h = 0; h < recursionLevel; h++) {
      ArrayList<Integer[]> temp = new ArrayList<>();
      for (Integer[] face : patches) {        
        int i = face[0];
        int j = face[1];
        int k = face[2];

        Long keyij = ((long) Math.min(i, j) << 32) + Math.max(i, j);
        Long keyjk = ((long) Math.min(j, k) << 32) + Math.max(j, k);
        Long keyki = ((long) Math.min(k, i) << 32) + Math.max(k, i);
        
        Integer x = cache.get(keyij);
        Integer y = cache.get(keyjk);
        Integer z = cache.get(keyki); 

        if (x == null) {
          vertices.add(vertices.get(i).midPoint(vertices.get(j)));
          x = vertices.size()-1;
          cache.put(keyij, x);
        }
        if (y == null) {
          vertices.add(vertices.get(j).midPoint(vertices.get(k)));
          y = vertices.size()-1;
          cache.put(keyjk, y);
        }
        if (z == null) {
          vertices.add(vertices.get(k).midPoint(vertices.get(i)));
          z = vertices.size()-1;
          cache.put(keyki, z);
        } 
        
        temp.add(new Integer[]{i, x, z});
        temp.add(new Integer[]{j, y, x});
        temp.add(new Integer[]{k, z, y});
        temp.add(new Integer[]{x, y, z});
      }
      patches = temp;
    }
    
    Model3dBuilder icosphere = new Model3dBuilder() 
      .addVertex(vertices.toArray(new Vector3d[]{}))
      .addPatch(patches.toArray(new Integer[][]{}));
    
    for (Vector3d vertex : vertices) {
      vertex.normalizeLocal();
      icosphere.addNormal(new Vector3d(vertex));
      vertex.mulLocal(radius);
    }
    
    return icosphere;
  }

  public static Model3dBuilder octahedron(double radius) {
    Model3dBuilder octahedron = new Model3dBuilder();

    octahedron.addVertex( radius,       0,       0);
    octahedron.addVertex(      0,       0, -radius);
    octahedron.addVertex(-radius,       0,       0);
    octahedron.addVertex(      0,       0,  radius);
    octahedron.addVertex(      0,  radius,       0);
    octahedron.addVertex(      0, -radius,       0); 

    for (Vector3d vertex : octahedron.vertices) {
      octahedron.addNormal(vertex.normalize());
    }

    octahedron.addFace(4, 0, 1);
    octahedron.addFace(4, 1, 2);
    octahedron.addFace(4, 2, 3);
    octahedron.addFace(4, 3, 0);
    octahedron.addFace(5, 0, 3);
    octahedron.addFace(5, 1, 0);
    octahedron.addFace(5, 2, 1);
    octahedron.addFace(5, 3, 2);
    
    return octahedron;
  }

  public static Model3dBuilder tetrahedron(double radius) {
    Model3dBuilder tetrahedron = new Model3dBuilder();
    
    tetrahedron.addVertex( 1,  1,  1);
    tetrahedron.addVertex( 1, -1, -1);
    tetrahedron.addVertex(-1,  1, -1);
    tetrahedron.addVertex(-1, -1,  1);

    for (Vector3d vertex : tetrahedron.vertices) {
      vertex.normalizeLocal();
      tetrahedron.addNormal(new Vector3d(vertex));
      vertex.mulLocal(radius);
    }

    tetrahedron.addFace(0, 1, 2);
    tetrahedron.addFace(0, 3, 1);
    tetrahedron.addFace(0, 2, 3);
    tetrahedron.addFace(2, 1, 3);
    
    return tetrahedron;
  } 

  public static Model3dBuilder prism(double radius, double length, int sides) {
    if (sides < 3) {
      throw new IllegalArgumentException("Error: sides of prism must be at least 3.");
    }
    
    Model3dBuilder prism = new Model3dBuilder();

    double deg = MathConsts.PI / (double) sides;
    
    for (int i = 0; i < sides*2; i+=2) {
      Vector3d point = Vector3d.fromSpherical(radius, i*deg, 0);
      
      prism.addVertex(point.add(0, length / 2, 0))
           .addVertex(point.sub(0, length / 2, 0));
      
      prism.addNormal(point.add(0, length / 2, 0))
           .addNormal(point.sub(0, length / 2, 0));
    }  
    
    for (int i = 0; i < sides*2; i+=2) { 
      prism.addFace(i, i+1, (i+3) % (sides*2), (i+2) % (sides*2));  
    }
    
    Integer[] top = new Integer[sides];
    Integer[] bottom = new Integer[sides];
    for (int i = 0, j = 0; i < sides*2; i+=2, j++) {
      top[j] = i;
      bottom[j] = sides * 2 - i-1;
    }
    
    prism.addPatch(top).addPatch(bottom);

    return prism;
  }

  public static Model3dBuilder pyramid(double radius, double height, int sides) {
    if (sides < 3) {
      throw new IllegalArgumentException("Error: sides of pyramid must be at least 3.");
    }
    
    Model3dBuilder pyramid = new Model3dBuilder();

    double deg = MathConsts.TWO_PI / (double) sides;
    double off = MathConsts.SQURT_2 * height / 2;

    for (int i = 0; i < sides; i++) {
      pyramid.addVertex(Vector3d.fromSpherical(radius, i*deg, 0).subLocal(0, height - off, 0)); 
    }
    pyramid.addVertex(0, off, 0);
    
    for (int i = 0; i < sides; i++) {
      pyramid.addFace(i, (i+1) % (sides), sides);
    }
    Integer[] bottom = new Integer[sides];
    for (int i = 0; i < sides; i++) {
      bottom[i] = sides - 1 - i;
    }
    pyramid.addPatch(bottom);
    
    return pyramid;
  }

  public static Model3dBuilder cube(double size) {
    return rectangle(size, size, size);
  }

  public static Model3dBuilder rectangle(double xDim, double yDim, double zDim) {
    double x = xDim / 2;
    double y = yDim / 2;
    double z = zDim / 2;
    
    Model3dBuilder rectangle = new Model3dBuilder();

    rectangle.addVertex( x,  y,  z);
    rectangle.addVertex(-x,  y,  z);
    rectangle.addVertex( x, -y,  z);
    rectangle.addVertex( x,  y, -z);
    rectangle.addVertex(-x, -y,  z);
    rectangle.addVertex(-x,  y, -z);
    rectangle.addVertex( x, -y, -z);
    rectangle.addVertex(-x, -y, -z);
    
    for (Vector3d vertex : rectangle.vertices) {
      rectangle.addNormal(quandrant(vertex));
    }

    rectangle.addFace(1, 4, 2, 0);
    rectangle.addFace(0, 2, 6, 3);
    rectangle.addFace(3, 6, 7, 5);
    rectangle.addFace(5, 7, 4, 1);
    rectangle.addFace(5, 1, 0, 3);
    rectangle.addFace(4, 7, 6, 2);
    
    return rectangle; 
  } 

  public static Model3dBuilder cone(double radius, double height) {
    return pyramid(radius, height, GeoUtils3d.getNumberOfSidesOfCircle(radius));
  }

  public static Model3dBuilder cylinder(double radius, double length) {
    return prism(radius, length, GeoUtils3d.getNumberOfSidesOfCircle(radius));
  }

  public static Model3dBuilder torus(double innerRadius, double outerRadius) {
    return torus(innerRadius, outerRadius, 
        GeoUtils3d.getNumberOfSidesOfCircle(outerRadius), 
        GeoUtils3d.getNumberOfSidesOfCircle((outerRadius-innerRadius) / 2.0));
  }

  public static Model3dBuilder torus(double innerRadius, double outerRadius, int numI, int numJ) {
    Model3dBuilder torus = new Model3dBuilder();

    double degI = MathConsts.TWO_PI / numI;
    double degJ = MathConsts.TWO_PI / numJ;
    
    double radiusOfSlice = (outerRadius - innerRadius) / 2.0;
    
    for (int i = 0; i < numI; i++) {
      Vector3d centerOfSlice = new Vector3d(innerRadius + radiusOfSlice, 0, 0)
          .rotateLocal(Vector3d.Z_AXIS, i*degI);
    
      for (int j = 0; j < numJ; j++) {
        Vector3d point = Vector3d.fromSpherical(radiusOfSlice, j*degJ, 0)
            .addLocal(centerOfSlice)
            .rotateLocal(centerOfSlice, Vector3d.Z_AXIS, i*degI);
        torus.addVertex(point);
        torus.addNormal(point.sub(centerOfSlice).normalizeLocal());
        torus.addFace(
            i*numJ + j, 
            i*numJ + (j+1)%numJ, 
            (i+1)%numI*numJ + (j+1)%numJ, 
            (i+1)%numI*numJ + j);
      } 
    }
    
    return torus;
  } 

  private static Vector3d quandrant(Vector3d vertex) {
    return vertex.pointWiseDiv(vertex)
        .normalizeLocal()
        .pointWiseMulLocal(
            Math.signum(vertex.x()),
            Math.signum(vertex.y()),
            Math.signum(vertex.z()));
  }

  private static Vector3d[] signPermutations(Vector3d point) {
    Set<Vector3d> set = new HashSet<>();
    
    set.add(point.pointWiseMul( 1, 1, 1));
    set.add(point.pointWiseMul(-1, 1, 1)); 
    set.add(point.pointWiseMul( 1,-1, 1));
    set.add(point.pointWiseMul( 1, 1,-1));
    set.add(point.pointWiseMul(-1,-1, 1));
    set.add(point.pointWiseMul(-1,-1,-1));
    set.add(point.pointWiseMul(-1, 1,-1));
    set.add(point.pointWiseMul( 1,-1,-1));
    set.add(point.pointWiseMul(-1,-1,-1));
    
    return set.toArray(new Vector3d[]{});
  }; 
}