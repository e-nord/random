'''
Write a function called answer(s) that, given a non-empty
string less than 200 characters in length describing the
sequence of M&Ms, returns the maximum number of equal parts
that can be cut from the cake without leaving any leftovers.

Inputs:
    (string) s = "abccbaabccba"
Output:
    (int) 2

Inputs:
    (string) s = "abcabcabcabc"
Output:
    (int) 4
'''

import random

EXAMPLE1 = 'abccbaabccba'
EXAMPLE2 = 'abcabcabcabc'

def answer(s):
    input_len = len(s)
    answer = 1;
    for num_slices in range(1, 200):
        if input_len % num_slices == 0:
            slice_length = input_len / num_slices
            slices = slice_cake(s, slice_length)
            if slices_equal(slices):
                answer = num_slices
    return answer

def slices_equal(parts):
    for part in parts:
        if part != parts[0]:
            return False
    return True

def slice_cake(s, pieces):
     return [''.join(x) for x in zip(*[list(s[z::pieces]) for z in range(pieces)])]

def generate(total_length, sequence_length):
    num_subs = total_length / sequence_length
    assert num_subs > 0
    sub = ''.join([random.choice('abcdefghijklmnopqrstuvwxyz') for _ in range(sequence_length)])
    stuff = ''.join(sub * (num_subs))
    assert len(stuff) > 0 and len(stuff) <= 200 
    return stuff
    
if __name__ == '__main__':
    assert answer(EXAMPLE1) == 2
    assert answer(EXAMPLE2) == 4
    assert answer(generate(1, 1)) == 1
    assert answer(generate(200, 200)) == 1
    assert answer(generate(12, 6)) == 2
    assert answer(generate(50, 6)) == 8
    assert answer(generate(59, 13)) == 4
    assert answer(generate(52, 13)) == 4
    assert answer(generate(52, 1)) == 52

