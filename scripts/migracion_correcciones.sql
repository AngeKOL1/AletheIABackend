-- =====================================================================
-- Migración para bases de datos YA existentes (ddl-auto=update no
-- modifica columnas existentes). En una BD nueva no hace falta.
-- Ejecutar una vez, con la aplicación detenida:
--   psql -h localhost -p 5433 -U <usuario> -d <bd> -f scripts/migracion_correcciones.sql
-- =====================================================================

BEGIN;

-- 1. hora_fin debe aceptar NULL (sesión abierta).
ALTER TABLE sesion_test ALTER COLUMN hora_fin DROP NOT NULL;

-- 2. cuerpo / texto_ocr_bruto: si se crearon como oid (por @Lob), pasar a TEXT
--    conservando el contenido.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name = 'noticia' AND column_name = 'cuerpo' AND data_type = 'oid') THEN
        ALTER TABLE noticia ALTER COLUMN cuerpo TYPE text
            USING convert_from(lo_get(cuerpo), 'UTF8');
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name = 'noticia' AND column_name = 'texto_ocr_bruto' AND data_type = 'oid') THEN
        ALTER TABLE noticia ALTER COLUMN texto_ocr_bruto TYPE text
            USING convert_from(lo_get(texto_ocr_bruto), 'UTF8');
    END IF;
END $$;

-- 3. img admite rutas/URLs largas.
ALTER TABLE noticia ALTER COLUMN img TYPE varchar(2048);

COMMIT;

-- =====================================================================
-- Verificaciones (solo lectura). Si alguna devuelve filas, corrígelas
-- antes de arrancar la aplicación.
-- =====================================================================

-- a) Duplicados que impedirían crear las restricciones únicas nuevas
--    (Hibernate las crea al arrancar y falla en silencio si hay duplicados).
SELECT participante_id, noticia_id, fase, count(*)
FROM prediccion_manual GROUP BY 1, 2, 3 HAVING count(*) > 1;

SELECT participante_id, fase, count(*)
FROM sesion_test GROUP BY 1, 2 HAVING count(*) > 1;

SELECT sesion_test_id, item_cuestionario_id, count(*)
FROM respuesta_cuestionario GROUP BY 1, 2 HAVING count(*) > 1;

-- b) Correos repetidos sin distinguir mayúsculas (el login ahora ignora mayúsculas).
SELECT lower(correo), count(*) FROM participante GROUP BY 1 HAVING count(*) > 1;

-- c) Valores que no coinciden con los enums nuevos (la app no podría leer esas filas).
SELECT id, genero FROM participante
WHERE genero IS NOT NULL AND genero NOT IN ('Femenino', 'Masculino', 'Otro', 'Prefiero no decirlo');

SELECT id, nivel_educativo FROM participante
WHERE nivel_educativo IS NOT NULL AND nivel_educativo NOT IN
      ('Secundaria completa', 'Técnico superior', 'Universitario en curso', 'Universitario completo', 'Posgrado');

SELECT id, frecuencia_consumo_noticias FROM participante
WHERE frecuencia_consumo_noticias IS NOT NULL AND frecuencia_consumo_noticias NOT IN
      ('Varias veces al día', 'Una vez al día', 'Varias veces por semana', 'Una vez por semana', 'Casi nunca');

SELECT id, tipo_respuesta, dimension FROM item_cuestionario
WHERE tipo_respuesta NOT IN ('likert_5', 'texto')
   OR dimension NOT IN ('TASA_ACIERTO', 'TIEMPO_VERIFICACION', 'CLASIFICACION_NOTICIAS',
                        'USABILIDAD', 'FUNCIONALIDAD', 'RENDIMIENTO');
