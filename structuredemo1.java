import java.util.ArrayList;
import java.util.List;

// Shape interface
interface Shape {
    void draw(String fill);
}

// Circle class implementing Shape interface
class Circle implements Shape {
    public void draw(String fill) {
        System.out.println("Drawing Circle with color " + fill);
    }
}

// Rectangle class implementing Shape interface
class Rectangle implements Shape {
    public void draw(String fill) {
        System.out.println("Drawing Rectangle with color " + fill);
    }
}

// CompositeShape class implementing Shape interface
class CompositeShape implements Shape {
    private List<Shape> shapes = new ArrayList<>();

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public void remove(Shape shape) {
        shapes.remove(shape);
    }

    public void draw(String fill) {
        for (Shape shape : shapes) {
            shape.draw(fill);
        }
    }
}

// Main class
public class structuredemo1 {
    public static void main(String[] args) {
        CompositeShape rootComposite = new CompositeShape();

        // Predefined shapes and colors
        Shape circle1 = new Circle();
        Shape rectangle1 = new Rectangle();
        Shape circle2 = new Circle();

        // Adding shapes to the root composite
        rootComposite.add(circle1);
        rootComposite.add(rectangle1);
        rootComposite.add(circle2);

        // Drawing individual shapes with their colors
        System.out.println("Drawing individual shapes with their colors:");
        circle1.draw("Blue");
        rectangle1.draw("Green");
        circle2.draw("Yellow");

        // Drawing all shapes in the root composite with a common color
        System.out.println("\nDrawing all shapes in the root composite with color 'Red':");
        rootComposite.draw("Red");
    }
}
