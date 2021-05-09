ALTER TABLE EnhancedFact ALTER COLUMN id SET DEFAULT uuid_in((md5((random())::text))::cstring);
