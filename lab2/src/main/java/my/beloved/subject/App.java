package my.beloved.subject;

public class App {
    public static void main(String[] args) {
        var prefix = "java -cp target/*.jar ";
        System.out.println(prefix + Server.class.getName());
        System.out.println(prefix + Client.class.getName());
        System.out.println(prefix + UberFuncComp.class.getName());
    }
}
