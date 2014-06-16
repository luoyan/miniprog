import java.util.Arrays;   
public class AA {  
        private static int[] a = new int[4];   
        private static AA aa = new AA();  
        boolean[] flag =new boolean[5];  
        private static char[] ad = new char[4];  
        private AA(){//构造方法   
                init();  
        }   
        public static AA getAA(){//得到单态实例   
                return aa;  
        }  
        public void init(){  
                Arrays.fill(a, 1);//此处不会出错   
                Arrays.fill(flag, true);//此处也不会出错，用作对比   
                Arrays.fill(ad,'d');//此处将会出错   
                System.out.println(a[0]);   
        } /** * @param args */   
        public static void main(String[] args) {  
        }   
} 
