import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static final int WIN_COUNT = 4;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;

    public static void main(String[] args) {
        while(true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if(checkGameState(DOT_HUMAN,"Вы победили"))
                    break;
                aiTurn();
                printField();
                if(checkGameState(DOT_AI,"Компьютер победил"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? Y - да: ");
            if(!scanner.next().equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    /**
     * Инициализация игрового поля
     */
    static void initialize() {
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");
        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }
        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * ход игрока (человека)
     */
    static  void humanTurn() {
        int x;
        int y;

        do {
            System.out.printf("Введите координаты хода X и Y (от 1 до %d)\nчерез пробел: ", WIN_COUNT);
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[y][x] = DOT_HUMAN;
    }

    /**
     * ход игрока (компьютера)
     */
    static void aiTurn() {
        int x;
        int y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));
        field[y][x] = DOT_AI;
    }

    /**
     * проверка, является ли игровая ячейка пустой
     * @param x координата X
     * @param y координата Y
     * @return TRUE, если ячейка пустая
     */
    static boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    /**
     * проверка доступности ячейки игрового поля
     * @param x координата X
     * @param y координата Y
     * @return TRUE, если ячейка в пределах игрового поля
     */
    static boolean isCellValid(int x, int y) {
            return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * метод проверки состояния игры
     * @param dot фишка игрока
     * @param s победный слоган
     * @return результат проверки состояния игры
     */
    static boolean checkGameState(char dot, String s) {
        if(checkWin(dot)) {
            System.out.println(s);
            return true;
        }
        if(checkDraw()) {
            System.out.println("Ничья");
            return true;
        }
        return  false; //Игра продолжается
    }

    /**
     * проверка на ничью
     * @return
     */
    static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if(isCellEmpty(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * проверка победы игрока
     * @param dot фишка игрока
     * @return признак победы
     */
    static  boolean checkWin(char dot){
        // проверка по трем горизонталям
        //   if(field[0][0] == dot && field[0][1] == dot && field[0][2] == dot) return true;
        //   if(field[1][0] == dot && field[1][1] == dot && field[1][2] == dot) return true;
        //   if(field[2][0] == dot && field[2][1] == dot && field[2][2] == dot) return true;

        int horizontalCount = 0;
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if(field[i][j] == dot) {
                    horizontalCount++;
                } else {
                    horizontalCount = 0;
                }
                if(horizontalCount == WIN_COUNT) return true;
            }
        }

        // проверка по трем вертикалям
        //   if(field[0][0] == dot && field[1][0] == dot && field[2][0] == dot) return true;
        //   if(field[0][1] == dot && field[1][1] == dot && field[2][1] == dot) return true;
        //   if(field[0][2] == dot && field[1][2 ] == dot && field[2][2] == dot) return true;

        int verticalCount = 0;
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if(field[j][i] == dot) {
                    verticalCount++;
                } else {
                    verticalCount = 0;
                }
                if(verticalCount == WIN_COUNT) return true;
            }
        }

        // проверка по диагонали вниз       НЕ ПОЛУЧИЛОСЬ
        // проверка по диагонали вверх      НЕ ПОЛУЧИЛОСЬ
        if(field[0][0] == dot && field[1][1] == dot && field[2][2] == dot) return true;
        if(field[0][2] == dot && field[1][1] == dot && field[2][0] == dot) return true;
        return false;
    }

    /**
     * Метод подсчета кол-ва подряд идущих фишек для победы <winCount>
     *     от фишки с координатами (X, Y) по горизонтали вправо
     * @param winCount количество подряд идущих фишек для победы
     * @return возврат результата
     */
    static boolean check1(int x, int y, int winCount) {

        return false;
    }

    /**
     * Метод подсчета кол-ва подряд идущих фишек для победы <winCount>
     *     от фишки с координатами (X, Y) по вертикали вниз
     * @param x координата X
     * @param y координата Y
     * @param winCount количество подряд идущих фишек для победы
     * @return возврат результата
     */
    static boolean check2(int x, int y, int winCount) {

        return false;
    }

    /**
     * Метод подсчета кол-ва подряд идущих фишек для победы <winCount>
     *     от фишки с координатами (X, Y) по диагонали вниз-вправо
     * @param x координата X
     * @param y координата Y
     * @param winCount количество подряд идущих фишек для победы
     * @return возврат результата
     */
    static boolean check3(int x, int y, int winCount) {

        return false;
    }

    /**
     * Метод подсчета кол-ва подряд идущих фишек для победы <winCount>
     *     от фишки с координатами (X, Y) по диагонали вверх-вправо
     * @param x координата X
     * @param y координата Y
     * @param winCount количество подряд идущих фишек для победы
     * @return возврат результата
     */
    static boolean check4(int x, int y, int winCount) {

        return false;
    }
}