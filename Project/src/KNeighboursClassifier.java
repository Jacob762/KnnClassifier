import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class KNeighboursClassifier {
    private static String setname;
    private static String nazwatrain;
    private static String nazwatest;
    private static int n_neighbours;
    private static String metric;
    private static double[][] tabTrain;
    private static int[] etykiety;
    private static int kolumny;
    private static double[][] tabTest;
    private static int[] etykietyTest;

    public static void dataSet(){
        Scanner skaner = new Scanner(System.in);
        System.out.println("Wybierz nazwe zbioru (iris lub cancer):");
        setname = skaner.nextLine();
        if(setname.equals("iris")){
            nazwatest = "test_data_iris.txt";
            nazwatrain = "train_data_iris.txt";
        } else{
            nazwatest = "test_data_cancer.txt";
            nazwatrain = "train_data_cancer.txt";
        }
        System.out.println("Podaj metryke (maanhatan lub euclidean)");
        metric = skaner.nextLine();
        System.out.println("Podaj liczbe sasiadow:");
        n_neighbours = skaner.nextInt();
    }

    public static void fit(double[][] X_train, int[] Y_train, int kolumny) {
        if (nazwatrain.equals("train_data_cancer.txt")) {
            int k = 0;
            for (int i = 0; i < DataReader.tablica.size(); i++) {
                for (int j = 2; j <= kolumny; j++) {
                    X_train[i][k] = DataReader.tablica.get(i).get(j);
                    k++;
                }
                k = 0;
            }
            for (int i = 0; i < DataReader.tablica.size(); i++)
                Y_train[i] = DataReader.tablica.get(i).get(1).intValue();
        } else {
            for (int i = 0; i < DataReader.tablica.size(); i++)
                for (int j = 0; j < kolumny; j++) X_train[i][j] = DataReader.tablica.get(i).get(j);
            for (int i = 0; i < DataReader.tablica.size(); i++)
                Y_train[i] = DataReader.tablica.get(i).get(kolumny).intValue();
        }
    }

    public static void predict(double[][] X_test) {
        ArrayList<ArrayList<Double>> odleglosci;
        ArrayList<Double> temp;
        double[][] tabodleglosci;
        int[] etykietyodleglosci;
        if (nazwatest.equals("test_data_cancer.txt")) {
            double[][] tempXtest = new double[DataReader.tablica.size()][kolumny + 1];
            int k = 0;
            for (int i = 0; i < DataReader.tablica.size(); i++)
                for (int j = 1; j <= kolumny; j++) {
                    tempXtest[i][j] = DataReader.tablica.get(i).get(j);
                }
            for (int i = 0; i < tempXtest.length; i++) {
                for (int j = 2; j < kolumny + 1; j++) {
                    X_test[i][k] = tempXtest[i][j];
                    k++;
                }
                k = 0;
                X_test[i][kolumny - 1] = tempXtest[i][1];
            }
        } else {
            for (int i = 0; i < DataReader.tablica.size(); i++)
                for (int j = 0; j < kolumny + 1; j++) X_test[i][j] = DataReader.tablica.get(i).get(j);
        }

        double odleglosc = 0;
        for (int k = 0; k < X_test.length; k++) {
            odleglosci = new ArrayList<>();
            if (metric.equals("euclidean")) {
                for (int i = 0; i < tabTrain.length; i++) {
                    temp = new ArrayList<>();
                    for (int j = 0; j < tabTrain[i].length; j++) {
                        odleglosc += (tabTrain[i][j] - X_test[k][j]) * (tabTrain[i][j] - X_test[k][j]);
                    }
                    temp.add(Math.sqrt(odleglosc));
                    temp.add((double) etykiety[i]);
                    odleglosci.add(temp);
                    odleglosc = 0;
                }
            } else if (metric.equals("maanhatan")) {
                for (int i = 0; i < tabTrain.length; i++) {
                    temp = new ArrayList<>();
                    for (int j = 0; j < tabTrain[i].length; j++) {
                        odleglosc += Math.abs(tabTrain[i][j] - X_test[k][j]);
                    }
                    temp.add(odleglosc);
                    temp.add((double) etykiety[i]);
                    odleglosci.add(temp);
                    odleglosc = 0;
                }
            }
            tabodleglosci = new double[odleglosci.size()][2];
            for (int i = 0; i < odleglosci.size(); i++)
                for (int j = 0; j < 2; j++) tabodleglosci[i][j] = odleglosci.get(i).get(j);
            double[][] temptab;
            for (int i = 0; i < tabodleglosci.length - 1; i++) {
                for (int j = 0; j < tabodleglosci.length - 1; j++) {
                    temptab = new double[1][2];
                    if (tabodleglosci[j + 1][0] < tabodleglosci[j][0]) {
                        temptab[0][0] = tabodleglosci[j][0];
                        temptab[0][1] = tabodleglosci[j][1];
                        tabodleglosci[j][0] = tabodleglosci[j + 1][0];
                        tabodleglosci[j][1] = tabodleglosci[j + 1][1];
                        tabodleglosci[j + 1][0] = temptab[0][0];
                        tabodleglosci[j + 1][1] = temptab[0][1];
                    }
                }
            }
            etykietyodleglosci = new int[n_neighbours];
            for (int i = 0; i < n_neighbours; i++) etykietyodleglosci[i] = (int) tabodleglosci[i][1];
            etykietyTest[k] = Metrics.moda(etykietyodleglosci);
        }
    }


    public static double calculate_accuracy(double[][] X_test, int[] Y_test) {
        double counter = 0;
        double result;
        if(nazwatest.equals("test_data_cancer.txt")){
            for (int i = 0; i < X_test.length; i++) {
                if (X_test[i][kolumny-1] == Y_test[i]) counter++;
            }
        } else{
            for (int i = 0; i < X_test.length; i++) {
                if (X_test[i][kolumny] == Y_test[i]) counter++;
            }
        }
        System.out.println("Accuracy = " + 100 * (counter / X_test.length) + "%");
        result = 100*(counter/X_test.length);
        return result;
    }

    public static void save(double accuracy) throws FileNotFoundException {
        File nowyPlik = new File("wyniki.txt");
        PrintWriter zapis = new PrintWriter(nowyPlik);
        zapis.print("Set: " + nazwatest + " Metric: " + metric + " Neighbours: " + n_neighbours + " Accuracy: " + accuracy);
        zapis.close();
    }

    public static void main (String[]args) throws FileNotFoundException {
        dataSet();
        double dokladnosc;
        DataReader.wczytywanie(nazwatrain);
        kolumny = DataReader.kolumny;
        if (nazwatest.equals("test_data_cancer.txt")) {
            tabTrain = new double[DataReader.tablica.size()][kolumny - 1];
            etykiety = new int[DataReader.tablica.size()];
        } else {
            tabTrain = new double[DataReader.tablica.size()][kolumny];
            etykiety = new int[DataReader.tablica.size()];
        }
        fit(tabTrain, etykiety, kolumny);
        System.out.println();
        System.out.println();
        DataReader.wczytywanie(nazwatest);
        if (nazwatest.equals("test_data_cancer.txt")) {
            tabTest = new double[DataReader.tablica.size()][kolumny];
            etykietyTest = new int[DataReader.tablica.size()];
        } else {
            tabTest = new double[DataReader.tablica.size()][kolumny + 1];
            etykietyTest = new int[DataReader.tablica.size()];
        }
        predict(tabTest);
        System.out.println();
        dokladnosc=calculate_accuracy(tabTest,etykietyTest);
        save(dokladnosc);
    }
}



