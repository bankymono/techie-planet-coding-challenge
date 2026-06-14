-- Correct options:

-- This returns the largest salary that is less than the overall maximum salary.
SELECT MAX(salary)
FROM emp
WHERE salary < (SELECT MAX(salary) FROM emp);

-- This gets the two highest distinct salaries, then returns the lower one.
SELECT salary
FROM (
    SELECT DISTINCT salary
    FROM emp
    ORDER BY salary DESC
    LIMIT 2
) AS salaries
ORDER BY salary
LIMIT 1;

-- The other options are not reliable because they can return the highest salary
-- again when multiple employees have the same highest salary.
