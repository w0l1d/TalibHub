import Review from "./review";

class Institut {
    id?: string;
    name: string;
    website: string;
    image?: File;
    reviews?: Review[];

    constructor(
        id:string, 
        name: string, 
        website: string, 
        image?: File,
        reviews?: Review[]
        ) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.image = image;
        this.reviews = reviews;
    }
}

export default Institut;
