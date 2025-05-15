public class PublicExample {
    public int age = 40;  //  Public variable
}

class TestPublic {
    public static void main(String[] args) {
        PublicExample obj = new PublicExample();
        System.out.println(obj.age);  //  Can access public variable directly
    }
}
