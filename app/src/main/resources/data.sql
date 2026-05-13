INSERT INTO nodes (id, code, name, type, description, parent_id) VALUES
(1, '1', 'Activo', 'GRUPO', 'Bienes y derechos de la empresa', NULL),
(2, '1.1', 'Activo Corriente', 'GRUPO', 'Activos que se convierten en efectivo a corto plazo', 1),
(3, '1.1.1', 'Caja', 'CUENTA', 'Dinero disponible en caja', 2),
(4, '1.1.2', 'Bancos', 'CUENTA', 'Dinero disponible en cuentas bancarias', 2),
(5, '1.2', 'Activo No Corriente', 'GRUPO', 'Activos de largo plazo', 1),
(6, '1.2.1', 'Mobiliario y Equipo', 'CUENTA', 'Muebles y equipo de oficina', 5),

(7, '2', 'Pasivo', 'GRUPO', 'Obligaciones de la empresa', NULL),
(8, '2.1', 'Pasivo Corriente', 'GRUPO', 'Deudas a corto plazo', 7),
(9, '2.1.1', 'Proveedores', 'CUENTA', 'Deudas con proveedores', 8),
(10, '2.1.2', 'Cuentas por Pagar', 'CUENTA', 'Obligaciones pendientes de pago', 8),

(11, '3', 'Patrimonio', 'GRUPO', 'Capital y resultados acumulados', NULL),
(12, '3.1', 'Capital', 'GRUPO', 'Aportes de los propietarios', 11),
(13, '3.1.1', 'Capital Social', 'CUENTA', 'Capital aportado por los socios', 12)
ON CONFLICT (id) DO NOTHING;