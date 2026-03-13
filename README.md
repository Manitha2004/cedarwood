# Cedar Woods Accommodation System

A JavaFX desktop application developed for the **Object-Oriented Systems Development** group assignment.

This system is designed for the fictional **Cedar Woods** accommodation site and allows staff to manage:
- accommodation details by area
- guest check-in
- guest check-out
- cleaning status updates
- breakfast and cleaning statistics

## Project Overview

The application manages four accommodation areas:
- Hilltop
- Meadow
- Woodland
- Lakeview

It also supports four accommodation types:
- Cabin
- Yurt
- Geodesic Dome
- Airstream

The software was developed using **Java** and **JavaFX** and follows an object-oriented design with:
- model classes
- controller/business logic
- JavaFX user interface

## Features

- View accommodation details by area
- View accommodation availability for a selected date
- Check guests in
- Check guests out
- Update room cleaning status
- View breakfast statistics for the selected date
- View current rooms requiring cleaning
- Display a live clock in the GUI
- Packaged Windows version with application icon

## Main Classes

### Core Model Classes
- `Accommodation`
- `Booking`
- `Guest`

### Accommodation Subclasses
- `Cabin`
- `Yurt`
- `GeodesicDome`
- `Airstream`

### Enums
- `AccommodationType`
- `AreaType`
- `CleaningStatus`
- `OccupancyStatus`

### Controller / Business Logic
- `CedarWoodSystem`

### JavaFX Main Application
- `HelloApplication`

## Technologies Used

- Java
- JavaFX
- NetBeans
- GitHub

## System Design

The project uses object-oriented principles such as:
- abstraction
- inheritance
- encapsulation
- separation of concerns

`Accommodation` is an abstract parent class that contains shared properties and behaviour for all accommodation types.

`CedarWoodSystem` acts as the main controller and handles:
- accommodation management
- booking logic
- date-based availability
- check-in and check-out operations
- cleaning status updates
- statistics

## Notes About Data Storage

This project does **not** use a database.

All data is handled **in memory while the application is running**.  
This was a deliberate design decision because the assignment brief stated that **data persistence was not required**.

## Project Structure

- `src/cedarwood` – Java source code
- `nbproject` – NetBeans project configuration
- `build.xml` – NetBeans build file
- `manifest.mf` – project manifest
- `README.md` – project overview and usage notes

## How to Run the Project

### In NetBeans
1. Open the project in NetBeans
2. Make sure JavaFX is configured correctly
3. Run `HelloApplication`

### As a Windows Application
The project was also packaged as a Windows application using `jpackage`.  
Run the generated installer and then open the installed application from the desktop shortcut or Start menu.

## GitHub Repository Purpose

This repository is used to store and share:
- Java source code
- NetBeans project structure
- project progress and updates

## Team Project

This project was completed as part of a group assignment.  
GitHub was used to upload and share the Java project files.

## Author

Developed as part of the **Cedar Woods Accommodation System** group coursework project.
