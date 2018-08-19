# PushPull
A puzzle app for Android smartphones, as seen on the Google Play Store (pending).

## Basics

The app is a puzzle game in which the user attempts to cover all of the targets on the screen with gray blocks.  The user can 
swipe to move around "player" blocks, which in turn have the ability to move the gray blocks.  The user can swipe left to move 
all of the player blocks left, up to move them all up, right to move right, and down to move down.  Depending on the color of
a player block, it can interact with the gray blocks in different ways.  Red blocks have the ability to push a single gray block,
blue blocks have the ability to pull a single gray block, and yellow blocks can move all connecting gray blocks alongside them
(where here "connecting" means connected through any chain of adjacent blocks with no blank spaces between them).  There are 
also walls, which cannot be moved through or pushed, and transformers, which change the color of player blocks.

## Specifications

This app is designed for Android smartphones with API level 19 (Kit Kat) and above.

