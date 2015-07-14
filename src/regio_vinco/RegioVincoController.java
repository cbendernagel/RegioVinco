package regio_vinco;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pacg.KeyPressHook;

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
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        dataModel.setGameType(0);
        dataModel.setRepeat(true);
        dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
        dataModel.reset(game);
    }
    
    public void processCloseButtonRequest(){
        RegioVincoDataModel dataModel = ((RegioVincoDataModel)((RegioVincoGame)game).getDataModel());
        dataModel.setGameType(0);
        dataModel.setRepeat(true);
        dataModel.setCurrentDirectory(dataModel.getCurrentDirectory().replace(dataModel.getRegionName() + "/", ""));
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
    
    @Override
    public void processKeyPressHook(KeyEvent ke)
    {
        KeyCode keyCode = ke.getCode();
        if (keyCode == KeyCode.C)
        {
            try
            {   
                game.beginUsingData();
                RegioVincoDataModel dataModel = (RegioVincoDataModel)(game.getDataModel());
                dataModel.removeAllButOneFromeStack(game);         
            }
            finally
            {
                game.endUsingData();
            }
        }
    }   
}
