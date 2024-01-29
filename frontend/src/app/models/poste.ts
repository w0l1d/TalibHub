import User from "./user";
import Comment from "./comment";

class Poste{
  id?: string;
    titre: string;
    description: string;
    image?: File;
  user?: User;
    comments?: Comment[];
    createdOn?: Date;
    lastUpdatedOn?: Date;

  constructor(id: string, titre: string, description: string, image?: File, comments?: Comment[], createdOn?: Date, lastUpdatedOn?: Date, user?: User) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image = image;
    this.user = user;
        this.comments = comments;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
    }

}

export default Poste;
