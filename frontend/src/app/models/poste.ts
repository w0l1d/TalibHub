class Poste{
    id?: number;
    titre: string;
    description: string;
    image?: File;
    comments?: Comment[];
    createdOn?: Date;
    lastUpdatedOn?: Date;

    constructor(id: number, titre: string, description: string, image?: File, comments?: Comment[], createdOn?: Date, lastUpdatedOn?: Date) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image = image;
        this.comments = comments;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }
    
}

export default Poste;