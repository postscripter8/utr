package com.example.filu.myapplication;

import android.util.Log;

import java.util.Random;

/**
 * Created by Filu on 2014-08-19.
 */
public class MazeCreator {

    private int level;
    private int size_x, size_y;

    public Field[][] getMaze() {
        return maze;
    }

    private Field[][] maze;

    public MazeCreator(int level) {
        this.level = level;

        this.size_x = ((level * 3) + 12)/3;
        this.size_y = ((level * 2) +10)/2;

        generateMaze();

    }

    private void setSize_x(int size_x) {
        this.size_x = size_x;
    }
    private void setSize_y(int size_y) {
        this.size_y = size_y;
    }
    public int getSize_x(){
        return size_x;
    }
    public int getSize_y(){
        return size_y;
    }

    public Field[][] generateMaze() {

        this.maze = new Field[size_x][size_y];
        for (int i = 0; i < size_x ; i++) {
            for (int j = 0; j < size_y ; j++) {
                this.maze[i][j] = new Field();
            }
        }
        --size_x;
        --size_y;
        setEndings();
        generatePath();



        return this.maze;
    }

    private void setEndings() {

        this.maze[0][0].north = true;
        this.maze[0][0].visited = true;
        this.maze[size_x][size_y].south = true;
        this.maze[size_x][size_y].visited = true;


    }

    /**
     * case 0 - north
     * case 1 - east
     * case 2 - west
     * case 3 - south
     */
    private void generatePath() {


        int position_y=0;
        int position_x=0;
        int counter = (size_x*size_y);
        Random r = new Random();

        while(counter>0) {
            Log.d("Counter",String.valueOf(counter));
            int direction = r.nextInt(4) ;
            Log.w("MazeCreator", String.valueOf(direction) );
            switch (direction) {
                case 0:

                    if(position_y-1>0) {
                        maze[position_x][position_y].north = true;
                        position_y--;
                        maze[position_x][position_y].south = true;
                        counter--;
                    }
                    break;
                case 1:

                    if(position_x+1<size_x) {
                        maze[position_x][position_y].east = true;
                        ++position_x;
                        maze[position_x][position_y].west = true;
                        counter--;
                    }
                    break;
                case 2:
                    if(position_x-1>0) {
                        maze[position_x][position_y].west = true;
                        --position_x;
                        maze[position_x][position_y].east = true;
                        counter--;
                    }
                    break;
                case 3:
                    if(position_y+1<size_y) {
                        maze[position_x][position_y].south = true;
                        ++position_y;
                        maze[position_x][position_y].north = true;
                        counter--;
                    }
                    break;
            }
        }
    }


    public String getMazeAsText(Field[][] maze) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for(int i=0;i<size_x;i++) {
            for(int j=0;j<size_y;j++){
                if(maze[i][j].north) {
                    sb.append("_").append(" ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("\n");
            for(int j=0;j<size_y;j++){
                if(maze[i][j].east) {
                    sb.append("|").append(" ");
                } else {
                    sb.append("  ");
                }
            }
        }
        Log.e("MAZE",sb.toString());
        return sb.toString();
    }

}
