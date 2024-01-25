class User {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  cin: string;
  enabled: boolean;
  picture?: File;

  constructor(
    firstName: string,
    lastName: string,
    email: string,
    phone: string,
    cin: string,
    enabled: boolean,
    picture?: File
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.cin = cin;
    this.enabled = enabled;
    this.picture = picture;
  }
}

export default User;
