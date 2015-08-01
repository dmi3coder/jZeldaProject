/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dmi3coder
 */
public class OutsetIslandAppState extends AbstractAppState {
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private Spatial sceneModel;
    private RigidBodyControl scene;
    private BulletAppState bulletAppState;
    private ThirdPersonPlayerNode player;
    private ViewPort viewPort;
    private int coins= 0;
    private Node rupeeNode =  new Node("rupeeNode");
    private ArrayList<String> rupeesLoc = new ArrayList();
    private BitmapText distanceText;
    
    public ThirdPersonPlayerNode getPlayer(){
        return player;
    }
    
    public void plusCoin(){
        coins++;
    }
    
    @Override
    public void initialize(AppStateManager  stateManager ,Application app){
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();    
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        
	bulletAppState = new BulletAppState();
	stateManager.attach(bulletAppState);
        setWorld();
        setPlayer();
        setWater();
        setHud();
        getRupees();
        
//        for(int i = 0;i<40;i++)
//            rootNode.attachChild(setCoin(new Vector3f(FastMath.nextRandomInt(-20, 20),FastMath.nextRandomInt(-20, 20),FastMath.nextRandomInt(-20, 20))));
        
    }
    
    private void setHud(){
        Picture frame = new Picture("RupeeImage");
        frame.setImage(assetManager, "Interface/RupeeCounter/RupeeImage.png", true);
        frame.move(app.getContext().getSettings().getWidth()-140,40,-2);
        frame.setWidth(30);
        frame.setHeight(30);
        app.getGuiNode().attachChild(frame);
        
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/GAMECUBEN.fnt");
        distanceText =  new BitmapText(guiFont);
        distanceText.setSize(guiFont.getCharSet().getRenderedSize());
        distanceText.move(app.getContext().getSettings().getWidth()-110,
                distanceText.getLineHeight()+40,
                0);
        distanceText.setName("Rupees");
        distanceText.setText("Test");
        app.getGuiNode().attachChild(distanceText);
    }   

    private void setWorld(){
        viewPort = app.getViewPort();
 
	//bulletAppState.getPhysicsSpace().enableDebug(assetManager);
 
        sceneModel = assetManager.loadModel("Scenes/TestScake/TestScake.j3o");
	//sceneModel.scale(10f,10f,10f);
        sceneModel.setLocalScale(10f, 10f, 10f);
        //sceneModel.setLocalTranslation(0f, 0f, -200f);
        //Make scenery short enough to jump on. =P
	CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
	scene = new RigidBodyControl(sceneShape, 0);
	sceneModel.addControl(scene);
        sceneModel.setName("Scene1");
        Node worldNode = new Node("World");
        rootNode.attachChild(worldNode);
        worldNode.attachChild(sceneModel);
        //getRupees();
        //setCoin(new Vector3f(-23.242813f, 4.064724f, -11.564289f));
//        Node Scene1 = (Node)rootNode.getChild("Scene1");
//        rupeeNode = (Node)Scene1.getChild("Rupees");
//        for(Spatial s : rupeeNode.getChildren()){
//            setPCoin(s);
//        }
        
    }
    private void getRupees(){
        rootNode.attachChild(rupeeNode);
        try{DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("assets/Scenes/TestScake/Rupees.xml"));
        Element rootElement = document.getDocumentElement();
            System.out.println(""+rootElement.getChildNodes());
        NodeList list = rootElement.getElementsByTagName("loc");
            if (list != null && list.getLength() > 0) {
                for(int i = 0; i<list.getLength(); i++){
            NodeList subList = list.item(i).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                rupeesLoc.add((String)subList.item(0).getNodeValue());
            }
                }
        }
        }
        catch(Exception e){
            System.out.println(""+e);
        }
        Vector3f loc;
        for(String s:rupeesLoc){
            char c = ',';
            char[] value1= new char[s.indexOf(c)+1];
            s.getChars(0, s.indexOf(c), value1, 1);
            String rupLoc1 = new String(value1);
            char[] value2= new char[s.lastIndexOf(c)-s.indexOf(c)+1];
            s.getChars(s.indexOf(c)+1, s.lastIndexOf(','), value2, 1);
            String rupLoc2 = new String(value2);
            char[] value3 = new char[s.length()-s.lastIndexOf(c)+1];
            s.getChars(s.lastIndexOf(c)+1, s.length(), value3, 1);
            String rupLoc3 = new String(value3);
            loc = new Vector3f(Float.parseFloat(rupLoc1),Float.parseFloat(rupLoc2),Float.parseFloat(rupLoc3));
            setCoin(loc);
        }
        
        
    }
    
    private void setPlayer(){
        bulletAppState.getPhysicsSpace().add(scene);
	Spatial playerModel = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
	player = new ThirdPersonPlayerNode(playerModel, app.getInputManager(), cam);
	player.getCharacterControl().setPhysicsLocation(new Vector3f(0f,6f,0f));
        rootNode.attachChild(player);
        player.setName("Ninja");
        
	bulletAppState.getPhysicsSpace().add(player);
        
        rootNode.attachChild(SkyFactory.createSky(
            assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
    }
    
    private void setWater(){
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-.1f, -.7f, -1f));
        rootNode.addLight(sun);
        // we create a water processor
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(rootNode);

        // we set the water plane
        Vector3f waterLocation=new Vector3f(0,0,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterProcessor);

        // we set wave properties
        waterProcessor.setWaterDepth(40);         // transparency of water
        waterProcessor.setDistortionScale(0.05f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves

        // we define the wave size by setting the size of the texture coordinates
        Quad quad = new Quad(400,400);
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        // we create the water geometry from the quad
        Geometry water=new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-200, -0.1f, 250);
        water.setShadowMode(RenderQueue.ShadowMode.Receive);
        water.setMaterial(waterProcessor.getMaterial());
        rootNode.attachChild(water);
     }
    
    private void setCoin(Vector3f loc){
        final Spatial coinModel = assetManager.loadModel("Materials/Rupee/Rupee.j3o");
        coinModel.scale(0.1f, 0.1f, 0.1f);
        
        Material coinMat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        //coinMat.setColor("Color", ColorRGBA.Green);
        coinMat.setFloat("Shininess", 5f);
        coinMat.setBoolean("UseMaterialColors",true);
        coinMat.setColor("Ambient", ColorRGBA.White);
        coinMat.setColor("Specular",ColorRGBA.White);
        coinMat.setColor("Diffuse", ColorRGBA.Green);
        coinModel.setMaterial(coinMat);
        coinModel.setLocalTranslation(loc);
        coinModel.setName("Rupee");
        System.out.println(coinModel.getName()+" created");
        coinModel.addControl(new RupeeControl(this, rootNode));
        rootNode.attachChild(coinModel);
    }
    
    @Override
    public void update(float tpf){
        player.update();
        distanceText.setText(""+coins);
     }
    
    @Override
    public void cleanup(){
        
    }
    

}
