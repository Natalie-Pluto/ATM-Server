BEGIN TRANSACTION;

SET search_path to 'atmserver';

INSERT INTO Card VALUES (11111, 0000, '2020-01-13', '2025-01-13', false, 10000, false); -- required for DB testing
INSERT INTO Card VALUES (10000, 1234, '2021-06-10', '2025-06-10', false, 10000, false);
INSERT INTO Card VALUES (12345, 1111, '2019-01-12', '2021-01-12', false, 10000, false);
INSERT INTO Card VALUES (23445, 5678, '2017-04-12', '2021-04-12', false, 10000, false);
INSERT INTO Card VALUES (33567, 1122, '2020-09-16', '2025-09-16', false, 10000, false);
INSERT INTO Card VALUES (99002, 3344, '2021-12-13', '2024-12-13', false, 10000, true);
INSERT INTO Card VALUES (45667, 5566, '2019-08-15', '2022-08-15', false, 10000, false);
INSERT INTO Card VALUES (94850, 7788, '2020-06-13', '2025-06-13', false, 10000, false);
INSERT INTO Card VALUES (85303, 9900, '2020-10-20', '2025-10-20', false, 10000, false);
INSERT INTO Card VALUES (93503, 0011, '2018-03-13', '2022-03-13', false, 10000, false);
INSERT INTO Card VALUES (10485, 4455, '2020-11-23', '2023-11-23', false, 10000, false);
INSERT INTO Card VALUES (99901,0000,'2020-11-23','2021-1-20',true,10000,true) -- required for DB testing
INSERT INTO Card VALUES (99902,0000,'2020-11-23','2021-1-20',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99903,0000,'2020-11-23','2026-1-21',true,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99904,0000,'2020-11-23','2026-1-21',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99905,0000,'2020-11-23','2021-1-20',true,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99906,0000,'2020-11-23','2021-1-20',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99907,0000,'2020-11-23','2026-1-21',true,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99908,0000,'2020-11-23','2026-1-21',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99901,0000,'2020-11-23','2021-1-20',true,10000,false) -- required for DB testing
INSERT INTO Card VALUES (99902,0000,'2020-11-23','2021-1-20',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99903,0000,'2020-11-23','2026-1-21',true,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99904,0000,'2020-11-23','2026-1-21',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99905,0000,'2020-11-23','2021-1-20',true,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99906,0000,'2020-11-23','2021-1-20',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99907,0000,'2020-11-23','2026-1-21',true,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99908,0000,'2020-11-23','2026-1-21',false,10000,false); -- required for DB testing


COMMIT;

