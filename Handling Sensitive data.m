Handling Sensitive data

Dont key encryption/decryption in secrets.yaml
who ever have access for kube can access it. Its is stored as just Base64 format

Secure -> use Application - managed encryption
Keep secrets in Valuts. When application starts fetch the secret and use for sensitive column

Cons:
you cannot use function or procedure for the particular column


How to make to read friendly - I know the value, fetch the column based on that

add one more column which stores hashId of the sensitive data. While saving use a Hashing algo and save it
While writing a query to read based on that value, use same Hashing algo to create a hash for that value and have a condition to check in that table

SELECT * FROM Passenger 
WHERE phone_hash = SHA256('+1 555-1234');