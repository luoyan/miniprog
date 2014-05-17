import java.lang.reflect.*;
class Student
{
        String name;
        int age;
        public Student()
        {
        }
        public Student(String name,Integer age)
        {
                this.name=name;
                this.age=age;
        }
        public String toString()
        {
                return "姓名："+name+",年龄:"+age + "\n";
        }
}
public class TestReflect {
        public static void main(String args[]) throws Exception
        {
                Object []o={"张三",new Integer(12)};
                Student s=(Student)createClass("Student",o);
                System.out.print(s);
        }
        public static Object createClass(String name,Object []o) throws Exception//自动找到合适的构造方法并构造
        {
                Class myClass =Class.forName(name);
                Class[] argsClass = new Class[o.length];
                System.out.println("o.length " + o.length);
                for (int i = 0; i< o.length; i++) {
                        argsClass[i] = o[i].getClass();
                }
                try
                {
                        Constructor cons = myClass.getConstructor(argsClass);
                        System.out.println("Normal");
                        return cons.newInstance(o);
                }
                catch (Exception e)
                {
                        System.out.println("Exception");
                        return myClass.newInstance();
                }
        }
}
