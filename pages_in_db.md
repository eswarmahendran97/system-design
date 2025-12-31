Select query
    Table scan
    Index scan
    Index seek


Pages

1) table != Page
2) table -> multiple pages of each of 8kb
3) these pages are not invidual files
4) these pages will be present in .ibd file.

how search works:
pk:
node - (pk + pageid)

from here we will get pageid. from here will calculate the offset
offset = pageid * 8kb
using this offset we will go to .ibd file and read 8kb of data.

open ibd files
fd = open("table.ibd", "rb")  # read-only
You will be in the beginning of the file.

Using offset, move to that position and read 8kb of data
seek(fd, offset)  # offset in bytes, e.g., 32768

read 8kb of data
page_bytes = read(fd, page_size)


indexed column:
B+ node - (indexed column id + pk)
based on indexed column id we will get pk.
based on pk we will get pageid.
from pageid we will get offset.
from offset we will read 8kb of data from .ibd file.


column with no index:
full table scan. read all pages one by one in .ibd file.
