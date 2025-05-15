public class PrivateExample {
    private int number = 10;

    public void showNumber() {
        System.out.println(number);
    }
}

class TestPrivate {
    public static void main(String[] args) {
        PrivateExample obj = new PrivateExample();
        obj.showNumber();  // Works
        // System.out.println(obj.number); // Error: number is private
    }
}
