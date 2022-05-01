import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataReader {


    public static ArrayList<ArrayList<Double>> tablica;
    public static int kolumny;

    public static void wczytywanie(String nazwa) throws FileNotFoundException {
        kolumny=0;
        File plik = new File(nazwa);
        Scanner skan = new Scanner(plik);
        String wiersz = skan.nextLine();
        for(int i=0; i<wiersz.length();i++){
            if(wiersz.charAt(i)==';') kolumny++;
        }
        ArrayList<Double> temp;
        tablica = new ArrayList<>();
        while(skan.hasNextLine()){
            wiersz = skan.nextLine();
            String[] tokens = wiersz.split(";");
            temp = new ArrayList<>();
            for(int i=0;i< tokens.length;i++)
            {
                temp.add(Double.parseDouble(tokens[i]));
            }
            tablica.add(temp);
        }
    }

}
