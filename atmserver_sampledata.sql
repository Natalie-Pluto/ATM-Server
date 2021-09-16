BEGIN TRANSACTION;

SET search_path to 'atmserver';

INSERT INTO Card VALUES (11111, 0000, '2020-01-13', '2025-01-13', false, 10000, false);

COMMIT;
