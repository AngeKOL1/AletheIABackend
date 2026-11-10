package com.example.aletheia.pretest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.aletheia.pretest.dto.NoticiaFeedDTO;
import com.example.aletheia.pretest.dto.NoticiaResponseDTO;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.models.enums.Forma;

public interface NoticiaRepo extends GenericRepo<Noticia, Integer> {

    /** Feed del participante: solo las columnas que se muestran (sin categoría, tema ni texto OCR). */
    @Query("""
            SELECT new com.example.aletheia.pretest.dto.NoticiaFeedDTO(
                n.id, n.titular, n.epigrafe, n.subtitulo, n.entradilla, n.cuerpo, n.cierre, n.img, n.fuente)
            FROM Noticia n
            WHERE n.forma = :forma
            ORDER BY n.id
            """)
    List<NoticiaFeedDTO> findFeedByForma(@Param("forma") Forma forma);

    /** Listado para investigadores: una sola consulta con el JOIN a la categoría, sin cuerpo ni OCR. */
    @Query("""
            SELECT new com.example.aletheia.pretest.dto.NoticiaResponseDTO(
                n.id, n.titular, n.img, n.fuente, n.tema, n.forma, c.nombre)
            FROM Noticia n JOIN n.categoriaVeredicto c
            ORDER BY n.id
            """)
    List<NoticiaResponseDTO> findAllResumen();

    @Query("SELECT n.forma FROM Noticia n WHERE n.id = :id")
    Optional<Forma> findFormaById(@Param("id") Integer id);

    long countByForma(Forma forma);
}
