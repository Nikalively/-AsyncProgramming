import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    // входные данные вынесены в константы
    private static final double A = 3.0;
    private static final double B = 4.0;
    private static final double C = 10.0;
    private static final double D = 16.0;

    public static void main(String[] args) {
        try {
            CompletableFuture<Double> squareSumFuture = CompletableFuture.supplyAsync(() -> calculateSquareSum(A, B));

            CompletableFuture<Double> naturalLogFuture = CompletableFuture.supplyAsync(() -> calculateNaturalLog(C));

            CompletableFuture<Double> sqrtFuture = CompletableFuture.supplyAsync(() -> calculateSquareRoot(D));

            CompletableFuture<Double> finalResultFuture = squareSumFuture
                    .thenCombine(naturalLogFuture, (squareSum, log) -> {
                        System.out.println("Комбинируем сумму квадратов и натуральный логарифм...");
                        return squareSum * log; // это является промежуточным значением
                    })
                    .thenCombine(sqrtFuture, (intermediateResult, sqrt) -> {
                        System.out.println("Делим промежуточное значение на квадратный корень: " + sqrt);
                        return intermediateResult / sqrt; // окончательный результат
                    });

            finalResultFuture
                    .thenAccept(result -> System.out.println("Финальное значение формулы: " + result))
                    .exceptionally(error -> {
                        System.err.println("Ошибка при вычислении формулы: " + error.getMessage());
                        return null;
                    });

            finalResultFuture.get();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static double calculateSquareSum(double a, double b) {
        try {
            System.out.println("Вычисляем сумму квадратов...");
            TimeUnit.SECONDS.sleep(5); // Задержка 5 секунд
        } catch (InterruptedException e) {
            System.err.println("Ошибка при вычислении: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        double result = Math.pow(a, 2) + Math.pow(b, 2);
        System.out.println("Сумма квадратов равна: " + result);
        return result;
    }


    private static double calculateNaturalLog(double c) {
        try {
            System.out.println("Вычисляем натуральный логарифм...");
            TimeUnit.SECONDS.sleep(15); // Задержка 15 секунд
        } catch (InterruptedException e) {
            System.err.println("Ошибка при вычислении: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        if (c <= 0) {
            throw new IllegalArgumentException("Входное значение для логарифма должно быть больше 0");
        }
        double result = Math.log(c);
        System.out.println("Натульный логарифм равен: " + result);
        return result;
    }


    private static double calculateSquareRoot(double d) {
        try {
            System.out.println("Вычисляем квадратный корень...");
            TimeUnit.SECONDS.sleep(10); // Задержка 10 секунд
        } catch (InterruptedException e) {
            System.err.println("Ошибка при вычислении: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        if (d < 0) {
            throw new IllegalArgumentException("Входное значение для квадратного корня должно быть положительным");
        }
        double result = Math.sqrt(d);
        System.out.println("Квадратный корень равен: " + result);
        return result;
    }
}