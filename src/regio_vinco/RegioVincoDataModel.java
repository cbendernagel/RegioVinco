package regio_vinco;

import audio_manager.AudioManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.event.EventType;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import pacg.PointAndClickGame;
import pacg.PointAndClickGameDataModel;
import static regio_vinco.RegioVinco.*;

/**
 * This class manages the game data for the Regio Vinco game application. Note
 * that this game is built using the Point & Click Game Framework as its base. 
 * This class contains methods for managing game data and states.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class RegioVincoDataModel extends PointAndClickGameDataModel {
    // THIS IS THE MAP IMAGE THAT WE'LL USE
    private WritableImage mapImage;
    private PixelReader mapPixelReader;
    private PixelWriter mapPixelWriter;
    
    // AND OTHER GAME DATA
    private String regionName;
    private String regionMapName;
    private String subRegionsType;
    private HashMap<Color, String> colorToSubRegionMappings;
    private HashMap<String, Color> subRegionToColorMappings;
    private HashMap<String, ArrayList<int[]>> pixels;
    private LinkedList<String> redSubRegions;
    private LinkedList<MovableText> subRegionStack;
    private LinkedList<Text> navigatedRegions;
    
    //GAME STATISTICS AND TEXT BOXES
    private Text regionsFound;
    private Text regionsLeft;
    private Text incorrectGuesses;
    private Text gameTimer;
    private Text fullStats;
    private int regionsFoundInt;
    private int regionsLeftInt;
    private int incorrectGuessesInt;
    private int totalSubRegions;
    
    //START TIMER
    private long startTimer;

    /**
     * Default constructor, it initializes all data structures for managing the
     * Sprites, including the map.
     */
    public RegioVincoDataModel() {
	// INITIALIZE OUR DATA STRUCTURES
	colorToSubRegionMappings = new HashMap();
	subRegionToColorMappings = new HashMap();
	subRegionStack = new LinkedList();
	redSubRegions = new LinkedList();
        navigatedRegions = new LinkedList();
    }
    
    public void setMapImage(WritableImage initMapImage) {
	mapImage = initMapImage;
	mapPixelReader = mapImage.getPixelReader();
	mapPixelWriter = mapImage.getPixelWriter();
    }

    public void removeAllButOneFromeStack(RegioVincoGame game) {
        Pane gameLayer = game.getGameLayer();
        for (String s : redSubRegions) {
		Color subRegionColor = subRegionToColorMappings.get(s);
		changeSubRegionColorOnMap(game, s, subRegionColor);
	    }
	    redSubRegions.clear();
        while (subRegionStack.size() > 1) {
	    MovableText text = subRegionStack.removeFirst();
            gameLayer.getChildren().remove(text.getRectangle());
            gameLayer.getChildren().remove(text.getText());
	    String subRegionName = text.getText().getText();
            
	    // TURN THE TERRITORY GREEN
	    changeSubRegionColorOnMap(game, subRegionName, Color.GREEN);
	}
	startTextStackMovingDown();
    }

    // ACCESSOR METHODS
    public String getRegionName() {
	return regionName;
    }
    
    public String getRegionMapName(){
        return regionMapName;
    }
    
    public void setRegionName(String regionName) {
	this.regionName = regionName;
    }
    
    public void setRegionMapName(String regionMapName){
        this.regionMapName = regionMapName;
    }


    public String getSubRegionsType() {
	return subRegionsType;
    }

    public void setSubRegionsType(String initSubRegionsType) {
	subRegionsType = initSubRegionsType;
    }

    public String getSecondsAsTimeText(long numSeconds) {
	long numHours = numSeconds / 3600;
	numSeconds = numSeconds - (numHours * 3600);
	long numMinutes = numSeconds / 60;
	numSeconds = numSeconds - (numMinutes * 60);

	String timeText = "";
	if (numHours > 0) {
	    timeText += numHours + ":";
	}
	timeText += numMinutes + ":";
	if (numSeconds < 10) {
	    timeText += "0" + numSeconds;
	} else {
	    timeText += numSeconds;
	}
	return timeText;
    }

    public int getRegionsFound() {
	return colorToSubRegionMappings.keySet().size() - subRegionStack.size();
    }

    public int getRegionsNotFound() {
	return subRegionStack.size();
    }
    
    public LinkedList<MovableText> getSubRegionStack() {
	return subRegionStack;
    }
    
    public String getSubRegionMappedToColor(Color colorKey) {
	return colorToSubRegionMappings.get(colorKey);
    }
    
    public Color getColorMappedToSubRegion(String subRegion) {
	return subRegionToColorMappings.get(subRegion);
    }

    // MUTATOR METHODS

    public void addColorToSubRegionMappings(Color colorKey, String subRegionName) {
	colorToSubRegionMappings.put(colorKey, subRegionName);
    }

    public void addSubRegionToColorMappings(String subRegionName, Color colorKey) {
	subRegionToColorMappings.put(subRegionName, colorKey);
    }

    public void respondToMapSelection(RegioVincoGame game, int x, int y) {
        // THIS IS WHERE WE'LL CHECK TO SEE IF THE
	// PLAYER CLICKED NO THE CORRECT SUBREGION
        Pane gameLayer = game.getGameLayer();
	Color pixelColor = mapPixelReader.getColor(x, y);
	String clickedSubRegion = colorToSubRegionMappings.get(pixelColor);
	if ((clickedSubRegion == null) || (subRegionStack.isEmpty())) {
	    return;
	}
	if (clickedSubRegion.equals(subRegionStack.get(0).getText().getText())) {
	    // YAY, CORRECT ANSWER
	    game.getAudio().play(SUCCESS, false);
            
            
	    // TURN THE TERRITORY GREEN
	    changeSubRegionColorOnMap(game, clickedSubRegion, Color.GREEN);

	    // REMOVE THE BOTTOM ELEMENT FROM THE STACK
	    MovableText removed = subRegionStack.removeFirst();
            gameLayer.getChildren().remove(removed.getText());
            gameLayer.getChildren().remove(removed.getRectangle());

	    // AND LET'S CHANGE THE RED ONES BACK TO THEIR PROPER COLORS
	    for (String s : redSubRegions) {
		Color subRegionColor = subRegionToColorMappings.get(s);
		changeSubRegionColorOnMap(game, s, subRegionColor);
	    }
	    redSubRegions.clear();

	    startTextStackMovingDown();
            
            regionsFoundInt++;
            regionsLeftInt--;

	    if (subRegionStack.isEmpty()) {
                gameLayer.getChildren().clear();
                colorToSubRegionMappings.clear();
                subRegionToColorMappings.clear();
                for(int i = 0; i < mapImage.getWidth(); i++){
                    for(int j = 0; j < mapImage.getHeight(); j++){
                        mapPixelWriter.setColor(i, j, Color.TRANSPARENT);
                    }
                }
                
                Long totalTime = (GregorianCalendar.getInstance().getTimeInMillis()/1000 - startTimer);
                long score = 1000 - (totalTime) - (100 * incorrectGuessesInt); 
                
                //CREATE STATISTIC PANE
                String statString = String.format("Region:  Afghanistan\nScore:  %d\nGame Duration:  %02d:%02d\nSub Regions:  %d\nIncorrect Guesses:  %d",score,totalTime/60,totalTime%60,totalSubRegions,incorrectGuessesInt);
                fullStats = new Text(statString);
                fullStats.setX(400);
                fullStats.setY(350);
                
                game.getGuiLayer().getChildren().add(fullStats);
                
		this.endGameAsWin();
		game.getAudio().stop(TRACKED_SONG);
		game.getAudio().play(AFGHAN_ANTHEM, false);  
	    }
	} else {
            incorrectGuessesInt++;
	    if (!redSubRegions.contains(clickedSubRegion)) {
		// BOO WRONG ANSWER
		game.getAudio().play(FAILURE, false);

		// TURN THE TERRITORY TEMPORARILY RED
		changeSubRegionColorOnMap(game, clickedSubRegion, Color.RED);
		redSubRegions.add(clickedSubRegion);
	    }
	}
    }

    public void startTextStackMovingDown() {
	// AND START THE REST MOVING DOWN
	for (MovableText mT : subRegionStack) {
            //mT.setAccelerationY(1);
	    mT.setVelocityY(SUB_STACK_VELOCITY);
        }
    }

    public void changeSubRegionColorOnMap(RegioVincoGame game, String subRegion, Color color) {
        // THIS IS WHERE WE'LL CHECK TO SEE IF THE
	// PLAYER CLICKED NO THE CORRECT SUBREGION
	ArrayList<int[]> subRegionPixels = pixels.get(subRegion);
	for (int[] pixel : subRegionPixels) {
	    mapPixelWriter.setColor(pixel[0], pixel[1], color);
	}
    }

    public int getNumberOfSubRegions() {
	return colorToSubRegionMappings.keySet().size();
    }

    /**
     * Resets all the game data so that a brand new game may be played.
     *
     * @param game the Zombiquarium game in progress
     */
    @Override
    public void reset(PointAndClickGame game) {
        
        if(((RegioVincoGame)game).getGuiLayer().getChildren().contains(fullStats))
            ((RegioVincoGame)game).getGuiLayer().getChildren().remove(fullStats);
        
        //RESET ALL STATISTICS
        //regionsFoundInt = 0;
        //incorrectGuessesInt = 0;
        //regionsLeftInt = 0;
        

        //Pane statistics = new Pane();

        
	// LET'S CLEAR THE DATA STRUCTURES
	colorToSubRegionMappings.clear();
	subRegionToColorMappings.clear();
	subRegionStack.clear();
	redSubRegions.clear();

        // INIT THE MAPPINGS - NOTE THIS SHOULD 
	// BE DONE IN A FILE, WHICH WE'LL DO IN
	// FUTURE HOMEWORK ASSIGNMENTS
        
        
        
        
        /**
         * HAVE THE WORLD MAP LOADED FIRST, THEN WAIT FOR MAP PRESS INPUT
         * CHECK IF DIRECTORY EXISTS, IF IT DOES LOAD IT. IF IT DOESN'T DON'T DO ANYTHING
         */
	
        
        regionsLeftInt = colorToSubRegionMappings.size();
        totalSubRegions = regionsLeftInt;
        
	// REST THE MOVABLE TEXT
	Pane gameLayer = ((RegioVincoGame)game).getGameLayer();
        
        if(navigatedRegions.size() > 1){
            navigatedRegions.getFirst().setFill(Color.WHITE);
        }
        
	gameLayer.getChildren().clear();
        Text regionText = new Text(regionName);
        regionText.setX(NAVIGATION_X * navigatedRegions.size());
        regionText.setY(NAVIGATION_Y);
        regionText.setFill(Color.YELLOW);
        regionText.resize(100, 50);
        regionText.setOnMouseClicked(e -> {
            System.out.println(regionText.getText());
            
        });
        regionText.setStyle("-fx-font: 30px Verdana");
        navigatedRegions.add(regionText);
        ((RegioVincoGame)game).getGuiLayer().getChildren().add(regionText); 
        
        regionsFound = new Text();
        regionsLeft = new Text();
        incorrectGuesses = new Text();
        gameTimer = new Text();
        
	for (Color c : colorToSubRegionMappings.keySet()) {
	    String subRegion = colorToSubRegionMappings.get(c);
	    subRegionToColorMappings.put(subRegion, c);
	    Text textNode = new Text(subRegion);
            textNode.setStyle("-fx-font: 25px Calibri");
            textNode.setFill(Color.NAVY);
	    MovableText subRegionText = new MovableText(textNode);
	    //subRegionText.getText().setFill(REGION_NAME_COLOR);
	    textNode.setX(STACK_X);
            subRegionText.getRectangle().setFill(c);
            gameLayer.getChildren().add(subRegionText.getRectangle());
            gameLayer.getChildren().add(textNode);
	    subRegionStack.add(subRegionText);
	}
	Collections.shuffle(subRegionStack);
	int y = STACK_INIT_Y;
	// NOW FIX THEIR Y LOCATIONS
	for (MovableText mT : subRegionStack) {
	    mT.getText().setY(y);
            y -= STACK_INIT_Y_INC;
            mT.getRectangle().setY(y + 20);
            mT.getRectangle().setX(mT.getText().getX());
	}

	// RELOAD THE MAP
	((RegioVincoGame) game).reloadMap(regionName, regionMapName);
        
        

	// LET'S RECORD ALL THE PIXELS
	pixels = new HashMap();
	for (MovableText mT : subRegionStack) {
	    pixels.put(mT.getText().getText(), new ArrayList());
	}
        Color orangeChange = mapPixelReader.getColor(0, 0);
	for (int i = 0; i < mapImage.getWidth(); i++) {
	    for (int j = 0; j < mapImage.getHeight(); j++) {
		Color c = mapPixelReader.getColor(i, j);
                //CHECKS FOR OUTSIDE BORDER ORANGE, CHANGES IF TRUE
                if (c.equals(orangeChange)){
                    mapPixelWriter.setColor(i, j, Color.TRANSPARENT);
                }
		if (colorToSubRegionMappings.containsKey(c)) {
		    String subRegion = colorToSubRegionMappings.get(c);
		    ArrayList<int[]> subRegionPixels = pixels.get(subRegion);
		    int[] pixel = new int[2];
		    pixel[0] = i;
		    pixel[1] = j;
		    subRegionPixels.add(pixel);
		}
	    }
	}
        
        //CREATING TEXT BOXES FOR STATISTICS
        regionsFound.setText("Regions Found: " + regionsFoundInt);
        regionsFound.setX(300);
        regionsFound.setY(650);
        regionsFound.setFill(Color.ORANGE);
        regionsFound.setStyle("-fx-font: 20px Calibri");
        
        regionsLeft.setText("Regions Left: " + regionsLeftInt);
        regionsLeft.setX(500);
        regionsLeft.setY(650);
        regionsLeft.setFill(Color.ORANGE);
        regionsLeft.setStyle("-fx-font: 20px Calibri");
        
        incorrectGuesses.setText("Incorrect Guesses: " + incorrectGuessesInt);
        incorrectGuesses.setX(700);
        incorrectGuesses.setY(650);
        incorrectGuesses.setFill(Color.ORANGE);
        incorrectGuesses.setStyle("-fx-font: 20px Calibri");
        
        startTimer = GregorianCalendar.getInstance().getTimeInMillis()/1000;
        gameTimer.setText("Time Elapsed: " + "00:" + startTimer);
        gameTimer.setX(100);
        gameTimer.setY(650);
        gameTimer.setFill(Color.ORANGE);
        gameTimer.setStyle("-fx-font: 20px Calibri");
        
        gameLayer.getChildren().add(regionsFound);
        gameLayer.getChildren().add(regionsLeft);
        gameLayer.getChildren().add(incorrectGuesses);
        gameLayer.getChildren().add(gameTimer);
        
	// RESET THE AUDIO
	AudioManager audio = ((RegioVincoGame) game).getAudio();
	audio.stop(AFGHAN_ANTHEM);

	if (!audio.isPlaying(TRACKED_SONG)) {
	    audio.play(TRACKED_SONG, true);
	}
	// LET'S GO
	beginGame();
    }
   
    // HELPER METHOD FOR MAKING A COLOR OBJECT
    public static Color makeColor(int r, int g, int b) {
	return Color.color(r/255.0, g/255.0, b/255.0);
    }

    // STATE TESTING METHODS
    // UPDATE METHODS
	// updateAll
	// updateDebugText
    
    /**
     * Called each frame, this thread already has a lock on the data. This
     * method updates all the game sprites as needed.
     *
     * @param game the game in progress
     */
    @Override
    public void updateAll(PointAndClickGame game, double percentage) {
	long minutes = ((GregorianCalendar.getInstance().getTimeInMillis()/1000) - startTimer)/60;
        long seconds = ((GregorianCalendar.getInstance().getTimeInMillis()/1000) - startTimer)%60;
        String time = String.format("Time Elapsed: %02d:%02d",minutes,seconds);
        for (MovableText mT : subRegionStack) {
            mT.getText().setY(mT.getText().getY() + mT.getVelocityY());
            mT.getRectangle().setY(mT.getRectangle().getY() + mT.getVelocityY());
	    mT.update(percentage);
	}

	if (!subRegionStack.isEmpty()) {
	    MovableText bottomOfStack = subRegionStack.get(0);
            bottomOfStack.getText().setFill(Color.RED);
            bottomOfStack.getRectangle().setFill(Color.GREEN);
            double bottomY = bottomOfStack.getText().getY() + bottomOfStack.getText().translateYProperty().doubleValue();
	    if (bottomY >= FIRST_REGION_Y_IN_STACK) {
		double diffY = bottomY - FIRST_REGION_Y_IN_STACK;
		for (MovableText mT : subRegionStack) {
		    mT.getText().setY(mT.getText().getY() - diffY);
		    mT.setVelocityY(0);
		}
            }
	}
        
        regionsFound.setText("Regions Found: " + regionsFoundInt);
        regionsLeft.setText("Regions Left: " + regionsLeftInt);
        incorrectGuesses.setText("Incorrect Guesses: " + incorrectGuessesInt);
        gameTimer.setText(time);
    }

    /**
     * Called each frame, this method specifies what debug text to render. Note
     * that this can help with debugging because rather than use a
     * System.out.print statement that is scrolling at a fast frame rate, we can
     * observe variables on screen with the rest of the game as it's being
     * rendered.
     *
     * @return game the active game being played
     */
    public void updateDebugText(PointAndClickGame game) {
	debugText.clear();
    }
}
