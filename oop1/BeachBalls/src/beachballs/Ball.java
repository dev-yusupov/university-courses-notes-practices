package beachballs;

public class Ball {
    private String color;
    private int size;

    public Ball(String color, int size) {
        this.color = color;
        this.size = size;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public void setColor(String newColor) {
        this.color = newColor;
    }
    
    public void inflate(int s) {
        // size = size + s
        size += s;
        
    }
    public void deflate(int s){
        size = Math.max(0, size - s);
    }
}
