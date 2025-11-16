package org.duikt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    // Український алфавіт для заповнення порожніх клітин
    private static final char[] UA_ALPHABET =
            "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя".toCharArray();

    // Клас для координат отворів трафарету
    private static class Coord {
        int x;
        int y;

        Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Grille {
        int size;
        char[][] data;

        Grille(int size) {
            this.size = size;
            this.data = new char[size][size];
        }

        private Coord rotate(int x, int y, int n) {
            return new Coord(y, n - 1 - x);
        }

        void fill(String plain, String[] mask) {
            char[] pt = plain.toCharArray();
            int ptIdx = 0;
            int alphaIdx = 0;

            List<Coord> baseHoles = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (mask[i].charAt(j) == '1') {
                        baseHoles.add(new Coord(i, j));
                    }
                }
            }

            for (int rot = 0; rot < 4; rot++) {
                List<Coord> rotCoords = new ArrayList<>();

                for (Coord c : baseHoles) {
                    int x = c.x;
                    int y = c.y;

                    for (int k = 0; k < rot; k++) {
                        Coord r = rotate(x, y, size);
                        x = r.x;
                        y = r.y;
                    }
                    rotCoords.add(new Coord(x, y));
                }

                rotCoords.sort(Comparator
                        .comparingInt((Coord c) -> c.x)
                        .thenComparingInt(c -> c.y));

                // Запис символів у клітини згідно з впорядкованими отворами
                for (Coord rc : rotCoords) {
                    int x = rc.x;
                    int y = rc.y;

                    // якщо вже щось записано – пропускаємо
                    if (data[x][y] != '\0') {
                        continue;
                    }

                    if (ptIdx < pt.length) {
                        data[x][y] = pt[ptIdx];
                        ptIdx++;
                    } else {
                        data[x][y] = UA_ALPHABET[alphaIdx % UA_ALPHABET.length];
                        alphaIdx++;
                    }
                }
            }
        }

        // Зчитування таблиці рядок за рядком
        String readRows() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sb.append(data[i][j]);
                }
            }
            return sb.toString();
        }

        // Друк таблиці у консоль
        void print() {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    System.out.print(data[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        String plain = "перестановка";

        String[] mask = {
                "0010",
                "0001",
                "0100",
                "1000"
        };

        int size = 4;

        Grille grille = new Grille(size);
        grille.fill(plain, mask);

        System.out.println("Filled grille:");
        grille.print();

        String cipher = grille.readRows();
        System.out.println("Cipher text: " + cipher);
    }
}
