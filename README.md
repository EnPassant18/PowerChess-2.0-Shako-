# cs0320 Term Project 2018

Power chess approved by adehovit, consider including a randomized start variant as well

**Team Members:** Brad Bentz, Daniel Kostovetsky, Dain Woods, Katie Normandin

**REPL Commands**


- `new game` start a new game
- `print board` print current board state
- `print on` turn on printing of board after each command (default: on)
- `print off` turn off printing of board after each command
- `move [loc] [loc]` or `move [loc] -> [loc]` move piece from first location to second location (format [a-h][1-8])
- `promote [piece name]` promotes pawn to specified pieec name. May type full piece name or character representation (e.g. 'queen' or 'q').
- `spawn [loc] [rarity]` spawn power object of specified rarity at specific location
- `give [powerAction Name] [loc]` give the active player the specified Power Actions as if it were captured at the given location.
- `power [index]` select a power action after capturing a power object
- `action [input]` execute a previously selected power action. The format of input may vary (e.g. [a-h][1-8] end location for an additional move or [piece name] for get a captured piece back).
- `quit` to exit the program completely

**System Tests**
NOTE: some currently do not work as powerups spawn randomly. Will need to implement
a way around that

castling_error.test - Checks that castling through pieces as well as castling a piece that has already moved does not work and throws an error.

castling_long_black.test - Checks that the black king can castle properly on its long side.

castling_long_white.test - Checks that the white king can castle properly on its long side.

castling_short_black.test - Checks that the black king can castle properly on its short side.

castling_short_white.test - Checks that the white king can castle properly on its short side.

en_passant.test - Checks that both sides can capture a pawn through en passant.

**Team Strengths and Weaknesses:**

Daniel: My strengths are functional programming, data structures and algorithms. I studies these extensively in CS19. My weaknesses are object-oriented design and UI design, since I have relatively little experience in these areas. I never took an object-oriented programming course before. Advanced chess knowledge/experience.

Katie: Relevant courses I have taken include data structures and algorithms. If we pursue an AI element, concepts from deep learning may be relevant. I also have strong organizational skills. My weaknesses include UI/UX since I have no experience beyond what we've learned in 032 (and I'm currently taking cs132).

Brad: Strengths are in Java, object oriented programming, general software design and algorithms. My biggest weakness is in front-end development.

Dain: My strengths are data structures, algorithms, and game design. My weaknesses would be the front-end development and making my code very abstract.


**Project Idea(s):**

(1) PowerChess: Chess with Powerups that temporarily change the rules of the game (for instance, allowing multiple moves by one player, allowing pawns to move backwards, resurrecting dead pieces, etc.). Powerups would randomly spawn on the board and players would be able to capture them the same way they can capture opponent's pieces. Accomodating such rule changes would make for an interesting design challenge. We would start by ensuring a person could play against an opponent using the same computer, but we could also add an AI to play against and a feature for playing with friends over the Internet.

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 16)_

**4-Way Checkpoint:** _(Schedule for on or before April 23)_

**Adversary Checkpoint:** _(Schedule once you are assigned an adversary TA)_

## Project Specs, Mockup, and Design (March 16)
_A link to your specifications, mockup, and your design presentation will go here!_

## How to Build and Run
_A necessary part of any README!_
