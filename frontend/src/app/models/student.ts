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
    enabled: boolean,
    phone: string,
    birthDate: Date,
    enrollmentYear: number,
    graduationYear: number,
    imageUri?: string
  ) {
    super(firstName, lastName, email, phone, cin, enabled, undefined, imageUri);
    this.cne = cne;
    this.birthDate = birthDate;
    this.enrollmentYear = enrollmentYear;
    this.graduationYear = graduationYear;
  }
}

export default Student;
