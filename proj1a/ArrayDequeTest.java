public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<Integer> myDeque = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            myDeque.addLast(i);
            myDeque.printDeque();
            System.out.println(" ");
        }
        for (int i = 0; i < 20; i++) {
            myDeque.addLast(i + 10);
            System.out.println(myDeque.removeFirst());
            System.out.println(myDeque.removeFirst());
        }
        myDeque.printDeque();
    }
}
