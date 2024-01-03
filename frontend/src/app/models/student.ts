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
        cin: string,
        email: string,
        password: string,
        enabled: boolean,
        phone: string,
        birthDate: Date,
        enrollmentYear: number,
        graduationYear: number
    ) {
      super(firstName, lastName, email, password, phone, cin, enabled);
        this.cne = cne;
        this.birthDate = birthDate;
        this.enrollmentYear = enrollmentYear;
        this.graduationYear = graduationYear;
    }
}

export default Student;
