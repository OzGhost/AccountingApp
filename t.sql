SELECT time, code, COUNT(code) as amount
FROM (
	SELECT  TO_CHAR(
			TRUNC(Ngay, 'MON')
			,'yyyy-mm-dd') as time,
		SUBSTR(SoPhieu,0,3) as code
	FROM PhieuThuChi
	WHERE SUBSTR(SoPhieu,0,3) IN ('NHH','TTM') AND 
		(Ngay BETWEEN
			TO_DATE('2015-01-01', 'yyyy-mm-dd')
		AND TO_DATE('2015-09-01', 'yyyy-mm-dd'))
	) 
GROUP BY time, code
ORDER BY code, time