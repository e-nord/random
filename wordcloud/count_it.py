from future_builtins import map  # Only on Python 2

from collections import Counter
from itertools import chain

filename = 'dump.txt'

def count_word_dist(file_path):
    with open(file_path) as f:
        return Counter(chain.from_iterable(map(str.split, f)))

c = count_word_dist(filename)
print(c['Sean'])
print(c['Ethan'])
print(c['John'])
print(c['Jack'])
#for w in c.most_common(20):
#    print(w)
