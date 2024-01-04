class User {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  cin: string;
  enabled: boolean;

  constructor(
    firstName: string,
    lastName: string,
    email: string,
    phone: string,
    cin: string,
    enabled: boolean
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.cin = cin;
    this.enabled = enabled;
  }
}

export default User;
