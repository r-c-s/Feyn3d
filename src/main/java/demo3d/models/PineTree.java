package demo3d.models;

import static rcs.feyn.three.render.RenderOptions3d.Option.gouraudShaded;
import static rcs.feyn.three.render.RenderOptions3d.Option.flatShaded;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import rcs.feyn.color.FeynColor;
import rcs.feyn.math.MathConsts;
import rcs.feyn.math.Vector3d;
import rcs.feyn.three.entities.models.Model3d;
import rcs.feyn.three.entities.models.Model3dFace;
import rcs.feyn.three.entities.models.Model3dFactory;
import rcs.feyn.three.entities.models.Model3dTexturedFace;
import rcs.feyn.three.entities.models.Model3dUtils;
import rcs.feyn.three.entities.models.Model3dVertices;
import rcs.feyn.three.gfx.TextureRaster;

/**
 * todo: create CompositeModel3d that encapsulates various Model3d's
 */
public class PineTree extends Model3d {
  
  private static final TextureRaster foliageTexture = 
      Model3dUtils.getImageData(System.getProperty("user.dir") + "/textures/foliage.jpg");

  private final Model3d cone;  
  private final Model3d[] branches = new Model3d[75];
  
  public PineTree(Vector3d position, int height) {
    
    cone = Model3dFactory
        .pyramid(height / 100d, height, 5)
        .setPosition(position)
        .setColor(FeynColor.saddleBrown)
        .build();

    double coneCenterOffset = height - MathConsts.SQURT_2 * height / 2;
    cone.translate(0, coneCenterOffset, 0);
    
    for (int i = 0; i < branches.length; i++) {
      double y = height * 0.1 + (i / (double) branches.length) * (height * 0.9);
      
      Vector3d branchPosition = position.add(0, y, 0);
      
      double maxBranchHeight = height / 5d;
      double branchHeight = maxBranchHeight - ((i / (double) branches.length) * maxBranchHeight);
      
      Model3d branch = Model3dFactory
          .pyramid(height / 50d, branchHeight, 3)
          .setTextureData(foliageTexture)
          .setPosition(branchPosition)
          .build();
      
      double branchCenterOffset = branchHeight - MathConsts.SQURT_2 * branchHeight / 2;
      branch.rotate(
          branchPosition.y(y - branchCenterOffset), 
          Vector3d.getRandomUnitVector().y(0), 
          -MathConsts.HALF_PI);
      
      branches[i] = branch;
    }
    
    Vector3d[] allVertices = Stream.concat(
        Arrays.stream(cone.getVertices().getVertices()),
        Arrays.stream(branches).flatMap(model -> Arrays.stream(model.getVertices().getVertices())))
        .toArray(Vector3d[]::new);
    
    int coneVerticesSize = cone.getVertices().size();
    
    Model3dFace[] allFaces = Stream.concat(
        Arrays.stream(cone.getFaces()),
        IntStream.range(0, branches.length)
          .mapToObj(i -> {
            Model3d model = branches[i];
            int modelVerticesLength = model.getVertices().size();
            return Arrays.stream(model.getFaces())
              .map(face -> {
                int[] adjustedIndices = Arrays.stream(face.getIndices()).map(j -> j + coneVerticesSize + i * modelVerticesLength).toArray();
                return new Model3dTexturedFace(adjustedIndices, ((Model3dTexturedFace) face).getTextureData()); 
              });
          })
          .collect(Collectors.toList())
          .stream()
          .flatMap(s -> s)
        )
        .toArray(Model3dFace[]::new);
    
    vertices = new Model3dVertices(allVertices);
    faces = allFaces;
   
    Model3dUtils.setOptions(
        this, 
        Set.of(flatShaded), 
        Set.of(gouraudShaded));
  }
}
