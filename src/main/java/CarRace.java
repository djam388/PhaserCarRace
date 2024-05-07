import java.util.concurrent.Phaser;

public class CarRace {
    // Создаем Phaser для 5 автомобилей + 1 главный поток
    private static final Phaser START = new Phaser(1); // Регистрируем главный поток
    // Условная длина гоночной трассы
    private static final int trackLength = 500000;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            START.register(); // Регистрируем участника (автомобиль)
            new Thread(new Car(i, (int) (Math.random() * 100 + 50))).start();
            Thread.sleep(1000);
        }


        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    Thread.sleep(1000);
                    System.out.println("На старт!");
                    Thread.sleep(1000);
                    System.out.println("Внимание!");
                    START.arriveAndDeregister();
                    Thread.sleep(1000);
                    System.out.println("Марш!");
                    break;
                case 2:
                    START.arriveAndDeregister();
//                    System.out.println("Финиш!");
                default:
                    START.arriveAndAwaitAdvance(); // Старт гонки

            }
        }
    }

    public static class Car implements Runnable {
        private int carNumber;
        private int carSpeed; // Считаем, что скорость автомобиля постоянная

        public Car(int carNumber, int carSpeed) {
            this.carNumber = carNumber;
            this.carSpeed = carSpeed;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к стартовой прямой.\n", carNumber);
                START.arriveAndAwaitAdvance(); // Автомобиль ждет старта
                Thread.sleep(trackLength / carSpeed); // Ждем пока проедет трассу
                System.out.printf("Автомобиль №%d финишировал!\n", carNumber);
                START.arriveAndDeregister();
            } catch (InterruptedException e) {
            }
        }
    }
}
