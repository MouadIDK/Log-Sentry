import java.util.HashMap;
import java.util.Map;

public class EntropyUtil {
   public static Map<Character,Integer> getFreqMap(String str){
       HashMap<Character,Integer> map= new HashMap<>();

       for(Character c:str.toCharArray()){
           map.put(c,map.getOrDefault(c,0)+1);
       }
       return map;
   }

   public static double calculate(String str){
       Map<Character,Integer> map = getFreqMap(str);
       double entropy = 0;
       double length = str.length();
       for( Character c : map.keySet()){
            double p = (double) map.get(c)/length;
            entropy -= p *(Math.log(p)/Math.log(2));
       }
       return entropy;
   }
}
