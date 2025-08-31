
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