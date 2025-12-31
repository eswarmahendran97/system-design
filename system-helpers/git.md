# Conflict resolution in GIT

## Conflicts
It occurs only when you try to merge.
When ever you try to merge, it will check remote tip is same as immediate tip back of you
Eg:
If it is same
Remote: a -- b
local: a -- b -- c(ours)
git will merge the commit. Remote tip will be your latest commit

else
Remote: a -- b -- d
local: a -- b -- d
             \ 
              c
Now you are behind the commit. 
Now the file which you are going to merge is compared with base version and lastest version using 3 way merge.
if
our version and their version have change is same line conflict will be shown
else
auto merge happens

## Senarios merge can happen
git pull(git fetch + git merge)
git merge(manually)
when rising Merge Request (done by github for PR)

## How conflict found - Internal working
Basically you need to compare your version (which is in your local) with the Their Version (latest tip in remote) considering Base commit pointer
Base commit pointer - Lowest common ancestor of Your version and Their Version
Eg: b is base here
a -- b -- d
      \ 
        c
why we need to cosider base?
I need to compare my change with old version then only I know what line is changed. I cannot compare with someother version to find changed lines

eg:
Base:  
Hi
Hello

Theirs: 
Hey
Hello1

Ours:  
Hi
Hello2

Result:
Hey
<<<<<< ours
Hello2
=======
Hello1
>>>>>> theirs

Step 1: Diff against base
Theirs vs Base: changed line 1 → Hi → Hey and line 1 → Hello → Hello1.
Ours vs Base: changed line 2 → Hello → Hello2.

Step 2: Apply changes
Line 1: only theirs touched it → result = Hey.
Line 2: both ours and theirs touched it → conflict

How changes are found?
Using Longest common Subsequence aglorithm


## Rebase vs Merge

Rebase - Commit the our commit newly after the lastest tip

Original:
A --- B --- C
       \
        D --- E

result:
A --- B --- C --- D' --- E'

Step 1: Apply (B→D) diff on C → D' (Base - B, Ours - D, Theirs - C)
Step 2: Apply (D→E) diff on D' → E' (Base - D, Ours - E, Theirs - D')

note: Here Lowest common ancestor is not found. Parent before our commit is taken as base


Merge - Combine two branches preserving history

Original:
main:    A --- B --- C
                \
                D --- E
Step 1: Find the base (lowest common ancestor)
Step 2: Compute diff between our latest tip and their latest tip
Step 3: Apply diffs (3-way merge)
Step 4: Create a merge commit

Result:
    A --- B --- C
            \     \
             D --- E
                    \
                     M   (merge commit)

## Fast forward merge condtion:

Initial
main:    A --- B
feature:           \
                     C --- D


After main gets commit C (someone merged/committed on main)

main:    A --- B --- C
feature:              \
                       D

Now If you try to pull (fetch + merge) -> fast forward will happen
main : C
feature : D


## Merging branch having no common ancestor
Recursive Merge (when there are multiple merge bases)

## Other algorithms
Both are version of LCS
Myers diff - by default (for all merge to find dif)
Patience - optional


