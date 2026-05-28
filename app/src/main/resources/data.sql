INSERT INTO nodes (id, code, name, type, description, parent_id) VALUES
-- 1. RAÍZ ÚNICA DEL SISTEMA
(1, '0', 'Plan de Cuentas General', 'SISTEMA', 'Raíz única del catálogo contable', NULL),

-- 2. LOS TRES GRANDES GRUPOS APUNTAN A LA RAÍZ REAL (parent_id = 1)
(2, '1', 'Activo', 'GRUPO', 'Bienes y derechos de la empresa', 1),
(3, '1.1', 'Activo Corriente', 'GRUPO', 'Activos que se convierten en efectivo a corto plazo', 2),
(4, '1.1.1', 'Caja', 'CUENTA', 'Dinero disponible en caja', 3),
(5, '1.1.2', 'Bancos', 'CUENTA', 'Dinero disponible en cuentas bancarias', 3),
(6, '1.2', 'Activo No Corriente', 'GRUPO', 'Activos de largo plazo', 2),
(7, '1.2.1', 'Mobiliario y Equipo', 'CUENTA', 'Muebles y equipo de oficina', 6),

(8, '2', 'Pasivo', 'GRUPO', 'Obligaciones de la empresa', 1),
(9, '2.1', 'Pasivo Corriente', 'GRUPO', 'Deudas a corto plazo', 8),
(10, '2.1.1', 'Proveedores', 'CUENTA', 'Deudas con proveedores', 9),
(11, '2.1.2', 'Cuentas por Pagar', 'CUENTA', 'Obligaciones pendientes de pago', 9),

(12, '3', 'Patrimonio', 'GRUPO', 'Capital y resultados acumulados', 1),
(13, '3.1', 'Capital', 'GRUPO', 'Aportes de los propietarios', 12),
(14, '3.1.1', 'Capital Social', 'CUENTA', 'Capital aportado por los socios', 13)
    ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('nodes', 'id'), COALESCE(MAX(id), 1)) FROM nodes;