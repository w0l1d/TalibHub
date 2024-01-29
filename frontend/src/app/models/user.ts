class User {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  cin: string;
  enabled: boolean;
  picture?: File;
  imageUri?: string;

  constructor(
    firstName: string,
    lastName: string,
    email: string,
    phone: string,
    cin: string,
    enabled: boolean,
    picture?: File,
    imageUri?: string
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.cin = cin;
    this.enabled = enabled;
    this.picture = picture;
    this.imageUri = imageUri;
  }
}

export default User;
