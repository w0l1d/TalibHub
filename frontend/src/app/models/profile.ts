import Education from "./education";
import Experience from "./experience";
import Student from "./student";

class Profile {
    about: string;
    student: Student;
    educations: Education[];
    experiences: Experience[];

    constructor(
        about: string, 
        student: Student,
        educations: Education[], 
        experiences: Experience[]
    ) {
        this.about = about;
        this.student = student;
        this.educations = educations;
        this.experiences = experiences;
    }
}

export default Profile;