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
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
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
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import java.util.List;

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
    private BitmapText rupeesText;
    private Node rupeeNode =  new Node();
    double[][] rupArray = {
        {-18.69154, 1.0931492E-4, -23.35616},
            {-18.69154, 1.0943413E-4, -23.35616},
    {-18.648258, 0.0025357008, -19.557613},
    {-18.648258, 0.0030688047, -19.557613},
    {-22.398228, 4.8184395E-4, -16.563429},
    {-22.398228, 4.8184395E-4, -16.563429},
    {-23.242813, 0.0012034178, -11.564289},
    {-23.242813, 0.002226472, -11.564289},
    {-23.242813, 4.064724, -11.564289},
    {-23.242813, 4.2730556, -11.564289},
    {-23.076057, 6.1941147E-4, -9.922739},
    {-23.076057, 6.195307E-4, -9.922739}
    };
    
    
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
        rootNode.attachChild(rupeeNode);
        setWorld();
        setPlayer();
        setWater();
        
//        for(int i = 0;i<40;i++)
//            rootNode.attachChild(setCoin(new Vector3f(FastMath.nextRandomInt(-20, 20),FastMath.nextRandomInt(-20, 20),FastMath.nextRandomInt(-20, 20))));
        rupeesText = (BitmapText)this.app.getGuiNode().getChild("Rupees");
        for(int i = 0; i<rupArray.length;i++){
            rootNode.attachChild(setCoin(new Vector3f((float)rupArray[i][0],(float)rupArray[i][1],(float)rupArray[i][2])));
        }
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
        rootNode.attachChild(sceneModel);
    }
    private void setPlayer(){
        bulletAppState.getPhysicsSpace().add(scene);
	Spatial playerModel = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
	player = new ThirdPersonPlayerNode(playerModel, app.getInputManager(), cam);
	player.getCharacterControl().setPhysicsLocation(new Vector3f(0f,6f,0f));
        rootNode.attachChild(player);
        
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
    
    private void setPCoin(Spatial r){
        //r.scale(0.1f,0.1f,0.1f);
        final Spatial coinModel = assetManager.loadModel("Materials/Rupee/Rupee.j3o");
        Material coinMat = new Material(assetManager,"Common/MatDefs/Light/Lighting.j3md");
        //coinMat.setColor("Color", ColorRGBA.Green);
        coinModel.scale(r.getWorldScale().x);
        coinModel.setLocalTranslation(r.getWorldTranslation().x, r.getWorldTranslation().y+1,r.getWorldTranslation().z);
        coinModel.setLocalRotation(r.getWorldRotation());
        coinMat.setFloat("Shininess", 5f);
        coinMat.setBoolean("UseMaterialColors",true);
        coinMat.setColor("Ambient", ColorRGBA.White);
        coinMat.setColor("Specular",ColorRGBA.White);
        coinMat.setColor("Diffuse", ColorRGBA.Green);
        coinModel.setMaterial(coinMat);
        coinModel.setName("Rupee");
        coinModel.addControl(new RupeeControl(this,rootNode));
        r.removeFromParent();
        rupeeNode.attachChild(coinModel);
        
    }
    private Spatial setCoin(Vector3f loc){
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
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        coinModel.setLocalTranslation(loc);
        //CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 3f, 1);
        //coinModel.setMaterial(mat);
        System.out.println(coinModel.getName()+" created");
        coinModel.addControl(new RupeeControl(this, rootNode));
        return coinModel;
    }
    int cont = 0;
    @Override
    public void update(float tpf){
        cont++;
        player.update();
        rupeesText.setText(""+coins);
        if(cont==10){
        cont=0;
        }
        
     }
    
    @Override
    public void cleanup(){
        
    }
    

}
