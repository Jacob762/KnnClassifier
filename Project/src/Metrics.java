import java.util.Arrays;

public class Metrics {
    public static int moda(int[] tab){
        Arrays.sort(tab);
        int modaliczba = tab[0];
        int count=1;
        int maxcount=1;
        for(int i=1;i<tab.length;i++){
            if(tab[i]==tab[i-1]) count++;
            else{
                if(count>maxcount){
                    maxcount=count;
                    modaliczba=tab[i-1];
                }
                count=1;
            }
        }
        if (count > maxcount)
        {
            maxcount = count;
            modaliczba = tab[tab.length - 1];
        }
        return modaliczba;
    }

}
