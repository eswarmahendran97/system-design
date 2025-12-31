# Remember

Deque q = new LinkedList<>();


String s = "HelloWorld";
System.out.println(s.substring(5)); //World
int startIndex=0;
int endIndex=4;
System.out.println(s.substring(startIndex, endIndex)); //Hell
System.out.println(s.substring(startIndex, endIndex+1)); //Hello

String compareTo
Compare lexicographically
"1101".compareTo("0011"); //1
"0011".compareTo("1011"); //-1
"0011".compareTo("0011"); //0

PriorityQueue<Integer> pq = new PriorityQueue<>(); // ascending
PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder()); // descending

// compare integer but on division (so use double)
PriorityQueue<Item> pq = new PriorityQueue<>((a, b) -> Double.compare(b.val * 1.0 / b.wt, a.val * 1.0 / a.wt));

Arrays.sort(arr, (a, b) -> a[0]-b[0])

// char[] to string
String.valueOf(charArray).trim()

// reverse
String s = "hello";
s = new StringBuilder(s).reverse().toString();

//ascii
A-Z = 65 - 90
a-z = 97 - 122

Character.isLetter(ch)
Character.isDigit(ch)


## Revise

## array
Tower of Hanoi
    FAT
    ATF

Merge
    m-l+1
    r-m

### Sliding window
Largest subarray of 0's and 1's
    no reminder needed

Longest Repeating Character Replacement
    use if.. dont use while

### backtracking
String permutation
    use char[] instead of string (substring)
    String.valueOf(charArray).trim()

Combination Sum II
    use for loop instead in take notake
    if (i > j && input[i] == input[i - 1]) continue;

IpAddress
    remember parts

Kth Permutation
    remember ind1, ind2 and reverse

Largest number in K swaps
Partition array to k subsets
Sum-string


### dp
claimbing stairs - no order
    -(i-1)/2
House robber
    dp[0] = nums[0]
    dp[1] = max(nums[0], nums[1])
    dp[i] = max(dp[i-1], nums[i] + dp[i-2]);

k coins
    dp[i][j][k+1]

Longest Increasing Subsequence
    foor loop, dp, max

Longest Bitonic subsequence
    if (dp[i] != 1 && dp2[i] != 1)
                max = Math.max(max, dp[i] + dp2[i] - 1);

Largest Divisible Subset
    use dp, prev(for track the path)

NumberOfLIS
    use occ[]
    [1,2,2,5] -> LIS -> [1,2,2,3] -> occ[i] = occ[i] + occ[j] = 1 + 1 = 2. so to reach 3 there is 2 paths

LongestPalindromicSubString
    use int[2] -> min and max
    For
        checkPal(i, i+1)
        checkPal(i, i+2)

MinimumOperation
    dp[i][0] = i
    dp[0][j] = j

    if equal
    dp[i][j] = dp[i-1][j-1];

    else
    dp[i][j] = 1+Math.min(add, Math.min(rm, replace));


distinct occurance
    top - less size
    left - more size

    dp[i][0]=1; -> more side

    if(equal) c + u
        dp[i][j] = dp[i-1][j-1] + dp[i-1][j];
    else u
        dp[i][j] = dp[i-1][j];


Wildcard Pattern Matching
    dp[0][0] = 1;

    //for first char
    for (int j = 1; j <= m; j++) {
        if (pat.charAt(j - 1) == '*') {
            dp[0][j] = dp[0][j - 1]; // row 0 not row i
        } else {
            break;
        }
    }

    if(equal || ?) 
        dp[i][j] = dp[i-1][j-1];

    else if (*)
    dp[i][j] = Math.max(dp[i][j-1], dp[i-1][j]);


### graph

shortest path of unweighted graph
    if you have start and if u use queue then its already sortest path
    make visited while adding to queue not after pooling. to avoid same thing multiple times

Topological sort
    recursion based



