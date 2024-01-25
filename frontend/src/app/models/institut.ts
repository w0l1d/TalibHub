class Institut {
    id?: string;
    name: string;
    website: string;
    image?: File;

    constructor(id:string, name: string, website: string, image?: File) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.image = image;
    }
}

export default Institut;
