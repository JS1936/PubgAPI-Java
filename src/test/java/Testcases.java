import org.junit.jupiter.api.Assertions;
//import static org.junit.jupiter.api.Assertions.assertEquals; //https://mkyong.com/gradle/junit-5-gradle-examples/
public class Testcases {

    public static void main(String[] args) {
        System.out.println("Beginning to run testcases now");
        Assertions.assertTrue(access_java_main());
        //Assertions.assertFalse(access_java_main());
    }


    public static boolean access_java_main() {
        //assertEquals(5,6);
        System.out.println("\tTesting acccess_java_main:");
        try {
            Main.initiateFunctionalities();
        } catch (UnsupportedOperationException e) {
            System.out.println("access_java_main FAILED");
            return false;
            //throw new UnsupportedOperationException("Cannot access method called 'Main.main()'");
        }
        System.out.println("access_java_main PASSED");
        return true;
    }
}