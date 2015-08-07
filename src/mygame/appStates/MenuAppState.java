/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.appStates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import java.util.logging.Handler;

/**
 *
 * @author Дмитрий
 */
public class MenuAppState extends AbstractAppState {
    private InputManager inputManager;
    private Node rootNode;
    private SimpleApplication app;
    Nifty nifty;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
        super.initialize(stateManager, app);
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode();
        this.inputManager = app.getInputManager();
        app.getContext().getMouseInput().setCursorVisible(true);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
        app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        /** Create a new NiftyGUI object */
        nifty = niftyDisplay.getNifty();
        /** Read your XML and initialize your custom ScreenController */
        nifty.fromXml("Interface/GUI/StartScreen.xml", "start");
        // nifty.fromXml("Interface/helloworld.xml", "start", new MySettingsScreen(data));
        // attach the Nifty display to the gui view port as a processor
        app.getGuiViewPort().addProcessor(niftyDisplay);
        
    }
 
    @Override
    public void update(float tpf) {
        nifty.update();
        
        
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
}
