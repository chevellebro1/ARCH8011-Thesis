class Agent {
  // PARAMETERS
  Vec3D pos; // position = where the agent is
  Vec3D vel; // velocity = speed and direction the agent is travelling
  Vec3D acc;//acceleration
  ArrayList<Vec3D> trail = new ArrayList<Vec3D>(); //previous positions of the agent
  ArrayList<Attractor> attractors = new ArrayList<Attractor>(); //attractors the agent reacts to
  WEVertex vertex;//closest vertex on the mesh to follow
  AAVertex vertexAA;//closest vertex on the mesh to follow
  AAVertex vertexAAF;//closest vertex on the mesh to follow
  int[] col;//color of the agent
  int index;
  int[] voxelIdx;
  Vec3D vtxColourAt = new Vec3D(0, 0, 0);
  //SETTINGS
  float maxVel;
  float maxAcc;
  float range;
  float facCohesion;
  float facAlignment;
  float facSeparation;
  float facCohesionTrail;
  float facAlignmentTrail;
  float facSeparationTrail;
  Vec3D unary;
  float facAttractors;
  float facFollowMesh;
  float[] stepSize;



  // CONSTRUCTOR
  Agent(Vec3D p, Vec3D v) {
    pos = p;
    vel = v;
    acc = new Vec3D();
    col = new int[] {0,0,0};
    index = agents.size();
    vertex = snapMesh(pos,true);
    //trail.add(new Vec3D(pos));
    
    //SETTINGS
    maxAcc = 0.2;
    maxVel = 2;
    range = 10;
    facCohesion = 0.8;
    facAlignment = 0.2;
    facSeparation = 2;
    facCohesionTrail = 0.0;
    facAlignmentTrail = 0.0;
    facSeparationTrail = 0.0;
    facAttractors=10;
    unary = new Vec3D(0.1,0.2,0.5);
    facFollowMesh=0.02;
    stepSize = new float[]{10,10,10};
  }
  
  
  
  // DISPLAY
  void display(){
    //agent
    if(showAgents){
      stroke(col[0],col[1],col[2]);
      //stroke(0,100,200);
      strokeWeight(3);
      point(pos.x, pos.y, pos.z);
    }
    
    //vertex
    stroke(255,0,0);
    strokeWeight(5);
    //point(vertexAA.pos.x,vertexAA.pos.y,vertexAA.pos.z);
    
    //trail
    if(showTrails){
      for(int i=0; i<trail.size()-1; i++){
        stroke(col[0],col[1],col[2],5);
        stroke(0,100,200,20);
        strokeWeight(1);
        /*float offset = 0.25;
        Vec3D addon = new Vec3D(random(-offset,offset),random(-offset,offset),random(-offset,offset));
        Vec3D posA = trail.get(i).add(addon);
        Vec3D posB = trail.get(i+1).add(addon);
        if(posA.z<0) posA.z=0;
        if(posB.z<0) posB.z=0;*/
        Vec3D posA = trail.get(i);
        Vec3D posB = trail.get(i+1);
        posA = new Vec3D(int(posA.x/stepSize[0])*stepSize[0], int(posA.y/stepSize[1])*stepSize[1], int(posA.z/stepSize[2])*stepSize[2]);
        posB = new Vec3D(int(posB.x/stepSize[0])*stepSize[0], int(posB.y/stepSize[1])*stepSize[1], int(posB.z/stepSize[2])*stepSize[2]);
        line(posA.x, posA.y, posA.z, posB.x, posB.y, posB.z);
        /*strokeWeight(3);  
        line(posA.x, posA.y, posA.z, posB.x, posB.y, posB.z);
        strokeWeight(5);  
        line(posA.x, posA.y, posA.z, posB.x, posB.y, posB.z);*/
      }
    }
  }
  
  
  
  // BEHAVIORS
  
  void move(){
    acc.clear();
    //voxelize(voxelField);
    
    //FLOCKING AND STIGMERGY
    if(facCohesion!=0) cohesion(agents, range*2, facCohesion);
    if(facAlignment!=0) alignment(agents, range*3, facAlignment);
    if(facSeparation!=0) separation(agents, range*1, facSeparation);
    if(facCohesionTrail!=0) cohesionTrail(agents, range*1, facCohesionTrail);
    if(facAlignmentTrail!=0) alignmentTrail(agents, range*1, facAlignmentTrail);
    if(facSeparationTrail!=0) separationTrail(agents, range*1, facSeparationTrail);
    if(facAttractors!=0) attractions(attractors, facAttractors);
    acc.addSelf(unary);//unary force
    
    //UPDATE
    acc.limit(maxAcc);
    vel.addSelf(acc);
    vel.limit(maxVel);
    pos.addSelf(vel);
    
    //CONSTRAIN POSITION
    followMesh(facFollowMesh);
    //if(pos.z<250) followMeshAA(strFollowMeshLow,-1);
    //else followMeshAA(strFollowMeshHigh,0);
    //followMeshAA(0,-0.001);
    //followMesh2(0.3,0);
    //followMesh3(myMesh.mesh, voxelField);
    //bounce(0.0);
    //vertexAA = snapMeshAA(pos,false); pos = new Vec3D(vertexAA.pos.x,vertexAA.pos.y,vertexAA.pos.z);//snaps the actual agent onto the mesh
    
    //TRAILS
    trail.add(new Vec3D(pos));//add actual agent position to trail
    /*WEVertex vNext = snapMesh(pos,true);
    if(vertex != vNext){  
      //if(vertex!=null) vertex.edge(vNext).countTrails++;
      vertex = vNext;
      trail.add(new Vec3D(vertex));//add snapped agent position to trail
      if (trail.size()>1000) trail.remove(0);
    }*/
    WEVertex vNext = meshSnap.getClosestVertexToPoint(pos);
    if(vNext!=vertex){
      vertex = vNext;
      //meshSnap.valuesVertex[meshSnap.verticesExt.indexOf((Vertex) vertex)]+=1;
      for(int i=0;i<vertex.edges.size();i++){
        if(vertex.edges.get(i).getOtherEndFor(vertex)==vNext){
          meshSnap.valuesEdge[meshSnap.edgesExt.indexOf(vertex.edges.get(i))]++;
        } 
      }
    }
  }
  


  //COHESION
  void cohesion(ArrayList<Agent> agents, float range, float strength){
    Vec3D vec = new Vec3D();
    float count = 0;
    for(Agent a : agents){
     if(a.pos.distanceToSquared(pos)<range*range && a.pos.distanceToSquared(pos)>0){
       vec.addSelf(a.pos);
       count++;
     }
   }
   if(count > 0){
     vec.scaleSelf(1/count);
     vec.subSelf(pos);
     vec.limit(maxAcc);
     vec.scaleSelf(strength);
   }
   acc.addSelf(vec);  
  }



  // ALIGNMENT
  void alignment(ArrayList<Agent> agents, float range, float strength){
    Vec3D vec = new Vec3D();
    float count = 0;
    for(Agent a : agents){  
      if(a.pos.distanceToSquared(pos)<range*range && a.pos.distanceToSquared(pos)>0){
        vec.addSelf(a.vel);
        count++;
      }
    }
    if(count > 0){
      vec.scaleSelf(1/count);
      vec.limit(maxAcc);
      vec.scaleSelf(strength);
    }
    acc.addSelf(vec);
  }



  //SEPARATION
  void separation(ArrayList<Agent> agents, float range, float strength){
    Vec3D vec = new Vec3D();
    float count = 0; 
   for(Agent a : agents){    
     if(a.pos.distanceToSquared(pos)<range*range && a.pos.distanceToSquared(pos)>0){
       Vec3D dir = pos.sub(a.pos);
       dir.scaleSelf(1/a.pos.distanceTo(pos));
       vec.addSelf(dir);
       count++;
     }
   } 
   if(count > 0){
     vec.scaleSelf(1/count);
     vec.limit(maxAcc);
     vec.scaleSelf(strength);
   }
   acc.addSelf(vec);
  }



  // TRAIL COHESION
  void cohesionTrail(ArrayList<Agent> agents, float range, float strength) { 
    Vec3D vec = new Vec3D();
    int count = 0;
    for(Agent a:agents){
      if(a!=this){
        for(Vec3D trailPoint:a.trail){
          if (pos.distanceToSquared(trailPoint)<range*range && pos.distanceToSquared(trailPoint)>0){
            vec.addSelf(a.pos);
            count++;
          }
        }
      }
    }
    if (count > 0) {
      vec.scaleSelf(1/count); // sum = average position of neighbours
      vec.subSelf(pos);       // sum = vector to the average pos
      vec.scaleSelf(strength);
      vec.limit(maxAcc);
    }
    acc.addSelf(vec);
  }



  // TRAIL ALIGNMENT
  void alignmentTrail(ArrayList<Agent> agents, float range, float strength) { 
    Vec3D vec = new Vec3D();
    float count = 0;
    for(Agent a:agents){
      if(a!=this){
        if(a.trail.size()>1){
          for(Vec3D trailPoint:a.trail){
            if (pos.distanceToSquared(trailPoint)<range*range && pos.distanceToSquared(trailPoint)>0){
              //find the direction of the trail at the closest point
              int index=a.trail.indexOf(trailPoint);
              Vec3D dir;
              if (index==0) dir=trailPoint.sub(a.trail.get(1));
              else dir=trailPoint.sub(a.trail.get(index-1));
              dir.normalize();
              if (vel.angleBetween(dir)>vel.angleBetween(dir.scale(-1))) dir.scaleSelf(-1);// the agent can move along the trail in both directions, so it finds the one better aligned with its current velocity
              vec.addSelf(dir);
              count++;
            }
          }
        }
      }
    }
    if (count > 0) {
      vec.scaleSelf(strength/count);
      vec.limit(maxAcc);
    }
    acc.addSelf(vec);
  }



  // TRAIL SEPARATION
  void separationTrail(ArrayList<Agent> agents, float range, float strength) { 
    for(Agent a:agents){
      if(a!=this){
        Vec3D closest = null;// the closest point on the trail
        for(Vec3D x:a.trail) closest=(closest==null||x.distanceToSquared(pos)<closest.distanceToSquared(pos))?x:closest;
          if (pos.distanceToSquared(closest)<range*range && pos.distanceToSquared(closest)>0){
          Vec3D vec = pos.sub(closest);
          vec.normalize();
          vec.scaleSelf(strength/pos.distanceTo(closest));        
          vec.limit(maxAcc);
          acc.addSelf(vec);
        }
      }
    }
  }
  
  
  
  //ATTRACTORS
  void attractions(ArrayList<Attractor> attractors, float strength){
    for(Attractor a : attractors){
      Vec3D vec = a.pos.sub(pos);
      vec.normalize();
      //vec.scaleSelf(strength * a.strength / pos.distanceTo(a.pos));
      vec.scaleSelf(strength * a.strength);
      vec.limit(maxAcc);
      acc.addSelf(vec);
    }
  }
  


  //SPRING FORCE
  public Vec3D spring(Vec3D target, float restlength, float strength) {
    Vec3D vec = this.pos.sub(target);  //vector from the hook to the agent
    float dist = vec.magnitude();  //distance between the agent and the hook
    Vec3D vecmove = vec.scaleSelf((restlength-dist)/dist);  //spring force formula
    return vecmove.scaleSelf(strength);  //adjust the strength for agents which are closer than the restlength
  }



  //POINT FORCE
  public Vec3D forcePoint(Vec3D target, float strength) {
    Vec3D vec = target.sub(this.pos);  //vector from the hook to the agent
    return vec.normalize().scale(strength);
  }

  
  
  //MESH FOLLOW
  public void followMesh(float factor) {
    WEVertex cv = meshFollow.getClosestVertexToPoint(pos);//closest vertex
    WEVertex cv2 = null;// the second closest vertex among cv's neighbors
    for(WEVertex x:cv.getNeighbors()) cv2=(cv2==null||x.distanceToSquared(pos)<cv2.distanceToSquared(pos))?x:cv2;
    //check how many faces the common edge has, if it is an open edge
    WingedEdge edgeCommon = null;
    for(WingedEdge edge : cv.edges){
      if(cv2.edges.contains(edge)) edgeCommon = edge;
    }
    if(edgeCommon.faces.size()>1){
      //inner edge, pull towards face
      Vec3D normal = cv.normal.normalize();
      float distance = pos.distanceTo(cv);
      float dot = normal.dot(pos.sub(cv));
      normal.scaleSelf(0.5);//----------------------------------------smaller scale factor for the mesh edges to reduce sticking
      if (dot>0) pos.addSelf(normal.scale(-factor * distance)); 
      else pos.addSelf(normal.scale(factor * distance)); 
    }else{
      //outer edge, pull towards edge
      Vec3D vecA = pos.sub(cv);
      Vec3D vecC = cv.sub(cv2);
      float t = -vecA.dot(vecC)/vecC.magSquared();
      Vec3D closest = cv.add(vecC.scale(t));
      Vec3D direction = closest.sub(pos);
      pos.addSelf(direction.scale(factor));
    }
  }

  
  
  //MESH FOLLOW AAMESH
  public void followMeshAA(float factor, float factorColor) {
    AAVertex cv = meshAAFollow.getClosestVertex(pos);//closest vertex
    if(factor!=0){
      AAVertex cv2 = null;// the second closest vertex among cv's neighbors
      for(AAVertex x:cv.vertices) cv2=(cv2==null||x.pos.distanceToSquared(pos)<cv2.pos.distanceToSquared(pos))?x:cv2;
      //check how many faces the common edge has, if it is an open edge
      AAEdge edgeCommon = null;
      for(AAEdge edge : cv.edges){
        if(edge.vertices.contains(cv2)) edgeCommon = edge;
      }
      if(edgeCommon!=null){
        if(edgeCommon.faces.size()>1){
          //inner edge, pull towards face
          Vec3D normal = cv.nor;
          float distance = pos.distanceTo(cv.pos);
          float dot = normal.dot(pos.sub(cv.pos));
          normal.scaleSelf(0.5);//----------------------------------------smaller scale factor for the mesh edges to reduce sticking
          if (dot>0) pos.addSelf(normal.scale(-factor * distance)); 
          else pos.addSelf(normal.scale(factor * distance)); 
        }else{
          //outer edge, pull towards edge
          Vec3D vecA = pos.sub(cv.pos);
          Vec3D vecC = cv.pos.sub(cv2.pos);
          float t = -vecA.dot(vecC)/vecC.magSquared();
          Vec3D closest = cv.pos.add(vecC.scale(t));
          Vec3D direction = closest.sub(pos);
          pos.addSelf(direction.scale(factor));
        }
      }else println("ERROR followMeshAA");
    }
    if(factorColor!=0.0){
      //react to mesh color
      Vec3D posL = pos.add(vel.getRotatedAroundAxis(cv.nor,0.2*PI) .scale(5));
      Vec3D posR = pos.add(vel.getRotatedAroundAxis(cv.nor,-0.2*PI) .scale(5));
      float colL=meshAAFollow.getClosestVertex(posL).colValue;
      float colR=meshAAFollow.getClosestVertex(posR).colValue;
      if(colL!=colR){
        float factorLR = colL-colR;
        vel = vel.getRotatedAroundAxis(cv.nor,0.1*factorColor*factorLR*PI);
      }
    }
  }
 

  
  WEVertex snapMesh(Vec3D pos, boolean includeCurrent){
    //initially pull the agent onto the mesh
    if(vertex==null){
      return meshSnap.getClosestVertexToPoint(pos);
    }
    //find the neighbouring vertices, move the agent to the one that is in line with its velocity
    WEVertex vNext = null;// the new vertex to which the agent will move
    List<WEVertex> neighbors = vertex.getNeighbors();
    if(includeCurrent==true) neighbors.add(vertex);
    for(WEVertex neighbor:neighbors){
      vNext=(vNext==null || pos.distanceToSquared(neighbor) < pos.distanceToSquared(vNext) )?neighbor:vNext;      
      //vNext=(vNext==null||acc.dot(neighbor.sub(vertex).normalize())>acc.dot(vNext.sub(vertex).normalize()))?neighbor:vNext;
    }
    return vNext;
  }
  
  
  
  AAVertex snapMeshAA(Vec3D pos, boolean includeCurrent){
    //initially pull the agent onto the mesh
    if(vertexAA==null){
      return meshAASnap.getClosestVertex(pos);
    }
    //find the neighbouring vertices, move the agent to the one that is in line with its velocity
    AAVertex vNext = null;// the new vertex to which the agent will move
    ArrayList<AAVertex> neighbors = new ArrayList<AAVertex>();
    for(AAVertex v: vertexAA.vertices) neighbors.add(v);
    if(includeCurrent==true) neighbors.add(vertexAA);
    for(AAVertex neighbor:neighbors){
      vNext=(vNext==null || pos.distanceToSquared(neighbor.pos) < pos.distanceToSquared(vNext.pos) )?neighbor:vNext;      
    }
    return vNext;
  }

  
  
  //BOUNDARY SITUATIONS
  void applyTorusSpace() {
    if (pos.x > envSize.get(1).x) pos.x = envSize.get(0).x;
    if (pos.x < envSize.get(0).x) pos.x = envSize.get(1).x;
    if (pos.y > envSize.get(1).y) pos.x = envSize.get(0).y;
    if (pos.y < envSize.get(0).y) pos.x = envSize.get(1).y;
    if (pos.z > envSize.get(1).z) pos.x = envSize.get(0).z;
    if (pos.z < envSize.get(0).z) pos.x = envSize.get(1).z;
  }
  
  
  
  void bounce(float strength){
    if(pos.x < envSize.get(0).x){
      pos.x = envSize.get(0).x;
      vel.x *= -strength;
    }
    if(pos.x > envSize.get(1).x){
      pos.x = envSize.get(1).x;
      vel.x *= -strength;
    }
    if(pos.y < envSize.get(0).y){
      pos.y = envSize.get(0).y;
      vel.y *= -strength;
    }
    if(pos.y > envSize.get(1).y){
      pos.y = envSize.get(1).y;
      vel.y *= -strength;
    }
    if(pos.z < envSize.get(0).z){
      pos.z = envSize.get(0).z;
      vel.z *= -strength;
    }
    if(pos.z > envSize.get(1).z){
      pos.z = envSize.get(1).z;
      vel.z *= -strength;
    }
  }


}