package rcs.feyn.math.linalg;

public class ParallelMatrixMultiplication {

  public static Matrix multiply(final Matrix A, final Matrix B, final int M) {
    if (!A.canMultiplyWith(B)) {
      throw new IllegalArgumentException("Cannot multiply such matrices.");
    }

    int AN = A.getN();
    int AM = A.getM();
    int BN = B.getN();
    int BM = B.getM();

    if (AN % M != 0 || AM % M != 0 || 
        BN % M != 0 || BM % M != 0) {
      throw new IllegalArgumentException("Block matrix out of bounds.");
    }
    
    Matrix result = new Matrix(AN, BM);

    final int processCount = (AN*BM)/(M*M);
    final int blockCount = AN/M;

    final Thread[] threads = new Thread[processCount];
    
    //partition matrix and assign blocks to a Multiply, and assign a Multiply to a thread
    for (int i = 0; i < processCount; i++) {
      
      Matrix[] blocksA = new Matrix[blockCount];  //array of blocks for matrix A
      Matrix[] blocksB = new Matrix[blockCount];  //array of blocks for matrix B

      int row = (i/blockCount)*M;
      int col = (i%blockCount)*M;
      
      for (int j = 0; j < blockCount; j++) {
        blocksA[j] = A.getBlock(row, j*M, M, M);
        blocksB[j] = B.getBlock(j*M, col, M, M);
      }

      threads[i] = new Thread(new Multiply(result, blocksA, blocksB, row, col));    
      threads[i].start();
    }
    
    for (int i = 0; i < processCount; i++) {
      try {
        threads[i].join();
      }
      catch (Exception x) {} 
    }
    
    return result;
  }

  private static class Multiply implements Runnable {
    private final int i, j;   // Indices of first element in the assigned block of result matrix
    private final int dim;    // dimension of block in result square matrix
    private final int blocks; // how many blocks

    private final Matrix[] A, B;
    
    private final Matrix result;

    public Multiply(Matrix result, Matrix[] A, Matrix[] B, int i, int j) {  
      assert (A.length == B.length);

      this.i = i;
      this.j = j;

      this.dim = A[0].getN();
      this.blocks = A.length;

      this.A = A;
      this.B = B; 
      
      this.result = result;
    }
    
    public void run() {
      for (int k = 0; k < this.blocks; k++) {
        result.setBlock(
            this.i, 
            this.j, 
            result.getBlock(this.i, this.j, this.dim, this.dim)
                .add(A[k].mul(B[k])));
      }
    }    
  }
}
