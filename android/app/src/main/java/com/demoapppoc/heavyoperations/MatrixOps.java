package com.demoapppoc.heavyoperations;

import java.util.Random;

public class MatrixOps {

    // Method to generate a random n*n matrix
    public static int[][] generateRandomMatrix(int n, int minValue, int maxValue) {
        int[][] matrix = new int[n][n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Generate a random number between minValue (inclusive) and maxValue (inclusive)
                matrix[i][j] = rand.nextInt((maxValue - minValue) + 1) + minValue;
            }
        }

        return matrix;
    }

    // Method to multiply two matrices
    public static int[][] multiplyMatrices(int[][] matrix1, int[][] matrix2) {
        int n = matrix1.length; // Assuming both matrices are n*n

        // Resultant matrix to store the product
        int[][] result = new int[n][n];

        // Multiply the matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = 0; // Initialize the result cell
                for (int k = 0; k < n; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return result;
    }

    // Method to print a matrix
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}
