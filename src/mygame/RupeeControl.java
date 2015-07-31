/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author dmi3coder
 */
public class RupeeControl extends AbstractControl {
    Node rootNode;
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    OutsetIslandAppState state;
    RupeeControl(OutsetIslandAppState state, Node rootNode){
        this.state = state;
        this.rootNode = rootNode;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        spatial.rotate(0,tpf*2,0);
//        Vector3f spat = spatial.getWorldTranslation();
//        Vector3f play = state.getPlayer().getCharacterControl().getPhysicsLocation();
        CollisionResults results = new CollisionResults();
        
        Ray ray = new Ray(spatial.getWorldTranslation(), new Vector3f(spatial.getWorldTranslation().x,spatial.getWorldTranslation().y+2,spatial.getWorldTranslation().z));
        rootNode.collideWith(spatial.getWorldBound() ,results);
        if(results.size()>0){
            //System.out.println("YEP");
            Geometry target = results.getClosestCollision().getGeometry();
            
                if(target.getName().contains("Ninja")){
                    state.plusCoin();
                    spatial.removeFromParent();
                }
            
        }
        
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
//    public Control cloneForSpatial(Spatial spatial) {
//        RupeeControl control = new RupeeControl();
//        //TODO: copy parameters to new Control
//        return control;
//    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
}
