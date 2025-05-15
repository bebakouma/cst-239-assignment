class Parent {
    protected int score = 30;  //  Protected access
}

class Child extends Parent {
    public void displayScore() {
        System.out.println(score);  //  Accesses protected variable
    }
}

class TestProtected {
    public static void main(String[] args) {
        Child obj = new Child();
        obj.displayScore();  //  Prints 30
    }
}
