import User from "./user";

class Student extends User {
    cne: string;
    birthDate: Date;
    enrollmentYear: number;
    graduationYear: number;

    constructor(
        firstName: string,
        lastName: string,
        cne: string,
        birthDate: Date,
        enrollmentYear: number,
        graduationYear: number
    ) {
        super(firstName, lastName);
        this.cne = cne;
        this.birthDate = birthDate;
        this.enrollmentYear = enrollmentYear;
        this.graduationYear = graduationYear;
    }
}

export default Student;
