import java.util.ArrayList;

import toxi.geom.Vec3D;

public class voxelgrid {
	
	  // currently set up only for pyramid grid voxels
	  
	  // VARIABLES
	  int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
	  float[] voxelSize;//sizes in x,y,z directions
	  ArrayList<Voxel> voxels = new ArrayList<Voxel>();// the solid voxels that contain an Agent
	  ArrayList<Vec3D> centersVoxel = new ArrayList<Vec3D>();// centerpoints of the solid voxels
	  ArrayList<Component> components = new ArrayList<Component>();// components placed in the Voxelgrid
	  ArrayList<Vec3D> centersComponent = new ArrayList<Vec3D>();// centerpoints of the solid voxels
	  int[] col = new int[] {150,200,250};
	  
	  
	  
	  // CONSTRUCTOR
	  Voxelgrid(int _voxelType, float[] _voxelSize){
	    voxelType = _voxelType;
	    voxelSize = _voxelSize;
	  }
	  
	  
	  
	  // VOXELPOINT
	  public Vec3D voxelize(Vec3D p){
	    // returns the nearest position on a voxel grid
	    float x = p.x/voxelSize[0];
	    float y = p.y/voxelSize[1];
	    float z = p.z/voxelSize[2];
	    if(voxelType==0){// square
	      x = Math.round(x);
	      y = Math.round(y);
	      z = Math.round(z);
	    }else if(voxelType==1){// pyramid
	      z = Math.round(z);
	      float t = (z%2)*0.5f;
	      x = Math.round(x-t) + t;
	      y = Math.round(y-t) + t;
	    }else if(voxelType==2){// triangular
	      z = Math.round(z);
	      float t = (z%2)*0.5f;
	      float v = ((Math.round(y-t)%2) + (z%2)) * 0.5f;
	      y = Math.round(y-t) + t;
	      x = Math.round(x-v) + v;
	    }
	    return new Vec3D(x*voxelSize[0],y*voxelSize[1],z*voxelSize[2]);
	  }
	  
	  
	  
	  // BUILD
	  public void rebuildVoxels(){
	    // rebuilds the solid voxels from scratch
	    //voxels.clear();
	    //centersVoxel.clear();
	    for(Agent a : agents) addAgent(a);
	  }
	  
	  public void addPoint(Vec3D p){
	    Voxel voxel = new Voxel(this, p);
	    if(centersVoxel.contains(voxel.center)==false){
	      voxels.add(voxel);
	      centersVoxel.add(voxel.center);
	    }
	  }
	  
	  public void addAgent(Agent a){
	    Voxel voxel = new Voxel(this, a);
	    if(a.voxel!=null){//agent has a voxel assigned already
	      if(voxel.center.distanceToSquared(a.voxel.center)<0.001f) return;//voxel has not changed, do nothing
	      else{//agent has moved
	        if(centersVoxel.contains(voxel.center)){//agent has moved to an existing voxel
	          if(a.voxel.agents.size()==1){//remove the old voxel
	            voxels.remove(centersVoxel.indexOf(a.voxel.center));
	            centersVoxel.remove(a.voxel.center);
	          }else a.voxel.agents.remove(a);//keep the voxel but remove the agent from it
	          a.voxel = voxels.get(centersVoxel.indexOf(voxel.center));//assign the existing voxel to the agent
	          a.voxel.agents.add(a);//assign the agent to its containing voxel
	        }else{//agents moved to an empty voxel
	          voxels.add(voxel);
	          centersVoxel.add(voxel.center);
	          a.voxel = voxel;
	          voxel.agents.add(a);
	        }
	      }
	    }else{//agent's voxel is empty
	      if(centersVoxel.contains(voxel.center)){//existing voxel
	        a.voxel = voxels.get(centersVoxel.indexOf(voxel.center));//assign the existing voxel to the agent
	        a.voxel.agents.add(a);//assign the agent to its containing voxel
	      }else{
	        voxels.add(voxel);
	        centersVoxel.add(voxel.center);
	        a.voxel = voxel;
	        voxel.agents.add(a);
	      }
	    }
	  }
	  
	  public void rebuildComponents(){
	    // rebuilds the solid component from scratch
	    components.clear();
	    centersComponent.clear();
	    for(Agent a : agents) new Component(this, a);
	  }
	  
	  public void buildComponents(){
	    // adjusts the solid component if agents have moved
	    for(Agent a : agents) buildComponent(a);
	  }
	  
	  public void buildComponent(Agent a){
	    Voxel voxel = new Voxel(this,a);
	    if(a.component!=null){//agent has an assigned component already
	      for(Vec3D center : a.component.centers){
	        if(center.distanceToSquared(voxel.center)<0.001f) return;//still in the same component: do nothing
	      }
	      //else: moved to a different component
	      boolean existing = false;
	      Component componentExisting = null;
	      for(Component component : components){
	        for(Vec3D center : component.centers){
	          if(center.distanceToSquared(voxel.center)<0.001f){
	            existing = true;
	            componentExisting = component;
	          }
	        }
	      }
	      if(existing){//moved to an existing component
	        if(a.component.agents.size()==1){//the only agent is being removed from this component
	          components.remove(a.component);//remove the component
	          for(Vec3D center : a.component.centers) centersComponent.remove(center);//remove the centers of the component
	        }else a.component.agents.remove(a);//keep the component but remove the agent from it
	        a.component = componentExisting;
	        componentExisting.agents.add(a);
	      }else{//moved to an empty voxel
	        components.remove(a.component);//remove the component
	        for(Vec3D center : a.component.centers) centersComponent.remove(center);//remove the centers of the component
	        a.component=null;
	        new Component(this, a);
	      }
	    }else{//agent does not have a component assigned
	      boolean existing = false;
	      Component componentExisting = null;
	      for(Component component : components){
	        for(Vec3D center : component.centers){
	          if(center.distanceToSquared(voxel.center)<0.001f){
	            existing = true;
	            componentExisting = component;
	          }
	        }
	      }
	      if(existing){//moved to an existing component
	        a.component = componentExisting;
	        componentExisting.agents.add(a);
	      }else{//agent needs a new component
	        new Component(this, a);
	      }
	    }
	  }
	  
	  
	  
	  
	  
	  // DISPLAY
	  public void displayVoxels(){
	    for(Voxel v : voxels) v.display();
	  }
	  
	  public void displayComponents(){
	    for(Component c : components) c.display();
	  }

	  
	  

}
