package com.shakal.SapperBot;

import java.util.Objects;

public class Cell {
    private int x;
    private int y;

    private Cell(int y, int x) {
        this.x = x;
        this.y = y;
    }

    static Cell of(int y, int x){
        return new Cell(y, x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
