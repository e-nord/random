'''
Bunny HQ has secretly taken control of two of the galaxy's more obscure numbers stations,
and will use them to broadcast lists of numbers. They've given you a numerical key,
and their messages will be encrypted within the first sequence of numbers that adds up to
that key within any given list of numbers. 

Given a non-empty list of positive integers l and a target positive integer t,
write a function answer(l, t) which verifies if there is at least one consecutive
sequence of positive integers within the list l (i.e. a contiguous sub-list) that can be
summed up to the given target positive integer t (the key) and returns the lexicographically
smallest list containing the smallest start and end indexes where this sequence can be found,
or returns the array [-1, -1] in the case that there is no such sequence (to throw off Lambda's spies,
not all number broadcasts will contain a coded message).

For example, given the broadcast list l as [4, 3, 5, 7, 8] and the key t as 12,
the function answer(l, t) would return the list [0, 2] because the list l contains
the sub-list [4, 3, 5] starting at index 0 and ending at index 2, for which 4 + 3 + 5 = 12,
even though there is a shorter sequence that happens later in the list (5 + 7).
On the other hand, given the list l as [1, 2, 3, 4] and the key t as 15,
the function answer(l, t) would return [-1, -1] because there is no sub-list of list l that
can be summed up to the given target value t = 15.

To help you identify the coded broadcasts, Bunny HQ has agreed to the following standards: 

- Each list l will contain at least 1 element but never more than 100.
- Each element of l will be between 1 and 100.
- t will be a positive integer, not exceeding 250.
- The first element of the list l has index 0. 
- For the list returned by answer(l, t), the start index must be equal or smaller than the end index. 

Remember, to throw off Lambda's spies, Bunny HQ might include more than one contiguous sublist of a
number broadcast that can be summed up to the key. You know that the message will always be hidden
in the first sublist that sums up to the key, so answer(l, t) should only return that sublist.

Inputs:
    (int list) l = [4, 3, 10, 2, 8]
    (int) t = 12
Output:
    (int list) [2, 3]

Inputs:
    (int list) l = [1, 2, 3, 4]
    (int) t = 15
Output:
    (int list) [-1, -1]
'''

def answer(l, t):
    print l
    print t
    for start_idx in range(len(l)):
        print 'Starting at l[{}]'.format(start_idx)
        total = 0
        for end_idx, val in enumerate(l[start_idx:]):
            total += val
            print 'Adding l[{}] -> {} - total={}'.format(end_idx, val, total)
            if total == t:
                print 'Returning [{}, {}]'.format(start_idx, start_idx + end_idx)
                return [start_idx, start_idx + end_idx]
            elif total > t:
                print 'Stopping sequence'
                break
    return [-1, -1]

if __name__ == '__main__':
    assert answer([4, 3, 10, 2, 8], 12) == [2, 3]
    assert answer([1, 2, 3, 4], 15) == [-1, -1]
