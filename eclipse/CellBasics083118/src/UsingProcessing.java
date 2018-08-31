/**
 * Cell Growth Simulation
 *
 * 180510
 * 
 * @author Christoph Klemmt
 * www.orproject.com
 *
 * Simulation of particles in 3D space that multiply and readjust their positions according to
 * intercellular behaviors and external forces. Forces include
 * - drag
 * - unary force
 * - point forces, with different types to vary strength/distance behaviors. used especially towards
 *        different groups of neighbors
 * - spring forces, with different types to vary strength/distance behaviors
 * - attractor forces, with point, line and plane attractors
 * - planarization force, to create local planarity
 * - strata force, to create parallel strata of cells
 * - orthogonal force, to create orthogonal arrangements of cells
 * - mesh force, to react to imported geometry
 * - voxel force, to react to voxel grids
 *
 * Voxelization possibilities in different grid types. Component placement within the voxel grid.
 * Includes import and export of .ply mesh files.
 *
 * Copyright (C) Christoph Klemmt
 * All Rights Reserved.
 * All information contained herein is, and remains the property of 
 * Christoph Klemmt. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from Christoph Klemmt. Unauthorized copying of this file
 * via any medium is strictly prohibited. Proprietary and confidential.
 */

import processing.core.PApplet;
import java.util.*;
import java.util.HashSet;
import java.text.*;
import java.net.*;
import java.lang.reflect.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.processing.*;
import peasy.*;

public class UsingProcessing extends PApplet{

	
}
