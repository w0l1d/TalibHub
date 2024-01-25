import Education from "./education";
import Experience from "./experience";
import Student from "./student";

class Profile {
    id: string;
    about: string;
    student: Student;
    educations: Education[];
    experiences: Experience[];

    constructor(
        id: string,
        about: string,
        student: Student,
        educations: Education[],
        experiences: Experience[]
    ) {
        this.id = id;
        this.about = about;
        this.student = student;
        this.educations = educations;
        this.experiences = experiences;
    }
}

export default Profile;
