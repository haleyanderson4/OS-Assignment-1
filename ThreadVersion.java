
/**
* Haley Anderson
* CPSC 380: Operating Systems
* Assignment 1: Sudoku Solution Validator
*/

import java.util.*;
import java.lang.String;
import java.io.*;

class MyThread implements Runnable
{
  /*public static*/ ArrayList<String> repeats = new ArrayList<String>(); //initalizing the list of repeating problems
  /*public static*/ ArrayList<String> missing = new ArrayList<String>(); //initalizing the list of missing numbers in puzzle
  /*public static*/ int[][] sudoku = new int[9][9]; //intializing the game board

  String name;
  Thread t;
  MyThread(String threadname, int[][] s, ArrayList<String> r, ArrayList<String> m)
  {
    name = threadname;
    sudoku = s;
    repeats = r;
    missing = m;
    t = new Thread(this, name);
    t.start();
  }


  public static void rowFind(int[][] sudoku, ArrayList<String> repeats, ArrayList<String> missing) //thread 1, row by row
  {
    String[] sudokuLine = new String[9];

    for(int row = 0; row < 9; row++)
    {
      for(int i = 0; i < 9; i++) //initalizing the array to being false, to zero out all other attempts
      {
        sudokuLine[i] = "";
      }

      for(int column = 0; column < 9; column++)
      {
        int numberAt = sudoku[row][column]; //this is the number we are looking at
        int rowPrint = row + 1;
        int columnPrint = column + 1;
        if(sudokuLine[numberAt - 1].equals("")) //to start from 0 not 1, if the number has not been seen yet it is false
        {
            sudokuLine[numberAt - 1] = numberAt + " " + rowPrint + " " + columnPrint;
        }
        else //if it was already true, this number has already been seen!
        {
          System.out.println("There is an error in row " + rowPrint + ", the number " + numberAt + " is listed twice.");
          repeats.add(sudokuLine[numberAt - 1]);
          repeats.add(numberAt + " " + rowPrint + " " + columnPrint); //adding it to the list of problems to bring up later
        }
      }

      for(int i = 0; i < 9; i++) //initalizing the array to being false, to zero out all other attempts
      {
        if(sudokuLine[i].equals(""))
        {
            int numberPrint = i + 1;
            int rowPrint = row + 1;
            System.out.println("Row " + rowPrint + " is missing the number " + numberPrint);
            missing.add(numberPrint + " " + rowPrint + " 0");
        }
      }

    }
  }


  public static void columnFind(int[][] sudoku, ArrayList<String> repeats, ArrayList<String> missing) //thread 2, column by column
  {
    String[] sudokuLine = new String[9];

    for(int column = 0; column < 9; column++)
    {
      for(int i = 0; i < 9; i++) //initalizing the array to being false, to zero out all other attempts
      {
        sudokuLine[i] = "";
      }

      for(int row = 0; row < 9; row++)
      {
        int numberAt = sudoku[row][column]; //this is the number we are looking at
        int rowPrint = row + 1;
        int columnPrint = column + 1;
        if(sudokuLine[numberAt - 1].equals("")) //to start from 0 not 1, if the number has not been seen yet it is false
        {
            sudokuLine[numberAt - 1] = numberAt + " " + rowPrint + " " + columnPrint;
        }
        else //if it was already true, this number has already been seen!
        {
          System.out.println("There is an error in column " + columnPrint + ", the number " + numberAt + " is listed twice.");
          repeats.add(sudokuLine[numberAt - 1]);
          repeats.add(numberAt + " " + rowPrint + " " + columnPrint); //adding it to the list of problems to bring up later
        }
      }

      for(int i = 0; i < 9; i++) //initalizing the array to being false, to zero out all other attempts
      {
        if(sudokuLine[i].equals(""))
        {
            int numberPrint = i + 1;
            int columnPrint = column + 1;
            System.out.println("Column " + columnPrint + " is missing the number " + numberPrint);
            missing.add(numberPrint + " 0 " + columnPrint);
        }
      }
    }
  }


  public static void combineAndSolve(int[][] sudoku, ArrayList<String> repeats, ArrayList<String> missing) //combines problem lists into actionable items & solves
  {
      ArrayList<String> combineRepeats = new ArrayList<String>(); //first creates a list of all repeating repeats, aka the ones to change!
      for(int i = 0; i < repeats.size(); i++)
      {
        String startProblem = repeats.get(i);
        for(int j = 0; j < repeats.size(); j++) //compares whole list to itself
        {
          if(i != j && startProblem.equals(repeats.get(j)))
          {
            combineRepeats.add(startProblem); //if the 2 problems are the same, add them to this list
            repeats.set(i,""); //remove this from the list so it can't be added twice
          }
        }
      }

      for(int i = 0; i < combineRepeats.size(); i++) //need to go through and find what the correct solution is, and add it to the puzzle
      {
        String numsMissingRow = ""; //creates a list of the numbers missing from the repeats row
        String numsMissingColumn = ""; //creates a list of the numbers missing from the repeats column
        String problem = combineRepeats.get(i);
        char row = problem.charAt(2); // the row
        char column = problem.charAt(4); // the column
        int solution = 0;
        int rowInt = Character.getNumericValue(row) - 1; //because the row & column number we added was in base 1, not base 0
        int columnInt = Character.getNumericValue(column) - 1;

        for(int j = 0; j < missing.size(); j++) //this is creating a list of all numbers that are missing from the problems row & column
        {
          String currentMissing = missing.get(j);
          if(currentMissing.charAt(2) == row) //if the row that is missing a number matches the row we are looking for, add it to this String
          {
            numsMissingRow = numsMissingRow + currentMissing.charAt(0) + ",";
          }
          if(currentMissing.charAt(4) == column) //if the column that is missing a number matches the column we are looking for, add it to this String
          {
            numsMissingColumn = numsMissingColumn + currentMissing.charAt(0) + ",";
          }
        }

        for(int a = 0; a < numsMissingRow.length(); a++) //now to comppare all the numbers missing from the row and the column
        {
          char compare1 = numsMissingRow.charAt(a);
          for(int b = 0; b < numsMissingColumn.length(); b++)
          {
            char compare2 = numsMissingColumn.charAt(b);
            if(compare1 != ',' && compare1 == compare2) //if a number missing from the row does not equals a , but does equal a number missing from the column
            {
              solution = Character.getNumericValue(compare1); //this is the solution to the problem!!
              sudoku[rowInt][columnInt] = solution; //now fix the sudoku puzzle
            }
          }
        }

        int rowPrint = rowInt + 1; //bring the row and column back to the readable base 1
        int columnPrint = columnInt + 1;
        System.out.println("Row " + rowPrint + " Column " + columnPrint + " had a " + problem.charAt(0) + ", the correct solution is a " + solution);

      }
  }


  public void run()
  {
    try
    {
      if(name == "c")
      {
        rowFind(sudoku, repeats, missing);
      }
      if(name == "r")
      {
        columnFind(sudoku, repeats, missing);
      }
      if(name == "f")
      {
        t.join(10000);
        combineAndSolve(sudoku, repeats, missing);
      }
    }
    catch (Exception e)
    {
      System.out.println(name + " Interrupted");
    }
  }

}

public class ThreadVersion
{
  public static void main(String[] args)
  {
    /*public static*/ ArrayList<String> repeats = new ArrayList<String>(); //initalizing the list of repeating problems
    /*public static*/ ArrayList<String> missing = new ArrayList<String>(); //initalizing the list of missing numbers in puzzle
    /*public static*/ int[][] sudoku = new int[9][9]; //intializing the game board

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

      MyThread thread1 = new MyThread("c", sudoku, repeats, missing);
      MyThread thread2 = new MyThread("r", sudoku, repeats, missing);
      if(repeats.size() == 0) //if we didnt find any problems, the solution was already correct.
      {
        System.out.println("Your solution has been validated. There are no errors");
        printSudoku(sudoku);
        System.out.println("Thank you for using Sudoku Solution Validator by Haley Anderson");
      }
      else //if there are problems, fix them!
      {
        MyThread thread3 = new MyThread("f", sudoku, repeats, missing);
        System.out.println("\nYour solution has been validated. \nCorrect solution:");
        printSudoku(sudoku);
        System.out.println("\nThank you for using Sudoku Solution Validator by Haley Anderson");
      }

      //use a boolean array to check if every number 1-9 is accounted for, when hit a double add it to the list and print
      //the number in question, the row and column number. Then find where the rows and columns intersect at the same number.

    }
    catch(Exception e)
    {
      System.out.println("Main thread Interrupted");
    }

  }

  public static void printSudoku(int[][] sudoku) //prints the sudoku grid again.
  {
    for(int i = 0; i < sudoku.length; i++)
    {
      for(int j = 0; j < sudoku[0].length; j++)
      {
        System.out.print(sudoku[i][j] + ",");
      }
      System.out.print("\n");
    }
  }

}
