import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;


public class Board {
    private final int[][] _tiles;
    private final int _size;
    private int _hamming = -1, _manhattan = -1;
    private int _twinCoords1X = -1, _twinCoords1Y = -1, _twinCoords2X = -1, _twinCoords2Y = -1;
    private int _emptySpaceX = -1, _emptySpaceY = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        _size = tiles.length;
        _tiles = Arrays.copyOf(tiles, tiles.length);
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_size);
        for (int i = 0; i < _size; i++) {
            sb.append("\n");
            for (int j = 0; j < _size; j++) {
                sb.append(_tiles[i][j]);
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    private int[] getEmptySpace() {
        if (_emptySpaceX < 0 || _emptySpaceY < 0)
            for (int i = 0; i < _size; i++) {
                for (int j = 0; j < _size; j++) {
                    if (_tiles[i][j] == 0) {
                        _emptySpaceY = i;
                        _emptySpaceX = j;
                    }
                }
            }
        return new int[]{_emptySpaceY, _emptySpaceX};
    }

    // board dimension n
    public int dimension() {
        return _size;
    }

    // number of tiles out of place
    public int hamming() {
        if (_hamming < 0) {
            int count = 0;
            for (int i = 0; i < _size; i++) {
                for (int j = 0; j < _size; j++) {
                    if (manhattanSingle(i, j) > 0) {
                        count++;
                    }
                }
            }
            _hamming = count;
        }
        return _hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (_manhattan < 0) {
            int sum = 0;
            for (int i = 0; i < _size; i++) {
                for (int j = 0; j < _size; j++) {
                    sum += manhattanSingle(i, j);
                }
            }
            _manhattan = sum;
        }
        return _manhattan;
    }

    private int manhattanSingle(int x, int y) {
        if (_tiles[y][x] == 0) return 0;
        int correctX = getCorrectX(_tiles[y][x]);
        int correctY = getCorrectY(_tiles[y][x]);

        return Math.abs(x - correctX) + Math.abs(y - correctY);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board)) return false;
        Board board2 = (Board) y;
        if (_size != board2._tiles.length) return false;

        for (int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                if (_tiles[i][j] != board2._tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<Board>();
        int[] emptySpace = getEmptySpace();

        if (emptySpace[0] - 1 >= 0) {
            Board b = duplicate();
            swap(b, emptySpace, emptySpace[0] - 1, emptySpace[1]);
            boards.add(b);
        }

        if (emptySpace[0] + 1 < _size) {
            Board b = duplicate();
            swap(b, emptySpace, emptySpace[0] + 1, emptySpace[1]);
            boards.add(b);
        }

        if (emptySpace[1] - 1 >= 0) {
            Board b = duplicate();
            swap(b, emptySpace, emptySpace[0], emptySpace[1] - 1);
            boards.add(b);
        }

        if (emptySpace[1] + 1 < _size) {
            Board b = duplicate();
            swap(b, emptySpace, emptySpace[0], emptySpace[1] + 1);
            boards.add(b);
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    private Board duplicate() {
        int[][] myInt = new int[_tiles.length][];
        for (int i = 0; i < _tiles.length; i++) {
            int[] aMatrix = _tiles[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        StdRandom.uniform();
        return new Board(myInt);
    }

    public Board twin() {
        Board dup = duplicate();

        if (_twinCoords1X < 0 || _twinCoords1Y < 0 || _twinCoords2X < 0 || _twinCoords2Y < 0) {
            while (_twinCoords1X == _twinCoords2X || _twinCoords1Y == _twinCoords2Y ||
                    dup._tiles[_twinCoords1Y][_twinCoords1X] == 0 || dup._tiles[_twinCoords2Y][_twinCoords2X] == 0) {
                _twinCoords1X = StdRandom.uniform(_size);
                _twinCoords2X = StdRandom.uniform(_size);
                _twinCoords1Y = StdRandom.uniform(_size);
                _twinCoords2Y = StdRandom.uniform(_size);
            }
        }


        int val = dup._tiles[_twinCoords1Y][_twinCoords1X];
        dup._tiles[_twinCoords1Y][_twinCoords1X] = dup._tiles[_twinCoords2Y][_twinCoords2X];
        dup._tiles[_twinCoords2Y][_twinCoords2X] = val;
        return dup;
    }

    private void swap(Board board, int[] emptySpace, int x2, int y2) {
        int tempVal = board._tiles[x2][y2];
        board._tiles[x2][y2] = 0;
        board._tiles[emptySpace[0]][emptySpace[1]] = tempVal;
    }

    private int getCorrectX(int number) {
        return (number - 1) % _size;
    }

    private int getCorrectY(int number) {
        return (number - 1) / _size;
    }
}
