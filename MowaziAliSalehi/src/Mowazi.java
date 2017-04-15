
import java.io.*;
import java.util.Random;
import mpi.*;

public class Mowazi
{

    public static void main(String[] args)
    {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int count = 10000000;
        if (rank == 0)
        {
            Random rand = new Random();
            int[] araye0 = new int[count];
            for (int i = 0; i < count; i++)
            {
                araye0[i] = rand.nextInt(99999999) + 1;
            }
            System.out.println("Araye Sakhte Shod.");

            MPI.COMM_WORLD.Send(araye0, 0, (araye0.length / 2), MPI.INT, 1, 10);
            MPI.COMM_WORLD.Send(araye0, (araye0.length / 2), (araye0.length / 2), MPI.INT, 2, 20);
        } else if (rank == 1)
        {
            int[] araye1 = new int[count / 2];
            MPI.COMM_WORLD.Recv(araye1, 0, araye1.length, MPI.INT, 0, 10);
            quickSort(araye1, 0, araye1.length-1);
            MPI.COMM_WORLD.Send(araye1, 0, araye1.length, MPI.INT, 3, 30);
        } else if (rank == 2)
        {
            int[] araye2 = new int[count / 2];
            MPI.COMM_WORLD.Recv(araye2, 0, araye2.length, MPI.INT, 0, 20);
            quickSort(araye2, 0, araye2.length-1);
            MPI.COMM_WORLD.Send(araye2, 0, araye2.length, MPI.INT, 3, 35);
        } else if (rank == 3)
        {
            int[] araye3 = new int[count];
            int[] araye4 = new int[count / 2];
            int[] araye5 = new int[count / 2];
            MPI.COMM_WORLD.Recv(araye4, 0, araye4.length, MPI.INT, 1, 30);
            MPI.COMM_WORLD.Recv(araye5, 0, araye5.length, MPI.INT, 2, 35);
            araye3 = merge(araye4, araye5);
            arrayToFile(araye3);
        }
    }

    public static int partition(int arr[], int left, int right)
    {
        int i = left, j = right;
        int tmp;
        int pivot = arr[(left + right) / 2];
        while (i <= j)
        {
            while (arr[i] < pivot)
            {
                i++;
            }
            while (arr[j] > pivot)
            {
                j--;
            }
            if (i <= j)
            {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        };
        return i;
    }

    public static void quickSort(int arr[], int left, int right)
    {
        int index = partition(arr, left, right);
        if (left < index - 1)
        {
            quickSort(arr, left, index - 1);
        }
        if (index < right)
        {
            quickSort(arr, index, right);
        }
    }

    public static int[] merge(int[] a, int[] b)
    {
        int[] answer = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length)
        {
            if (a[i] < b[j])
            {
                answer[k] = a[i];
                i++;
            } else
            {
                answer[k] = b[j];
                j++;
            }
            k++;
        }

        while (i < a.length)
        {
            answer[k] = a[i];
            i++;
            k++;
        }

        while (j < b.length)
        {
            answer[k] = b[j];
            j++;
            k++;
        }

        return answer;
    }

    public static void arrayToFile(int[] array)
    {
        try
        {
            java.io.File file = new java.io.File("File.txt");
            file.createNewFile();
            java.io.FileWriter wr = new java.io.FileWriter(file);
            for (int a : array)
            {
                wr.append(a + ",");
            }
            wr.flush();
            wr.close();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
