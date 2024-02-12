public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<Integer> myDeque = new ArrayDeque<>();
        for (int i = 0; i < 64; i++) {
            myDeque.addLast(i);
        }
        for (int i = 0; i < 63; i++) {
            myDeque.removeFirst();
        }
        System.out.println(myDeque.getLength());
    }
}
