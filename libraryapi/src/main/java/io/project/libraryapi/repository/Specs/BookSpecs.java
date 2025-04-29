package io.project.libraryapi.repository.Specs;

import io.project.libraryapi.model.Book;
import io.project.libraryapi.model.BookGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    // criteriaBuilder.equal(root.get("isbn"), isbn) = "where isbn = :isbn"
    public static Specification<Book> isbnEqual(String isbn) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> titleLike(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Book> genreEqual(BookGenre genre) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("genre"), genre);
    }

    public static Specification<Book> yearReleaseEqual(Integer yearRelease) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(
                    criteriaBuilder.function("YEAR", Integer.class, root.get("dateRelease")),
                    yearRelease);
        };
    }

    public static Specification<Book> nameAuthorLike(String name){
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> joinAuthor = root.join("author", JoinType.INNER);
            return criteriaBuilder.like( criteriaBuilder.upper(joinAuthor.get("name")), name.toUpperCase() + "%" );

//            return cb.like( cb.upper(root.get("author").get("name")), name.toUpperCase() + "%" );
        };
    }
}
