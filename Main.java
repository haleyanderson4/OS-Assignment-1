
/**
* Haley Anderson and Jennifer Prosinski
* CPSC 380: Operating Systems
* Assignment 1: Sudoku Solution Validator
*/

import java.util.*;
import java.lang.String;
import java.io.*;

public class Main
{

  public static void main(String[] args)
  {
    try
    {
      File file = null;
      if (0 < args.length)
      {
        file = new File(args[0]);
      }
      else
      {
        System.err.println("Invalid arguments count:" + args.length);
      }

      Scanner scan = new Scanner(file);
      int[][] sudoku = new int[9][9]; //intializing the game board

      while (scan.hasNextLine())  //loop through all the lines in text file
      {

          for(int row = 0; row < 9; row++)
          {
              String line = scan.nextLine();
              int column = 0;
              for(int i = 0; i < line.length(); i++)
              {
                  char c = line.charAt(i);
                  if(c != ',')
                  {
                      sudoku[row][column] = Integer.parseInt("" + c); //adding the non comma chars to the gameboard
                      column++;
                  }
              }
          }
      }

      //start thread 1, row by row
      boolean[] sudokuLine = new boolean[9];
      ArrayList<String> problems = new ArrayList<String>();


      for(int row = 0; row < 9; row++)
      {
        for(int i = 0; i < 9; i++) //initalizing the array to being false, to zero out all other attempts
        {
          sudokuLine[i] = false;
        }

        for(int column = 0; column < 9; column++)
        {
          int numberAt = sudoku[row][column]; //this is the number we are looking at
          if(!sudokuLine[numberAt - 1]) //to start from 0 not 1, if the number has not been seen yet it is false
          {
              sudokuLine[numberAt - 1] = true;
          }
          else //if it was already true, this number has already been seen!
          {
            int rowPrint = row + 1;
            int columnPrint;
            for(int i = 0; i < 9; i++) // need to look again for the first time this was used
            {

                if(column != i && sudoku[row][i] == numberAt) //if its not in the same place but is the same as the one we are looking at
                {
                    columnPrint = i + 1;
                    problems.add(numberAt + " " + rowPrint + " " + columnPrint);
                }
            }
              columnPrint = column + 1;
              problems.add(numberAt + " " + rowPrint + " " + columnPrint); //adding it to the list of problems to bring up later
          }
        }
      }

      System.out.println(problems);

      //use a boolean array to check if every number 1-9 is accounted for, when hit a double add it to the list and print
      //the number in question, the row and column number. Then find where the rows and columns intersect at the same number.

    }
    catch(Exception e)
    {

    }

  }
}
