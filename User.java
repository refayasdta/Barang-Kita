import java.util.List;

public class User extends Akun {

    private String nama;
    private String foto_profile;
    private String no_telepon;
    private String alamat;

    @Override
    public String getRole() {
        return "User";
    }

    public void register() {
        System.out.println("User registered.");
    }

    public void editProfile() {
        System.out.println("Profile updated.");
    }

    // Note: You will need 'Item' and 'Order' classes created for these to work
    public List<String> searchItem(String keyword) {
        System.out.println("Searching for: " + keyword);
        return null; 
    }

    public List<String> viewOrderHistory() {
        System.out.println("Viewing order history.");
        return null;
    }
}
