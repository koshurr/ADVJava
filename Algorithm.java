import java.util.ArrayList;
import java.util.List;

/**
 * Created by yakov on 3/15/2018.
 */
public final class Algorithm {
    public static<T extends Comparable> T max(List<T> arr, int beg, int end){
        T max = arr.get(beg);
        for(int i = beg + 1; i < end ; i ++){
            if(max.compareTo(arr.get(i))<=0){
                max = arr.get(i);
            }
        }
        return max;
    }
//    public static void main(String[] args){
//        ArrayList<Integer> a = new ArrayList<>();
//        a.add(5);
//        a.add(1);
//        a.add(10);
//        a.add(-5);
//        a.add(6);
//        a.add(1);
//        List<Integer> l = a;
//        int j = max(l,1,5);
//        System.out.println(j);
//    }
}

