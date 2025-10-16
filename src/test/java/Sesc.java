import org.radikutils.numeric.StaticBases;

import java.util.Random;
import java.util.Scanner;

import static org.radikutils.numeric.StaticBases.base2ten;
import static org.radikutils.numeric.StaticBases.ten2base;

public class Sesc {
    public static void main(String[] args) {
        System.out.println((Math.pow(2, 20) * 40 - 640 * 1536 * 8) / 640 / 640 / 400 * 8);
    }
}
