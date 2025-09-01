
-- =========================================================
-- TABLA: tbl_franquicia
-- Representa una franquicia, la entidad principal del modelo
-- =========================================================
CREATE TABLE tbl_franquicia (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) UNIQUE NOT NULL
);

-- =========================================================
-- TABLA: tbl_sucursal
-- Representa las sucursales asociadas a una franquicia
-- Relación: Muchas sucursales pertenecen a una franquicia
-- =========================================================
CREATE TABLE tbl_sucursal (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    id_franquicia BIGINT,
    CONSTRAINT fk_franquicia FOREIGN KEY (id_franquicia) REFERENCES tbl_franquicia(id)
);

-- =========================================================
-- TABLA: tbl_producto
-- Representa los productos ofertados en una sucursal
-- Relación: Muchos productos pertenecen a una sucursal
-- =========================================================
CREATE TABLE tbl_producto (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    id_sucursal BIGINT,
    CONSTRAINT fk_sucursal FOREIGN KEY (id_sucursal) REFERENCES tbl_sucursal(id)
);


-- ==============================
-- DATOS DE PRUEBA PARA tbl_franquicia
-- ==============================
INSERT INTO tbl_franquicia (nombre) VALUES
('Franquicia El Buen Sabor'),
('Franquicia TechStore'),
('Franquicia Salud Vida'),
('Franquicia Mundo Gamer'),
('Franquicia Hogar Feliz');

-- ==============================
-- DATOS DE PRUEBA PARA tbl_sucursal
-- Cada franquicia tendrá varias sucursales
-- ==============================
INSERT INTO tbl_sucursal (nombre, id_franquicia) VALUES
-- Sucursales Buen Sabor
('Sucursal Buen Sabor - Centro', 1),
('Sucursal Buen Sabor - Norte', 1),
('Sucursal Buen Sabor - Sur', 1),

-- Sucursales TechStore
('Sucursal TechStore - Bogotá', 2),
('Sucursal TechStore - Medellín', 2),
('Sucursal TechStore - Cali', 2),

-- Sucursales Salud Vida
('Sucursal Salud Vida - Bogotá', 3),
('Sucursal Salud Vida - Bucaramanga', 3),

-- Sucursales Mundo Gamer
('Sucursal Mundo Gamer - Centro', 4),
('Sucursal Mundo Gamer - Online', 4),

-- Sucursales Hogar Feliz
('Sucursal Hogar Feliz - Norte', 5),
('Sucursal Hogar Feliz - Occidente', 5);

-- ==============================
-- DATOS DE PRUEBA PARA tbl_producto
-- ==============================
INSERT INTO tbl_producto (nombre, stock, id_sucursal) VALUES
-- Productos Buen Sabor
('Hamburguesa Clásica', 50, 1),
('Perro Caliente Especial', 30, 1),
('Pizza Familiar', 20, 2),
('Ensalada César', 15, 2),
('Pollo Asado Entero', 25, 3),
('Sopa del Día', 40, 3),

-- Productos TechStore
('Laptop Pro 15"', 10, 4),
('Smartphone X200', 25, 4),
('Auriculares Bluetooth', 40, 5),
('Monitor 27" 4K', 15, 5),
('Teclado Mecánico RGB', 30, 6),
('Mouse Gamer Óptico', 50, 6),

-- Productos Salud Vida
('Multivitamínico A-Z', 60, 7),
('Proteína Whey 2kg', 20, 7),
('Colágeno Hidrolizado', 35, 8),
('Omega 3 Cápsulas', 50, 8),

-- Productos Mundo Gamer
('Consola X-Series', 15, 9),
('Control Inalámbrico', 40, 9),
('Videojuego RPG Fantasía', 30, 10),
('Tarjeta Prepago Gamer 50USD', 100, 10),

-- Productos Hogar Feliz
('Sofá 3 Puestos', 10, 11),
('Juego de Vajilla 16pzas', 25, 11),
('Cama Doble Premium', 5, 12),
('Mesa de Comedor 6 puestos', 8, 12);
