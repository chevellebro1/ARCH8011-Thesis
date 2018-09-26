void setupGUI() {

  gui = new ControlP5(this);
  gui.setColorForeground (color (150));
  gui.setColorBackground (color (200));
  gui.setColorActive (color (100));
  gui.setColorCaptionLabel(color (0));
  gui.setColorValueLabel(color (0));

  int posY = 20;
  int sLength= 100;
  int sHeight= 20;
  int gap = 2;

  //Components:
  Toggle t;
  t = gui.addToggle("showComponents").setPosition(20, posY).setSize(sLength, sHeight*2);
  t.getCaptionLabel().setPaddingX(15); t.getCaptionLabel().setPaddingY(-25);
  posY += sHeight*2+gap;
  gui.addButton("Refresh").setPosition(20, posY).setSize(sLength, sHeight*2);
  posY += sHeight*2+gap;
  guiListType = gui.addDropdownList("voxeltype").setPosition(20, posY).setBarHeight(20).setItemHeight(20);
  guiListType.addItem("rectangular", 0); guiListType.addItem("pyramid", 1); guiListType.addItem("triangular", 2);
  posY += sHeight*4+gap;
  gui.addSlider("voxelsizeX", 0, 10, voxelsizeY, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("voxelsizeY", 0, 10, voxelsizeY, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("voxelsizeZ", 0, 10, voxelsizeZ, 20, posY, sLength, sHeight);
  posY += sHeight+gap;
  gui.addSlider("voxelcountA", 0, 20, voxelcountA, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("voxelcountB", 0, 20, voxelcountB, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("voxelcountC", 0, 20, voxelcountC, 20, posY, sLength, sHeight);
  posY += sHeight+gap;
  guiListOrientation = gui.addDropdownList("orientation").setPosition(20, posY).setBarHeight(20).setItemHeight(20);
  for(int i=0;i<componentOrientations.length;i++) guiListOrientation.addItem(componentOrientations[i], i);
  posY += sHeight*4+gap;
  t = gui.addToggle("showDensities").setPosition(20, posY).setSize(sLength, sHeight);
  t.getCaptionLabel().setPaddingX(15); t.getCaptionLabel().setPaddingY(-15);
  posY += sHeight+gap;
  t = gui.addToggle("multiplyAgents").setPosition(20, posY).setSize(sLength, sHeight);
  t.getCaptionLabel().setPaddingX(15); t.getCaptionLabel().setPaddingY(-15);
  posY += sHeight+gap;
  t = gui.addToggle("sparsifyX").setPosition(20, posY).setSize(sLength, sHeight);
  t.getCaptionLabel().setPaddingX(15); t.getCaptionLabel().setPaddingY(-15);
  posY += sHeight;
  t = gui.addToggle("sparsifyY").setPosition(20, posY).setSize(sLength, sHeight);
  t.getCaptionLabel().setPaddingX(15); t.getCaptionLabel().setPaddingY(-15);
  posY += sHeight;
  t = gui.addToggle("sparsifyZ").setPosition(20, posY).setSize(sLength, sHeight);
  t.getCaptionLabel().setPaddingX(15); t.getCaptionLabel().setPaddingY(-15);
  posY += sHeight+gap;
  gui.addSlider("densityDistance", 0, 200, densityDistance, 20, posY, sLength, sHeight);
  posY += sHeight+gap;
  float range=200;
  gui.addSlider("density1X", -range, range, density1X, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("density1Y", -range, range, density1Y, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("density1Z", -range, range, density1Z, 20, posY, sLength, sHeight);
  posY += sHeight+gap;
  gui.addSlider("density2X", -range, range, density2X, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("density2Y", -range, range, density2Y, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("density2Z", -range, range, density2Z, 20, posY, sLength, sHeight);
  posY += sHeight+gap;
  gui.addSlider("density3X", -range, range, density3X, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("density3Y", -range, range, density3Y, 20, posY, sLength, sHeight);
  posY += sHeight;
  gui.addSlider("density3Z", -range, range, density3Z, 20, posY, sLength, sHeight);
  posY += sHeight+gap;
  
  gui.setAutoDraw(false);
}



void controlEvent(ControlEvent theEvent) {
  if (theEvent.isGroup()) {
    // check if the Event was triggered from a ControlGroup
    println("event from group : "+theEvent.getGroup().getValue()+" from "+theEvent.getGroup());
  } 
  else if (theEvent.isController()) {
    if(theEvent.getController()==guiListType) voxelType = (int) theEvent.getController().getValue();
    else if(theEvent.getController()==guiListOrientation) componentOrientation = componentOrientations[(int) theEvent.getController().getValue()];
  }
}



void drawGUI() {    
  if (gui.isMouseOver()) {
    cam.setActive(false);
  } else {
    cam.setActive(true);
  }
  
  if(showDensities) for(Vec3D d : densities){
    densities = new ArrayList<Vec3D>(Arrays.asList(new Vec3D(density1X,density1Y,density1Z), new Vec3D(density2X,density2Y,density2Z), new Vec3D(density3X,density3Y,density3Z)));
  displayPoint(d,densityDistance, new int[]{255,0,0,10});
  }

  hint(DISABLE_DEPTH_TEST);//let processing stop calculate in 3D
  cam.beginHUD();
  noLights();
  gui.draw();
  cam.endHUD();
  hint(ENABLE_DEPTH_TEST);
}



void Refresh() {
  densities = new ArrayList<Vec3D>(Arrays.asList(new Vec3D(density1X,density1Y,density1Z), new Vec3D(density2X,density2Y,density2Z), new Vec3D(density3X,density3Y,density3Z)));
  densityAxes.clear();
  if(sparsifyX) densityAxes.add(0);
  if(sparsifyY) densityAxes.add(1);
  if(sparsifyZ) densityAxes.add(2);
  voxelcounts = new ArrayList<Integer>(Arrays.asList(voxelcountA,voxelcountB,voxelcountC));//amount of voxels that the component covers. Must be sorted from low to high.
  voxelgrid = new Voxelgrid(voxelType, new float[]{voxelsizeX,voxelsizeY,voxelsizeZ});
  for(Agent a : agents){
    a.component=null;
    a.voxel=null;
  }
  voxelgrid.buildComponents(agents);
  
  //multiply agents
  if(multiplyAgents){
    for(Agent a : agents){
      //add center of faces
      if(a.faces.size()==0) a.findFaces();
      for(ArrayList<Agent> face : a.faces){
        Vec3D mid = new Vec3D();
        Vec3D nor = new Vec3D();
        for(Agent p : face){
          mid.addSelf(p);
          nor.addSelf(p.normal);
        }
        mid.scaleSelf(1/face.size());
        nor.normalize();
        Agent aNew = new Agent(mid, new Vec3D());
        aNew.normal = nor;
        voxelgrid.buildComponent(aNew);
      }
      //add center of edges
      for(Agent n : a.neighbors){
        if(agents.indexOf(a)<agents.indexOf(n)){
          Vec3D mid = a.add(n.sub(a).scale(0.5));
          Vec3D nor = a.normal.add(n.normal);
          nor.normalize();
          Agent aNew = new Agent(mid, new Vec3D());
          aNew.normal = nor;
          voxelgrid.buildComponent(aNew);
        }
      }
    }
  }
  showComponents=true;
  saveVariables();
}
