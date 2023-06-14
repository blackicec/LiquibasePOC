DROP VIEW IF EXISTS LiquibaseBuiltMe.v_orders_with_domain
CREATE VIEW LiquibaseBuiltMe.v_orders_with_domain
AS
    SELECT
        o.id,
        o.firstName,
        o.lastName,
        o.email
    FROM orders o
    WHERE o.email like '%gmail.com'