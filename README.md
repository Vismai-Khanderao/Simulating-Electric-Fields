# Simulating Electric Field

## Analysing the electric field at a point due to charged objects

**Features provided:**
- Add multiple objects to 3D coordinate system
- Superpose Electric Fields at a point due to multiple objects
- Calculate Electric Field numerically

**Uniformly charged objects available:**
- *Point*
- *Rod*
- *Plane*
- *Conducting Sphere*

## User Stories:

- As a user, I want to add a charged Point to the System
- As a user, I want to add a charged Conducting Sphere to the System
- As a user, I want to add a charged Rod to the System
- As a user, I want to add a charged Plane to the System
- As a user, I want to add objects to the System
- As a user, I want to remove objects from the System
- As a user, I want to find the value of the Electric Field at the origin in the System
- As a user, I want to save the state of the system
- As a user, I want to load the state of a previous system

## Instructions for grader

- Choose a charged object from the drop down list at the top.

- When adding a Point, no need to fill Coordinate 2, 3, Number of calculation points, and Radius
- When adding a Rod, no need to fill Coordinate 3, and Radius
    - Coordinate points are
        - 1
        - |
        - 2
- When adding a Plane, no need to fill Radius
    - Coordinate points are (this is to avoid an unintentional "twisted" plane)
         - 2---4
         - | &nbsp;&nbsp;&nbsp; |
         - 1---3
- When adding a Conducting Sphere, no need to fill Coordinate 2, 3, and Number of calculation points 
- Make sure all the required fields have values of the right data type
- Click on the add button to add the shape to the field system and to the left hand panel, be sure to zoom in/out if 
your shape is not on screen 
- The XY, YZ, XZ change the view of the field system with the first letter on the button being the horizontal axis and 
second being the vertical
- Save System and Load System saves and reloads the system respectively from a text file held at \data 
- Calculate the electric field due to all charged objects in the system using the Calculate electric field button, the 
values should be displayed at the bottom of the right hand panel (note: this only updates when pressed, not when an
object is added or removed to avoid unnecessary computation)
- The zoom in/out buttons are to adjust the view of the objects in the system on the left
- Objects are added serially to an arraylist, so to remove an object type its index number (0-based), enter index number
of object and click on the remove button to remove the shape from the field system and screen
- Rod and Plane electric fields are calculated using numerical integration therefore the higher number of calculation 
points, more accurate the approximation is and also the longer it takes 
- Adding objects too far or large will "spill" onto the right side of the gui, zoom out to fix this
- Objects may overlap in the wrong order, look at objects from another angle to see actual locations

## Phase 4: Task 2

- ChargedObject is an abstract class that is extended by ConductingSphere, Plane, Point, and Rod. 
- The ChargedObject class has chargeDensity, coordinate, and numPoints fields that are protected as all shapes have
those fields.
- The ChargedObject class also has abstract and protected methods to be used by the classes extending it.

## Phase 4: Task 3

- There was poor cohesion in AppController as the shape to be added to the gui was being made in AppController, the
shape was first recognised and then the respective shape method was called within the same class,
to fix this, an abstract method was made in ChargedObject and all shapes which extended it have their own implementation
which returns the respective shape.
- A translateH and translateV protected method was also made in ChargedObject as only when shapes are added, the 
coordinates need to be translated from center origin to having the origin at the top left.