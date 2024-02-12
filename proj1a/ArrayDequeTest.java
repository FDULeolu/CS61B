public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<Integer> myDeque = new ArrayDeque<>();
        myDeque.addFirst(2);
        myDeque.addFirst(4);
        myDeque.addLast(3);
        myDeque.printDeque();
        for (int i = 0; i < myDeque.size(); i++) {
            System.out.println(myDeque.get(i));
        }
        System.out.println(myDeque.removeFirst());
        System.out.println(myDeque.removeLast());
        System.out.println(myDeque.removeFirst());
        System.out.println(myDeque.removeFirst());
        System.out.println(myDeque.isEmpty());
        myDeque.printDeque();
    }
}
