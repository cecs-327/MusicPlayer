package gson;
public class Users{

    private String UserName;
    private String Password;

    public Users(String UserName, String Password){
        this.UserName = UserName;
        this.Password = Password;
    }

    public String getUsername(){
        return UserName;
    }

    public String getPassword(){
        return Password;
    }
}