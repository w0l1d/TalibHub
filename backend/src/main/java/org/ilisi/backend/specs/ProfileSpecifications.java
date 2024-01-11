package org.ilisi.backend.specs;


import jakarta.persistence.criteria.Join;
import org.ilisi.backend.model.*;
import org.springframework.data.jpa.domain.Specification;

import static org.ilisi.backend.specs.SpecUtils.likePattern;

public class ProfileSpecifications {
    private ProfileSpecifications() {
    }

    /**
     * Returns a Specification for Profiles that deeply matches the given keyword.
     * The keyword is used in a like pattern search on the Profile's about field, Education's title and location,
     * Experience's title and location, and Institute's name associated with both Education and Experience.
     *
     * @param keyword the keyword to search for
     * @return a Specification for Profiles that deeply matches the given keyword
     */
    public static Specification<Profile> profileDeeplyHasKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = likePattern(keyword);
            Join<Profile, Education> educationJoin = root.join(Profile_.EDUCATIONS);
            Join<Education, Institut> institutJoinByEducation = educationJoin.join(Education_.INSTITUT);
            Join<Profile, Experience> experienceJoin = root.join(Profile_.EXPERIENCES);
            Join<Experience, Institut> institutJoinByExperience = experienceJoin.join(Experience_.INSTITUT);

            return builder.or(
                    builder.like(educationJoin.get(Education_.TITLE), pattern),
                    builder.like(experienceJoin.get(Education_.LOCATION), pattern),
                    builder.like(institutJoinByEducation.get(Institut_.NAME), pattern),
                    builder.like(experienceJoin.get(Experience_.TITLE), pattern),
                    builder.like(experienceJoin.get(Experience_.LOCATION), pattern),
                    builder.like(institutJoinByExperience.get(Institut_.NAME), pattern),
                    builder.like(root.get(Profile_.ABOUT), pattern)
            );
        };
    }
}
