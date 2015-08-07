/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import mygame.appStates.OutsetIslandAppState;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
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
 * @author Дмитрий
 */
public class GrassControl extends AbstractControl {
    Node rootNode;
    private AudioNode audio_gun;
    OutsetIslandAppState state;
    private boolean onGrass = false;
    private boolean isOnGrass = false;
    public GrassControl(OutsetIslandAppState state, Node rootNode){
        this.state = state;
        this.rootNode = rootNode;
        
    }
    int counter = 0;
    @Override
    protected void controlUpdate(float tpf) {
        //Grass wind emulator
       if(counter < 200){
           spatial.rotate(0.001f,0,0);
           counter++;
       }
       else if(counter <400){
           spatial.rotate(-0.001f,0,0);
           counter++;
       }
       else{
           counter=0;
       }
       CollisionResults results = new CollisionResults();
       rootNode.getChild("Link").collideWith(spatial.getWorldBound(),results);
        if(results.size()>0){
//            System.out.println("YEP");
            Geometry target = results.getClosestCollision().getGeometry();
            System.out.println(target.getName());
                if(target.getName().contains("Ninja")){
                   
                }
                else{
                    if(onGrass){
                        spatial.rotate(-0.5f,0,0);
                        onGrass = false;
                    }
                    
                }
            
        }
        
      
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
   
    
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
