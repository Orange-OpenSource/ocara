ALTER TABLE RulsetDetails ADD COLUMN authorName VARCHAR;
ALTER TABLE RulsetDetails ADD COLUMN ruleCategoryName VARCHAR;

UPDATE RulsetDetails SET authorName = "Gzan", ruleCategoryName = "Accessibilité des locaux" WHERE reference LIKE "RS1";
UPDATE RulsetDetails SET authorName = "Gzan", ruleCategoryName = "Accessibilité des Circulations" WHERE reference LIKE "RS2";