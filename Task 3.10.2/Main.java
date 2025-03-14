import java.util.*;
import java.util.concurrent.*;

public class Main {

    static class Transaction {
        final int senderId;
        final int receiverId;
        final int amount;

        Transaction(int senderId, int receiverId, int amount) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.amount = amount;
        }
    }

    public static void main(String[] args) {
        // входные данные задания
        final int userCount = 4;
        final int[] userBalances = {500, 200, 300, 400};
        final int transactionCount = 3;
        final Transaction[] transactionQueue = {
                new Transaction(0, 1, 100),
                new Transaction(2, 3, 50),
                new Transaction(1, 3, 100)
        };

        final int[] finalBalances = Arrays.copyOf(userBalances, userCount);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // мьютекс
        final Object balanceLock = new Object();

        List<Callable<Void>> tasks = new ArrayList<>();
        for (Transaction transaction : transactionQueue) {
            tasks.add(() -> {
                processTransaction(finalBalances, transaction, balanceLock);
                return null;
            });
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        printFinalBalances(finalBalances, userCount);
    }

    private static void processTransaction(int[] balances, Transaction transaction, Object lock) {
        synchronized (lock) {
            balances[transaction.senderId] -= transaction.amount;
            balances[transaction.receiverId] += transaction.amount;
        }
    }


    private static void printFinalBalances(int[] balances, int userCount) {
        for (int i = 0; i < userCount; i++) {
            System.out.println("User " + i + " final balance: " + balances[i]);
        }
    }
}