# Primeval
A doom-style game engine. (WIP)

## Features
- 2.5D rendering
- Custom level format
- Many more to come...

## The Engine
This engine is intended as a base framework for creating games with the same 
style as classic Doom. It will include features for editing levels, textures,
game mechanics, and for making custom changes to the underlying framework. This 
engine will be a loooooong project and is currently in very early development.

## TODO
- Map editor
- Drawing floors and ceilings
- Collisions
- Sprites
- Interactable objects
- Level selection / dynamic loading

## Dev Log
### Humble beginnings: 
The first problem I decided to tackle is rendering walls. This naive version
works in a world made of only axis-aligned cubes. The height of each vertical
column of pixels is determined using the wall's distance from the camera. This
achieves a (almost) 3D look similar to Doom. (I also added colours for fun)

<img src="https://github.com/user-attachments/assets/d19aead0-61d5-4378-a34d-63217041e875" width="50%">

Once this was working, I added "sectors" (rooms with a custom shape). I did
this by drawing walls between each corner (specified as a 2D coordinate) to
create the illusion of a strangely shaped room. This approach allows for rooms 
of any shape and size. At this stage, I also created my plain-text level format
"pmap" which could be loaded into the engine as either a world made of cubes
"CUBEWORLD" or a world made of sectors "SECTORWORLD".

<img src="https://github.com/user-attachments/assets/bedb2c4f-6bef-4538-a473-6bfb66c49620" width="50%">

<img src="https://github.com/user-attachments/assets/13b76fc5-4da0-45f6-97d7-d76d1a4aa6d3" width="50%">

