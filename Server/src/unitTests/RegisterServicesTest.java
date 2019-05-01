package unitTests;
import services.RegisterServices;

public class RegisterServicesTest{

public static void main(String[] args) {
    RegisterServices rs = new RegisterServices();

    //Test case 1
    String testuser1 = "easd";
    String testpw1 = "pw";
    String testres1 = rs.registerUser(testuser1, testpw1);
    System.out.println(testres1);

    //Test case 2
    String testuser2 = "sda";
    String testpw2 = "lol";
    String testres2 = rs.registerUser(testuser2, testpw2);
    System.out.println(testres2);
    
    }
}