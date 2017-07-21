import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private final float BORDER_ENERGY = 195075;
    private Picture currentPic;
    private float[][] pixelEnergy;


    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException();
        }

        this.pixelEnergy = new float[picture.height()][picture.width()];

        this.currentPic = new Picture(picture);
    }


    public Picture picture() {
        return this.currentPic;
    }


    public int width()  {
        // width  of current picture
        return this.currentPic.width();

    }

    public int height() {
        // height of current picture
        return this.currentPic.height();

    }

    public double energy(int x, int y) {
        // energy of pixel at column x and row y in current picture
        if (x > this.width() || x < 0 || y > this.height() || y < 0) {
            throw new IndexOutOfBoundsException();
        }

        int x1 = x - 1;
        int x2 = x + 1;
        int y1 = y - 1;
        int y2 = y + 1;

        if (x1 < 0) {
            x1 = this.width();
        }
        if (x2 > this.width()) {
            x2 = 0;
        }
        if (y1 < 0) {
            y1 = this.height();
        }
        if (y2 < 0) {
            y2 = 0;
        }

        int x1Red = this.currentPic.get(x1,y).getRed();
        int x1Blue = this.currentPic.get(x1,y).getBlue();
        int x1Green = this.currentPic.get(x1,y).getGreen();
        int x2Red = this.currentPic.get(x2,y).getRed();
        int x2Blue = this.currentPic.get(x2,y).getBlue();
        int x2Green = this.currentPic.get(x2,y).getGreen();
        int y1Red = this.currentPic.get(x,y1).getRed();
        int y1Blue = this.currentPic.get(x,y1).getBlue();
        int y1Green = this.currentPic.get(x,y1).getGreen();
        int y2Red = this.currentPic.get(x,y2).getRed();
        int y2Blue = this.currentPic.get(x,y2).getBlue();
        int y2Green = this.currentPic.get(x,y2).getGreen();

        Double deltaXRed = Math.pow(x2Red - x1Red, 2);
        Double deltaXBlue = Math.pow(x2Blue - x1Blue, 2);
        Double deltaXGreen = Math.pow(x2Green - x1Green, 2);
        Double deltaYRed = Math.pow(y2Red - y1Red, 2);
        Double deltaYBlue = Math.pow(y2Blue - y1Blue, 2);
        Double deltaYGreen = Math.pow(y2Green - y1Green, 2);

        double deltaXSumSquare = deltaXRed + deltaXBlue + deltaXGreen;
        double deltaYSumSquare = deltaYRed + deltaYBlue + deltaYGreen;

        return deltaXSumSquare + deltaYSumSquare;
    }


    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam in current picture
        int width = this.width();
        int height = this.height();
        int[] edgeTo = new int [width * height];

        this.computeEnergy();

        for (int col = 1; col < width; col++) {
            for (int row = 0; row < height; row++) {
                relax(row, col, edgeTo, false);
            }
        }

        int minEnergyIndex = getMin(false);

        return getSeam(minEnergyIndex, edgeTo, false);
    }


    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam in current picture
        int width = this.width();
        int height = this.height();
        int[] edgeTo = new int [width * height];
        this.computeEnergy();
        for (int row = 1; row < height; row++) {
            for (int col = 0; col < width; col++) {
                relax(row, col, edgeTo, true);
            }
        }

        int minEnergyIndex = getMin(true);

        return getSeam(minEnergyIndex, edgeTo, true);
    }


    public void removeHorizontalSeam(int[] a) {
        // remove horizontal seam from current picture
        SeamRemover.removeHorizontalSeam(this.picture(), a);
    }


    public void removeVerticalSeam(int[] a) {
        // remove vertical seam from current picture
        SeamRemover.removeVerticalSeam(this.picture(), a);
    }

    private void computeEnergy() {
        for (int row = 0; row < this.pixelEnergy.length; row++) {
            for (int col = 0; col < this.pixelEnergy[row].length; col++) {
                this.pixelEnergy[row][col] = (float) this.energy(col, row);
            }
        }
    }

    private int[] getSeam(int minEnergyIndex, int[] edgeTo, boolean isVertical) {
        int[] seam;
        int vertex;
        if (isVertical) {
            seam = new int[this.height()];
            seam[seam.length - 1] = minEnergyIndex;
            vertex = this.getVertexID(this.height() - 1, minEnergyIndex);
        } else {
            seam = new int[this.width()];
            seam[seam.length - 1] = minEnergyIndex;
            vertex = this.getVertexID(minEnergyIndex, this.width() - 1);
        }
        for (int i = seam.length - 2; i >= 0; i--) {
            vertex = edgeTo[vertex];
            if (isVertical) {
                seam[i] = this.getCol(vertex);
            } else {
                seam[i] = this.getRow(vertex);
            }
        }
        return seam;
    }

    private int getMin(boolean isVertical) {
        int width = this.width();
        int height = this.height();
        float min = Float.MAX_VALUE;
        int minIndex = 0;

        if (isVertical) {
            for (int col = 0; col < width; col++) {
                if (this.pixelEnergy[height - 1][col] < min) {
                    min = this.pixelEnergy[height - 1][col];
                    minIndex = col;
                }
            }
        } else {
            for (int row = 0; row < height; row++) {
                if (this.pixelEnergy[row][width - 1] < min) {
                    min = this.pixelEnergy[row][width - 1];
                    minIndex = row;
                }
            }
        }
        return minIndex;
    }

    private void relax(int row, int col, int[] edgeTo, boolean isVertical) {
        // get neighbors of current vertex
        // for each neighbor calculate the energy + current Energy
        double minEnergy = Double.MAX_VALUE;
        int minNeighbor = 0;
        int[] neighbors;

        if (isVertical) {
            neighbors = getNeighborsV(row, col);
        } else {
            neighbors = getNeighborsH(row, col);
        }

        for (int id : neighbors) {
            if (this.pixelEnergy[this.getRow(id)][this.getCol(id)] < minEnergy) {
                minNeighbor = id;
                minEnergy = this.pixelEnergy[this.getRow(id)][this.getCol(id)];
            }
        }
        this.pixelEnergy[row][col] += minEnergy;
        edgeTo[this.getVertexID(row, col)] = minNeighbor;
    }

    private int[] getNeighborsV(int row, int col) {
        // get each of the neighbors that point to the current vertex
        // for vertices on the border you will have two neighbors
        // for vertices not on the border you will have three neighbors
        int [] neighbors;
        if (col == 0) {
            neighbors = new int[2];
            neighbors[0] = this.getVertexID(row - 1, col);
            neighbors[1] = this.getVertexID(row - 1, col + 1);
        } else if (col == this.width() - 1) {
            neighbors = new int[2];
            neighbors[0] = this.getVertexID(row - 1, col - 1);
            neighbors[1] = this.getVertexID(row - 1, col);
        } else {
            neighbors = new int[3];
            neighbors[0] = this.getVertexID(row - 1, col - 1);
            neighbors[1] = this.getVertexID(row - 1, col);
            neighbors[2] = this.getVertexID(row - 1, col + 1);
        }
        return neighbors;
    }

    private int[] getNeighborsH(int row, int col) {
        // get each of the neighbors that point to the current vertex
        // for vertices on the border you will have two neighbors
        // for vertices not on the border you will have three neighbors
        int [] neighbors;
        if (row == 0) {
            neighbors = new int[2];
            neighbors[0] = this.getVertexID(row, col - 1);
            neighbors[1] = this.getVertexID(row + 1, col - 1);
        } else if (row == this.height() - 1) {
            neighbors = new int[2];
            neighbors[0] = this.getVertexID(row - 1, col - 1);
            neighbors[1] = this.getVertexID(row, col - 1);
        } else {
            neighbors = new int[3];
            neighbors[0] = this.getVertexID(row - 1, col - 1);
            neighbors[1] = this.getVertexID(row, col - 1);
            neighbors[2] = this.getVertexID(row + 1, col - 1);
        }
        return neighbors;
    }

    private float yGradient(int y, int x) {
        float r = Math.abs(this.currentPic.get(x, y - 1).getRed()
                - this.currentPic.get(x, y + 1).getRed());

        float g = Math.abs(this.currentPic.get(x, y - 1).getGreen()
                - this.currentPic.get(x, y + 1).getGreen());

        float b = Math.abs(this.currentPic.get(x, y - 1).getBlue()
                - this.currentPic.get(x, y + 1).getBlue());

        return r * r + g * g + b * b;
    }

    private float xGradient(int y, int x) {
        float r = Math.abs(this.currentPic.get(x - 1, y).getRed()
                - this.currentPic.get(x + 1, y).getRed());

        float g = Math.abs(this.currentPic.get(x - 1, y).getGreen()
                - this.currentPic.get(x + 1, y).getGreen());

        float b = Math.abs(this.currentPic.get(x - 1, y).getBlue()
                - this.currentPic.get(x + 1, y).getBlue());

        return r * r + g * g + b * b;
    }

    private int getVertexID(int r, int c) {
        int W = this.width();
        return W * r + c;
    }

    private int getRow(int id) {
        int W = this.width();
        return id / W;
    }

    private int getCol(int id) {
        int W = this.width();
        return id % W;
    }
}
