package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * jZeldaProject
 *
 * @author dmi3coder
 */
public class Main extends SimpleApplication {

    public ViewPort viewPortP = viewPort;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        OutsetIslandAppState state = new OutsetIslandAppState();
        flyCam.setMoveSpeed((float) 40.0);
        mouseInput.setCursorVisible(false);
        flyCam.setEnabled(false);
        stateManager.attach(state);
        setDisplayStatView(false);
        setDisplayFps(false);




    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
