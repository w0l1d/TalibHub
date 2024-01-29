class User {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  cin: string;
  enabled: boolean;
  picture?: File;
  authorities?: any[] = [];

  constructor(
    firstName: string,
    lastName: string,
    email: string,
    phone: string,
    cin: string,
    enabled: boolean,
    picture?: File,
    authorities?: any[]
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.cin = cin;
    this.enabled = enabled;
    this.picture = picture;
    this.authorities = authorities;
  }
}

export default User;
