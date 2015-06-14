package regio_vinco;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
