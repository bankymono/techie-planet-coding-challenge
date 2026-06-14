-- Select the country where the games took place each year.
-- games.city stores the host city, while city.name maps that city to a country.

SELECT g.yr, c.country
FROM games g
JOIN city c ON g.city = c.name
ORDER BY g.yr;

-- Expected result for the sample data:
-- yr    country
-- 1896  Greece
-- 1948  UK
-- 2004  Greece
-- 2008  China
-- 2012  UK
