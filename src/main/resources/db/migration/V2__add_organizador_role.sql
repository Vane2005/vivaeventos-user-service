ALTER TABLE user_entity DROP CONSTRAINT IF EXISTS user_entity_role_check;
ALTER TABLE user_entity ADD CONSTRAINT user_entity_role_check
    CHECK (role::text = ANY (ARRAY[
    'CLIENTE'::character varying,
    'GERENTE'::character varying,
    'ORGANIZADOR'::character varying
    ]::text[]));