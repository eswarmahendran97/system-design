Short url
Usecase to consider:
    url should too short
    10M/day

Assuming I will refresh in 10 days
So I need ~100M unique hashcodes 

I can use 4 type
    Integer(10)
    Alphabet(52)
    AlphaNumeric(62)
    ASCII(127)

I can use ascii for url to be too short... but 66 chars are not reserved for URL (A-Za-z0-9_-.~)
which more or less equal to AlphaNumeric

so choosing AlphaNumeric
calculation for uniqueness based on digits
4 digits -> 62^4 = ~14M
5 digits -> 62^5 = ~916M

we can also use Alphabet
5 digits -> 52^5 = ~800M

There can be possiblity for collision(which we cannot neglect)

how to get new hashcode for diff url?
we can use salt -> juz an extra counter

psuedo

salt = 0;
while(true){
    hashCode = generateHash(url+slat);
    if(db.get(hashcode) != null){
        salt++;
    } else{ 
        break;
    }
}

so, 
5 digit is fine for this usecase

edge case to consider: one you reach 100M request,
attempts_per_success:
total available possibilities = 916M
poll occupied = 100M = 10%

so, 10% of time u will see collision.
we can increase the digit to decrease this.
