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

The next step to implementing Doom-like sectors was to add "2 sided line-defs".
Internally, I decided to call these portals. These are walls of sectors that can
be seen through as if they were not there. This will allow for sectors to be joined 
by an intermediate wall.

<img src="https://github.com/user-attachments/assets/2b16d338-9aa4-497f-9780-a099cf864e15" width="50%">

I then added support for floor height and ceiling height of sectors. This can be
seen working with a few walls here, where the floor and ceiling are both higher
than in previous examples. This is a crucial step in allowing for creative level
layouts as it will allow for adding verticality to maps.

<img src="https://github.com/user-attachments/assets/682c2bfd-569d-479d-affd-4409225cc1d8" width="50%">

