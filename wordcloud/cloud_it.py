#!/usr/bin/env python
from os import path
import numpy as np
from PIL import Image
from wordcloud import WordCloud, STOPWORDS

maskfile = "mask.png"
infile = "dump.txt"
outfile = "cloud2.png"
stopwordsfile = "stopwords.txt"

stopwords = list(STOPWORDS)
stopwords.extend([line.rstrip('\n') for line in open(stopwordsfile)])
d = path.dirname(__file__)
m = np.array(Image.open(path.join(d, maskfile)))
text = open(path.join(d, infile)).read()
wc = WordCloud(max_font_size=80, min_font_size=6, max_words=4000, mask=m, scale=2, stopwords=stopwords)
cloud = wc.generate(text)
cloud.to_file(path.join(d, outfile))
