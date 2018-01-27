# SpeedDialUtility
A simple tool that manage the url saved with the old SpeedDial Firefox plugin. 
It create a DB with the recovered urls, download the images representing the 
site and display them.
The input file is "prefs.js" located in Mozilla's profile directory.

## Requirements
* SQLite3 library
* PhantomJS (external program) to grab the thumbnails