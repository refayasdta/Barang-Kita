public abstract class Akun {
    protected int id_akun;
    protected String email;
    protected String password;

    public boolean login(){
        return true;
    }

    public void logout(){

    }

    public String getRole(){
        return "Role";
    }
}