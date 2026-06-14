-- LEFT JOIN keeps all rows from the table on the left side of the join.
-- In this example, every row in games is returned. If a game's city is not
-- found in the city table, the country columns will be NULL.

SELECT g.yr, g.city, c.country
FROM games g
LEFT JOIN city c ON g.city = c.name
ORDER BY g.yr;

-- Expected result for the sample data:
-- yr    city     country
-- 2004  Athens   Greece
-- 2008  Beijing  China
-- 2012  London   UK
-- 2032  NULL     NULL


-- RIGHT JOIN keeps all rows from the table on the right side of the join.
-- In this example, every row in city is returned. If a city has no matching
-- row in games, the game year will be NULL.

SELECT g.yr, c.name AS city, c.country
FROM games g
RIGHT JOIN city c ON g.city = c.name
ORDER BY c.name;

-- Expected result for the sample data:
-- yr    city     country
-- 2004  Athens   Greece
-- 2008  Beijing  China
-- 2012  London   UK
-- NULL  Sydney   Australia

-- A RIGHT JOIN can usually be rewritten as a LEFT JOIN by swapping the table order.
-- In day-to-day SQL, LEFT JOIN is more commonly used because it is easier to read.
