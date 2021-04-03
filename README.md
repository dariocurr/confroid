# Confroid

## Usage
Confroid is a simple but functional application to manage and store your personal configurations, accessible from third-part applications via service or api.

For further information see [here](https://github.com/dariocurr/confroid/blob/main/doc/User%20Guide.pdf)

## Introduction
Our project consists of two applications:
- *Confroid*:
is an app that manages files, more specifically
has the task to export or import configuration files from the device or from a server.
- *Shopping*:
is an app where to insert data relating to purchases, 
has the task of creating those configuration files through entered personal data by an user.

### Strong points
1. the use of API
2. high security due to the fact that a malicious user cannot modify the data of other users thanks to token authentication

### Weak points and bugs
1. the frontend is very basic
2. to view a configuration that is too long, you must first click on the "edit" button (Confroid)
3. import from server is a simulation due to the fact that the server is not connected to the web (Confroid)

### Tasks repartition
- Dario Curreri: Token authentication, API
- Domenico Di Fina: Storage, Import from the device, Export to the device
- Emanuele Domingo: Front-end Confroid, Shopping app
- Salvatore A. Gristina: Import from the Server, Export to the server
- Arnaud Vanspauwen: Geolocalization, Annotations, Testing, Fastlane directory
