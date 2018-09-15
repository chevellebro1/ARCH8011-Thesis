package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import main.cellKaramba.Agent;
import processing.core.PApplet;
import toxi.geom.Vec3D;

/**
 * Cell Growth Simulation
 *
 * 180909
 * 
 * @author Christoph Klemmt www.orproject.com
 *
 *         Voxel classes for voxelization and component placement
 *
 *         Copyright (C) Christoph Klemmt All Rights Reserved. All information
 *         contained herein is, and remains the property of Christoph Klemmt.
 *         Dissemination of this information or reproduction of this material is
 *         strictly forbidden unless prior written permission is obtained from
 *         Christoph Klemmt. Unauthorized copying of this file via any medium is
 *         strictly prohibited. Proprietary and confidential. If distributed as
 *         part of an educational program, the registered students may use the
 *         information contained herein for their personal, confidential,
 *         unpublished an non-commercial applications.
 */

class Voxelgrid {
	// currently set up only for pyramid grid voxels

	// VARIABLES
	int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
	float[] voxelSize;// sizes in x,y,z directions
	ArrayList<Voxel> voxels = new ArrayList<Voxel>();// the solid voxels that contain an Agent
	ArrayList<Vec3D> centersVoxel = new ArrayList<Vec3D>();// centerpoints of the solid voxels
	ArrayList<Component> components = new ArrayList<Component>();// components placed in the Voxelgrid
	ArrayList<Vec3D> centersComponent = new ArrayList<Vec3D>();// centerpoints of the solid voxels
	int[] col = new int[] { 150, 200, 250 };

	
	PApplet parent;
	
	cellKaramba cell = new cellKaramba();
	Component comp = new Component(parent);
	
	
	Voxelgrid(PApplet p){
		p = parent;
	}
	
	// CONSTRUCTOR
	Voxelgrid(int _voxelType, float[] _voxelSize) {
		voxelType = _voxelType;
		voxelSize = _voxelSize;
	}

	// VOXELPOINT
	public Vec3D voxelize(Vec3D p) {
		// returns the nearest position on a voxel grid
		float x = p.x / voxelSize[0];
		float y = p.y / voxelSize[1];
		float z = p.z / voxelSize[2];
		if (voxelType == 0) {// square
			x = Math.round(x);
			y = Math.round(y);
			z = Math.round(z);
		} else if (voxelType == 1) {// pyramid
			z = Math.round(z);
			float t = (z % 2) * 0.5f;
			x = Math.round(x - t) + t;
			y = Math.round(y - t) + t;
		} else if (voxelType == 2) {// triangular
			z = Math.round(z);
			float t = (z % 2) * 0.5f;
			float v = ((Math.round(y - t) % 2) + (z % 2)) * 0.5f;
			y = Math.round(y - t) + t;
			x = Math.round(x - v) + v;
		}
		return new Vec3D(x * voxelSize[0], y * voxelSize[1], z * voxelSize[2]);
	}

	// BUILD
	public void rebuildVoxels() {
		// rebuilds the solid voxels from scratch
		// voxels.clear();
		// centersVoxel.clear();
		for (Agent a : cell.agents)
			addAgent(a);
	}

	public void addPoint(Vec3D p) {
		Voxel voxel = new Voxel(this, p);
		if (centersVoxel.contains(voxel.center) == false) {
			voxels.add(voxel);
			centersVoxel.add(voxel.center);
		}
	}

	public void addAgent(Agent a) {
		Voxel voxel = new Voxel(this, a);
		if (a.voxel != null) {// agent has a voxel assigned already
			if (voxel.center.distanceToSquared(a.voxel.center) < 0.001f)
				return;// voxel has not changed, do nothing
			else {// agent has moved
				if (centersVoxel.contains(voxel.center)) {// agent has moved to an existing voxel
					if (a.voxel.agents.size() == 1) {// remove the old voxel
						voxels.remove(centersVoxel.indexOf(a.voxel.center));
						centersVoxel.remove(a.voxel.center);
					} else
						a.voxel.agents.remove(a);// keep the voxel but remove the agent from it
					a.voxel = voxels.get(centersVoxel.indexOf(voxel.center));// assign the existing voxel to the agent
					a.voxel.agents.add(a);// assign the agent to its containing voxel
				} else {// agents moved to an empty voxel
					voxels.add(voxel);
					centersVoxel.add(voxel.center);
					a.voxel = voxel;
					voxel.agents.add(a);
				}
			}
		} else {// agent's voxel is empty
			if (centersVoxel.contains(voxel.center)) {// existing voxel
				a.voxel = voxels.get(centersVoxel.indexOf(voxel.center));// assign the existing voxel to the agent
				a.voxel.agents.add(a);// assign the agent to its containing voxel
			} else {
				voxels.add(voxel);
				centersVoxel.add(voxel.center);
				a.voxel = voxel;
				voxel.agents.add(a);
			}
		}
	}

	public void rebuildComponents() {
		// rebuilds the solid component from scratch
		components.clear();
		centersComponent.clear();
		for (Agent a : cell.agents)
			new Component(this, a);
	}

	public void buildComponents() {
		// adjusts the solid component if agents have moved
		for (Agent a : cell.agents)
			buildComponent(a);
	}

	public void buildComponent(Agent a) {
		Voxel voxel = new Voxel(this, a);
		if (a.component != null) {// agent has an assigned component already
			for (Vec3D center : a.component.centers) {
				if (center.distanceToSquared(voxel.center) < 0.001f)
					return;// still in the same component: do nothing
			}
			// else: moved to a different component
			boolean existing = false;
			Component componentExisting = null;
			for (Component component : components) {
				for (Vec3D center : component.centers) {
					if (center.distanceToSquared(voxel.center) < 0.001f) {
						existing = true;
						componentExisting = component;
					}
				}
			}
			if (existing) {// moved to an existing component
				if (a.component.agents.size() == 1) {// the only agent is being removed from this component
					components.remove(a.component);// remove the component
					for (Vec3D center : a.component.centers)
						centersComponent.remove(center);// remove the centers of the component
				} else
					a.component.agents.remove(a);// keep the component but remove the agent from it
				a.component = componentExisting;
				componentExisting.agents.add(a);
			} else {// moved to an empty voxel
				components.remove(a.component);// remove the component
				for (Vec3D center : a.component.centers)
					centersComponent.remove(center);// remove the centers of the component
				a.component = null;
				new Component(this, a);
			}
		} else {// agent does not have a component assigned
			boolean existing = false;
			Component componentExisting = null;
			for (Component component : components) {
				for (Vec3D center : component.centers) {
					if (center.distanceToSquared(voxel.center) < 0.001f) {
						existing = true;
						componentExisting = component;
					}
				}
			}
			if (existing) {// moved to an existing component
				a.component = componentExisting;
				componentExisting.agents.add(a);
			} else {// agent needs a new component
				new Component(this, a);
			}
		}
	}

	// COMPONENT LENGTH
	public float componentLength(float voxelLength) {
		float l = 0;
		for (Component c : components)
			l += c.centers.size() * voxelLength;
		return l;
	}

	// DISPLAY
	public void displayVoxels() {
		for (Voxel v : voxels)
			v.display();
	}

	public void displayComponents() {
		for (Component c : components)
			c.display();
	}

}

class Voxel {

	// VARIABLES
	Voxelgrid voxelgrid;// the Voxelgrid to which the Voxel belongs
	int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
	float[] voxelSize;// sizes in x,y,z directions
	int[] positions;// numbers of the row/column/layer of the voxel
	int type;// for pyramid grid: shape of the voxel: 0 pyramid; 1 tetrahedron
	Vec3D center;// center of the voxel
	ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();// the corners of the voxel
	ArrayList<ArrayList<Integer>> facevertices = new ArrayList<ArrayList<Integer>>();// the corners of the voxel
	ArrayList<Agent> agents = new ArrayList<Agent>();// the agents that are contained in the voxel
	
	PApplet parent;
	
	Voxel(PApplet p){
		p = parent;
	}

	// CONSTRUCTOR
	Voxel(Voxelgrid _voxelgrid, Vec3D p) {
		voxelgrid = _voxelgrid;
		voxelType = voxelgrid.voxelType;
		voxelSize = voxelgrid.voxelSize;
		findVertices(p);
		findFacevertices();
		if (voxelType == 0) {
			center = vertices.get(0).add(vertices.get(6)).scale(0.5f);// rectangular grid
			positions = new int[] { PApplet.parseInt(center.x / voxelSize[0] - voxelSize[0] * 0.5f),
					PApplet.parseInt(center.y / voxelSize[1] - voxelSize[1] * 0.5f),
					PApplet.parseInt(center.z / voxelSize[2] - voxelSize[2] * 0.5f) };
		} else if (voxelType == 1) {// pyramid grid
			if (type == 0)
				center = new Vec3D((vertices.get(1).x + vertices.get(3).x) * 0.5f,
						(vertices.get(1).y + vertices.get(3).y) * 0.5f,
						(vertices.get(0).z + vertices.get(1).z) * 0.5f);
			else
				center = vertices.get(0).add(vertices.get(1)).add(vertices.get(2)).add(vertices.get(3))
						.scale(0.25f);
			positions = new int[] { PApplet.parseInt(center.x * 2 / voxelSize[0] - voxelSize[0] * 0.25f),
					PApplet.parseInt(center.y * 2 / voxelSize[1] - voxelSize[1] * 0.25f),
					PApplet.parseInt(center.z / voxelSize[2] - voxelSize[2] * 0.5f) };
		} else
			PApplet.println("Voxel of tetrahedron grid not implemented yet");// tetrahedron grid

	}

	// SET UP VERTICES AND FACES
	public ArrayList<Vec3D> voxelBox(Vec3D p) {
		// returns a list: {the closest position on a voxel grid; x,y,z location of the
		// voxel box in relation to the closest position}
		float x = p.x / voxelSize[0];
		float y = p.y / voxelSize[1];
		float z = p.z / voxelSize[2];
		int posx = 0;
		int posy = 0;
		int posz = 0;
		if (voxelType == 0) {// square
			x = Math.round(x);
			y = Math.round(y);
			z = Math.round(z);
			float dx = (p.x / voxelSize[0]) - x;
			float dy = (p.y / voxelSize[1]) - y;
			float dz = (p.z / voxelSize[2]) - z;
			if (dx < 0)
				posx = -1;// lower voxels along x axis
			else
				posx = 0;// upper voxels along x axis
			if (dy < 0)
				posy = -1;// lower voxels along y axis
			else
				posy = 0;// upper voxels along y axis
			if (dz < 0)
				posz = -1;// lower voxels along z axis
			else
				posz = 0;// upper voxels along z axis
		} else if (voxelType == 1) {// pyramid
			z = Math.round(z);
			float t = (z % 2) * 0.5f;
			x = Math.round(x - t) + t;
			y = Math.round(y - t) + t;
			float dx = (p.x / voxelSize[0]) - x;
			float dy = (p.y / voxelSize[1]) - y;
			float dz = (p.z / voxelSize[2]) - z;
			if (dz < 0)
				posz = -1;// lower voxels
			else
				posz = 1;// upper voxels
			if (PApplet.abs(dx) / PApplet.abs(dz) < 0.5f)
				posx = 0;// middle voxel
			else {
				if (dx < 0)
					posx = -1;// left voxel
				else
					posx = 1;// right voxel
			}
			if (PApplet.abs(dy) / PApplet.abs(dz) < 0.5f)
				posy = 0;// middle voxel
			else {
				if (dy < 0)
					posy = -1;// left voxel
				else
					posy = 1;// right voxel
			}
		} else if (voxelType == 2) {// triangular
			// NOT IMPLEMENTED YET!
			z = Math.round(z);
			float t = (z % 2) * 0.5f;
			float v = ((Math.round(y - t) % 2) + (z % 2)) * 0.5f;
			y = Math.round(y - t) + t;
			x = Math.round(x - v) + v;
		}
		ArrayList<Vec3D> list = new ArrayList<Vec3D>();
		list.add(new Vec3D(x * voxelSize[0], y * voxelSize[1], z * voxelSize[2]));
		list.add(new Vec3D(posx, posy, posz));
		return list;
	}

	public void findVertices(Vec3D p) {
		ArrayList<Vec3D> list = voxelBox(p);
		Vec3D voxel = list.get(0);
		int dx = (int) list.get(1).x;
		int dy = (int) list.get(1).y;
		int dz = (int) list.get(1).z;

		if (voxelType == 0) {// rectangular grid
			Vec3D vertex0 = voxel.add(voxelSize[0] * dx, voxelSize[1] * dy, voxelSize[2] * dz);// lowest vertex of
																								// the occupied
																								// voxel. Vertex
																								// order as in Rhino
																								// Bounding Box.
			vertices = new ArrayList<Vec3D>(Arrays.asList(vertex0, vertex0.add(new Vec3D(voxelSize[0], 0, 0)),
					vertex0.add(new Vec3D(voxelSize[0], voxelSize[1], 0)),
					vertex0.add(new Vec3D(0, voxelSize[1], 0)), vertex0.add(new Vec3D(0, 0, voxelSize[2])),
					vertex0.add(new Vec3D(voxelSize[0], 0, voxelSize[2])),
					vertex0.add(new Vec3D(voxelSize[0], voxelSize[1], voxelSize[2])),
					vertex0.add(new Vec3D(0, voxelSize[1], voxelSize[2]))));

		} else if (voxelType == 1) {// pyramid grid
			if (dx == 0 && dy == 0) {// pyramid with tip being this
				type = 0;// pyramid
				vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
						voxel.add(new Vec3D(voxelSize[0] * -0.5f, voxelSize[1] * -0.5f, voxelSize[2] * dz)),
						voxel.add(new Vec3D(voxelSize[0] * 0.5f, voxelSize[1] * -0.5f, voxelSize[2] * dz)),
						voxel.add(new Vec3D(voxelSize[0] * 0.5f, voxelSize[1] * 0.5f, voxelSize[2] * dz)),
						voxel.add(new Vec3D(voxelSize[0] * -0.5f, voxelSize[1] * 0.5f, voxelSize[2] * dz))));
			} else if (dx == 0 || dy == 0) {// tetrahedron voxel
				type = 1;// tetrahedron
				if (dx == 0) {
					vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
							voxel.add(new Vec3D(0, voxelSize[1] * dy, 0)),
							voxel.add(new Vec3D(voxelSize[0] * 0.5f, voxelSize[1] * 0.5f * dy, voxelSize[2] * dz)),
							voxel.add(
									new Vec3D(voxelSize[0] * -0.5f, voxelSize[1] * 0.5f * dy, voxelSize[2] * dz))));
				} else {
					vertices = new ArrayList<Vec3D>(Arrays.asList(voxel,
							voxel.add(new Vec3D(voxelSize[0] * dx, 0, 0)),
							voxel.add(new Vec3D(voxelSize[0] * 0.5f * dx, voxelSize[1] * 0.5f, voxelSize[2] * dz)),
							voxel.add(
									new Vec3D(voxelSize[0] * 0.5f * dx, voxelSize[1] * -0.5f, voxelSize[2] * dz))));
				}
			} else {// pyramid voxel
				type = 0;// pyramid
				vertices = new ArrayList<Vec3D>(Arrays.asList(
						voxel.add(new Vec3D(voxelSize[0] * 0.5f * dx, voxelSize[1] * 0.5f * dy, voxelSize[2] * dz)),
						voxel, voxel.add(new Vec3D(voxelSize[0] * dx, 0, 0)),
						voxel.add(new Vec3D(voxelSize[0] * dx, voxelSize[1] * dy, 0)),
						voxel.add(new Vec3D(0, voxelSize[1] * dy, 0))));
			}

		} else {// tetrahedron grid
			PApplet.println("Voxel of tetrahedron grid not implemented yet");
		}
	}

	public void findFacevertices() {
		if (voxelType == 0) {// rectangular grid
			facevertices.add(new ArrayList<Integer>(Arrays.asList(0, 3, 2, 1, 0)));
			facevertices.add(new ArrayList<Integer>(Arrays.asList(0, 1, 5, 4, 0)));
			facevertices.add(new ArrayList<Integer>(Arrays.asList(1, 2, 6, 5, 1)));
			facevertices.add(new ArrayList<Integer>(Arrays.asList(2, 3, 7, 6, 2)));
			facevertices.add(new ArrayList<Integer>(Arrays.asList(3, 0, 4, 7, 3)));
			facevertices.add(new ArrayList<Integer>(Arrays.asList(4, 5, 6, 7, 4)));

		} else if (voxelType == 1) {// pyramid grid
			if (type == 0) {// pyramid voxel
				for (int i = 1; i < 5; i++)
					facevertices.add(new ArrayList<Integer>(Arrays.asList(0, i, (i % 4) + 1, 0)));
				facevertices.add(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 1)));
			} else {// tetrahedron voxel
				for (int i = 0; i < 4; i++) {
					for (int j = i + 1; j < 4; j++) {
						for (int k = j + 1; k < 4; k++) {
							facevertices.add(new ArrayList<Integer>(Arrays.asList(i, j, k, i)));
						}
					}
				}
			}

		} else {// tetrahedron grid
			PApplet.println("Voxel of tetrahedron grid not implemented yet");
		}
	}

	// DISPLAY
	public void display() {
		parent.fill(voxelgrid.col[0], voxelgrid.col[1], voxelgrid.col[2], 100);
		parent.noStroke();
		parent.stroke(voxelgrid.col[0], voxelgrid.col[1], voxelgrid.col[2], 100);
		parent.strokeWeight(1);
		for (ArrayList<Integer> facevertex : facevertices) {
			parent.beginShape();
			for (Integer fv : facevertex)
				parent.vertex(vertices.get(fv).x, vertices.get(fv).y, vertices.get(fv).z);
			parent.endShape();
		}
	}

}

class Component {
	// a component is made up of 8 voxels in a horizontal row

	// VARIABLES
	Voxelgrid voxelgrid;
	int voxelType;// type of the grid: 0 rectangular; 1 pyramid; 2 triangular
	Vec3D dir;// direction of the component
	ArrayList<Integer> voxelcountsReversed;
	ArrayList<Voxel> voxels = new ArrayList<Voxel>();// the 8 voxels in a row
	ArrayList<Vec3D> centers = new ArrayList<Vec3D>();// the centers of the 8 voxels
	ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();// the corners of the voxel
	ArrayList<ArrayList<Integer>> facevertices = new ArrayList<ArrayList<Integer>>();// the corners of the voxel
	ArrayList<Agent> agents = new ArrayList<Agent>();// the agents that are in the component
	int[] col;
	
	PApplet parent;

	cellKaramba cell = new cellKaramba();
	
	Component(PApplet p){
		p = parent;
	}
	
	// CONSTRUCTOR
	Component(Voxelgrid _voxelgrid, Agent a) {
		voxelgrid = _voxelgrid;
		voxelType = voxelgrid.voxelType;
		col = new int[] { voxelgrid.col[0], voxelgrid.col[1], voxelgrid.col[2] };
		voxelcountsReversed = new ArrayList<Integer>(cell.voxelcounts);
		Collections.reverse(voxelcountsReversed);
		// define component direction
		if (voxelType == 0) {// rectangular grid
			if (cell.componentsAligned) {
				if (cell.axisComponentAlignment == 0)
					dir = new Vec3D(voxelgrid.voxelSize[0], 0, 0);
				else if (cell.axisComponentAlignment == 1)
					dir = new Vec3D(0, voxelgrid.voxelSize[1], 0);
				else
					dir = new Vec3D(0, 0, voxelgrid.voxelSize[2]);
			} else {
				int[] axisNormal = new int[3];// largest axis of the normal
				if (cell.componentsInLayers) {
					if (PApplet.abs(a.normal.x) > PApplet.abs(a.normal.y))
						axisNormal = new int[] { 2, 0, 1 };
					else
						axisNormal = new int[] { 2, 1, 0 };
				} else {
					if (PApplet.abs(a.normal.x) > PApplet.abs(a.normal.y) && PApplet.abs(a.normal.x) > PApplet.abs(a.normal.z)) {
						if (PApplet.abs(a.normal.y) > PApplet.abs(a.normal.z))
							axisNormal = new int[] { 0, 1, 2 };
						else
							axisNormal = new int[] { 0, 2, 1 };
					} else if (PApplet.abs(a.normal.y) > PApplet.abs(a.normal.z)) {
						if (PApplet.abs(a.normal.x) > PApplet.abs(a.normal.z))
							axisNormal = new int[] { 1, 0, 2 };
						else
							axisNormal = new int[] { 1, 2, 0 };
					} else {
						if (PApplet.abs(a.normal.x) > PApplet.abs(a.normal.y))
							axisNormal = new int[] { 2, 0, 1 };
						else
							axisNormal = new int[] { 2, 1, 0 };
					}
				}
				if (cell.componentsAlternating) {
					Voxel voxel = new Voxel(voxelgrid, a);
					int positionSide = 0;// position in the grid in the sideways direction, to eliminate alternating
											// components
					int axis = 0;// axis of the component
					if (axisNormal[0] == 0) {
						if (voxel.positions[axisNormal[0]] % 2 == 0) {
							axis = 1;
							dir = new Vec3D(0, voxelgrid.voxelSize[1], 0);
							positionSide = voxel.positions[2];
						} else {
							axis = 2;
							dir = new Vec3D(0, 0, voxelgrid.voxelSize[2]);
							positionSide = voxel.positions[1];
						}
					} else if (axisNormal[0] == 1) {
						if (voxel.positions[axisNormal[0]] % 2 == 0) {
							axis = 0;
							dir = new Vec3D(voxelgrid.voxelSize[0], 0, 0);
							positionSide = voxel.positions[2];
						} else {
							axis = 2;
							dir = new Vec3D(0, 0, voxelgrid.voxelSize[2]);
							positionSide = voxel.positions[0];
						}
					} else {
						if (voxel.positions[axisNormal[0]] % 2 == 0) {
							axis = 0;
							dir = new Vec3D(voxelgrid.voxelSize[0], 0, 0);
							positionSide = voxel.positions[1];
						} else {
							axis = 1;
							dir = new Vec3D(0, voxelgrid.voxelSize[1], 0);
							positionSide = voxel.positions[0];
						}
					}
					// check for densities
					if (cell.densities.size() > 0) {
						ArrayList<Float> distances = new ArrayList<Float>();
						for (Vec3D d : cell.densities)
							distances.add(a.distanceTo(d));
						float distance = Collections.min(distances);// distance to density points
						int density = Math.min(PApplet.parseInt(distance / cell.densityDistance) + 1, 4);// density:
																									// 1=dense,
																									// 4=sparse
						if (density == 1)
							col = new int[] { 0, 80, 130 };
						else if (density == 2)
							col = new int[] { 50, 120, 170 };
						else if (density == 3)
							col = new int[] { 100, 160, 210 };
						if (positionSide % density != 0 && cell.densityAxes.contains(axis)) {// don't create component
							a.component = null;
							return;
						}
					}
				} else {
					if (cell.componentsInPlane) {
						if (axisNormal[2] == 0)
							dir = new Vec3D(voxelgrid.voxelSize[0], 0, 0);
						else if (axisNormal[2] == 1)
							dir = new Vec3D(0, voxelgrid.voxelSize[1], 0);
						else
							dir = new Vec3D(0, 0, voxelgrid.voxelSize[2]);
					} else {
						if (axisNormal[0] == 0)
							dir = new Vec3D(voxelgrid.voxelSize[0], 0, 0);
						else if (axisNormal[0] == 1)
							dir = new Vec3D(0, voxelgrid.voxelSize[1], 0);
						else
							dir = new Vec3D(0, 0, voxelgrid.voxelSize[2]);
					}
				}
			}
		} else if (voxelType == 1) {// pyramid grid
			if (PApplet.abs(a.normal.x) > PApplet.abs(a.normal.y) ^ cell.componentsInPlane)
				dir = new Vec3D(voxelgrid.voxelSize[0] * 0.5f, 0, 0);
			else
				dir = new Vec3D(0, voxelgrid.voxelSize[1] * 0.5f, 0);
			if (cell.componentsAligned)
				dir = new Vec3D(voxelgrid.voxelSize[0] * 0.5f, 0, 0);// all components are aligned in x direction
		} else {// tetrahedron grid
			PApplet.println("Component of tetrahedron grid not implemented yet");
		}
		// try to place a component
		placeComponent(a);
		if (voxels.size() >= cell.voxelcounts.get(0)) {// keep component
			voxelgrid.components.add(this);
			for (Vec3D c : centers)
				voxelgrid.centersComponent.add(c);
			a.component = this;
			agents.add(a);
			findVertices();
			findFacevertices();
		} else
			a.component = null;// a new component could not be created
	}

	// SET UP COMPONENT
	public void placeComponent(Agent a) {
		boolean empty;
		Voxel voxel = new Voxel(voxelgrid, a);
		for (Vec3D centerComponent : voxelgrid.centersComponent) {
			if (voxel.center.distanceToSquared(centerComponent) < 0.001f)
				return;// exit if the voxel is already occupied
		}
		// find empty voxels towards the direction, and afterwards in the opposite
		// direction
		ArrayList<Voxel> voxelsUp = new ArrayList<Voxel>(Arrays.asList(voxel));
		ArrayList<Vec3D> centersUp = new ArrayList<Vec3D>(Arrays.asList(voxel.center));
		while (voxelsUp.size() <= cell.voxelcounts.get(cell.voxelcounts.size() - 1)) {
			empty = true;
			for (Vec3D centerComponent : voxelgrid.centersComponent) {
				if (centersUp.get(centersUp.size() - 1).add(dir).distanceToSquared(centerComponent) < 0.001f)
					empty = false;
			}
			if (empty) {
				Voxel voxelNew = new Voxel(voxelgrid, centersUp.get(centersUp.size() - 1).add(dir));
				voxelsUp.add(voxelNew);
				centersUp.add(voxelNew.center);
			} else
				break;
		}
		// try to find empty voxels in the opposite direction
		ArrayList<Voxel> voxelsDown = new ArrayList<Voxel>(Arrays.asList(voxel));
		ArrayList<Vec3D> centersDown = new ArrayList<Vec3D>(Arrays.asList(voxel.center));
		while (voxelsDown.size() <= cell.voxelcounts.get(cell.voxelcounts.size() - 1)) {
			empty = true;
			for (Vec3D centerComponent : voxelgrid.centersComponent) {
				if (centersDown.get(0).sub(dir).distanceToSquared(centerComponent) < 0.001f)
					empty = false;
			}
			if (empty) {
				Voxel voxelNew = new Voxel(voxelgrid, centersDown.get(0).sub(dir));
				voxelsDown.add(0, voxelNew);
				centersDown.add(0, voxelNew.center);
			} else
				break;
		}
		// check lengths and decide component position
		int space = voxelsUp.size() + voxelsDown.size() - 1;// available amount of voxels
		if (space < cell.voxelcounts.get(0))
			return;// component has not enough space and can not be placed
		int componentLength = cell.voxelcounts.get(0);
		for (Integer voxelcount : voxelcountsReversed) {
			if (voxelcount < space) {
				componentLength = voxelcount;
				break;
			}
		}
		if (space > voxelcountsReversed.get(0))
			componentLength = cell.voxelcounts.get(0);// shortest component if there aren't neighbours on both sides
		if (voxelsUp.size() > voxelcountsReversed.get(0) && voxelsDown.size() > voxelcountsReversed.get(0)) {
			voxels = new ArrayList<Voxel>(voxelsDown.subList(voxelsDown.size() - PApplet.parseInt(cell.voxelcounts.get(0) * 0.5f), voxelsDown.size()));
			voxels.addAll(voxelsUp.subList(1, (int) Math.ceil(cell.voxelcounts.get(0) * 0.5f) + 1));
			centers = new ArrayList<Vec3D>(centersDown.subList(voxelsDown.size() - PApplet.parseInt(cell.voxelcounts.get(0) * 0.5f), voxelsDown.size()));
			centers.addAll(centersUp.subList(1, (int) Math.ceil(cell.voxelcounts.get(0) * 0.5f) + 1));
			return;
		}
		if (voxelsDown.size() <= componentLength) {// shift to bottom end
			voxels = new ArrayList<Voxel>(voxelsDown);
			voxels.addAll(voxelsUp.subList(1, componentLength - voxels.size() + 1));
			centers = new ArrayList<Vec3D>(centersDown);
			centers.addAll(centersUp.subList(1, componentLength - centers.size() + 1));
			return;
		} else if (voxelsUp.size() <= componentLength) {// shift to top end
			voxels = new ArrayList<Voxel>(voxelsDown.subList(voxelsDown.size() - 1 - (componentLength - voxelsUp.size()),voxelsDown.size() - 1));
			voxels.addAll(voxelsUp);
			centers = new ArrayList<Vec3D>(centersDown.subList(centersDown.size() - 1 - (componentLength - centersUp.size()), centersDown.size() - 1));
			centers.addAll(centersUp);
			return;
		}
	}

	public void findVertices() {
		if (voxelType == 0) {// rectangular grid
			vertices = new ArrayList<Vec3D>(voxels.get(0).vertices);
			if (dir.x > 0 && dir.y == 0 && dir.z == 0) {
				vertices.set(1, voxels.get(voxels.size() - 1).vertices.get(1));
				vertices.set(2, voxels.get(voxels.size() - 1).vertices.get(2));
				vertices.set(5, voxels.get(voxels.size() - 1).vertices.get(5));
				vertices.set(6, voxels.get(voxels.size() - 1).vertices.get(6));
			} else if (dir.x < 0 && dir.y == 0 && dir.z == 0) {
				vertices.set(0, voxels.get(voxels.size() - 1).vertices.get(0));
				vertices.set(3, voxels.get(voxels.size() - 1).vertices.get(3));
				vertices.set(4, voxels.get(voxels.size() - 1).vertices.get(4));
				vertices.set(7, voxels.get(voxels.size() - 1).vertices.get(7));
			} else if (dir.x == 0 && dir.y > 0 && dir.z == 0) {
				vertices.set(2, voxels.get(voxels.size() - 1).vertices.get(2));
				vertices.set(3, voxels.get(voxels.size() - 1).vertices.get(3));
				vertices.set(6, voxels.get(voxels.size() - 1).vertices.get(6));
				vertices.set(7, voxels.get(voxels.size() - 1).vertices.get(7));
			} else if (dir.x == 0 && dir.y < 0 && dir.z == 0) {
				vertices.set(0, voxels.get(voxels.size() - 1).vertices.get(0));
				vertices.set(1, voxels.get(voxels.size() - 1).vertices.get(1));
				vertices.set(4, voxels.get(voxels.size() - 1).vertices.get(4));
				vertices.set(5, voxels.get(voxels.size() - 1).vertices.get(5));
			} else if (dir.x == 0 && dir.y == 0 && dir.z > 0) {
				vertices.set(4, voxels.get(voxels.size() - 1).vertices.get(4));
				vertices.set(5, voxels.get(voxels.size() - 1).vertices.get(5));
				vertices.set(6, voxels.get(voxels.size() - 1).vertices.get(6));
				vertices.set(7, voxels.get(voxels.size() - 1).vertices.get(7));
			} else if (dir.x == 0 && dir.y == 0 && dir.z < 0) {
				vertices.set(0, voxels.get(voxels.size() - 1).vertices.get(0));
				vertices.set(1, voxels.get(voxels.size() - 1).vertices.get(1));
				vertices.set(2, voxels.get(voxels.size() - 1).vertices.get(2));
				vertices.set(3, voxels.get(voxels.size() - 1).vertices.get(3));
			}
			// find offset inner vertices
			ArrayList<Integer> opposites = new ArrayList<Integer>();
			if (dir.x != 0)
				opposites = new ArrayList<Integer>(Arrays.asList(7, 6, 5, 4, 3, 2, 1, 0));
			if (dir.y != 0)
				opposites = new ArrayList<Integer>(Arrays.asList(5, 4, 7, 6, 1, 0, 3, 2));
			if (dir.z != 0)
				opposites = new ArrayList<Integer>(Arrays.asList(2, 3, 0, 1, 6, 7, 4, 5));
			for (int i = 0; i < 8; i++) {
				Vec3D toMid = vertices.get(i).add(vertices.get(opposites.get(i))).scale(0.5f).sub(vertices.get(i));
				vertices.add(vertices.get(i).add(toMid.scale(0.1f)));
			}

		} else if (voxelType == 1) {// pyramid grid
			Vec3D dirOrtho = dir.cross(new Vec3D(0, 0, 1));// orthogonal direction to the component
			// find the triangles at the ends
			ArrayList<Vec3D> pointsStart = new ArrayList<Vec3D>(voxels.get(0).vertices);
			// sort vertices by direction along dir
			Collections.sort(pointsStart, new Comparator<Vec3D>() {
				@Override
				public int compare(Vec3D a, Vec3D b) {
					return Float.compare((Float) dir.dot(a), (Float) dir.dot(b));
				}
			});
			if (voxels.get(0).type == 0) {// pyramid
				if (dirOrtho.dot(pointsStart.get(0)) < dirOrtho.dot(pointsStart.get(1))) {// sort vertices, swap
																							// vertex 0 and 1
					vertices.add(pointsStart.get(1));
					vertices.add(pointsStart.get(0));
				} else {
					vertices.add(pointsStart.get(0));
					vertices.add(pointsStart.get(1));
				}
				vertices.add(pointsStart.get(2));// tip of the pyramid
			} else {// tetrahedron
				vertices.add(pointsStart.get(0));
				if (dirOrtho.dot(pointsStart.get(1)) < dirOrtho.dot(pointsStart.get(2))) {// sort vertices, swap
																							// vertex 1 and 2
					vertices.add(pointsStart.get(2));
					vertices.add(pointsStart.get(1));
				} else {
					vertices.add(pointsStart.get(1));
					vertices.add(pointsStart.get(2));
				}
			}
			// the same at the other side
			ArrayList<Vec3D> pointsEnd = new ArrayList<Vec3D>(voxels.get(7).vertices);
			// sort vertices by direction along dir
			Collections.sort(pointsEnd, new Comparator<Vec3D>() {
				@Override
				public int compare(Vec3D a, Vec3D b) {
					return Float.compare((Float) dir.dot(a), (Float) dir.dot(b));
				}
			});
			if (voxels.get(7).type == 0) {// pyramid
				vertices.add(pointsEnd.get(2));
				if (dirOrtho.dot(pointsEnd.get(3)) < dirOrtho.dot(pointsEnd.get(4))) {// sort vertices, swap vertex
																						// 1 and 2
					vertices.add(pointsEnd.get(4));
					vertices.add(pointsEnd.get(3));
				} else {
					vertices.add(pointsEnd.get(3));
					vertices.add(pointsEnd.get(4));
				}
			} else {// tetrahedron
				if (dirOrtho.dot(pointsEnd.get(1)) < dirOrtho.dot(pointsEnd.get(2))) {// sort vertices, swap vertex
																						// 0 and 1
					vertices.add(pointsEnd.get(2));
					vertices.add(pointsEnd.get(1));
				} else {
					vertices.add(pointsEnd.get(1));
					vertices.add(pointsEnd.get(2));
				}
				vertices.add(pointsEnd.get(3));
			}
			// find offset inner vertices
			for (int i = 0; i < 3; i++) {
				Vec3D toMid = vertices.get((i + 1) % 3).add(vertices.get((i + 2) % 3)).scale(0.5f)
						.sub(vertices.get(i));
				vertices.add(vertices.get(i).add(toMid.scale(0.1f)));
			}
			for (int i = 3; i < 6; i++) {
				Vec3D toMid = vertices.get((i + 1) % 3 + 3).add(vertices.get((i + 2) % 3 + 3)).scale(0.5f)
						.sub(vertices.get(i));
				vertices.add(vertices.get(i).add(toMid.scale(0.1f)));
			}
		} else {// tetrahedron grid
			PApplet.println("Component of tetrahedron grid not implemented yet");
		}
	}

	public void findFacevertices() {
		if (voxelType == 0) {// rectangular grid
			// flat faces
			if (dir.x == 0) {
				facevertices.add(new ArrayList<Integer>(Arrays.asList(3, 0, 4, 7)));// outer face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(1, 2, 6, 5)));// outer face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(11, 15, 12, 8)));// inner face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(9, 13, 14, 10)));// inner face
			}
			if (dir.y == 0) {
				facevertices.add(new ArrayList<Integer>(Arrays.asList(0, 1, 5, 4)));// outer face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(2, 3, 7, 6)));// outer face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(8, 12, 13, 9)));// inner face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(10, 14, 15, 11)));// inner face
			}
			if (dir.z == 0) {
				facevertices.add(new ArrayList<Integer>(Arrays.asList(0, 3, 2, 1)));// outer face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(4, 5, 6, 7)));// outer face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(8, 9, 10, 11)));// inner face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(12, 15, 14, 13)));// inner face
			}
			// edge faces
			if (dir.x != 0) {
				facevertices.add(new ArrayList<Integer>(Arrays.asList(3, 0, 8, 11)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(0, 4, 12, 8)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(4, 7, 15, 12)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(7, 3, 11, 15)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(1, 2, 10, 9)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(2, 6, 14, 10)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(6, 5, 13, 14)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(5, 1, 9, 13)));// edge face
			}
			if (dir.y != 0) {
				facevertices.add(new ArrayList<Integer>(Arrays.asList(0, 1, 9, 8)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(1, 5, 13, 9)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(5, 4, 12, 13)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(4, 0, 8, 12)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(2, 3, 11, 10)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(3, 7, 15, 11)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(7, 6, 14, 15)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(6, 2, 10, 14)));// edge face
			}
			if (dir.z != 0) {
				facevertices.add(new ArrayList<Integer>(Arrays.asList(0, 3, 11, 8)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(3, 2, 10, 11)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(2, 1, 9, 10)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(1, 0, 8, 9)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(4, 5, 13, 12)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(5, 6, 14, 13)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(6, 7, 15, 14)));// edge face
				facevertices.add(new ArrayList<Integer>(Arrays.asList(7, 4, 12, 15)));// edge face
			}

		} else if (voxelType == 1) {// pyramid grid
			// check if the component faces up or down
			ArrayList<Float> valuesZ = new ArrayList<Float>(
					Arrays.asList(vertices.get(0).z, vertices.get(1).z, vertices.get(2).z));
			float once = 0;
			float twice = 0;
			if (PApplet.abs(valuesZ.get(0) - valuesZ.get(1)) < 0.001f) {
				once = valuesZ.get(2);
				twice = valuesZ.get(0);
			}
			if (PApplet.abs(valuesZ.get(0) - valuesZ.get(2)) < 0.001f) {
				once = valuesZ.get(1);
				twice = valuesZ.get(0);
			}
			if (PApplet.abs(valuesZ.get(1) - valuesZ.get(2)) < 0.001f) {
				once = valuesZ.get(0);
				twice = valuesZ.get(1);
			}
			if (once > twice) {// component points up
				for (int i = 0; i < 3; i++)
					facevertices.add(new ArrayList<Integer>(Arrays.asList(i, (i + 1) % 3, (i + 1) % 3 + 3, i + 3)));// the
																													// outer
																													// sides
				for (int i = 6; i < 9; i++)
					facevertices.add(new ArrayList<Integer>(
							Arrays.asList(i % 3 + 9, (i + 1) % 3 + 9, (i + 1) % 3 + 6, i % 3 + 6)));// the inner
																									// sides
				for (int i = 0; i < 3; i++)
					facevertices
							.add(new ArrayList<Integer>(Arrays.asList(i + 6, (i + 1) % 3 + 6, (i + 1) % 3, i % 3)));// start
																													// triangle
				for (int i = 0; i < 3; i++)
					facevertices.add(new ArrayList<Integer>(
							Arrays.asList(i + 3, (i + 1) % 3 + 3, (i + 1) % 3 + 9, i % 3 + 9)));// start triangle}
			} else {// component points down
				for (int i = 0; i < 3; i++)
					facevertices.add(new ArrayList<Integer>(Arrays.asList(i + 3, (i + 1) % 3 + 3, (i + 1) % 3, i)));// the
																													// outer
																													// sides
				for (int i = 6; i < 9; i++)
					facevertices.add(new ArrayList<Integer>(
							Arrays.asList(i % 3 + 6, (i + 1) % 3 + 6, (i + 1) % 3 + 9, i % 3 + 9)));// the inner
																									// sides
				for (int i = 0; i < 3; i++)
					facevertices
							.add(new ArrayList<Integer>(Arrays.asList(i % 3, (i + 1) % 3, (i + 1) % 3 + 6, i + 6)));// start
																													// triangle
				for (int i = 0; i < 3; i++)
					facevertices.add(new ArrayList<Integer>(
							Arrays.asList(i % 3 + 9, (i + 1) % 3 + 9, (i + 1) % 3 + 3, i + 3)));// start triangle}
			}
			// facevertices.add(new ArrayList<Integer>(Arrays.asList(0,1,2)));//start
			// triangle
			// facevertices.add(new ArrayList<Integer>(Arrays.asList(3,4,5)));//end triangle
		} else {// tetrahedron grid
			PApplet.println("Component of tetrahedron grid not implemented yet");
		}
	}

	// DISPLAY
	public void display() {
		parent.noStroke();
		parent.fill(col[0], col[1], col[2]);
		parent.stroke(0);
		parent.strokeWeight(1);
		for (ArrayList<Integer> facevertex : facevertices) {
			parent.beginShape();
			for (Integer fv : facevertex)
				parent.vertex(vertices.get(fv).x, vertices.get(fv).y, vertices.get(fv).z);
			parent.vertex(vertices.get(facevertex.get(0)).x, vertices.get(facevertex.get(0)).y,
					vertices.get(facevertex.get(0)).z);
			parent.endShape();
		}
	}

}