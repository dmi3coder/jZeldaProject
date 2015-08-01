/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.scenes;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mygame.ThirdPersonPlayerNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dmi3coder
 */
//Test class for extension
public class BaseScene extends AbstractAppState {
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private Spatial sceneModel;
    private RigidBodyControl scene;
    private BulletAppState bulletAppState;
    private ThirdPersonPlayerNode player;
    private ViewPort viewPort;
    private BitmapText rupeesText;
    private Node rupeeNode =  new Node();
    ArrayList rupeesLoc;
    
    @Override
    public void initialize(AppStateManager  stateManager ,Application app){
       
    super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();    
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        
	bulletAppState = new BulletAppState();
	stateManager.attach(bulletAppState);
        rootNode.attachChild(rupeeNode);
    }
    
    private float[][] getRupees(String scene){
        try{DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("assets/Scenes/"+scene+"/Rupees.xml"));
        Element rootElement = document.getDocumentElement();
            System.out.println(""+rootElement.getChildNodes());
        NodeList list = rootElement.getElementsByTagName("loc");
            if (list != null && list.getLength() > 0) {
                for(int i = 0; i<list.getLength(); i++){
            NodeList subList = list.item(i).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                System.out.println(""+subList.item(0).getNodeValue());
            }
                }
        }
        }
        catch(Exception e){
            System.out.println(""+e);
        }
        return null;
        
    }
}
