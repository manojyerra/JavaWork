import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> toSort = new ArrayList<String>(Arrays.asList(
                                                                       new String[]{
                                                                           "abc1_diff.png",
                                                                           "def10_diff.png",
                                                                           "filename2_diff.png",
                                                                           "filename20_diff.png",}));
        
        Sort(toSort);
        System.out.println(toSort);
    }
    
    public static void Sort(ArrayList<String> toSort)
    {
        Collections.sort(toSort, new Comparator<String>() {
            
            Pattern pat = Pattern.compile("filename(\\d+)\\_diff.png");
            
            public int compare(String a, String b) {
                Matcher matA = pat.matcher(a);
                Matcher matB = pat.matcher(b);
                matA.find();
                matB.find();
                String ga = matA.group(1);
                String gb = matB.group(1);
                return Integer.valueOf(ga).compareTo(Integer.valueOf(gb));
            }
        });
    }
}
