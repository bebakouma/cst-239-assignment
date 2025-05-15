class DefaultExample {
    int value = 20;  //  No modifier = default access
}

class TestDefault {
    public static void main(String[] args) {
        DefaultExample obj = new DefaultExample();
        System.out.println(obj.value);  //  Works in the same file and package
    }
}
