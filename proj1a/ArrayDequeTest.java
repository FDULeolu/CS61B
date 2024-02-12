public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<Integer> myDeque = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            myDeque.addLast(i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(myDeque.removeFirst());
        }
    }
}
