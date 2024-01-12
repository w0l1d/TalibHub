package org.ilisi.backend.specs;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.ilisi.backend.model.Profile;
import org.ilisi.backend.model.Student;
import org.ilisi.backend.model.Student_;
import org.springframework.data.jpa.domain.Specification;

import static org.ilisi.backend.model.Student_.ENROLLMENT_YEAR;
import static org.ilisi.backend.model.User_.*;
import static org.ilisi.backend.specs.ProfileSpecifications.profileDeeplyHasKeyword;
import static org.ilisi.backend.specs.SpecUtils.likePattern;

/**
 * This class provides specifications for the Student entity.
 * Specifications are predicates that can be used in queries to filter results.
 */
public class StudentSpecifications {
    // Private constructor to prevent instantiation of utility class
    private StudentSpecifications() {
    }

    /**
     * Returns a Specification for Students that matches the given keyword.
     * The keyword is used in a like pattern search on the Student's firstName, lastName, email, and enrollmentYear.
     * Additionally, a subquery is used to match Profiles that deeply contain the keyword.
     *
     * @param keyword the keyword to search for
     * @return a Specification for Students that matches the given keyword
     */
    public static Specification<Student> hasKeyword(String keyword) {
        return (root, query, builder) -> {
            // Convert the keyword to a like pattern
            String pattern = likePattern(keyword.toLowerCase());

            // Create a subquery to match Profiles that deeply contain the keyword
            Subquery<Profile> subquery = query.subquery(Profile.class);
            Root<Profile> subqueryRoot = subquery.from(Profile.class);
            subquery.select(subqueryRoot).distinct(true).where(profileDeeplyHasKeyword(keyword).toPredicate(subqueryRoot, query, builder));


//            subquery.select(subqueryRoot).where(profileDeeplyHasKeyword(keyword).toPredicate(subqueryRoot, query, builder));
//            subquery.where(profileDeeplyHasKeyword(keyword).toPredicate(subqueryRoot, query, builder));

            // Return a predicate that matches Students based on the like pattern and the subquery
            return builder.or(
                    builder.like(builder.lower(root.get(FIRST_NAME)), pattern),
                    builder.like(builder.lower(root.get(LAST_NAME)), pattern),
                    builder.like(builder.lower(root.get(EMAIL)), pattern),
                    builder.like(root.get(ENROLLMENT_YEAR).as(String.class), keyword),
                    builder.in(root.get(Student_.PROFILE)).value(subquery)
            );
        };
    }


}