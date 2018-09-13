package main;

import processing.core.*;
import java.util.*;
import java.util.Set;

import javax.swing.text.html.parser.Element;

import java.util.HashSet;
import java.text.*;
import java.lang.reflect.*;
import toxi.geom.*;
import peasy.*;
import feb.Beam3D;
import feb.Beam3DCroSec;
import feb.Deform;
import feb.License;
import feb.Material;
import feb.Model;
import feb.Node;
import feb.Response;
import feb.Vec3d;
import feb.LoadPoint;

import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;

public class karambaTest extends PApplet {

	/**
	 * Cell Growth Simulation
	 *
	 * 180909
	 * 
	 * @author Christoph Klemmt www.orproject.com
	 *
	 *         Simulation of particles in 3D space that multiply and readjust their
	 *         positions according to intercellular behaviors and external forces.
	 *         Forces include - drag - unary force - point forces, with different
	 *         types to vary strength/distance behaviors. used especially towards
	 *         different groups of neighbors - spring forces, with different types
	 *         to vary strength/distance behaviors - attractor forces, with point,
	 *         line and plane attractors - planarization force, to create local
	 *         planarity - strata force, to create parallel strata of cells -
	 *         orthogonal force, to create orthogonal arrangements of cells - mesh
	 *         force, to react to imported geometry - voxel force, to react to voxel
	 *         grids
	 *
	 *         Voxelization possibilities in different grid types. Component
	 *         placement within the voxel grid. Includes import and export of .ply
	 *         mesh files.
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

// VARIABLES
	PeasyCam cam;
	ArrayList<Vec3D> envSize = new ArrayList<Vec3D>(Arrays.asList(new Vec3D(-1000, -1000, 0), new Vec3D(1000, 1000, 1000)));
	Vec3D center;
	boolean loaded = false;
	ArrayList<Agent> agents = new ArrayList<Agent>();
	ArrayList<Agent> agentsNew = new ArrayList<Agent>();
	ArrayList<Agent> agentsDisconnected = new ArrayList<Agent>(); //new array list
	ArrayList<Agent> agentsIndex = new ArrayList<Agent>(); // agent index
	ArrayList<Attractor> attractors = new ArrayList<Attractor>();
	
	ArrayList<Integer> agentsIA = new ArrayList<Integer>();
	ArrayList<Integer> agentsIB = new ArrayList<Integer>();
	
	
	boolean getDisplacements = true;
	int getDisplacementInterval = 50;
	int timeoutKaramba = 2;// maximum seconds to wait for Karamba
	int supportMin = (int) 1.4;
	int agentIndex;
	int neighborIndex;
	int agentSize;
	Agent element1;
	Agent element2;
	float elementIA;
	float elementIB;
	int save;
	
	String debug1;
	Agent debug2;
	Agent debug3;
	int debug4;
	int debug5;
	
	

// VOXELS AND COMPONENTS
	Voxelgrid voxelgrid = new Voxelgrid(0, new float[] { 2, 2, 2 });// voxelType: 0: reactangular; 1: pyramid; 2: triangular 3; gridsize should be larger than 0.1.
	ArrayList<Integer> voxelcounts = new ArrayList<Integer>(Arrays.asList(8, 12, 16));// amount of voxels that the component covers. Must be sorted from low to high.
	boolean componentsAligned = false;// Aligns all components. Overwrites componentsInPlane and componentsAlternating
	int axisComponentAlignment = 0;// alignment axis for the components, only works if componentsAligned=true.
	boolean componentsAlternating = true;// places components alternatingly in two direction. Overwrites componentsInPlane
	boolean componentsInPlane = true;// the components are either placed within the plane of the agents, or orthogonal to it
	boolean componentsInLayers = false;// the components are always placed in horizontal layers, if componentsInPlane or componentsAlternating							
	ArrayList<Vec3D> densities = new ArrayList<Vec3D>();// points around which the component density is largest
	float densityDistance = 30;// distance from the points in densities at which the component density gets reduced
	ArrayList<Integer> densityAxes = new ArrayList<Integer>(Arrays.asList(0, 1));// the axes along that the components can be removed

// GENERIC AGENT SETTINGS (Can be overridden in the Agent's constructor for separate groups of Agents.)
	int[] _col = new int[] { 0, 0, 0 };// color of the agent
	int _maxNeighbors = 10;// maximum neighbors to calculate neighbor forces
	float _range = 5.0f;// range to search for neighbors
	float _rangeClose = 1.0f;// range to define close neighbors.do not change
	float _drag = 0.5f;// drag coefficient (0.5)
	float _facNeighborsClose = -0.7f;// attraction/repulsion to close neighbors
	float _facNeighborsClosest = -0.15f;// attraction/repulsion to closest neighbor
	float _facNeighborsFar = 0.0f;// attraction/repulsion to far neighbors
	float _facPlanarize = 0.2f;// planarity force (0.2)
	float _facStrata = 0.0f;// strata force (0.03-0.04)
	float _facOrthogonal = 0.0f;// orthogonal force (0.05)
	float _facAttractors = 0.0f;// force towards attractors (0.05)
	float _facAttractorRotation = 0.0f;// force around attractors (0.01)
	Vec3D _unary = new Vec3D(0.0f, 0.0f, 0.005f);// unary force (-0.005)
	float _facFollowMesh = 0.01f;// force towards meshes (+/-0.01-0.05)
	float _facVoxel = 0.0f;// force towards the closest voxel
	int _minAge = 10;// minimum age for cell division (a larger number (10) inhibits the growth of tentacles)
	int _countDivide = 2;// amount of neighbors to check distance to, to trigger cell division
	float _rangeDivide = 3.5f;// maximum average distance after which the division is triggered
	float _offsetDivision = 0.1f;// random offset of the child cell from the parent cell (0.1)
	float _facVelChild = 0.0f;// scale factor for child velocity after cell division (a negative value inhibits the growth of tentacles)
	float _facVelParent = -1.0f;// scale factor for parent velocity after cell division (a negative value inhibits the growth of tentacles)

	
// MESHES
	Mesh meshStart01;
	Mesh meshStart02;
	Mesh meshFollow;

// DATE FOR IMAGE SAVING
	Date date = new Date();
	SimpleDateFormat ft = new SimpleDateFormat("yyMMdd_HHmm");
	String[] path = split(Agent.class.getProtectionDomain().getCodeSource().getLocation().getPath(), '/');
//String name = path[path.length-2].substring(0,path[path.length-2].length()-23);
	String name = "0";
	String nameRun = name + "-" + ft.format(date);

// VISUALIZATION
	boolean run = true;
	boolean showAgents = true;
	boolean showSpheres = false;
	boolean showMesh = false;
	boolean showEnv = false;
	boolean showNormals = false;
	boolean showNormalsVoxelized = false;
	boolean showAttractors = false;
	boolean showEdges = false;
	boolean showFaces = false;
	boolean voxelize = false;
	boolean showVoxels = false;
	boolean showComponents = false;
	boolean printLength = false;
	boolean showDisplacements = false;

	public void setup() {
		println("starting ", name);
		// CAMERA
		// size(1920, 1080, P3D);

		frameRate(30);
		cam = new PeasyCam(this, 0, 0, 0, 100);
		cam.setRotations(-1.57f, -1.57f, 0.0f);// left view
		// perspective(PI/3.0, width/height, 0.001,100000);
		// hint(DISABLE_DEPTH_TEST);

		thread("loadFiles");
	}

	public void settings() {
		size(1000, 1000, P3D);
	}

	public void loadFiles() {
		println("loading files...");
		// MESHES
		meshFollow = new Mesh("MeshFollow.ply");
		// envSize = meshFollow.boundingBox();
		center = envSize.get(0).add(envSize.get(1)).scale(0.5f);

		// VARYING COMPONENT DENSITIES
		// If densities is empty, all components will be placed everywhere. Otherwise
		// they will be sparser away from the points in densities.
		densities.add(new Vec3D(20, 0, 0));
		densities.add(new Vec3D(-20, 0, 50));

		// ATTRACTORS
		attractors.add(new Attractor(new Vec3D(0, 0, -10), -1));
		attractors.add(new Attractor(new Vec3D(50, 50, 0), -0.5f, 50, new boolean[] { true, true, false }));
		attractors.add(new Attractor(new Vec3D(-50, -50, 0), -1, 50, new boolean[] { true, true, false }));
		// Create attractors from text file
		// for(Vec3D pos : ImportPoints("input/Attractor.txt")) attractors.add(new
		// Attractor(pos,1,70));
		for (Vec3D pos : ImportPoints("input/Repeller.txt"))
			attractors.add(new Attractor(pos, -1, 70));

		// AGENTS
		randomSeed(0);
		for (int i = 0; i < 10; i++) {
			Vec3D pos = new Vec3D(random(-5.0f, 5.0f), random(-5.0f, 5.0f), random(0, 5.0f));
			Vec3D vel = new Vec3D();
			// Vec3D vel = new Vec3D(random(-0.01,0.01), random(-0.01,0.01),
			// random(-0.01,0.01));
			Agent a = new Agent(pos, vel);
			agents.add(a);
		}

		println("agents ", agents.size(), ", attractors ", attractors.size());

		synchronized (this) {
			loaded = true;
		}

	}

	/**
	 * Cell Growth Simulation
	 *
	 * 180909
	 * 
	 * @author Christoph Klemmt www.orproject.com
	 *
	 *         Agent class
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

	class Agent extends Vec3D {

		// PARAMETERS
		Vec3D vel; // velocity = speed and direction the agent is travelling
		Vec3D acc;// acceleration
		ArrayList<Attractor> atts = new ArrayList<Attractor>(); // attractors the agent reacts to
		ArrayList<Attractor> attsRotation = new ArrayList<Attractor>(); // attractors the agent rotates around
		ArrayList<Vec3D> attsRotationAxes = new ArrayList<Vec3D>(); // attractors the agent rotates around
		ArrayList<Agent> neighbors = new ArrayList<Agent>();// neighbors
		ArrayList<Agent> neighborsClose = new ArrayList<Agent>();// neighbors
		ArrayList<Agent> neighborsFar = new ArrayList<Agent>();// neighbors
		ArrayList<Float> distances = new ArrayList<Float>();// distances to neighbors
		Agent agentClosest;// the closest Agent, required if no agent is a neighbor within range
		int countClose = 0;// amount of close neighbors
		int index;
		int age;
		Vec3D normal;// the normal of the agent according to its neighbors
		Vec3D displacement;// displacement as imported from GH Karamba
		Voxel voxel;// voxel of the agent for voxelization
		Component component;// component of the agent, for voxelization

		// SETTINGS
		int[] col = _col;
		int maxNeighbors = _maxNeighbors;
		float range = _range;
		float drag = _drag;
		float dragAcc;// drag affecting the acceleration
		float facNeighborsClose = _facNeighborsClose;
		float facNeighborsClosest = _facNeighborsClosest;
		float facNeighborsFar = _facNeighborsFar;
		float facPlanarize = _facPlanarize;
		float facStrata = _facStrata;
		float facOrthogonal = _facOrthogonal;
		float facAttractors = _facAttractors;
		float facAttractorRotation = _facAttractorRotation;
		Vec3D unary = _unary;
		float facFollowMesh = _facFollowMesh;
		float facVoxel = _facVoxel;
		int minAge = _minAge;
		int countDivide = _countDivide;
		float rangeDivide = _rangeDivide;
		float offsetDivision = _offsetDivision;
		float facVelChild = _facVelChild;
		float facVelParent = _facVelParent;

		// CONSTRUCTOR
		Agent(Vec3D _pos, Vec3D _vel) {
			super(_pos);
			vel = _vel;
			acc = new Vec3D();
			index = agents.size();
			age = 0;
			atts = attractors;
			attsRotation = new ArrayList<Attractor>(Arrays.asList(attractors.get(1), attractors.get(2)));
			attsRotationAxes = new ArrayList<Vec3D>(Arrays.asList(new Vec3D(0, 0, 1), new Vec3D(0, 0, -1)));
			normal = new Vec3D();
			displacement = new Vec3D();
		}

		// BEHAVIORS

		public void move() {
			acc.clear();
			if (age < 250)
				col = new int[] { 0, PApplet.parseInt(age * 0.5f), age };
			findNeighbors();
			normal = findNormal();

			// CELL FORCES
			acc.addSelf(forcePoint(neighborsClose, facNeighborsClose, _rangeClose));// repelling point force between
																					// close neighbours
			if (neighbors.size() > 0)
				acc.addSelf(forcePoint(neighbors.get(0), facNeighborsClosest));// force towards closest neighbor
			acc.addSelf(forcePoint(neighborsFar, facNeighborsFar));// force towards all far neighbors

			acc.addSelf(forceAttractors(atts, facAttractors));// force towards attractors
			acc.addSelf(forceAttractorRotation(attsRotation, new Vec3D(0, 0, 1), facAttractorRotation));// force towards
																										// attractors
			acc.addSelf(planarize(facPlanarize));// pull each cell onto a plane
			acc.addSelf(forceStrata(facStrata));// pull each cell into parallel planes
			acc.addSelf(forceOrthogonal(facOrthogonal));// pull each cell into orthogonal planes
			acc.addSelf(unary);// unary force
			acc.addSelf(followMesh(facFollowMesh));// pull towards the mesh
			acc.addSelf(forceVoxel(facVoxel));// pull towards the closest voxel

			// CONSTRAIN POSITION
			// bounce(0.0);

			// DIVIDE
			if (age > minAge) {
				int count = Math.min(countDivide, distances.size());
				if (count > 0) {
					float distanceAverage = 0;
					for (int i = 0; i < count; i++) {
						distanceAverage += distances.get(i);
					}
					distanceAverage = distanceAverage / PApplet.parseFloat(count);
					if (distanceAverage > rangeDivide) {
						Vec3D posNew = this;// position of the child cell
						posNew.addSelf(new Vec3D(random(-offsetDivision, offsetDivision),
								random(-offsetDivision, offsetDivision), random(-offsetDivision, offsetDivision)));// random
																													// offset
																													// within
																													// a
																													// range
						Vec3D velNew = vel.scale(facVelChild);// velocity of the child cell
						Agent agentNew = new Agent(posNew, velNew);
						agentNew.displacement = displacement;
						agentNew.neighbors = new ArrayList<Agent>(neighbors);
						agentNew.neighbors.add(this);
						neighbors.add(agentNew);
						agentsNew.add(agentNew);
						vel.scaleSelf(facVelParent);// set the velocity of the parent cell
						age = 0;// set the age of the parent cell to 0
					}
				}
			}
		}

		public void update() {
			// UPDATE
			vel.scaleSelf(1.0f - drag);// apply the drag to the previous velocity
			vel.addSelf(acc);// add the acceleration to the velocity
			if (age > 250)
				vel.scaleSelf(0.2f);// slower movement for older agents
			this.addSelf(vel);// add the velocity to the position
			age += 1;
		}

		// FIND THE CLOSEST NEIGHBORS
		public void findNeighbors() {
			ArrayList<Agent> agentsSorted = new ArrayList<Agent>();
			// construct list of neighbors to be evaluated
			if (frameCount % 200 == 0 || neighbors.size() < 4) {
				agentsSorted = new ArrayList<Agent>(agents);// sorted agents, aSorted.get(0) will be "this"
			} else {
				Set<Agent> agentSet = new HashSet<Agent>(neighbors);
				for (Agent n : neighbors) {
					for (Agent nn : n.neighbors) {
						agentSet.add(nn);
						if (frameCount % 20 == 0) {
							for (Agent nnn : nn.neighbors) {
								agentSet.add(nnn);
							}
						}
					}
				}
				agentsSorted = new ArrayList<Agent>(agentSet);
			}
			// reset agent variables
			neighbors.clear();
			neighborsClose.clear();
			neighborsFar.clear();
			distances.clear();
			countClose = 0;
			// sort list by distances
			final Vec3D thisPos = new Vec3D(this);
			Collections.sort(agentsSorted, new Comparator<Agent>() {
				@Override
				public int compare(Agent a, Agent b) {
					return Float.compare((Float) a.distanceToSquared(thisPos), (Float) b.distanceToSquared(thisPos));
				}
			});
			agentsSorted.remove(0);// remove self from the list
			agentClosest = agentsSorted.get(0);// the closest agent, even if it is outside of the range for neighbors
			if (agentsSorted.size() > _maxNeighbors)
				agentsSorted = new ArrayList<Agent>(agentsSorted.subList(0, _maxNeighbors));
			// Distances
			for (Agent neighbor : agentsSorted) {
				float dist = this.distanceTo(neighbor);
				if (dist < range) {
					neighbors.add(neighbor);
					if (dist < _rangeClose) {
						neighborsClose.add(neighbor);
						countClose += 1;
					} else
						neighborsFar.add(neighbor);
					distances.add(dist);
				} else
					break;
			}
		}

		// find the midpoint between the agent and another agent
		public Vec3D mid(Object obj) {
			Vec3D pos2 = (Vec3D) obj;
			return new Vec3D(pos2.x + (0.5f * (x - pos2.x)), pos2.y + (0.5f * (y - pos2.y)),
					pos2.z + (0.5f * (z - pos2.z)));
		}

		// NORMAL
		public Vec3D findNormal() {
			ArrayList<Agent> neighbors2 = new ArrayList<Agent>(neighbors);
			if (neighbors2.size() < 3) {
				if (agents.size() < 4) {
					println("planarize error", neighbors2.size(), agents.size());// not enough agents in the scene
				} else {
					ArrayList<Agent> aSorted = new ArrayList<Agent>(agents);// sorted agents, aSorted.get(0) will be
																			// "this"
					final Vec3D thisPos = new Vec3D(this);
					Collections.sort(aSorted, new Comparator<Agent>() {
						@Override
						public int compare(Agent a, Agent b) {
							return Float.compare((Float) a.distanceToSquared(thisPos),
									(Float) b.distanceToSquared(thisPos));
						}
					});
					neighbors2 = new ArrayList<Agent>(aSorted.subList(1, 4));// item 0 will be this
				}
			}
			Vec3D e1 = neighbors2.get(1).sub(neighbors2.get(0));
			Vec3D e2 = neighbors2.get(2).sub(neighbors2.get(0));
			return e1.cross(e2).normalize();
		}

		// POINT FORCE
		public Vec3D forcePoint(Vec3D target, float strength) {
			Vec3D vec = target.sub(this); // vector from the target to the agent
			return vec.normalize().scale(strength);
		}

		// POINT FORCE acting within a radius
		public Vec3D forcePoint(Vec3D target, float strength, float radius) {
			Vec3D vec = target.sub(this); // vector from the target to the agent
			float factor = vec.magnitude();// factor to be multiplied by the strength
			factor = Math.max(0, Math.min(radius, factor));// map the range from 0 to 1
			factor = factor / radius;
			factor = (float) (Math.cos(factor * Math.PI) + 1) * 0.5f;// cosinus instead of a bezier distribution, to
																		// mimic SI's FCurve
			return vec.scale(factor * strength);
		}

		// POINT FORCE, exponent defines the strength per distance. radius is the
		// distance at which the strength is 1*strength.
		public Vec3D forcePoint(Vec3D target, float strength, float radius, float exponent) {
			Vec3D vec = target.sub(this); // vector from the target to the agent
			float factor = 1 / pow(vec.magnitude() / radius, exponent);
			return vec.normalize().scale(strength * factor);
		}

		// POINT FORCE
		public Vec3D forcePoint(ArrayList<Agent> targets, float strength) {
			Vec3D vec = new Vec3D();
			for (Vec3D target : targets) {
				Vec3D vecAdd = target.sub(this); // vector from the target to the agent
				vecAdd.normalize().scaleSelf(strength);
				vec.addSelf(vecAdd);
			}
			return vec;
		}

		// POINT FORCE acting within a radius
		public Vec3D forcePoint(ArrayList<Agent> targets, float strength, float radius) {
			Vec3D vec = new Vec3D();
			for (Vec3D target : targets) {
				Vec3D vecAdd = target.sub(this); // vector from the target to the agent
				float factor = vecAdd.magnitude();// factor to be multiplied by the strength
				factor = Math.max(0, Math.min(radius, factor));// map the range from 0 to 1
				factor = factor / radius;
				factor = (float) (Math.cos(factor * Math.PI) + 1) * 0.5f;// cosinus instead of a bezier distribution, to
																			// mimic SI's FCurve
				vecAdd.scaleSelf(factor * strength);
				vec.addSelf(vecAdd);
			}
			return vec;
		}

		// POINT FORCE, exponent defines the strength per distance. radius is the
		// distance at which the strength is 1*strength.
		public Vec3D forcePoint(ArrayList<Agent> targets, float strength, float radius, float exponent) {
			Vec3D vec = new Vec3D();
			for (Vec3D target : targets) {
				Vec3D vecAdd = target.sub(this); // vector from the target to the agent
				float factor = 1 / pow(vecAdd.magnitude() / radius, exponent);
				vecAdd.normalize().scaleSelf(strength * factor);
				vec.addSelf(vecAdd);
			}
			return vec;
		}

		// SPRING FORCE
		public Vec3D spring(Vec3D target, float restlength, float strength) {
			Vec3D vec = this.sub(target); // vector from the hook to the agent
			float dist = vec.magnitude(); // distance between the agent and the hook
			vec.scaleSelf((restlength - dist) / dist); // spring force formula
			vec.scaleSelf(strength);
			return vec; // adjust the strength for agents which are closer than the restlength
		}

		// MULTIPLE SPRINGS
		public Vec3D spring(ArrayList<Vec3D> targets, float restlength, float strength) {
			Vec3D vec = new Vec3D();
			for (Vec3D target : targets) {
				Vec3D vecMove = this.sub(target); // vector from the hook to the agent
				float dist = vecMove.magnitude(); // distance between the agent and the hook
				vecMove.scaleSelf((restlength - dist) / dist); // spring force formula
				vec.addSelf(vecMove);
			}
			vec.scaleSelf(strength);// adjust the strength for agents which are closer than the restlength
			return vec;
		}

		// ATTRACTORS
		public Vec3D forceAttractors(Attractor a, float strength) {
			Vec3D vec = new Vec3D();
			Vec3D pos = new Vec3D(a);
			if (a.activeDir[0] == false)
				pos.x = this.x;
			if (a.activeDir[1] == false)
				pos.y = this.y;
			if (a.activeDir[2] == false)
				pos.z = this.z;
			if (a.radius == 0)
				vec = forcePoint(pos, a.strength);
			else if (a.exponent == 0)
				vec = forcePoint(pos, a.strength, a.radius);
			else
				vec = forcePoint(pos, a.strength, a.radius, a.exponent);
			vec.scaleSelf(strength);
			return vec;
		}

		// ATTRACTORS
		public Vec3D forceAttractors(ArrayList<Attractor> attractors, float strength) {
			Vec3D vec = new Vec3D();
			for (Attractor a : attractors)
				vec.addSelf(forceAttractors(a, 1));
			vec.scaleSelf(strength);
			return vec;
		}

		// ATTRACTOR ROTATION
		public Vec3D forceAttractorRotation(Attractor a, Vec3D axis, float strength) {
			Vec3D vec = new Vec3D();
			Vec3D pos = new Vec3D(a);
			if (a.activeDir[0] == false)
				pos.x = this.x;
			if (a.activeDir[1] == false)
				pos.y = this.y;
			if (a.activeDir[2] == false)
				pos.z = this.z;
			if (a.radius == 0)
				vec = forcePoint(pos, a.strength);
			else if (a.exponent == 0)
				vec = forcePoint(pos, a.strength, a.radius);
			else
				vec = forcePoint(pos, a.strength, a.radius, a.exponent);
			vec = vec.getRotatedAroundAxis(axis, (float) Math.PI * 0.5f);
			vec.scaleSelf(strength);
			return vec;
		}

		// ATTRACTOR ROTATION
		public Vec3D forceAttractorRotation(ArrayList<Attractor> attractors, Vec3D axis, float strength) {
			Vec3D vec = new Vec3D();
			for (Attractor a : attractors)
				vec.addSelf(forceAttractorRotation(a, axis, 1));
			vec.scaleSelf(strength);
			return vec;
		}

		// ATTRACTOR ROTATION
		public Vec3D forceAttractorRotation(ArrayList<Attractor> attractors, ArrayList<Vec3D> axes, float strength) {
			Vec3D vec = new Vec3D();
			for (Attractor a : attractors)
				vec.addSelf(forceAttractorRotation(a, axes.get(attractors.indexOf(a)), 1));
			vec.scaleSelf(strength);
			return vec;
		}

		// PLANARIZE: pulls a point towards the plane through its 3 closest neighbours
		public Vec3D planarize(float strength) {
			Vec3D v3 = this.sub(agentClosest);
			float dot = v3.dot(normal);
			Vec3D vec = normal.scale(dot);
			vec = this.sub(vec);
			vec = vec.sub(this);
			vec.scaleSelf(strength);
			return vec;
		}

		// STRATA FORCE
		public Vec3D forceStrata(float strength) {
			if (neighbors.size() == 0)
				return new Vec3D();
			Vec3D mid = new Vec3D();
			for (Agent n : neighbors) {
				mid.addSelf(n);
			}
			mid.scaleSelf(1.0f / neighbors.size());
			Vec3D target = new Vec3D(x, mid.y, z);// strata in y direction
			return forcePoint(target, strength);
		}

		// ORTHOGONAL FORCE
		public Vec3D forceOrthogonal(float strength) {
			Vec3D target;
			if (neighbors.size() == 0)
				return new Vec3D();
			Vec3D mid = new Vec3D();
			for (Agent n : neighbors)
				mid.addSelf(n);
			mid.scaleSelf(1.0f / neighbors.size());
			if (Math.abs(x - mid.x) < Math.abs(y - mid.y) && Math.abs(x - mid.x) < Math.abs(z - mid.z))
				target = new Vec3D(mid.x, y, z);// pull in x direction
			else if (Math.abs(y - mid.y) < Math.abs(z - mid.z))
				target = new Vec3D(x, mid.y, z);// pull in y direction
			else
				target = new Vec3D(x, y, mid.z);// pull in z direction
			return forcePoint(target, strength);
		}

		// VOXEL FORCE
		public Vec3D forceVoxel(float strength) {
			Vec3D vec = this.sub(voxelgrid.voxelize(this));
			vec.scaleSelf(strength);
			return vec;
		}

		// MESH FOLLOW
		public Vec3D followMesh(float strength) {
			if (strength == 0.0f)
				return new Vec3D();
			Vertex cv = meshFollow.getClosestVertex(this);// closest vertex
			Vertex cv2 = null;// the second closest vertex among cv's neighbors
			for (Vertex x : cv.vertices)
				cv2 = (cv2 == null || x.distanceToSquared(this) < cv2.distanceToSquared(this)) ? x : cv2;
			// check how many faces the common edge has, if it is an open edge
			Edge edgeCommon = null;
			for (Edge edge : cv.edges) {
				if (edge.vertices.contains(cv2))
					edgeCommon = edge;
			}
			if (edgeCommon == null) {
				println("ERROR followMesh");
				return new Vec3D();
			}
			if (edgeCommon.faces.size() > 1) {
				// inner edge, pull towards face
				float distance = this.distanceTo(cv);
				float dot = cv.normal.dot(this.sub(cv));
				if (dot > 0)
					return cv.normal.scale(-strength * distance);
				return cv.normal.scale(strength * distance);
			} else {
				// outer edge
				// pull towards edge
				Vec3D vecA = this.sub(cv);
				Vec3D vecC = cv.sub(cv2);
				float t = -vecA.dot(vecC) / vecC.magSquared();
				Vec3D closest = cv.add(vecC.scale(t));
				Vec3D direction = closest.sub(this);
				direction.scaleSelf(0.1f);// ----------------------------------------smaller scale factor for the mesh
											// edges to reduce sticking
				return direction.scale(strength);
				// don't react
				// return new Vec3D();
			}
		}

		// STAY WITHIN THE ENVIRONMENT BOX
		public void bounce(float strength) {
			if (x < envSize.get(0).x) {
				x = envSize.get(0).x;
				vel.x *= -strength;
			}
			if (x > envSize.get(1).x) {
				x = envSize.get(1).x;
				vel.x *= -strength;
			}
			if (y < envSize.get(0).y) {
				y = envSize.get(0).y;
				vel.y *= -strength;
			}
			if (y > envSize.get(1).y) {
				y = envSize.get(1).y;
				vel.y *= -strength;
			}
			if (z < envSize.get(0).z) {
				z = envSize.get(0).z;
				vel.z *= -strength;
			}
			if (z > envSize.get(1).z) {
				z = envSize.get(1).z;
				vel.z *= -strength;
			}
		}

		// DISPLAY
		public void display() {
			stroke(col[0], col[1], col[2]);
			strokeWeight(3);
			Vec3D p0 = new Vec3D(this);
			point(p0.x, p0.y, p0.z);
		}

		// DISPLAY SPHERES
		public void displaySpheres() {
			fill(col[0] * 0.5f + 126, col[1] * 0.5f + 126, col[2] * 0.5f + 126);
			noStroke();
			Vec3D p0 = new Vec3D(this);
			pushMatrix();
			translate(p0.x, p0.y, p0.z);
			sphere(range * 0.5f);
			popMatrix();
		}

		// DISPLAY EDGES
		public void displayEdges() {
			strokeWeight(1);
			stroke(50, 100, 255, 150);
			for (Agent n : neighbors) {
				if (agents.indexOf(this) < agents.indexOf(n)) {
					Vec3D p0 = new Vec3D(this);
					Vec3D p1 = new Vec3D(n);
					if (voxelize) {
						p0 = voxelgrid.voxelize(p0);
						p1 = voxelgrid.voxelize(p1);
					}
					line(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);
				}
			}
		}

		// DISPLAY FACES
		public void displayFaces() {
			noStroke();
			fill(255, 150);
			for (Agent n1 : neighbors) {
				for (Agent n2 : neighbors) {
					if (n1.neighbors.contains(n2) || n2.neighbors.contains(n1)) {// triangle
						if (agents.indexOf(this) < agents.indexOf(n1) && agents.indexOf(n1) < agents.indexOf(n2)) {
							Vec3D p0 = new Vec3D(this);
							Vec3D p1 = new Vec3D(n1);
							Vec3D p2 = new Vec3D(n2);
							if (voxelize) {
								p0 = voxelgrid.voxelize(p0);
								p1 = voxelgrid.voxelize(p1);
								p2 = voxelgrid.voxelize(p2);
							}
							beginShape();
							vertex(p0.x, p0.y, p0.z);
							vertex(p1.x, p1.y, p1.z);
							vertex(p2.x, p2.y, p2.z);
							vertex(p0.x, p0.y, p0.z);
							endShape();
						}
					} else {
						for (Agent n3 : n1.neighbors) {
							if (n3 != this && n3 != n2) {
								if (n2.neighbors.contains(n3) && neighbors.contains(n3) == false) {// quadrangle
									if (agents.indexOf(this) < agents.indexOf(n1)
											&& agents.indexOf(this) < agents.indexOf(n2)
											&& agents.indexOf(this) < agents.indexOf(n3)
											&& agents.indexOf(n1) < agents.indexOf(n2)) {
										Vec3D p0 = new Vec3D(this);
										Vec3D p1 = new Vec3D(n1);
										Vec3D p2 = new Vec3D(n2);
										Vec3D p3 = new Vec3D(n3);
										if (voxelize) {
											p0 = voxelgrid.voxelize(p0);
											p1 = voxelgrid.voxelize(p1);
											p2 = voxelgrid.voxelize(p2);
											p3 = voxelgrid.voxelize(p3);
										}
										beginShape();
										vertex(p0.x, p0.y, p0.z);
										vertex(p1.x, p1.y, p1.z);
										vertex(p3.x, p3.y, p3.z);
										vertex(p2.x, p2.y, p2.z);
										vertex(p0.x, p0.y, p0.z);
										endShape();
									}
								}
							}
						}
					}
				}
			}
		}

		public void displayNormals() {
			Vec3D dir = new Vec3D(normal);
			if (showNormalsVoxelized) {
				if (Math.abs(dir.x) > Math.abs(dir.y) && Math.abs(dir.x) > Math.abs(dir.z))
					dir = new Vec3D(dir.x, 0, 0);
				else if (Math.abs(dir.y) > Math.abs(dir.z))
					dir = new Vec3D(0, dir.y, 0);
				else
					dir = new Vec3D(0, 0, dir.z);
			}
			Vec3D p = this.add(dir.normalizeTo(5));
			strokeWeight(1);
			stroke(0, 125, 100);
			line(x, y, z, p.x, p.y, p.z);
		}

		public void displayDisplacements() {
			Vec3D p = this.add(displacement.scale(1));
			strokeWeight(1);
			stroke(125, 0, 100);
			line(x, y, z, p.x, p.y, p.z);
		}

	}

	/**
	 * Cell Growth Simulation
	 *
	 * 180909
	 * 
	 * @author Christoph Klemmt www.orproject.com
	 *
	 *         Attractor class
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

	class Attractor extends Vec3D {

		// VARIABLES
		float strength;
		float radius = 0;// radius of the attractor
		int exponent = 0;// exponent to have the attractor act differently according to distance
		boolean[] activeDir = new boolean[] { true, true, true };// can be set to false in order to make a line
																	// attractor or a surface attractor
		int[] col;// color of the attractor

		// CONSTRUCTOR
		Attractor(Vec3D _pos, float _strength) {
			super(_pos);
			strength = _strength;
			if (strength > 0)
				col = new int[] { 0, 255, 100 };
			else
				col = new int[] { 0, 100, 255 };
		}

		Attractor(Vec3D _pos, float _strength, float _radius) {
			super(_pos);
			strength = _strength;
			radius = _radius;
			if (strength > 0)
				col = new int[] { 0, 255, 100 };
			else
				col = new int[] { 0, 100, 255 };
		}

		Attractor(Vec3D _pos, float _strength, float _radius, int _exponent) {
			super(_pos);
			strength = _strength;
			radius = _radius;
			exponent = _exponent;
			if (strength > 0)
				col = new int[] { 0, 255, 100 };
			else
				col = new int[] { 0, 100, 255 };
		}

		Attractor(Vec3D _pos, float _strength, boolean[] _activeDir) {
			super(_pos);
			strength = _strength;
			activeDir = _activeDir;
			if (strength > 0)
				col = new int[] { 0, 255, 100 };
			else
				col = new int[] { 0, 100, 255 };
		}

		Attractor(Vec3D _pos, float _strength, float _radius, boolean[] _activeDir) {
			super(_pos);
			strength = _strength;
			radius = _radius;
			activeDir = _activeDir;
			if (strength > 0)
				col = new int[] { 0, 255, 100 };
			else
				col = new int[] { 0, 100, 255 };
		}

		Attractor(Vec3D _pos, float _strength, float _radius, int _exponent, boolean[] _activeDir) {
			super(_pos);
			strength = _strength;
			radius = _radius;
			exponent = _exponent;
			activeDir = _activeDir;
			if (strength > 0)
				col = new int[] { 0, 255, 100 };
			else
				col = new int[] { 0, 100, 255 };
		}

		// DISPLAY FUNCTION
		public void display() {
			// draws a blurry point
			stroke(col[0], col[1], col[2], 25);
			for (int i = 0; i < 7; i++) {
				strokeWeight((i * 2) + 1);
				if (activeDir[0] == true && activeDir[1] == true && activeDir[2] == true) {
					point(x, y, z);
				} else if (activeDir[0] == false) {
					line(x + 100, y, z, x - 100, y, z);
				} else if (activeDir[1] == false) {
					line(x, y + 100, z, x, y - 100, z);
				} else if (activeDir[2] == false) {
					line(x, y, z + 100, x, y, z - 100);
				}
			}
		}

	}

	/**
	 * Cell Growth Simulation
	 *
	 * 180909
	 * 
	 * @author Christoph Klemmt www.orproject.com
	 *
	 *         Mesh classes for import of .ply files
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

	class Mesh {

		ArrayList<Vertex> vertices;
		ArrayList<Face> faces;
		ArrayList<Edge> edges;

		Mesh(String fileName) {
			vertices = new ArrayList<Vertex>();
			faces = new ArrayList<Face>();
			edges = new ArrayList<Edge>();
			importFile(fileName);
		}

		public void importFile(String fileName) {
			String[] fileLines = loadStrings(fileName);
			// check file details
			int lineVertex = 0;
			int lineFace = 0;
			int countVertices = 0;
			int countFaces = 0;
			boolean hasNormals = false;
			boolean hasColors = false;
			for (String fileLine : fileLines) {
				if (fileLine.length() > 14) {
					if (fileLine.substring(0, 14) == "element vertex") {
						lineVertex = Arrays.asList(fileLines).indexOf(fileLine);
						countVertices = PApplet.parseInt(fileLine.substring(15, fileLine.length()));
					} else if (fileLine.substring(0, 12) == "element face") {
						lineFace = Arrays.asList(fileLines).indexOf(fileLine);
						countFaces = PApplet.parseInt(fileLine.substring(13, fileLine.length()));
					}
				}
			}
			// check vertex properties
			for (int i = lineVertex + 1; i < lineFace; i++) {
				if (fileLines[i].substring(0, 8) == "property") {
					if (fileLines[i].substring(15, 16) == "nx")
						hasNormals = true;
					if (fileLines[i].substring(15, 18) == "red")
						hasColors = true;
				}
			}

			for (String fileLine : fileLines) {
				String[] items = split(fileLine, ' ');
				if (Double.isNaN(PApplet.parseFloat(items[0])) == false) {
					// items is a vertex, face or material
					if ((items[0] == "3" || items[0] == "4") == false
							&& (items.length == 3 || items.length == 6 || items.length == 9)) {
						// vertex
						Vec3D pos = new Vec3D(PApplet.parseFloat(items[0]), PApplet.parseFloat(items[1]),
								PApplet.parseFloat(items[2]));
						Vec3D nor = new Vec3D();
						if (hasNormals)
							nor = new Vec3D(PApplet.parseFloat(items[3]), PApplet.parseFloat(items[4]),
									PApplet.parseFloat(items[5]));
						int[] col = new int[] { 0, 0, 0 };
						if (hasColors)
							col = new int[] { PApplet.parseInt(items[6]), PApplet.parseInt(items[7]),
									PApplet.parseInt(items[8]) };
						new Vertex(pos, nor, col, this);
					} else if ((PApplet.parseFloat(items[0]) == 3 && items.length == 4)
							|| (PApplet.parseFloat(items[0]) == 4 && items.length == 5)) {
						// face
						int[] indices;
						if (PApplet.parseFloat(items[0]) == 3)
							indices = new int[] { PApplet.parseInt(items[1]), PApplet.parseInt(items[2]),
									PApplet.parseInt(items[3]) };
						else
							indices = new int[] { PApplet.parseInt(items[1]), PApplet.parseInt(items[2]),
									PApplet.parseInt(items[3]), PApplet.parseInt(items[4]) };
						new Face(indices, this);
					} else
						println("error import mesh");
				}
			}
			if (hasNormals == false)
				for (Vertex v : vertices)
					v.computeNormal();
			// if(vertices.size()!=countVertices) println("ERROR Mesh import vertices:
			// countVertices=",countVertices," imported:",vertices.size());
			// if(faces.size()!=countFaces) println("ERROR Mesh import faces:
			// countFaces=",countFaces," imported:",faces.size());
			println("mesh ", fileName, " imported. v", vertices.size(), " e", edges.size(), " f", faces.size());
		}

		public Vertex getClosestVertex(Vec3D point) {
			Vertex closest = null;// the closest vertex among cv's neighbors
			for (Vertex x : vertices)
				closest = (closest == null || x.distanceToSquared(point) < closest.distanceToSquared(point)) ? x
						: closest;
			return closest;
		}

		public ArrayList<Vec3D> boundingBox() {
			// Returns the two corners of the bounding box of the mesh. If boxMin is not
			// empty, the points of boxMin are also included.
			Vec3D low = new Vec3D(vertices.get(0));
			Vec3D high = new Vec3D(vertices.get(0));
			for (Vertex v : vertices) {
				if (v.x < low.x)
					low.x = v.x;
				if (v.x > high.x)
					high.x = v.x;
				if (v.y < low.y)
					low.y = v.y;
				if (v.y > high.y)
					high.y = v.y;
				if (v.z < low.z)
					low.z = v.z;
				if (v.z > high.z)
					high.z = v.z;
			}
			return new ArrayList<Vec3D>(Arrays.asList(low, high));
		}

		public ArrayList<Vec3D> boundingBox(ArrayList<Vec3D> boxMin) {
			// Returns the two corners of the bounding box of the mesh. If boxMin is not
			// empty, the points of boxMin are also included.
			Vec3D low = new Vec3D(vertices.get(0));
			Vec3D high = new Vec3D(vertices.get(0));
			for (Vertex v : vertices) {
				if (v.x < low.x)
					low.x = v.x;
				if (v.x > high.x)
					high.x = v.x;
				if (v.y < low.y)
					low.y = v.y;
				if (v.y > high.y)
					high.y = v.y;
				if (v.z < low.z)
					low.z = v.z;
				if (v.z > high.z)
					high.z = v.z;
			}
			for (Vec3D v : boxMin) {
				if (v.x < low.x)
					low.x = v.x;
				if (v.x > high.x)
					high.x = v.x;
				if (v.y < low.y)
					low.y = v.y;
				if (v.y > high.y)
					high.y = v.y;
				if (v.z < low.z)
					low.z = v.z;
				if (v.z > high.z)
					high.z = v.z;
			}
			return new ArrayList<Vec3D>(Arrays.asList(low, high));
		}

		public void display(boolean showVertices, boolean showEdges, boolean showFaces) {
			if (showVertices)
				drawVertex();
			if (showEdges)
				drawEdges();
			if (showFaces)
				drawFaces();
		}

		public void drawVertex() {
			for (Vertex v : vertices) {
				stroke(v.col[0], v.col[1], v.col[2]);
				strokeWeight(3);
				point(v.x, v.y, v.z);
			}
		}

		public void drawEdges() {
			for (Edge e : edges) {
				stroke(e.col[0], e.col[1], e.col[2], 50);
				strokeWeight(1);
				line(e.vertices.get(0).x, e.vertices.get(0).y, e.vertices.get(0).z, e.vertices.get(1).x,
						e.vertices.get(1).y, e.vertices.get(1).z);
			}
		}

		public void drawStructure() {
			for (Edge e : edges) {
				if (e.countTrails > 5) {
					float diameter = e.countTrails * 0.05f + 1;
					Vec3D mid = e.mid();
					Vec3D vec = e.vertices.get(1).sub(e.vertices.get(0));
					Vec3D vecFlat = new Vec3D(vec);
					vecFlat.z = 0;
					float angleZ = angleDir(new Vec3D(1, 0, 0), vecFlat, new Vec3D(0, 0, 1));
					Vec3D normal2 = new Vec3D(0, 1, 0).getRotatedZ(angleZ);
					float angleY = angleDir(new Vec3D(0, 0, 1), vec, normal2);
					noStroke();
					fill(255);
					pushMatrix();
					translate(mid.x, mid.y, mid.z);
					rotateZ(angleZ);
					rotateY(angleY);
					box(diameter, diameter, e.length());
					popMatrix();

					Vec3D p0 = e.vertices.get(0);
					float colorValue = meshFollow.getClosestVertex(p0).colValue;
					Vec3D vec2 = new Vec3D(10, 20, colorValue * 20);
					vec2.scaleSelf(diameter);
					Vec3D p2 = p0.add(vec2);
					stroke(PApplet.parseInt(colorValue * 255), 200, 100);
					strokeWeight(1);
					line(p0.x, p0.y, p0.z, p2.x, p2.y, p2.z);
				}
			}
		}

		public float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal) {
			// normal is the reference normal to define if the angle is positive or negative
			float angle = acos(vecA.copy().normalize().dot(vecB.copy().normalize()));
			Vec3D cross = vecA.cross(vecB);
			if (normal.dot(cross) < 0)
				angle = -angle;// Or > 0
			return angle;
		}

		public void drawFaces() {
			for (Face f : faces) {
				fill(f.col[0], f.col[1], f.col[2], 50);
				noStroke();
				if (f.vertices.size() == 3) {
					beginShape(TRIANGLES);
					vertex(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);
					vertex(f.vertices.get(1).x, f.vertices.get(1).y, f.vertices.get(1).z);
					vertex(f.vertices.get(2).x, f.vertices.get(2).y, f.vertices.get(2).z);
					endShape();
				} else if (f.vertices.size() == 4) {
					beginShape(QUAD);
					vertex(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);
					vertex(f.vertices.get(1).x, f.vertices.get(1).y, f.vertices.get(1).z);
					vertex(f.vertices.get(2).x, f.vertices.get(2).y, f.vertices.get(2).z);
					vertex(f.vertices.get(3).x, f.vertices.get(3).y, f.vertices.get(3).z);
					endShape();
				}
			}
		}
	}

	class Vertex extends Vec3D {

		int index;
		Mesh mesh;
		ArrayList<Vertex> vertices;// neighbouring vertices
		ArrayList<Edge> edges;
		ArrayList<Face> faces;
		Vec3D normal;
		int[] col;
		float colValue;

		Vertex(Vec3D _pos, Vec3D _normal, int[] _col, Mesh _mesh) {
			super(_pos);
			normal = _normal;
			col = _col;
			colValue = PApplet.parseFloat(col[0] + col[1] + col[2]) / PApplet.parseFloat(255 + 255 + 255);
			normal.normalize();
			mesh = _mesh;
			index = mesh.vertices.size();
			mesh.vertices.add(this);
			vertices = new ArrayList<Vertex>();
			edges = new ArrayList<Edge>();
			faces = new ArrayList<Face>();
		}

		public void computeNormal() {
			normal = new Vec3D();
			for (Face f : faces)
				normal.addSelf(f.normal);
			normal.normalize();
		}

		public Edge edge(Vertex other) {
			// returns the edge that corresponds to the other vertex
			return edges.get(vertices.indexOf(other));
		}

	}

	class Edge {

		int index;
		Mesh mesh;
		ArrayList<Vertex> vertices;
		ArrayList<Face> faces;
		int[] col;
		float colValue;
		int countTrails;

		Edge(int i1, int i2, Mesh _mesh) {
			mesh = _mesh;
			Vertex v1 = mesh.vertices.get(i1);
			Vertex v2 = mesh.vertices.get(i2);
			faces = new ArrayList<Face>();
			index = mesh.edges.size();
			mesh.edges.add(this);
			countTrails = 0;
			vertices = new ArrayList<Vertex>();
			vertices.add(v1);
			vertices.add(v2);
			v1.edges.add(this);
			v2.edges.add(this);
			v1.vertices.add(v2);
			v2.vertices.add(v1);
			col = new int[] { PApplet.parseInt((vertices.get(0).col[0] + vertices.get(1).col[0]) * 0.5f),
					PApplet.parseInt((vertices.get(0).col[1] + vertices.get(1).col[1]) * 0.5f),
					PApplet.parseInt((vertices.get(0).col[2] + vertices.get(1).col[2]) * 0.5f) };
			colValue = col[0] + col[1] + col[2] / (255 + 255 + 255);
		}

		public Vec3D mid() {
			return vertices.get(0).add(vertices.get(1)).scale(0.5f);
		}

		public float length() {
			return vertices.get(0).distanceTo(vertices.get(1));
		}
	}

	class Face {

		int index;
		Mesh mesh;
		ArrayList<Vertex> vertices;
		ArrayList<Edge> edges;
		Vec3D normal;
		int[] col;
		float colValue;

		Face(int[] indices, Mesh _mesh) {
			mesh = _mesh;
			index = mesh.faces.size();
			mesh.faces.add(this);
			vertices = new ArrayList<Vertex>();
			edges = new ArrayList<Edge>();
			for (int v : indices)
				vertices.add(mesh.vertices.get(v));
			for (Vertex v : vertices)
				v.faces.add(this);
			for (int i = 0; i < indices.length; i++) {
				Vertex v1 = vertices.get(i);
				Vertex v2 = vertices.get((i - 1 + indices.length) % indices.length);
				Edge edge = null;
				if (v2.vertices.contains(v1)) {
					for (Edge e : v2.edges) {
						if (e.vertices.contains(v1))
							edge = e;
					}
				} else
					edge = new Edge(v1.index, v2.index, mesh);
				if (edge != null) {
					edges.add(edge);
					edge.faces.add(this);
				} else
					println("ERROR mesh import from txt");
			}
			// normal
			Vec3D e1 = vertices.get(1).sub(vertices.get(0));
			Vec3D e2 = vertices.get(2).sub(vertices.get(0));
			normal = e1.cross(e2).normalize();
			// color
			col = new int[] { 0, 0, 0 };
			for (Vertex v : vertices) {
				col[0] += v.col[0];
				col[1] += v.col[1];
				col[2] += v.col[2];
			}
			col[0] = PApplet.parseInt(col[0] / vertices.size());
			col[1] = PApplet.parseInt(col[1] / vertices.size());
			col[2] = PApplet.parseInt(col[2] / vertices.size());
			colValue = col[0] + col[1] + col[2] / (255 + 255 + 255);
		}
	}

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
			for (Agent a : agents)
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
						a.voxel = voxels.get(centersVoxel.indexOf(voxel.center));// assign the existing voxel to the
																					// agent
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
			for (Agent a : agents)
				new Component(this, a);
		}

		public void buildComponents() {
			// adjusts the solid component if agents have moved
			for (Agent a : agents)
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
				println("Voxel of tetrahedron grid not implemented yet");// tetrahedron grid

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
				if (abs(dx) / abs(dz) < 0.5f)
					posx = 0;// middle voxel
				else {
					if (dx < 0)
						posx = -1;// left voxel
					else
						posx = 1;// right voxel
				}
				if (abs(dy) / abs(dz) < 0.5f)
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
				println("Voxel of tetrahedron grid not implemented yet");
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
				println("Voxel of tetrahedron grid not implemented yet");
			}
		}

		// DISPLAY
		public void display() {
			fill(voxelgrid.col[0], voxelgrid.col[1], voxelgrid.col[2], 100);
			noStroke();
			stroke(voxelgrid.col[0], voxelgrid.col[1], voxelgrid.col[2], 100);
			strokeWeight(1);
			for (ArrayList<Integer> facevertex : facevertices) {
				beginShape();
				for (Integer fv : facevertex)
					vertex(vertices.get(fv).x, vertices.get(fv).y, vertices.get(fv).z);
				endShape();
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

		// CONSTRUCTOR
		Component(Voxelgrid _voxelgrid, Agent a) {
			voxelgrid = _voxelgrid;
			voxelType = voxelgrid.voxelType;
			col = new int[] { voxelgrid.col[0], voxelgrid.col[1], voxelgrid.col[2] };
			voxelcountsReversed = new ArrayList<Integer>(voxelcounts);
			Collections.reverse(voxelcountsReversed);
			// define component direction
			if (voxelType == 0) {// rectangular grid
				if (componentsAligned) {
					if (axisComponentAlignment == 0)
						dir = new Vec3D(voxelgrid.voxelSize[0], 0, 0);
					else if (axisComponentAlignment == 1)
						dir = new Vec3D(0, voxelgrid.voxelSize[1], 0);
					else
						dir = new Vec3D(0, 0, voxelgrid.voxelSize[2]);
				} else {
					int[] axisNormal = new int[3];// largest axis of the normal
					if (componentsInLayers) {
						if (abs(a.normal.x) > abs(a.normal.y))
							axisNormal = new int[] { 2, 0, 1 };
						else
							axisNormal = new int[] { 2, 1, 0 };
					} else {
						if (abs(a.normal.x) > abs(a.normal.y) && abs(a.normal.x) > abs(a.normal.z)) {
							if (abs(a.normal.y) > abs(a.normal.z))
								axisNormal = new int[] { 0, 1, 2 };
							else
								axisNormal = new int[] { 0, 2, 1 };
						} else if (abs(a.normal.y) > abs(a.normal.z)) {
							if (abs(a.normal.x) > abs(a.normal.z))
								axisNormal = new int[] { 1, 0, 2 };
							else
								axisNormal = new int[] { 1, 2, 0 };
						} else {
							if (abs(a.normal.x) > abs(a.normal.y))
								axisNormal = new int[] { 2, 0, 1 };
							else
								axisNormal = new int[] { 2, 1, 0 };
						}
					}
					if (componentsAlternating) {
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
						if (densities.size() > 0) {
							ArrayList<Float> distances = new ArrayList<Float>();
							for (Vec3D d : densities)
								distances.add(a.distanceTo(d));
							float distance = Collections.min(distances);// distance to density points
							int density = Math.min(PApplet.parseInt(distance / densityDistance) + 1, 4);// density:
																										// 1=dense,
																										// 4=sparse
							if (density == 1)
								col = new int[] { 0, 80, 130 };
							else if (density == 2)
								col = new int[] { 50, 120, 170 };
							else if (density == 3)
								col = new int[] { 100, 160, 210 };
							if (positionSide % density != 0 && densityAxes.contains(axis)) {// don't create component
								a.component = null;
								return;
							}
						}
					} else {
						if (componentsInPlane) {
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
				if (abs(a.normal.x) > abs(a.normal.y) ^ componentsInPlane)
					dir = new Vec3D(voxelgrid.voxelSize[0] * 0.5f, 0, 0);
				else
					dir = new Vec3D(0, voxelgrid.voxelSize[1] * 0.5f, 0);
				if (componentsAligned)
					dir = new Vec3D(voxelgrid.voxelSize[0] * 0.5f, 0, 0);// all components are aligned in x direction
			} else {// tetrahedron grid
				println("Component of tetrahedron grid not implemented yet");
			}
			// try to place a component
			placeComponent(a);
			if (voxels.size() >= voxelcounts.get(0)) {// keep component
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
			while (voxelsUp.size() <= voxelcounts.get(voxelcounts.size() - 1)) {
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
			while (voxelsDown.size() <= voxelcounts.get(voxelcounts.size() - 1)) {
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
			if (space < voxelcounts.get(0))
				return;// component has not enough space and can not be placed
			int componentLength = voxelcounts.get(0);
			for (Integer voxelcount : voxelcountsReversed) {
				if (voxelcount < space) {
					componentLength = voxelcount;
					break;
				}
			}
			if (space > voxelcountsReversed.get(0))
				componentLength = voxelcounts.get(0);// shortest component if there aren't neighbours on both sides
			if (voxelsUp.size() > voxelcountsReversed.get(0) && voxelsDown.size() > voxelcountsReversed.get(0)) {
				voxels = new ArrayList<Voxel>(voxelsDown.subList(voxelsDown.size() - PApplet.parseInt(voxelcounts.get(0) * 0.5f), voxelsDown.size()));
				voxels.addAll(voxelsUp.subList(1, (int) Math.ceil(voxelcounts.get(0) * 0.5f) + 1));
				centers = new ArrayList<Vec3D>(centersDown.subList(voxelsDown.size() - PApplet.parseInt(voxelcounts.get(0) * 0.5f), voxelsDown.size()));
				centers.addAll(centersUp.subList(1, (int) Math.ceil(voxelcounts.get(0) * 0.5f) + 1));
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
				println("Component of tetrahedron grid not implemented yet");
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
				if (abs(valuesZ.get(0) - valuesZ.get(1)) < 0.001f) {
					once = valuesZ.get(2);
					twice = valuesZ.get(0);
				}
				if (abs(valuesZ.get(0) - valuesZ.get(2)) < 0.001f) {
					once = valuesZ.get(1);
					twice = valuesZ.get(0);
				}
				if (abs(valuesZ.get(1) - valuesZ.get(2)) < 0.001f) {
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
				println("Component of tetrahedron grid not implemented yet");
			}
		}

		// DISPLAY
		public void display() {
			noStroke();
			fill(col[0], col[1], col[2]);
			stroke(0);
			strokeWeight(1);
			for (ArrayList<Integer> facevertex : facevertices) {
				beginShape();
				for (Integer fv : facevertex)
					vertex(vertices.get(fv).x, vertices.get(fv).y, vertices.get(fv).z);
				vertex(vertices.get(facevertex.get(0)).x, vertices.get(facevertex.get(0)).y,
						vertices.get(facevertex.get(0)).z);
				endShape();
			}
		}

	}

	/**
	 * Cell Growth Simulation
	 *
	 * 180909
	 * 
	 * @author Christoph Klemmt www.orproject.com
	 *
	 *         draw function
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

	public synchronized void draw() {

		if (!loaded) {
			background(0);
		} else {

			background(255);
			pointLight(126, 126, 106, 0, 0, 0);
			pointLight(126, 136, 156, 1280, 720, 720);
			ambientLight(76, 76, 76);

			if (run == true) {
				if (getDisplacements && frameCount % getDisplacementInterval == 0)
					getDisplacements();// read from GH Karamba
				// for(Agent a:agents) a.findNeighbors();
				for (Agent a : agents)
					a.move();
				for (Agent a : agents)
					a.update();
				agents.addAll(agentsNew);
				agentsNew.clear();
				//saveVariables();
			}

			// pushMatrix();//for rotation of the model
			// rotateZ(frameCount*0.01);//for rotation of the model

			// BOX
			if (showEnv) {
				stroke(0, 50);
				strokeWeight(1);
				noFill();
				pushMatrix();
				translate(center.x, center.y, center.z);
				box(envSize.get(1).x - envSize.get(0).x, envSize.get(1).y - envSize.get(0).y,
						envSize.get(1).z - envSize.get(0).z);
				popMatrix();
			}
			
			// AGENTS
			if (showAgents)
				for (Agent a : agents)
					a.display();
			if (showSpheres)
				for (Agent a : agents)
					a.displaySpheres();
			if (showEdges)
				for (Agent a : agents)
					a.displayEdges();
			if (showFaces)
				for (Agent a : agents)
					a.displayFaces();
			if (showNormals)
				for (Agent a : agents)
					a.displayNormals();
			if (showDisplacements)
				for (Agent a : agents)
					a.displayDisplacements();
			if (showVoxels) {
				if (run || voxelgrid.voxels.size() == 0)
					voxelgrid.rebuildVoxels();
				voxelgrid.displayVoxels();
			}
			if (showComponents) {
				if (run || voxelgrid.components.size() == 0)
					voxelgrid.buildComponents();
				voxelgrid.displayComponents();
			}

			// ATTRACTORS
			if (showAttractors)
				for (Attractor a : attractors)
					a.display();

			// MESH
			if (showMesh)
				meshFollow.display(false, true, false);// show vertices, show edges, show faces

			// popMatrix();//for rotation of the model

			if (run && frameCount == 5000) {
				// saveImage();
				// export();
			}
			if (run) {
				print("f:", frameCount, " a:", agents.size());
				if (showComponents)
					print(" component length:", ceil(voxelgrid.componentLength(0.04f)));
				println();
				// saveImage();//make a video
			}
			if (printLength) {
				println("component length:", ceil(voxelgrid.componentLength(0.04f)));
				printLength = false;
			}
			
			debugPrint();
		}
	}

	/**
	 * Cell Growth Simulation
	 *
	 * 180909
	 * 
	 * @author Christoph Klemmt www.orproject.com
	 *
	 *         utility functions
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

// ANGLE BETWEEN TWO LINES WITH A DIRECTION DEFINED BY A NORMAL
	public float angleDir(Vec3D vecA, Vec3D vecB, Vec3D normal) {
		// normal is the reference normal to define if the angle is positive or negative
		float angle = acos(vecA.copy().normalize().dot(vecB.copy().normalize()));
		Vec3D cross = vecA.cross(vecB);
		if (normal.dot(cross) < 0)
			angle = -angle;// Or > 0
		return angle;
	}

// SAVE ALL IMPORTANT VARIABLES TO A FILE
	public void saveVariables() {
		File file = new File(sketchPath("") + nameRun + "/" + nameRun + "-variables.txt");
		if (file.exists())
			return;
		PrintWriter output = createWriter(nameRun + "/" + nameRun + "-variables.txt");
		// agent variables
		output.println("AGENT VARIABLES");
		Field[] fields = Agent.class.getDeclaredFields();
		for (Field f : fields) {
			String value;
			try {
				value = (String) f.get(agents.get(0));
			} catch (IllegalAccessException e) {
				break;
			} catch (ClassCastException e1) {
				try {
					value = f.get(agents.get(0)).toString();
				} catch (IllegalAccessException e2) {
					break;
				}
			}
			output.println(f.getName() + " " + value);
		}
		// attractors
		output.println();
		output.println("ATTRACTORS");
		Field[] fields2 = Attractor.class.getDeclaredFields();
		for (Attractor a : attractors) {
			output.println("ATTRACTOR " + attractors.indexOf(a));
			output.println(a.toString());
			for (Field f : fields2) {
				String value;
				try {
					value = (String) f.get(a);
				} catch (IllegalAccessException e) {
					break;
				} catch (ClassCastException e1) {
					try {
						value = f.get(a).toString();
					} catch (IllegalAccessException e2) {
						break;
					}
				}
				output.println(f.getName() + " " + value);
			}
		}
		// voxelgrid
		output.println();
		output.println("VOXELGRID");
		output.println("voxelType " + voxelgrid.voxelType);
		output.println(
				"voxelSize " + voxelgrid.voxelSize[0] + ", " + voxelgrid.voxelSize[1] + ", " + voxelgrid.voxelSize[2]);
		output.print("voxelcounts ");
		for (Integer vc : voxelcounts)
			output.print(vc + " ");
		output.println();
		output.println("componentsAligned " + componentsAligned);
		output.println("axisComponentAlignment " + axisComponentAlignment);
		output.println("componentsAlternating " + componentsAlternating);
		output.println("componentsInPlane " + componentsInPlane);
		output.println("componentsInLayers " + componentsInLayers);
		output.print("densities ");
		for (Vec3D d : densities)
			output.print(d.toString() + " ");
		output.println();
		output.println("densityDistance " + densityDistance);
		output.print("densityAxes ");
		for (Integer da : densityAxes)
			output.print(da + " ");
		output.println();
		// camera
		output.println();
		output.println("PEASYCAM");
		output.println("lookAt " + cam.getLookAt()[0] + ", " + cam.getLookAt()[1] + ", " + cam.getLookAt()[2]);
		output.println("distance " + cam.getDistance());
		output.println(
				"rotations " + cam.getRotations()[0] + ", " + cam.getRotations()[1] + ", " + cam.getRotations()[2]);
		output.flush();
		output.close();
	}

	
	
	
//PRINT DEBUG VARIABLES
	public void debugPrint() {
		PrintWriter output = createWriter("debug.txt");
		// agent variables
		output.println("AGENT VARIABLES");
		output.println();
		
		output.println("Agents Index:" + agentsIA);
		output.println();
		
		output.println("Neighbor Index:" + agentsIB);
		output.println();
		
		output.println("Agents:" + agentsDisconnected);
		agentSize = agents.size();
		output.println();
		
		output.println(debug4);
		output.println(debug5);
		
		output.println();
		
		output.println(debug1);
		
		output.println();
		
		output.println(debug2);
		output.println(debug3);
		
		output.println();
		
		output.println("f:"+ frameCount + " " + "a:" + agentSize);
		output.flush();
		output.close();
	}
	  

	
//EXPORT KEYS
	public void keyPressed() {

		// PAUSE growth
		if (key == 'p') {
			run = false;
			println("paused. camera lookAt; distance; rotations: ", cam.getLookAt()[0], ",", cam.getLookAt()[1], ",",
					cam.getLookAt()[2], cam.getDistance(), cam.getRotations()[0], ",", cam.getRotations()[1], ",",
					cam.getRotations()[2]);
		}
		if (key == 'r') {
			run = true;
			println("running");
		}
		if (key == 'a')
			showAgents ^= true;
		if (key == 'o')
			showSpheres ^= true;
		if (key == 'e')
			showEdges ^= true;
		if (key == 'f')
			showFaces ^= true;
		if (key == 'q')
			showAttractors ^= true;
		if (key == 'm')
			showMesh ^= true;
		if (key == 'b')
			showEnv ^= true;
		if (key == 'n')
			showNormals ^= true;
		if (key == 'h')
			showNormalsVoxelized ^= true;
		if (key == 'd')
			showDisplacements ^= true;
		if (key == 's')
			voxelize ^= true;
		if (key == 'v') {
			if (showVoxels) {
				for (Agent a : agents)
					a.voxel = null;
				voxelgrid.voxels.clear();
				voxelgrid.centersVoxel.clear();
			}
			showVoxels ^= true;
		}
		if (key == 'c') {
			if (showComponents) {
				for (Agent a : agents)
					a.component = null;
				voxelgrid.components.clear();
				voxelgrid.centersComponent.clear();
			} else
				printLength = true;
			showComponents ^= true;
		}
		// SAVE image
		if (key == 'i') {
			saveImage();
			println("frame", frameCount, "image saved");
		}
		// SAVE geometry
		if (key == 'x') {
			export();
			println("frame", frameCount, "exported");
		}
	}

//IMPORT EXPORT

//SAVE IMAGE
	public void saveImage() {
		saveVariables();
		saveFrame(nameRun + "/" + nameRun + "-f####.png");
	}

//EXPORT GEOMETRY
	public void export() {
		saveVariables();
		// agents
		PrintWriter output = createWriter(nameRun + "/" + nameRun + "-f" + str(frameCount) + "-agents.txt");
		for (Agent a : agents) {
			Vec3D v = new Vec3D(a);
			output.println(v.x + ";" + v.y + ";" + v.z);
		}
		output.flush();
		output.close();
		// components
		if (showComponents) {
			int countV = voxelgrid.components.get(0).vertices.size();
			PrintWriter output2 = createWriter(nameRun + "/" + nameRun + "-f" + str(frameCount) + "-components.ply");
			output2.println("ply");
			output2.println("format ascii 1.0");
			output2.println("comment File exported by Processing Christoph Klemmt");
			output2.println("element vertex " + voxelgrid.components.size() * countV);
			output2.println("property float x");
			output2.println("property float y");
			output2.println("property float z");
			output2.println(
					"element face " + voxelgrid.components.size() * voxelgrid.components.get(0).facevertices.size());
			output2.println("property list uchar uint vertex_index");
			output2.println("end_header");
			for (Component c : voxelgrid.components) {
				for (Vec3D v : c.vertices) {
					output2.println(v.x + " " + v.y + " " + v.z);
				}
			}
			for (int i = 0; i < voxelgrid.components.size(); i++) {
				Component c = voxelgrid.components.get(i);
				for (ArrayList<Integer> fv : c.facevertices) {
					if (fv.size() == 3) {// triangle
						output2.println(fv.size() + " " + (fv.get(0) + (i * countV)) + " " + (fv.get(1) + (i * countV))
								+ " " + (fv.get(2) + (i * countV)));
					} else {// quadrangle
						output2.println(fv.size() + " " + (fv.get(0) + (i * countV)) + " " + (fv.get(1) + (i * countV))
								+ " " + (fv.get(2) + (i * countV)) + " " + (fv.get(3) + (i * countV)));
					}
				}
			}
			output2.flush();
			output2.close();
		}
	}

//IMPORT POINTS
	public ArrayList<Vec3D> ImportPoints(String fileName) {
		ArrayList<Vec3D> collection = new ArrayList<Vec3D>();
		String lines[] = loadStrings(fileName);
		if (lines == null)
			return collection;// file does not exist: return an empty list
		for (String line : lines) {
			float values[] = PApplet.parseFloat(line.split(","));
			collection.add(new Vec3D(values[0], values[1], values[2]));
		}
		return collection;
	}

	
		
	
	
	

	
	
//GET DISPLACEMENTS FROM KARAMBA
	
	public void getDisplacements() {

		System.load(
				"K:/University of Cincinnati/2019 Thesis Year/Thesis Studio 8009/Processing/karamba/Java/64bit/Cantilever/karambaJAVA.dll");

		// Load the license
		License lic = License.Instance();
		lic.loadLicense("license.lic", "public.key");
		if (lic.error_flg()) {
			System.out.println(lic.error_msg());
			System.exit(1);
		}

		
		
		//POINTS
	
		//POINTS OF AGENTS
		for (Agent a : agents) { //agents list
			if (a.neighbors.size() > 0) { //if agents neighbors larger than 0
				agentsDisconnected.add(a); //add agents to new list
			}
		}
		
		//INDEX OF POINTS
		agentIndex = 0;
		neighborIndex = 0;

		for (Agent a : agents) {// beams
			for (Agent n : a.neighbors) { //agents for neighbor
				if (n.neighbors.contains(a) == false || a.index < n.index) { //if agents have less than neighbors
					agentIndex = agents.indexOf(a);
					neighborIndex = agents.indexOf(n);
					agentsIA.add(agentIndex);
					agentsIB.add(neighborIndex);
				}
					/*output.println(agents.indexOf(a) + "_" + agents.indexOf(n)); print index of agents & neighbors**/
			}
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
		
		
		//INDEX TO BEAM (ELEMENTS)
		
		ArrayList<Agent> elementT = new ArrayList<Agent>();
		
		int n1 = 0;
		int n2 = 0;
		int n3 = 0;
		int n4 = 0;
		int n5 = 0;
		int n6 = 0;
		
		
		String element1x;
		
		
		int i1 = agentIndex; //index of agent
		int i2 = neighborIndex; //index of neighbor
		
		element1 = agents.get(i1); // element1 is point from agent
		element2 = agents.get(i2); //element2 is point from neighbor

		element1x = element1.toString();
		
		
		

		
		
		
		
		
		//DEBUG
		
		
		debug1 = element1x;
		debug2 = element2;
		debug3 = element1;
		debug4 = i1;
		debug5 = i2;
		
		
		
		
		
		feb.Node nodes[] = new Node[2];
		nodes[0] = new Node(n1,n2,n3);
		nodes[1] = new Node(n4,n5,n6);
		Beam3D beam = new Beam3D(nodes[0], nodes[1], material, crosec);
	}
	
		
		/*
		
		//SUPPORT
		
		for(Agent a : agents) {
			if(a.z < supportMin) {
				agents = new ArrayList<Agent>(agents.subList(0, supportMin));
			} else {
				
			}
		}
		
		//LOAD
		
		Vec3d f = new Vec3d(0.0,0.0,1.0);
		LoadPoint load = new LoadPoint(f);
	
				
		
		//ASSEMBLE (MODEL)
		
		Model model = new Model();
		for (Node node : nodes) {
			node.swigCMemOwn = false;
			model.add(node);
		}
		
		model.add(material);
		model.add(crosec);
		model.add(beam);
		
		//ANALYZE
		Deform analysis = new Deform(model);
		Response response = new Response(analysis);
		
		//DISPLAY

		ArrayList<Vec3D> displacements = new ArrayList<Vec3D>();
	}
		
**/
	

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "main.karambaTest" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}


























//GRASSHOPPER



//OUTPUT
/*
ArrayList<Agent> agentsDisconnected = new ArrayList<Agent>(); //new array list
PrintWriter output = createWriter("input/Processing_Karamba.txt"); //output address
output.println(agents.size()); //output ArrayList<Agent> size
for (Agent a : agents) {
	if (a.neighbors.size() > 0)
		output.println(a.x + "," + a.y + "," + a.z); //output ArrayList<Agents> points
	else
		agentsDisconnected.add(a);
}
for (Agent a : agents) {// beams
	for (Agent n : a.neighbors) { //agents for neighbor and agents
		if (n.neighbors.contains(a) == false || a.index < n.index) //if agents have less than neighbors
			output.println(agents.indexOf(a) + "_" + agents.indexOf(n)); //print index of agents & neighbors
	}
}
output.flush(); //clear output files
output.close(); //close output task
File file = new File(sketchPath("") + "input/Processing_Karamba.txt"); //output address
long lastModified = file.lastModified(); //last modified information
file = new File(sketchPath("") + "input/Karamba_Processing.txt"); //output address
while (true) {
	if (file.exists()) {
		if (file.lastModified() > lastModified) //if last modified is older than current continue
			break;
	}
	if ((new Date()).getTime() > lastModified + timeoutKaramba * 1000) { //if last modified is older than current TIMEOUT
		println("TIMEOUT for GH Karamba");
		if (frameCount == getDisplacementInterval) {
			println("getDisplacements turned off");
			getDisplacements = false;
		}
		return;
	}
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		println("sleep interrupted");
	}
}

**/



//INPUT
/*
ArrayList<Vec3D> displacements = ImportPoints("input/Karamba_Processing.txt"); //import updated points
if (agents.size() - agentsDisconnected.size() != displacements.size()) {
	println("ERROR getDisplacements(): agents.size()-agentsDisconnected.size():",
			agents.size() - agentsDisconnected.size(), " displacements.size():", displacements.size());
	return;
}
if (agentsDisconnected.size() > 0)
	println("disconnected agents not implemented yet!p");
for (int i = 0; i < agents.size(); i++)
	agents.get(i).displacement = displacements.get(i);
println("displacements imported:", displacements.size()); //display imported size of agents
}


**/