
1) Find nth highest salary

this can find only second
select max(salary) from employee e where e.salary < (select max(e1.salary) from employee e1)

4th highest
select * from employee e1 3 = (select count(distinct e2.salary) from employee e2 where  e2.salary >= e1.salary);

2) Duplicate record

to find distinct
select distinct * from employee e

non-distinct

select * from employee where email in (select email from employee group by email having count(*) > 1)

3) each users latest trans

select u.* from Transaction t join User u on t.userId = u.userId where t.txn_time = (select max(t1.txn_time) from Transaction t1 where t1.userId = u.userId);

4) customer who made trans in last 30 days (not before)

select DISTINCT userId from Transaction t where t.txn_time >= CURRENT_DATE - INTERVAL '30 day'and userId not in (
    select DISTINCT userId from Transaction t where t.txn_time < CURRENT_DATE - INTERVAL '30 day'
)

5) Higest salary per department

SELECT department, MAX(salary) AS max_salary
FROM employee
GROUP BY department;


6) Employees Earning More Than Department Average

select * from employee e  where salary > (select avg(salary) from employee e1 where e.dprt = e1.dprt)