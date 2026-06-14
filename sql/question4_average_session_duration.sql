-- Select each user with more than one session and return their average duration.

SELECT userId, AVG(duration) AS AverageDuration
FROM sessions
GROUP BY userId
HAVING COUNT(*) > 1;

-- Expected result for the sample data:
-- UserId  AverageDuration
-- 1       12
