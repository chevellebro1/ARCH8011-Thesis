package main;

import java.util.ArrayList;
import java.util.Arrays;
import feb.Beam3D;
import feb.Beam3DCroSec;
import feb.BoundaryCondition;
import feb.Deform;
import feb.License;
import feb.Material;
import feb.Model;
import feb.Node;
import feb.Response;
import feb.Vec3d;
import main.cellKaramba.Agent;
import toxi.geom.Vec3D;

public class Karamba {
	
	public void getDisplacements() {
		
		cellKaramba cell = new cellKaramba();
		
	
		System.load(
				"K:/University of Cincinnati/2019 Thesis Year/Thesis Studio 8009/Processing/karamba/Java/64bit/Cantilever/karambaJAVA.dll");

		// Load the license
		License lic = License.Instance();
		lic.loadLicense("license.lic", "public.key");
		if (lic.error_flg()) {
			System.out.println(lic.error_msg());
			System.exit(1);
		}
		
		
		//MATERIAL
        double E = 210000; // MN/m2
        double G = 80000; // MN/m2
        double gamma = 78.5; // MN/m3
        Material material = new Material(E,G,gamma);
        material.swigCMemOwn = false;           
        
        //CROSSEC
        double A = 0.05; // m2
        double Iyy = 0.00001; // m4 moment of inertia about z axis
        double Izz = 0.00001; // m4 moment of inertia about y axis
        double Ipp = 0.003; // m4 torsional moment of inertia
        double ky = 0.0; // rigid shear
        double kz = 0.0; // rigid shear
        Beam3DCroSec crosec = new Beam3DCroSec(A,Iyy,Izz,Ipp,ky,kz);
        crosec.swigCMemOwn = false;
        
        
      //NODES
        ArrayList<Agent> points = new ArrayList<Agent>();
        for(Agent a : cell.agents) {
               if(a.neighbors.size()>0) points.add(a);
        }
        feb.Node nodes[] = new Node[points.size()];
        for(int i=0;i<points.size();i++) {
               nodes[i] = new Node(points.get(i).x,points.get(i).y,points.get(i).z);//not sure what info the Node needs
        }
        
        //ELEMENTS
        ArrayList<ArrayList<feb.Node>> nodePairs = new ArrayList<ArrayList< feb.Node>>();
        for (Agent a : points) {//loop through points, not agents
               for (Agent n : a.neighbors) { //agents for neighbor
                     if (a.index < n.index || n.neighbors.contains(a) == false) {//agents can have each other as neighbors. therefore in order to avoid duplicating elements, beams are only added if the index of a is smaller, or if the neighbor does not have the agent in its list of neighbors   
                    	 nodePairs.add(new ArrayList<feb.Node>(Arrays.asList(nodes[points.indexOf(a)],nodes[points.indexOf(n)])));
                     }
               }
        }
        Beam3D beams[] = new Beam3D[nodePairs.size()];
        for(int i=0;i<nodePairs.size();i++) {
        	beams[i] = new Beam3D(nodePairs.get(i).get(0), nodePairs.get(i).get(1), material, crosec);
        }

      		
      //ASSEMBLE (MODEL)
		
      	Model model = new Model();
      	for (Node node : nodes) {
      		node.swigCMemOwn = false;
    		model.add(node);
      	}
      		
      	model.add(material);
      	model.add(crosec);
      		
      	for (Beam3D beam : beams) {
      		beam.swigCMemOwn = false;
      		model.add(beam);
      	}
      	
      	ArrayList<BoundaryCondition> bcs = new ArrayList<BoundaryCondition>();
      	
      	
      	for (int i = 0; i<points.size(); i++) {
      		if(points.get(i).z < 1) {
      			bcs.add(new BoundaryCondition(i, Node.DOF.x_t, BoundaryCondition.BCType.disp, 0));
      			bcs.add(new BoundaryCondition(i, Node.DOF.y_t, BoundaryCondition.BCType.disp, 0));
      			bcs.add(new BoundaryCondition(i, Node.DOF.z_t, BoundaryCondition.BCType.disp, 0));
      			bcs.add(new BoundaryCondition(i, Node.DOF.x_r, BoundaryCondition.BCType.disp, 0));
      			bcs.add(new BoundaryCondition(i, Node.DOF.y_r, BoundaryCondition.BCType.disp, 0));
      			bcs.add(new BoundaryCondition(i, Node.DOF.z_r, BoundaryCondition.BCType.disp, 0));
      		} else {
      			bcs.add(new BoundaryCondition(i, Node.DOF.z_t, BoundaryCondition.BCType.force, -1));
      		}
      	}
      
      	for (BoundaryCondition bc : bcs) {
			bc.swigCMemOwn = false;
			model.add(bc);
		}
      	
      	
      	Deform analysis = new Deform(model);
		Response response = new Response(analysis);
		
		
		response.updateNodalDisplacements();
		
		
		if(points.size()==response.model().Nodes().size()) {
			for (int i = 0; i < points.size(); i++) {

				Vec3d disp = response.model().Nodes().get(i).getDisplacement();
				points.get(i).displacement = new Vec3D((float)disp.x(),(float)disp.y(),(float)disp.z());
				
			}
		}
	}
}	