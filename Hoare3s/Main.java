package Hoare3s;

public class Main {
    public static void main(String[] args) {
        System.out.println(maxAbs(-3, 4));
        System.out.println(maxAbs(4, -3));
        System.out.println(maxAbs(0, 0));
        System.out.println(maxAbs(1000000000, 999999999));
    }



    static int abs(int x) {
        int result;
        if (x < 0) {
            result = -x;
        } else {
            result = x;
        }
        assert result == Math.abs(x);
        return result;
    }


    static int max(int a, int b) {
        int result;
        if (a > b) {
            result = a;
        } else {
            result = b;
        }
        assert result == Math.max(a, b);
        return result;
    }



    static int maxAbs(int a, int b) {

        assert Integer.MIN_VALUE <= a && a <= Integer.MAX_VALUE;
        assert Integer.MIN_VALUE <= b && b <= Integer.MAX_VALUE;

        int absA = abs(a);
        int absB = abs(b);
        int result = max(absA, absB);


        assert result == Math.max(Math.abs(a), Math.abs(b));

        return result;
    }

}