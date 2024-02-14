import Review from "./review";

class Institut {
    id?: string;
    name: string;
    website: string;
    image?: File;
    imageUri?: string;
    reviews?: Review[];

    constructor(
        id:string, 
        name: string, 
        website: string, 
        image?: File,
        imageUri?: string,
        reviews?: Review[]
        ) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.image = image;
        this.imageUri = imageUri;
        this.reviews = reviews;
    }
}

export default Institut;
