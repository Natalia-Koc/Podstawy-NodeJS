package natalia.koc.sklepZoologiczny.repositories;

import natalia.koc.sklepZoologiczny.controllers.filters.Filter;
import natalia.koc.sklepZoologiczny.domain.Kategoria;
import natalia.koc.sklepZoologiczny.domain.Produkt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProduktRepozytorium extends JpaRepository<Produkt, Integer>, JpaSpecificationExecutor<Produkt> {
    Page<Produkt> findProduktsByNazwaIgnoreCaseContainingOrAndOpisIgnoreCaseContaining(
            String phraseNazwa, String phraseOpis, Pageable pageable);

    Page<Produkt> findProduktUsingNameQuery(
            String phrase,
            Float minCena,
            Float maxCena,
            Float minOcena,
            Float maxOcena,
            List<Kategoria> kategoria,
            Pageable pageable
    );

    @Query("SELECT b FROM Produkt b WHERE" +
            "(" +
            ":phrase is null OR :phrase = '' OR " +
            "upper(b.nazwa) LIKE upper(:phrase) OR " +
            "upper(b.opis) LIKE upper(:phrase)" +
            " ) " +
            "AND (:minCena is null OR :minCena <= b.cena)" +
            "AND (:maxCena is null OR :maxCena >= b.cena)" +
            "AND (:minOcena is null OR :minOcena <= b.ocena)" +
            "AND (:maxOcena is null OR :maxOcena >= b.ocena)" +
            "AND (COALESCE(:kategoria) is null OR EXISTS (SELECT g FROM b.kategoria g WHERE g in :kategoria))" +
            ""
    )
    Page<Produkt> findProduktUsingQuery(
            String phrase,
            Float minCena,
            Float maxCena,
            Float minOcena,
            Float maxOcena,
            List<Kategoria> kategoria,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "kategoria")
    @Query("SELECT b FROM Produkt b WHERE" +
            "(" +
            ":#{#filter.phraseLIKE} = '' OR " +
            "upper(b.nazwa) LIKE upper(:#{#filter.phraseLIKE}) OR " +
            "upper(b.opis) LIKE upper(:#{#filter.phraseLIKE})" +
            " ) " +
            "AND (:#{#filter.minCena} is null OR :#{#filter.minCena} <= b.cena)" +
            "AND (:#{#filter.maxCena} is null OR :#{#filter.maxCena} >= b.cena)" +
            "AND (:#{#filter.minOcena} is null OR :#{#filter.minOcena} <= b.ocena)" +
            "AND (:#{#filter.maxOcena} is null OR :#{#filter.maxOcena} >= b.ocena)" +
            "AND (:#{#filter.kategoriaEmpty} = true OR EXISTS (SELECT g FROM b.kategoria g WHERE g in :#{#filter.kategoria}))" +
            ""
    )
    Page<Produkt> findProduktUsingSpEL(
            Filter filter,
            Pageable pageable
    );
}
