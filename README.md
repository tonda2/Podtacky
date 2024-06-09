# Podtácky

### Description
This is an app made for beer coaster collectors who want to keep track of their collection.

It is a database where you can see all your coasters, add new ones, delete old ones or edit information about those already in the database. 
The app allows you to add pictures of each coaster - one of each side. 
Combined with the option to search through the database, these pictures will help you to easily tell whether you already have a given coaster or not.

### Implementation

The app stores all its data locally in a Room database. 
Pictures are stored using their URI pointing to the apps storage. 
Searching is done based on similarity of brewery name or coaster description. In order to save storage space, coaster photos are compressed.