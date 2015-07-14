package regio_vinco;

import audio_manager.AudioManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import pacg.PointAndClickGame;
import static regio_vinco.RegioVinco.*;

/**
 * This class is a concrete PointAndClickGame, as specified in The PACG
 * Framework. Note that this one plays Regio Vinco.
 *
 * @author McKillaGorilla
 */
public class RegioVincoGame extends PointAndClickGame {

    // THIS PROVIDES GAME AND GUI EVENT RESPONSES
    RegioVincoController controller;
    RegioVincoDataModel dataModel;

    // THIS PROVIDES MUSIC AND SOUND EFFECTS
    AudioManager audio;
    
    // THESE ARE THE GUI LAYERS
    Pane backgroundLayer;
    Pane gameLayer;
    Pane guiLayer;
    Pane settingsLayer;
    Pane helpLayer;
    
    // GAME BUTTONS
    Button regionButton;
    Button leaderButton;
    Button capitalButton;
    Button flagButton;
    Button stopButton;

    /**
     * Get the game setup.
     */
    public RegioVincoGame(Stage initWindow) {
	super(initWindow, APP_TITLE, TARGET_FRAME_RATE);
        window.setMaximized(false);
	initAudio();
    }
    
    public AudioManager getAudio() {
	return audio;
    }
    
    public Pane getBackgroundLayer(){
        return backgroundLayer;
    }
    
    public Pane getGameLayer() {
	return gameLayer;
    }
    
    public Pane getGuiLayer() {
        return guiLayer;
    }
    
    public Pane getSettingsLayer() {
        return settingsLayer;
    }
    
    public Pane getHelpLayer() {
        return helpLayer;
    }
    
    public Button getRegionButton(){
        return regionButton;
    }
    
    public Button getCapitalButton(){
        return capitalButton;
    }
    
    public Button getLeaderButton(){
        return leaderButton;
    }
    
    public Button getFlagButton(){
        return flagButton;
    }
    
    public Button getStopButton(){
        return stopButton;
    }

    /**
     * Initializes audio for the game.
     */
    private void initAudio() {
	audio = new AudioManager();
	try {
	    audio.loadAudio(TRACKED_SONG, TRACKED_FILE_NAME);
	    audio.play(TRACKED_SONG, true);

	    audio.loadAudio(AFGHAN_ANTHEM, AFGHAN_ANTHEM_FILE_NAME);
	    audio.loadAudio(SUCCESS, SUCCESS_FILE_NAME);
	    audio.loadAudio(FAILURE, FAILURE_FILE_NAME);
	} catch (Exception e) {
	    
	}
    }

    // OVERRIDDEN METHODS - REGIO VINCO IMPLEMENTATIONS
    // initData
    // initGUIControls
    // initGUIHandlers
    // reset
    // updateGUI
    /**
     * Initializes the complete data model for this application, forcing the
     * setting of all game data, including all needed SpriteType objects.
     */
    @Override
    public void initData() {
	// INIT OUR DATA MANAGER
	data = new RegioVincoDataModel();
	data.setGameDimensions(GAME_WIDTH, GAME_HEIGHT);

	boundaryLeft = 0;
	boundaryRight = GAME_WIDTH;
	boundaryTop = 0;
	boundaryBottom = GAME_HEIGHT;
    }

    /**
     * For initializing all GUI controls, specifically all the buttons and
     * decor. Note that this method must construct the canvas with its custom
     * renderer.
     */
    @Override
    public void initGUIControls() {
	// LOAD THE GUI IMAGES, WHICH INCLUDES THE BUTTONS
	// THESE WILL BE ON SCREEN AT ALL TIMES
	backgroundLayer = new Pane();
	addStackPaneLayer(backgroundLayer);
	addGUIImage(backgroundLayer, BACKGROUND_TYPE, loadImage(BACKGROUND_FILE_PATH), BACKGROUND_X, BACKGROUND_Y);
	backgroundLayer.setStyle("-fx-background-color: #000000;");
	// THEN THE GAME LAYER
	gameLayer = new Pane();
	addStackPaneLayer(gameLayer);
	
	// THEN THE GUI LAYER
	guiLayer = new Pane();
	addStackPaneLayer(guiLayer);
        Rectangle subRegionBlock = new Rectangle();
        
        //addGUIButton(guiLayer, STOP_TYPE, loadImage(STOP_BUTTON_FILE_PATH), STOP_X, STOP_Y);
        
        
        // THEN THE SETTINGS WINDOWPANE
        settingsLayer = new Pane();
        addStackPaneLayer(settingsLayer);
        settingsLayer.setStyle("-fx-background-color: #3498db");
        addGUIButton(settingsLayer, WORLD_TYPE2, loadImage(WORLD_BUTTON_FILE_PATH), WORLD_X, WORLD_Y);
        settingsLayer.setVisible(false);
        
        // THEN THE HELP WINDOWPANE
        helpLayer = new Pane();
        addStackPaneLayer(helpLayer);
        helpLayer.setStyle("-fx-background-color: #3498db");
        addGUIButton(helpLayer, WORLD_TYPE, loadImage(WORLD_BUTTON_FILE_PATH), WORLD_X, WORLD_Y);
        helpLayer.setVisible(false);
        
        //
        
        subRegionBlock.setWidth(300);
        subRegionBlock.setHeight(225);
        subRegionBlock.setX(900);
        
        subRegionBlock.setFill(Color.BLACK);
        
        guiLayer.getChildren().add(subRegionBlock);
	addGUIImage(guiLayer, TITLE_TYPE, loadImage(TITLE_FILE_PATH), TITLE_X, TITLE_Y);
        addGUIButton(guiLayer, ENTER_TYPE, loadImage(ENTER_BUTTON_FILE_PATH), ENTER_X, ENTER_Y);
        addGUIButton(guiLayer, SETTINGS_TYPE, loadImage(SETTINGS_BUTTON_FILE_PATH), SETTINGS_X, SETTINGS_Y);
        addGUIButton(guiLayer, HELP_TYPE, loadImage(HELP_BUTTON_FILE_PATH), HELP_X, HELP_Y);
        addGUIButton(guiLayer, REGION_TYPE, loadImage(REGION_BUTTON_FILE_PATH), REGION_X, REGION_Y);
        addGUIButton(guiLayer, LEADER_TYPE, loadImage(LEADER_BUTTON_FILE_PATH), LEADER_X, LEADER_Y);
        addGUIButton(guiLayer, CAPITAL_TYPE, loadImage(CAPITAL_BUTTON_FILE_PATH), CAPITAL_X, CAPITAL_Y);
        addGUIButton(guiLayer, FLAG_TYPE, loadImage(FLAG_BUTTON_FILE_PATH), FLAG_X, FLAG_Y);
        addGUIButton(guiLayer, STOP_TYPE, loadImage(STOP_BUTTON_FILE_PATH), STOP_X, STOP_Y);
        
        regionButton = guiButtons.get(REGION_TYPE);
        regionButton.setDisable(true);
        capitalButton = guiButtons.get(CAPITAL_TYPE);
        capitalButton.setDisable(true);
        leaderButton = guiButtons.get(LEADER_TYPE);
        leaderButton.setDisable(true);
        flagButton = guiButtons.get(FLAG_TYPE);
        flagButton.setDisable(true);
        stopButton = guiButtons.get(STOP_TYPE);
        stopButton.setDisable(true);
        
        
        //uiLayer.getChildren().
	
	// NOTE THAT THE MAP IS ALSO AN IMAGE, BUT
	// WE'LL LOAD THAT WHEN A GAME STARTS, SINCE
	// WE'LL BE CHANGING THE PIXELS EACH TIME
	// FOR NOW WE'LL JUST LOAD THE ImageView
	// THAT WILL STORE THAT IMAGE
	ImageView mapView = new ImageView();
	mapView.setX(MAP_X);
	mapView.setY(MAP_Y);
	guiImages.put(MAP_TYPE, mapView);
	guiLayer.getChildren().add(mapView);

	// NOW LOAD THE WIN DISPLAY, WHICH WE'LL ONLY
	// MAKE VISIBLE AND ENABLED AS NEEDED
	ImageView winView = addGUIImage(guiLayer, WIN_DISPLAY_TYPE, loadImage(WIN_DISPLAY_FILE_PATH), WIN_X, WIN_Y);
	winView.setVisible(false);
        
        addGUIButton(guiLayer, CLOSE_TYPE, loadImage(CLOSE_BUTTON_FILE_PATH), CLOSE_X, CLOSE_Y);
        Button closeButton = guiButtons.get(CLOSE_TYPE);
        closeButton.setBackground(Background.EMPTY);
        closeButton.setVisible(false);
    }
    
    // HELPER METHOD FOR LOADING IMAGES
    private Image loadImage(String imagePath) {	
	Image img = new Image("file:" + imagePath);
	return img;
    }

    /**
     * For initializing all the button handlers for the GUI.
     */
    @Override
    public void initGUIHandlers() {
	controller = new RegioVincoController(this);
        
        Button enterButton = guiButtons.get(ENTER_TYPE);
        enterButton.setBackground(Background.EMPTY);
        enterButton.setOnAction(e -> {
            getBackgroundLayer().getChildren().clear();
            enterButton.setVisible(false);
            controller.processEnterGameRequest();
        });

        Button settingsButton = guiButtons.get(SETTINGS_TYPE);
        settingsButton.setBackground(Background.EMPTY);
        settingsButton.setOnAction(e ->{
            controller.processSettingsButtonRequest();
        });
        
        Button helpButton = guiButtons.get(HELP_TYPE);
        helpButton.setBackground(Background.EMPTY);
        helpButton.setOnAction(e ->{
            controller.processHelpButtonRequest();
        });
        
        regionButton = guiButtons.get(REGION_TYPE);
        regionButton.setBackground(Background.EMPTY);
        regionButton.setOnAction(e ->{
            controller.processRegionButtonRequest();
        });
        
        leaderButton = guiButtons.get(LEADER_TYPE);
        leaderButton.setBackground(Background.EMPTY);
        leaderButton.setOnAction(e ->{
            controller.processLeaderButtonRequest();
        });
        
        capitalButton = guiButtons.get(CAPITAL_TYPE);
        capitalButton.setBackground(Background.EMPTY);
        capitalButton.setOnAction(e ->{
            controller.processCapitalButtonRequest();
        });
        
        flagButton = guiButtons.get(FLAG_TYPE);
        flagButton.setBackground(Background.EMPTY);
        flagButton.setOnAction(e ->{
            controller.processFlagButtonRequest();
        });
        
        stopButton = guiButtons.get(STOP_TYPE);
        stopButton.setBackground(Background.EMPTY);
        stopButton.setOnAction(e ->{
            controller.processStopButtonRequest();
        });
        
        Button closeButton = guiButtons.get(CLOSE_TYPE);
        closeButton.setOnAction(e ->{
            controller.processCloseButtonRequest();
            closeButton.setVisible(false);
            ImageView winView = guiImages.get(WIN_DISPLAY_TYPE);
            winView.setVisible(false);
        });
        
        Button worldButton = guiButtons.get(WORLD_TYPE);
        worldButton.setBackground(Background.EMPTY);
        worldButton.setOnAction(e ->{
            controller.processWorldButtonRequest();
        });
        
        Button worldButton2 = guiButtons.get(WORLD_TYPE2);
        worldButton2.setBackground(Background.EMPTY);
        worldButton2.setOnAction(e ->{
            controller.processWorldButtonRequest();
        });
        
	// MAKE THE CONTROLLER THE HOOK FOR KEY PRESSES
	keyController.setHook(controller);

	// SETUP MOUSE PRESSES ON THE MAP
	ImageView mapView = guiImages.get(MAP_TYPE);
	mapView.setOnMousePressed(e -> {
	    controller.processMapClickRequest((int) e.getX(), (int) e.getY());
	});
	
	// KILL THE APP IF THE USER CLOSES THE WINDOW
	window.setOnCloseRequest(e->{
	    controller.processExitGameRequest();
	});
    }

    /**
     * Called when a game is restarted from the beginning, it resets all game
     * data and GUI controls so that the game may start anew.
     */
    @Override
    public void reset() {
	// IF THE WIN DIALOG IS VISIBLE, MAKE IT INVISIBLE
	ImageView winView = guiImages.get(WIN_DISPLAY_TYPE);
	winView.setVisible(false);
        
        
	// AND RESET ALL GAME DATA
        ((RegioVincoDataModel)data).setRegionName(WORLD_NAME);
        ((RegioVincoDataModel)data).setRegionMapName(WORLD_MAP_NAME);
	data.reset(this);
    }

    /**
     * This mutator method changes the color of the debug text.
     *
     * @param initColor Color to use for rendering debug text.
     */
    public static void setDebugTextColor(Color initColor) {
//        debugTextColor = initColor;
    }

    /**
     * Called each frame, this method updates the rendering state of all
     * relevant GUI controls, like displaying win and loss states and whether
     * certain buttons should be enabled or disabled.
     */
    int backgroundChangeCounter = 0;

    @Override
    public void updateGUI() {
	// IF THE GAME IS OVER, DISPLAY THE APPROPRIATE RESPONSE
        
	if (data.won()) {
	    ImageView winImage = guiImages.get(WIN_DISPLAY_TYPE);
            Button closeButton = guiButtons.get(CLOSE_TYPE);
            closeButton.setVisible(true);
	    winImage.setVisible(true);
	}
    }

    public void reloadMap(String map, String mapName) {
        String path = ((RegioVincoDataModel) getDataModel()).getCurrentDirectory();
	Image tempMapImage = loadImage(path + map + "/" + mapName);
	PixelReader pixelReader = tempMapImage.getPixelReader();
	WritableImage mapImage = new WritableImage(pixelReader, (int) tempMapImage.getWidth(), (int) tempMapImage.getHeight());
	ImageView mapView = guiImages.get(MAP_TYPE);
	mapView.setImage(mapImage);
	int numSubRegions = ((RegioVincoDataModel) data).getRegionsFound() + ((RegioVincoDataModel) data).getRegionsNotFound();
	this.boundaryTop = -(numSubRegions * 50);

	// AND GIVE THE WRITABLE MAP TO THE DATA MODEL
	((RegioVincoDataModel) data).setMapImage(mapImage);
    }
}
