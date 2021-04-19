package natalia.koc.sklepZoologiczny.repositories.kryteria;

import natalia.koc.sklepZoologiczny.domain.Produkt;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.StringUtils;

public class produktSpecyfikacje {
    public static Specification<Produkt> findByPhrase(final String phrase) {
        return (root, query, cb) -> {
            if(StringUtils.isEmpty(phrase) == false){
                String phraseLike = "%"+phrase.toUpperCase() +"%";
                return cb.or(
                        cb.like(cb.upper(root.get("nazwa")), phraseLike),
                        cb.like(cb.upper(root.get("opis")), phraseLike)
                );
            }return null;
        };
    }

    public static Specification<Produkt> findByPriceRange(Float minCena, Float maxCena) {
        return (root, query, cb) -> {
            if(minCena != null && maxCena != null){
                return cb.between(root.get("cena"), minCena, maxCena);
            }else if(minCena != null){
                return cb.greaterThanOrEqualTo(root.get("cena"), minCena);
            }else if(maxCena != null) {
                return cb.lessThanOrEqualTo(root.get("cena"), maxCena);
            }
            return null;
        };
    }
    public static Specification<Produkt> findByOcenaRange(Float minOcena, Float maxOcena) {
        return (root, query, cb) -> {
            if(minOcena != null && maxOcena != null){
                return cb.between(root.get("ocena"), minOcena, maxOcena);
            }else if(minOcena != null){
                return cb.greaterThanOrEqualTo(root.get("ocena"), minOcena);
            }else if(maxOcena != null) {
                return cb.lessThanOrEqualTo(root.get("ocena"), maxOcena);
            }
            return null;
        };
    }
}
