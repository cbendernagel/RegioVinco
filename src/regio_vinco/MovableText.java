package regio_vinco;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This class represents a game text node that can be moved and
 * rendered and can respond to mouse interactions.
 */
public class MovableText {
    // A JAVAFX TEXT NODE IN THE SCENE GRAPH
    protected Text text;
    protected ImageView imageView;
    protected Rectangle  rectangle;
    
    // USED FOR MANAGING NODE MOVEMENT
    protected double[] velocity = new double[2];
    protected double[] acceleration = new double[2];

    /**
     * Constructor for initializing a GameNode, note that the provided
     * text argument should not be null.
     * 
     * @param initText The text managed by this object.
     */
    public MovableText(Text initText) {
	text = initText;
        rectangle = new Rectangle();
        rectangle.setWidth(300);
        rectangle.setHeight(50);
        rectangle.setFill(Color.BLUE);
        //rectangle.setOpacity(0);
    }
    
    public MovableText(){}
  
    public MovableText(ImageView initImageView, Text initText){
        imageView = initImageView;
        rectangle = new Rectangle();
        rectangle.setWidth(300);
        rectangle.setHeight(imageView.getImage().getHeight());
        rectangle.setFill(Color.BLUE);
        text = initText;
    }
    
    // ACCESSOR AND MUTATOR METHODS
    
    public Text getText() {
	return text;
    }
    
    public void setText(Text initText) {
	text = initText;
    }
    
    public ImageView getImageView(){
        return imageView;
    }
    
    public void getImageView(ImageView initImageView){
        imageView = initImageView;
    }
    
    public Rectangle getRectangle(){
        return rectangle;
    }
    
    public void setRectangle(Rectangle initRectangle){
        rectangle = initRectangle;
    }
    
    public double getVelocityX() {
	return velocity[0];
    }
    
    public double getVelocityY() {
	return velocity[1];
    }
    
    public void setVelocityX(double initVelocityX) {
	velocity[0] = initVelocityX;
    }
    
    public void setVelocityY(double initVelocityY) {
	velocity[1] = initVelocityY;
    }
    
    public void setAccelerationX(double initAccelerationX){
        acceleration[0] = initAccelerationX;
    }
    
    public void setAccelerationY(double initAccelerationY){
        acceleration[1] = initAccelerationY;
    }

    /**
     * Called each frame, this function moves the node according
     * to its current velocity and updates the velocity according to
     * its current acceleration, applying percentage as a weighting for how
     * much to scale the velocity and acceleration this frame.
     * 
     * @param percentage The percentage of a frame this the time step
     * that called this method represents.
     */
    public void update(double percentage) {
	// UPDATE POSITION
	double x = text.translateXProperty().doubleValue();
	text.translateXProperty().setValue(x + (velocity[0] * percentage));
	double y = text.translateYProperty().doubleValue();
	text.translateYProperty().setValue(y + (velocity[1] * percentage));
        
        // UPDATE POSITION
	double ix = imageView.translateXProperty().doubleValue();
	imageView.translateXProperty().setValue(ix + (velocity[0] * percentage));
	double iy = imageView.translateYProperty().doubleValue();
	imageView.translateYProperty().setValue(iy + (velocity[1] * percentage));
	
        double rX = rectangle.translateXProperty().doubleValue();
        rectangle.translateXProperty().setValue(x + (velocity[0] * percentage));
        double rY = rectangle.translateYProperty().doubleValue();
	rectangle.translateYProperty().setValue(y + (velocity[1] * percentage));
        
	// UPDATE VELOCITY
	velocity[0] += (acceleration[0] * percentage);
	velocity[1] += (acceleration[1] * percentage);
    }
}