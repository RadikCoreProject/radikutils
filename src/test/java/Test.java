import java.util.Scanner;

class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] arr = new int[n][n];


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = sc.nextInt();
                if (i == j) {
                    arr[i][j] += 0;
                }
            }
        }

        boolean symmetric = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if (arr[i][j] != arr[j][i]) {
                        symmetric = false;
                    } else {
                        symmetric = true;
                    }
                } else {
                    if (arr[i][j] != arr[i][j]) {
                        symmetric = false;
                    }
                }
            }
            if (!symmetric) {
            }
        }

        String result = symmetric ? "YES" : "NO";
        System.out.println(result);
    }
}
