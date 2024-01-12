package org.ilisi.backend.specs;


import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.ilisi.backend.model.Education;
import org.ilisi.backend.model.Experience;
import org.ilisi.backend.model.Institut;
import org.ilisi.backend.model.Profile;
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
            String pattern = likePattern(keyword.toLowerCase());
            Join<Profile, Education> educationJoin = root.join(Profile_.EDUCATIONS, JoinType.LEFT);
            Join<Education, Institut> institutJoinByEducation = educationJoin.join(Education_.INSTITUT, JoinType.LEFT);
            Join<Profile, Experience> experienceJoin = root.join(Profile_.EXPERIENCES, JoinType.LEFT);
            Join<Experience, Institut> institutJoinByExperience = experienceJoin.join(Experience_.INSTITUT, JoinType.LEFT);

            return builder.or(
                    builder.like(builder.lower(educationJoin.get(Education_.TITLE)), pattern),
                    builder.like(builder.lower(educationJoin.get(Education_.LOCATION)), pattern),
                    builder.like(builder.lower(educationJoin.get(Education_.STUDY_FIELD)), pattern),
                    builder.like(builder.lower(institutJoinByEducation.get(Institut_.NAME)), pattern),
                    builder.like(builder.lower(experienceJoin.get(Experience_.TITLE)), pattern),
                    builder.like(builder.lower(experienceJoin.get(Experience_.LOCATION)), pattern),
                    builder.like(builder.lower(experienceJoin.get(Experience_.DESCRIPTION)), pattern),
                    builder.like(builder.lower(institutJoinByExperience.get(Institut_.NAME)), pattern),
                    builder.like(root.get(Profile_.ABOUT), pattern)
            );
        };
    }


//    public static Specification<Profile> belownsTo(String studentId) {
//        return (root, query, builder) -> {
//            return builder.equal(root.get(Profile_.STUDENT).get(Student_.ID), studentId);
//        };
//    }

}
