package rcs.feyn.math.linalg;

import rcs.feyn.math.Arithmetical;
import rcs.feyn.math.MathUtils;


public class Matrix implements Arithmetical<Matrix> {

  private final double[][] matrix;
  
  private final int N; //rows
  private final int M; //cols 

  public Matrix(double[][] matrix) {
    this.matrix = matrix;
    this.N = matrix.length;
    this.M = matrix[0].length;
  } 

  public Matrix(int n, int m) {
    this(new double[n][m]);
  } 

  public Matrix(Matrix that) {
    this(that.N, that.M);
    
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        this.matrix[i][j] = that.matrix[i][j];
      }
    }
  }

  public double[][] get() {
    return matrix;
  }

  public double get(int i, int j) {
    return matrix[i][j];
  }
 
  public void set(int i, int j, double val) {
    matrix[i][j] = val;
  }

  public int getN() {
    return N;
  }

  public int getM() {
    return M;
  }

  public double[] getRow(int row) {
    return matrix[row];
  }

  public double[] getCol(int col) {
    double[] column = new double[M];
    for (int i = 0; i < M; i++) {
      column[i] = matrix[i][col];
    }
    return column;
  }

  public Matrix getBlock(int a, int b, int n, int m) {
    if (a + n > N || b + m > M) {
      throw new IllegalArgumentException("Submatrix out of bounds.");
    }

    Matrix block = new Matrix(n, m);

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        block.matrix[i][j] = this.matrix[i+a][j+b];
      }
    }

    return block;
  }

  public void setBlock(int a, int b, Matrix that) {
    if (that.N + a > N || that.M + b > M) {
      throw new IllegalArgumentException("Submatrix out of bounds.");
    }

    for (int i = 0; i < that.N; i++) {
      for (int j = 0; j < that.M; j++) {
        this.matrix[i+a][j+b] = that.matrix[i][j];
      }
    }
  }

  @Override
  public Matrix add(Matrix that) {
    if (!sameDimensionsAs(that)) {
      throw new IllegalArgumentException("Cannot add matrices of different dimensions.");
    }

    Matrix result = new Matrix(N, M);

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        result.matrix[i][j] = this.matrix[i][j] + that.matrix[i][j];
      }
    }
    return result;
  }

  @Override
  public Matrix sub(Matrix that) {
    if (!sameDimensionsAs(that)) {
      throw new IllegalArgumentException("Cannot subtract matrices of different dimensions.");
    }

    Matrix result = new Matrix(N, M);

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        result.matrix[i][j] = this.matrix[i][j] - that.matrix[i][j];
      }
    }
    return result;
  }

  @Override
  public Matrix mul(Matrix that) {
    if (!canMultiplyWith(that)) {
      throw new IllegalArgumentException("Cannot multiply matrices of such dimensions.");
    }

    Matrix result = new Matrix(N, that.M);

    for (int i = 0; i < that.M; i++) {
      for (int j = 0; j < M; j++) {
        for (int k = 0; k < N; k++) {
          result.matrix[k][i] += this.matrix[k][j] * that.matrix[j][i];
        }
      }
    }
    return result;
  }

  public Matrix mul(double scalar) {
    Matrix result = new Matrix(N, M);

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        result.matrix[i][j] = this.matrix[i][j] * scalar;
      }
    }
    return result;
  }

  @Override
  public Matrix div(Matrix that) {
    return this.mul(that.inverse());
  }

  public double determinant() {
    if (!isSquare()) {
      throw new IllegalArgumentException("Non-square matrices do not have determinants.");
    }

    //recursion base case
    if (N == 2) {
      return MathUtils.determinant(matrix[0][0], matrix[0][1], matrix[1][0], matrix[1][1]); 
    }

    double det = 0;

    for (int i = 0; i < M; i++) {
      det += 
        Math.pow(-1, i)
        * matrix[0][i]
        * getBlock(1, 0, N-1, i).combineWith(getBlock(1, i+1, N-1, N-1-i)).determinant();
    }
    return det;
  } 
  
  /**
   * determinant helper method
   */
  private Matrix combineWith(Matrix that) {
    if (this.N != that.N) {
      throw new IllegalArgumentException("Cannot combine matrices with different number of rows.");
    }

    Matrix combined = new Matrix(this.N, this.M+that.M);

    if (this.M > 0) combined.setBlock(0, 0, this); 
    if (that.M > 0) combined.setBlock(0, M, that);

    return combined;
  }

  public Matrix transpose() {
    Matrix transposed = new Matrix(M, N);

    for (int i = 0; i < M; i++) {
      for (int j = 0; j < N; j++) {
        transposed.matrix[i][j] = this.matrix[j][i];
      }
    }
    return transposed;
  }

  public Matrix identity() {
    if (!isSquare()) {
      throw new IllegalArgumentException("Non-square matrices do not have identities.");
    }

    return identity(N);
  }

  public static Matrix identity(int N) {
    double[][] identity = new double[N][N];

    for (int i = 0; i < N; i++) {
      identity[i][i] = 1;
    }

    return new Matrix(identity);
  }

  public Matrix inverse() {
    if (!isSquare()) {
      throw new IllegalArgumentException("Non-square matrices do not have inverses.");
    }
    if (determinant() == 0) {
      throw new IllegalArgumentException("This matrix is not invertible.");
    }

    Matrix cpmatrix = new Matrix(this);
    Matrix identity = identity();

    for (int i = 0; i < M; i++) {
      int j        = i;
      int maxIndex = i;
      
      double maxVal = cpmatrix.matrix[i][j];
      for (int k = i+1; k < M; k++) {
        if (Math.abs(cpmatrix.matrix[k][j]) > Math.abs(maxVal)) {
          maxVal   = cpmatrix.matrix[k][j];
          maxIndex = k;
        }
      }
      if (maxVal != 0) {
        Matrix temp;
        
        temp = cpmatrix.getBlock(i, 0, 1, M);
        cpmatrix.setBlock(       i, 0, cpmatrix.getBlock(maxIndex, 0, 1, M));
        cpmatrix.setBlock(maxIndex, 0, temp);
        
        temp = identity.getBlock(i, 0, 1, M);
        identity.setBlock(       i, 0, identity.getBlock(maxIndex, 0, 1, M));
        identity.setBlock(maxIndex, 0, temp);

        for (int l = 0; l < M; l++) {
          cpmatrix.matrix[i][l] = cpmatrix.matrix[i][l] /maxVal;
          identity.matrix[i][l] = identity.matrix[i][l] /maxVal;
        }
      }
      for (int m = 0; m < M; m++) {
        if (m == i) {
          continue;
        }
        double factor = cpmatrix.matrix[m][i];
        for (int n = 0; n < M; n++) {
          cpmatrix.matrix[m][n] = cpmatrix.matrix[m][n] -factor * cpmatrix.matrix[i][n];
          identity.matrix[m][n] = identity.matrix[m][n] -factor * identity.matrix[i][n];
        }
      }
    }
    return identity;
  }

  public boolean isSquare() {
    return N == M;
  }
  
  public boolean canMultiplyWith(Matrix that) {
    return this.M == that.N;
  }

  public boolean sameDimensionsAs(Matrix that) {
    return this.N == that.N && this.M == that.N;
  }

  @Override
  public int hashCode() {
    int result = 17;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        result += 31 * result + MathUtils.hashDouble(matrix[i][j]);
      }
    }
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Matrix)) {
      return false;
    }
    
    Matrix that = (Matrix) obj;

    if (!this.sameDimensionsAs(that)) {
      return false;
    }

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        if (!MathUtils.epsilonEquals(this.matrix[i][j], that.matrix[i][j])) {
          return false;
        }
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
    int width = 0;
    for (int i = 0; i < N; i++) {
      int w = (matrix[i][j] + "").length();
      if (w > width) {
        width = w;
      }
    }
    return width;
  } 
}