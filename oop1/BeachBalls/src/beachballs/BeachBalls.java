package beachballs;

public class BeachBalls {
    public static void main(String[] args) {
        Ball ball1 = new Ball("red", 5);
        
        System.out.println(ball1.color);
        System.out.println(ball1.size);
        
        ball1.inflate(2);
        System.out.println(ball1.size);
        
        ball1.deflate(3);
        System.out.println(ball1.size);
        
        
        ball1.deflate(6);
        System.out.println(ball1.size);
        
    }
    
}
