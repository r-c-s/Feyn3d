package rcs.feyn.math.linalg;

import rcs.feyn.math.Arithmetical;
import rcs.feyn.math.Rational;

public class RationalMatrix implements Arithmetical<RationalMatrix> {

  private final Rational[][] matrix;
  private final int N; //rows
  private final int M; //cols

  public RationalMatrix(Rational[][] matrix) {
    this.matrix = matrix;
    this.N = matrix.length;
    this.M = matrix[0].length;
  }

  public RationalMatrix(int n, int m) {
    this(new Rational[n][m]);
  }

  public RationalMatrix(RationalMatrix that) {
    this(that.N, that.M);
    
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        this.matrix[i][j] = that.matrix[i][j];
      }
    }
  }

  public Rational[][] get() {
    return matrix;
  }

  public void set(int i, int j, Rational val) {
    this.matrix[i][j] = val;
  }

  public Rational get(int i, int j) {
    return this.matrix[i][j];
  }

  public int getN() {
    return this.N;
  }

  public int getM() {
    return this.M;
  }

  public Rational[] getRow(int row) {
    return matrix[row];
  }

  public Rational[] getCol(int col) {
    return transpose().matrix[col];
  }

  public RationalMatrix getBlock(int a, int b, int n, int m) {
    if (a+n > this.N || b+m > this.M) {
      throw new IllegalArgumentException("Submatrix out of bounds.");
    }

    RationalMatrix block = new RationalMatrix(n, m);

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        block.matrix[i][j] = this.matrix[i+a][j+b];
      }
    }
    return block;
  }

  public void setBlock(int a, int b, RationalMatrix that) {
    if (that.N+a > this.N || that.M+b > this.M) {
      throw new IllegalArgumentException("Submatrix out of bounds.");
    }

    for (int i = 0; i < that.N; i++) {
      for (int j = 0; j < that.M; j++) {
        this.matrix[i+a][j+b] = that.matrix[i][j];
      }
    }
  }

  @Override
  public RationalMatrix add(RationalMatrix that) {
    if (!this.sameDimensionAs(that)) {
      throw new IllegalArgumentException("Cannot add matrices of different dimensions.");
    }

    RationalMatrix result = new RationalMatrix(this.N, this.M);

    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.M; j++) {
        result.matrix[i][j] = this.matrix[i][j].add(that.matrix[i][j]);
      }
    }
    return result;
  }

  @Override
  public RationalMatrix sub(RationalMatrix that) {
    if (!this.sameDimensionAs(that)) {
      throw new IllegalArgumentException("Cannot subtract matrices of different dimensions.");
    }

    RationalMatrix result = new RationalMatrix(this.N, this.M);

    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.M; j++) {
        result.matrix[i][j] = this.matrix[i][j].sub(that.matrix[i][j]);
      }
    }
    return result;
  }

  @Override
  public RationalMatrix mul(RationalMatrix that) {
    if (!this.canMultiplyWith(that)) {
      throw new IllegalArgumentException("Cannot multiply matrices of such dimensions.");
    }

    RationalMatrix result = new RationalMatrix(this.N, that.M);

    for (int i = 0; i < this.M; i++) {
      for (int j = 0; j < this.M; j++) {
        for (int k = 0; k < this.N; k++) {
          result.matrix[k][i] = result.matrix[k][i].add(this.matrix[k][j].mul(that.matrix[j][i]));
        }
      }
    }
    return result;
  }

  public RationalMatrix mul(Rational scalar) {
    RationalMatrix result = new RationalMatrix(this.N, this.M);

    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.M; j++) {
         result.matrix[i][j] = this.matrix[i][j].mul(scalar);
      }
    }
    return result;
  }

  @Override
  public RationalMatrix div(RationalMatrix that) {
    return this.mul(that.inverse());
  }
  
  public Rational determinant() {
    if (!isSquare()) {
      throw new IllegalArgumentException("Non-square matrices do not have determinants.");
    }

    //recursion base case
    if (N == 2) {
      return matrix[0][0].mul(matrix[1][1]).sub(matrix[0][1].mul(matrix[1][0]));
    }

    Rational det = new Rational(0);
    for (int i = 0; i < M; i++) {
      det.addLocal(
        new Rational((int)Math.pow(-1, i)).mul(
          matrix[0][i].mul(
          getBlock(1, 0, N-1, i).combineWith(getBlock(1, i+1, N-1, N-1-i)).determinant())));
    }
    return det;
  }

  private RationalMatrix combineWith(RationalMatrix that) {
    if (this.N != that.N) {
      throw new IllegalArgumentException("Cannot combine matrices with different number of rows.");
    }

    RationalMatrix combined = new RationalMatrix(this.N, this.M+that.M);

    if (this.M > 0) combined.setBlock(0, 0, this);
    if (that.M > 0) combined.setBlock(0, M, that);

    return combined;
  }

  public RationalMatrix transpose() {
    RationalMatrix transposed = new RationalMatrix(this.M, this.N);

    for (int i = 0; i < this.M; i++) {
      for (int j = 0; j < this.N; j++) {
        transposed.matrix[i][j] = this.matrix[j][i];
      }
    }
    return transposed;
  }
  
  public RationalMatrix identity() {
    if (!isSquare()) {
      throw new IllegalArgumentException("Non-square matrices do not have identities.");
    }

    return identity(N);
  }
  
  public static RationalMatrix identity(int N) {
    Rational[][] identity = new Rational[N][N];

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        identity[i][j] = new Rational(i == j ? 1 : 0);
      }
    }

    return new RationalMatrix(identity);
  }
  
  public RationalMatrix inverse() {
    if (!this.isSquare()) {
      throw new IllegalArgumentException("Non-square matrices do not have inverses.");
    }
    if (this.determinant().equals(Rational.ZERO)) {
      throw new IllegalArgumentException("This matrix is not invertible.");
    }

    RationalMatrix cpmatrix = new RationalMatrix(this);
    RationalMatrix identity = identity(N);

    for (int i = 0; i < this.M; i++) {
      int j        = i;
      int maxIndex = i;
      
      Rational maxVal = cpmatrix.get(i, j);
      for (int k = i+1; k < this.M; k++) {
        if (Math.abs(cpmatrix.matrix[k][j].doubleValue()) > Math.abs(maxVal.doubleValue())) {
          maxVal   = cpmatrix.matrix[k][j];
          maxIndex = k;
        }
      }
      
      if (!maxVal.equals(Rational.ZERO)) {
        RationalMatrix temp;
        
        temp = cpmatrix.getBlock(i, 0, 1, M);
        cpmatrix.setBlock(       i, 0, cpmatrix.getBlock(maxIndex, 0, 1, M));
        cpmatrix.setBlock(maxIndex, 0, temp);
        
        temp = identity.getBlock(i, 0, 1, M);
        identity.setBlock(       i, 0, identity.getBlock(maxIndex, 0, 1, M));
        identity.setBlock(maxIndex, 0, temp);
        
        for (int l = 0; l < M; l++) {
          cpmatrix.matrix[i][l] = cpmatrix.matrix[i][l].div(maxVal);
          identity.matrix[i][l] = identity.matrix[i][l].div(maxVal);
        }
      }
      
      for (int m = 0; m < M; m++) {
        if (m == i) {
          continue;
        }
        Rational factor = cpmatrix.matrix[m][i];
        for (int n = 0; n < this.M; n++) {
          cpmatrix.matrix[m][n] = cpmatrix.matrix[m][n].sub(factor.mul(cpmatrix.matrix[i][n]));
          identity.matrix[m][n] = identity.matrix[m][n].sub(factor.mul(identity.matrix[i][n]));
        }
      }
    }
    return identity;
  }
  
  public Matrix toDoubleMatrix() {
    double[][] mx = new double[this.N][this.M];

    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.M; j++) {
        mx[i][j] = matrix[i][j].doubleValue();
      }
    }

    return new Matrix(mx);
  }
  
  public boolean isSquare() {
    return this.N == this.M;
  }

  public boolean canMultiplyWith(RationalMatrix that) {
    return this.M == that.N;
  }
  
  public boolean sameDimensionAs(RationalMatrix that) {
    return this.N == that.N && this.M == that.N;
  }
  
  @Override
  public int hashCode() {
    int result = 17;
    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.M; j++) {
        result += 31 * result + matrix[i][j].hashCode();
      }
    }
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof RationalMatrix)) {
      return false;
    }
    
    RationalMatrix that = (RationalMatrix) obj;
    
    if (!this.sameDimensionAs(that)) {
      return false;
    }

    for (int i = 0; i < this.N; i++) {
      for (int j = 0; j < this.M; j++) {
        if (this.matrix[i][j].compareTo(that.matrix[i][j]) != 0)
          return false;
      }
    }
    return true;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int numSpaces = 2;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        sb.append(String.format("%" + (widthOfColumn(j) + numSpaces) + "s", matrix[i][j]));
      }
      sb.append("\n");
    }
    return sb.toString();
  }
  
  private int widthOfColumn(int j) {
    int max = 0;
    for (int i = 0; i < this.N; i++) {
      int width = this.matrix[i][j].toString().length();
      if (width > max) {
        max = width;
      }
    }
    return max;
  }
}