class Institut {
    id?: string;
    name: string;
    website: string;
    image?: File;
  imageUri?: string;

  constructor(id: string, name: string, website: string, image?: File, imageUri?: string) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.image = image;
    this.imageUri = imageUri;
    }
}

export default Institut;
