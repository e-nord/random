'''
As a henchman on Commander Lambda's space station, you're expected to be resourceful,
smart, and a quick thinker. It's not easy building a doomsday device and capturing
bunnies at the same time, after all! In order to make sure that everyone working for
her is sufficiently quick-witted, Commander Lambda has installed new flooring outside
the henchman dormitories. It looks like a chessboard, and every morning and evening
you have to solve a new movement puzzle in order to cross the floor. That would be fine
if you got to be the rook or the queen, but instead, you have to be the knight. Worse,
if you take too much time solving the puzzle, you get "volunteered" as a test subject
for the LAMBCHOP doomsday device!

To help yourself get to and from your bunk every day, write a function called answer(src, dest)
which takes in two parameters: the source square, on which you start, and the destination square,
which is where you need to land to solve the puzzle.  The function should return an integer
representing the smallest number of moves it will take for you to travel from the source square
to the destination square using a chess knight's moves (that is, two squares in any direction
immediately followed by one square perpendicular to that direction, or vice versa, in an "L" shape).
Both the source and destination squares will be an integer between 0 and 63, inclusive, and are
numbered like the example chessboard below:

-------------------------
| 0| 1| 2| 3| 4| 5| 6| 7|
-------------------------
| 8| 9|10|11|12|13|14|15|
-------------------------
|16|17|18|19|20|21|22|23|
-------------------------
|24|25|26|27|28|29|30|31|
-------------------------
|32|33|34|35|36|37|38|39|
-------------------------
|40|41|42|43|44|45|46|47|
-------------------------
|48|49|50|51|52|53|54|55|
-------------------------
|56|57|58|59|60|61|62|63|
-------------------------
Test cases
==========

Inputs:
    (int) src = 19
    (int) dest = 36
Output:
    (int) 1

Inputs:
    (int) src = 0
    (int) dest = 1
Output:
    (int) 3
'''

from collections import deque
from collections import namedtuple
from math import ceil
from math import log

PositionNode = namedtuple('PositionNode', 'col row level')

def answer(src, dest):
    board = create_board(8)
    start_position = get_start_position_for_value(src, board)
    return find_least_moves(start_position, dest, board)

def create_board(size):
    return [[y * size + x for x in range(size)] for y in range(size)]

def get_start_position_for_value(value, board):
    for row in range(len(board)):
        for col in range(len(board[row])):
            if board[col][row] == value:
                return PositionNode(col, row, 0)
    raise(AssertionError)

def is_valid_board_position(position, board):
    board_size = len(board)
    return (position.row >= 0 and position.row < board_size) and (position.col >= 0 and position.col < board_size)

def get_next_positions(position, board):
    return [
        PositionNode(position.col + 2, position.row + 1, position.level + 1),
        PositionNode(position.col + 2, position.row - 1, position.level + 1),
        PositionNode(position.col - 2, position.row + 1, position.level + 1),
        PositionNode(position.col - 2, position.row - 1, position.level + 1),
        PositionNode(position.col + 1, position.row + 2, position.level + 1),
        PositionNode(position.col + 1, position.row - 2, position.level + 1),
        PositionNode(position.col - 1, position.row + 2, position.level + 1),
        PositionNode(position.col - 1, position.row - 2, position.level + 1)
    ]

def find_least_moves(position, target_value, board):
    visited_positions = set()
    positions_to_visit = deque()
    positions_to_visit.append(position)

    while positions_to_visit:
        current_position = positions_to_visit.popleft()
        
        #filter out positions if they are invalid or we've visited already
        if (is_valid_board_position(current_position, board) 
            and not current_position in visited_positions):
            
            position_value = board[current_position.col][current_position.row]
            visited_positions.add(current_position)

            if position_value == target_value: #we've arrived
                #tree level is equivalent to the number of moves taken
                num_moves = current_position.level
                return num_moves

            #get all possible 'knight' moves from this position
            next_positions = get_next_positions(current_position, board)
            
            for next_position in next_positions:
                 positions_to_visit.append(next_position)
    
    raise(AssertionError)

if __name__ == '__main__':
    assert answer(12, 12) == 0
    assert answer(19, 2) == 1
    assert answer(19, 36) == 1
    assert answer(59, 61) == 2
    assert answer(0, 1) == 3
    assert answer(62, 20) == 3
    
