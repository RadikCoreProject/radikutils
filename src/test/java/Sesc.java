import java.util.*;
import java.util.Scanner;

public class Sesc {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            points.add(new Point(sc.nextInt(), sc.nextInt()));
        }

        for (int i = 0; i < b; i++) {
            int index = sc.nextInt() - 1;
            int x = sc.nextInt();
            int y = sc.nextInt();
            points.set(index, new Point(x, y));
            List<Point> hull = build(points);
            double area = calc(hull);
            System.out.printf("%.2f%n", area);
        }
    }

    public static double calc(List<Point> points) {
        if (points.size() < 3) return 0.0;

        double area = 0.0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % n);
            area += (p1.x * p2.y) - (p1.y * p2.x);
        }
        return Math.abs(area) / 2.0;
    }

    public static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }
    }

    public static List<Point> build(List<Point> points) {
        if (points.size() <= 3) {
            return remove(points);
        }

        List<Point> unique = remove(points);
        if (unique.size() <= 3) {
            return unique;
        }

        Point start = unique.getFirst();
        for (int i = 1; i < unique.size(); i++) {
            Point p = unique.get(i);
            if (p.y < start.y || (p.y == start.y && p.x < start.x)) {
                start = p;
            }
        }

        List<Point> sorted = new ArrayList<>(unique);
        Point f = start;
        sorted.sort((p1, p2) -> {
            if (p1.equals(f)) return -1;
            if (p2.equals(f)) return 1;

            int cross = cross(f, p1, p2);
            if (cross != 0) {
                return -cross;
            }

            long dist1 = distance(f, p1);
            long dist2 = distance(f, p2);
            return Long.compare(dist1, dist2);
        });

        List<Point> filter = new ArrayList<>();
        filter.add(sorted.getFirst());
        for (int i = 1; i < sorted.size(); i++) {
            while (i < sorted.size() - 1 && cross(start, sorted.get(i), sorted.get(i + 1)) == 0) {
                i++;
            }
            filter.add(sorted.get(i));
        }

        Stack<Point> stack = new Stack<>();
        stack.push(filter.get(0));
        stack.push(filter.get(1));

        for (int i = 2; i < filter.size(); i++) {
            Point top = stack.pop();
            while (!stack.isEmpty() && cross(stack.peek(), top, filter.get(i)) <= 0) {
                top = stack.pop();
            }
            stack.push(top);
            stack.push(filter.get(i));
        }

        Point last = stack.pop();
        if (cross(stack.peek(), last, stack.get(0)) != 0) {
            stack.push(last);
        }

        return new ArrayList<>(stack);
    }

    public static int cross(Point p1, Point p2, Point p3) {
        return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
    }

    public static long distance(Point p1, Point p2) {
        long dx = p1.x - p2.x;
        long dy = p1.y - p2.y;
        return dx * dx + dy * dy;
    }

    public static List<Point> remove(List<Point> points) {
        Set<Point> seen = new HashSet<>();
        List<Point> result = new ArrayList<>();
        for (Point p : points) {
            if (!seen.contains(p)) {
                seen.add(p);
                result.add(p);
            }
        }
        return result;
    }
}