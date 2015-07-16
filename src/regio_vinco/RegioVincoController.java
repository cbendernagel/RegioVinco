package regio_vinco;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pacg.KeyPressHook;
import static regio_vinco.RegioVinco.WIN_DISPLAY_TYPE;

/**
 * This controller provides the apprpriate responses for all interactions.
 */
public class RegioVincoController implements KeyPressHook {
    RegioVincoGame game;
    
    public RegioVincoController(RegioVincoGame initGame) {
	game = initGame;
    }
    
    public void processStartGameRequest(){
        game.reset();
    }
    
    public void processExitGameRequest() {
	game.killApplication();
    }
    
    public void processEnterGameRequest(){
        game.reset();
    }
    
    public void processSettingsButtonRequest(){
        game.getSettingsLayer().setVisible(true);
    }
    
    public void processRegionButtonRequest(){
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        dataModel.setGameType(1);
        dataModel.setRepeat(true);
        dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
        dataModel.reset(game);
        
    }
    
    public void processLeaderButtonRequest(){
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        dataModel.setGameType(2);
        dataModel.setRepeat(true);
        dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
        dataModel.reset(game);
    }
    
    public void processCapitalButtonRequest(){
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        dataModel.setGameType(3);
        dataModel.setRepeat(true);
        dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
        dataModel.reset(game);
    }
    
    public void processFlagButtonRequest(){
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        dataModel.setGameType(4);
        dataModel.setRepeat(true);
        dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
        dataModel.reset(game);
    }
    
    public void processStopButtonRequest(){
        Alert stopGame = new Alert(AlertType.CONFIRMATION);
        stopGame.setTitle("Stop Game Confirmation");
        stopGame.setHeaderText("Do you want to stop the game?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        stopGame.getButtonTypes().setAll(yesButton,noButton);
        
        Optional<ButtonType> result = stopGame.showAndWait();
        
        
        if(result.get() == yesButton){
            RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
            dataModel.setGameType(0);
            dataModel.setRepeat(true);
            dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
            dataModel.reset(game);
        }
    }
    
    public void processCloseButtonRequest(){
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        dataModel.setGameType(0);
        dataModel.setRepeat(true);
        dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
        ((RegioVincoGame)game).getGUIImages().get(WIN_DISPLAY_TYPE).setVisible(false);
        dataModel.reset(game);
    }
    
    public void processHelpButtonRequest(){
        game.getHelpLayer().setVisible(true);
    }
    
    public void processWorldButtonRequest(){
        game.beginUsingData();
        game.getSettingsLayer().setVisible(false);
        game.getHelpLayer().setVisible(false);
    }
    
    public void processMapClickRequest(int x, int y) {
	((RegioVincoDataModel)game.getDataModel()).respondToMapSelection(game, x, y);
    }
    
    public void processMouseOverRequest(int x, int y){
        ((RegioVincoDataModel)game.getDataModel()).respondToMouseOver(game, x, y);
    }
    
    @Override
    public void processKeyPressHook(KeyEvent ke)
    {
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        KeyCode keyCode = ke.getCode();
        if (keyCode == KeyCode.C && dataModel.getGameType ()!= 0)
        {
            try
            {   
                game.beginUsingData();
                dataModel.removeAllButOneFromeStack(game);         
            }
            finally
            {
                game.endUsingData();
            }
        }
    }   
}
